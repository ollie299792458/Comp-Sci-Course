package uk.ac.cam.olb22.oop.tick3star;

/**
 * Created by oliver on 21/11/16.
 */

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class PatternStore {
    private List<Pattern> mPatterns = new LinkedList<>();
    private Map<String,List<Pattern>> mMapAuths = new HashMap<>();
    private Map<String,Pattern> mMapName = new HashMap<>();

    public PatternStore(String source) throws IOException {
        if (source.startsWith("http://")) {
            loadFromURL(source);
        }
        else {
            loadFromDisk(source);
        }
    }

    public PatternStore(Reader source) throws IOException {
        load(source);
    }

    private void load(Reader r) throws IOException {
        // DONE: read each line from the reader and add it to the data structures
        BufferedReader b = new BufferedReader(r);
        String line = b.readLine();
        while ( line != null) {
            try {
                Pattern pattern = new Pattern(line);
                mPatterns.add(pattern);
                List<Pattern> authsPatterns = new LinkedList<>();
                if (mMapAuths.get(pattern.getAuthor()) != null) {
                    authsPatterns.addAll(mMapAuths.get(pattern.getAuthor()));
                }
                authsPatterns.add(pattern);
                mMapAuths.put(pattern.getAuthor(), authsPatterns);
                mMapName.put(pattern.getName(), pattern);
            } catch (PatternFormatException e) {
                System.out.println(line);
            }
            line=b.readLine();
        }
    }


    private void loadFromURL(String url) throws IOException {
        // DONE: Create a Reader for the URL and then call load on it
        URL destination = new URL(url);
        URLConnection conn = destination.openConnection();
        Reader r = new InputStreamReader(conn.getInputStream());
        load(r);
    }

    private void loadFromDisk(String filename) throws IOException {
        // DONE: Create a Reader for the file and then call load on it
        Reader r = new FileReader(filename);
        load(r);

    }

    public List<Pattern> getPatternsNameSorted() {
        // DONE: Get a list of all patterns sorted by name
        Collections.sort(mPatterns);
        return new LinkedList<Pattern>(mPatterns);
    }

    public List<Pattern> getPatternsAuthorSorted() {
        // DONE: Get a list of all patterns sorted by author then name
        Collections.sort(mPatterns, new Comparator<Pattern>() {
            @Override
            public int compare(Pattern o1, Pattern o2) {
                int dif = o1.getAuthor().compareTo(o2.getAuthor());
                if (dif == 0) {
                    dif = o1.compareTo(o2);
                }
                return dif;
            }
        });
        return new LinkedList<>(mPatterns);
    }

    public List<Pattern> getPatternsByAuthor(String author) throws PatternNotFound {
        // DONE:  return a list of patterns from a particular author sorted by name
        List<Pattern> authPatterns = new LinkedList<>();
        authPatterns.addAll(mMapAuths.get(author));
        if (authPatterns.isEmpty()) {
            throw new PatternNotFound("No patterns found for author: \""+author+"\"");
        }
        Collections.sort(authPatterns);
        return new LinkedList<>(authPatterns);
    }

    public Pattern getPatternByName(String name) throws PatternNotFound {
        // DONE: Get a particular pattern by name
        Pattern p = mMapName.get(name);
        if (p==null) {
            throw new PatternNotFound("Pattern with name: \""+name+"\" does not exist");
        }
        return p;
    }

    public List<String> getPatternAuthors() {
        // DONE: Get a sorted list of all pattern authors in the store
        List<String> auths = new LinkedList<>();
        auths.addAll(mMapAuths.keySet());
        Collections.sort(auths);
        return new LinkedList<>(auths);
    }

    public List<String> getPatternNames() {
        // DONE: Get a list of all pattern names in the store, sorted by name
        List<String> names = new LinkedList<>();
        names.addAll(mMapName.keySet());
        Collections.sort(names);
        return new LinkedList<>(names);
    }

    public static void main(String args[]) throws IOException {
        args = new String[] {"http://www.cl.cam.ac.uk/teaching/current/OOProg/ticks/lifetest.txt"};
        PatternStore p = new PatternStore(args[0]);
    }
}