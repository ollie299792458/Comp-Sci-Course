package uk.ac.cam.cl.olb22.exercises;

import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.IExercise1;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Exercise1 implements IExercise1 {
    @Override
    public Map<Path, Sentiment> simpleClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        Map<String, Sentiment> lexicon = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(lexiconFile.toFile()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] splitSpace = line.split(" ");
            String name = null;
            Sentiment sentiment = null;
            for (String equation : splitSpace) {
                boolean polarity = false;
                if (equation.startsWith("word1=")||(polarity = equation.startsWith("priorpolarity="))) {
                    String[] words = equation.split("=");
                    if (polarity) {
                        if (words[1].equals("positive")){
                            sentiment = Sentiment.POSITIVE;
                        } else if (words[1].equals("negative")){
                            sentiment = Sentiment.NEGATIVE;
                        }
                    } else {
                        name = words[1];
                    }
                }
            }
            if (sentiment != null) {
                lexicon.put(name, sentiment);
            }
        }

        Map<Path, Sentiment> calculatedSentiments = new HashMap<>(testSet.size());

        for (Path path:testSet) {
            List<String> tokens = Tokenizer.tokenize(path);
            int posScore = 0;
            int negScore = 0;
            for (String token : tokens) {
                if (lexicon.containsKey(token)) {
                    Sentiment sentiment = lexicon.get(token);
                    switch (sentiment) {
                        case POSITIVE:
                            posScore++;
                            break;
                        case NEGATIVE:
                            negScore++;
                            break;
                    }
                }
            }
            Sentiment sentiment = Sentiment.NEGATIVE;
            if (posScore - negScore >= 0) {
                sentiment = Sentiment.POSITIVE;
            }
            calculatedSentiments.put(path, sentiment);
        }

        return calculatedSentiments;
    }

    @Override
    public double calculateAccuracy(Map<Path, Sentiment> trueSentiments, Map<Path, Sentiment> predictedSentiments) {
        int correct = 0;
        int total = predictedSentiments.size();

        for (Map.Entry<Path, Sentiment> predictedSentiment : predictedSentiments.entrySet()) {
            Sentiment trueSentiment;
            if ((trueSentiment = trueSentiments.get(predictedSentiment.getKey())) != null) {
                if (trueSentiment == predictedSentiment.getValue()){
                    correct++;
                }
            }
        }
        return ((double)correct)/((double)total);
    }

    @Override
    public Map<Path, Sentiment> improvedClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        Map<String, LexiconItem> lexicon = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(lexiconFile.toFile()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] splitSpace = line.split(" ");
            String name = null;
            LexiconItem lexiconItem = new LexiconItem();
            lexiconItem.sentiment = null;
            lexiconItem.strong = false;
            for (String equation : splitSpace) {
                if (equation.startsWith("priorpolarity=")) {
                    String[] words = equation.split("=");
                    if (words[1].equals("positive")){
                        lexiconItem.sentiment = Sentiment.POSITIVE;
                    } else if (words[1].equals("negative")){
                        lexiconItem.sentiment = Sentiment.NEGATIVE;
                    }
                } else if (equation.startsWith("word1=")){
                    String[] words = equation.split("=");
                    name = words[1];
                } else if (equation.startsWith("type=")) {
                    String[] words = equation.split("=");
                    if (words[1].equals("strongsubj")) {
                        lexiconItem.strong = true;
                    } else if (words[1].equals("weaksubj")) {
                        lexiconItem.strong = false;
                    }
                }
            }
            if (lexiconItem.sentiment != null) {
                lexicon.put(name, lexiconItem);
            }
        }

        Map<Path, Sentiment> calculatedSentiments = new HashMap<>(testSet.size());

        for (Path path:testSet) {
            List<String> tokens = Tokenizer.tokenize(path);
            int posScore = 0;
            int negScore = 0;
            for (String token : tokens) {
                if (lexicon.containsKey(token)) {
                    LexiconItem lexiconItem = lexicon.get(token);
                    int deltaScore = 1;
                    if (lexiconItem.strong) {
                        deltaScore = 10;
                    }
                    switch (lexiconItem.sentiment) {
                        case POSITIVE:
                            posScore = posScore + deltaScore;
                            break;
                        case NEGATIVE:
                            negScore = negScore + deltaScore;
                            break;
                    }
                }
            }
            Sentiment sentiment = Sentiment.NEGATIVE;
            if (posScore - negScore >= 48) {
                sentiment = Sentiment.POSITIVE;
            }
            calculatedSentiments.put(path, sentiment);
        }

        return calculatedSentiments;
    }

    private class LexiconItem {
        public Sentiment sentiment;
        public boolean strong;
    }
}
