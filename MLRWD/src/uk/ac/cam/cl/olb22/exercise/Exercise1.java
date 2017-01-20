package uk.ac.cam.cl.olb22.exercise;

import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.IExercise1;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrwd.exercises.sentiment_detection.Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Exercise1 implements IExercise1 {
    @Override
    public Map<Path, Sentiment> simpleClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        return null;
    }

    @Override
    public double calculateAccuracy(Map<Path, Sentiment> trueSentiments, Map<Path, Sentiment> predictedSentiments) {
        return 0;
    }

    @Override
    public Map<Path, Sentiment> improvedClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        HashMap<String, Sentiment> lexicon = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(lexiconFile.toFile()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] splitSpace = line.split(" ");
            String name = null;
            Sentiment sentiment = null;
            for (String equation : splitSpace) {
                boolean polarity = false;
                if (equation.startsWith("word1=")||(polarity = equation.startsWith("priorpolarity=")) {
                    String[] words = equation.split("=");
                    if (polarity) {
                        if (words[1].equals("positive")){
                            sentiment = Sentiment.POSITIVE;
                        } else {
                            sentiment = Sentiment.NEGATIVE;
                        }
                    } else {
                        name = words[1];
                    }
                }
            }
            lexicon.put(name, sentiment);
        }

        HashMap<Path, Sentiment> calculatedSentiments = new HashMap<>(testSet.size());

        for (Path path:testSet) {
            List<String> tokens = Tokenizer.tokenize(path);
            int posScore = 0;
            int negScore = 0;
            for (String token : tokens) {
                Sentiment sentiment = lexicon.get(token);
                switch (sentiment) {
                    case POSITIVE:
                        posScore++;
                        break;
                    case NEGATIVE:
                        negScore--;
                        break;
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
}
