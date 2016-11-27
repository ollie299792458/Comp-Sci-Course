package uk.ac.cam.olb22.oop.tick4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by oliver on 14/11/16.
 */
public class GameOfLife {

    private World mWorld;
    private List<World> mCachedWorlds;
    private PatternStore mStore;

    public GameOfLife(PatternStore ps) {
        mStore = ps;
        mCachedWorlds = new LinkedList<>();
    }

    public void nextGeneration() {
        if (mCachedWorlds.size()>mWorld.getGenerationCount()) {
            mWorld = mCachedWorlds.get(mWorld.getGenerationCount()+1);
        } else {
            mWorld = copyWorld(true);
            mWorld.nextGeneration();
            mCachedWorlds.add(mWorld);
        }
    }

    public void previousGeneration() {
        mWorld = mCachedWorlds.get(mWorld.getGenerationCount()-1);
    }

    private World copyWorld(boolean useCloning) {
        World result;
        if (useCloning) {
            result = null;
        } else {
            if (mWorld instanceof PackedWorld) {
                result = new PackedWorld((PackedWorld) mWorld);
            } else if (mWorld instanceof ArrayWorld) {
                result = new ArrayWorld((ArrayWorld) mWorld);
            } else {
                result = null;
            }
        }
        return result;
    }

    public void print() {
        System.out.println("- "+mWorld.getGenerationCount());
        for (int row = 0; row < mWorld.getHeight(); row++) {
            for (int col = 0; col < mWorld.getWidth(); col++) {
                System.out.print(mWorld.getCell(col, row) ? "#" : "_");
            }
            System.out.println();
        }
    }
    public void play() throws IOException, PatternFormatException {

        String response="";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please select a pattern to play (l to list:");
        while (!response.equals("q")) {
            response = in.readLine();
            System.out.println(response);
            if (response.equals("f")) {
                if (mWorld==null) System.out.println("Please select a pattern to play (l to list):");
                else {
                    nextGeneration();
                    print();
                }
            }
            else if (response.equals("l")) {
                List<Pattern> names = mStore.getPatternsNameSorted();
                int i=0;
                for (Pattern p : names) {
                    System.out.println(i+" "+p.getName()+"  ("+p.getAuthor()+")");
                    i++;
                }
            }
            else if (response.startsWith("p")) {
                List<Pattern> names = mStore.getPatternsNameSorted();
                // DONE: Extract the integer after the p in response
                int patternNumber = Integer.parseInt(response.substring(1));
                // DONE: Get the associated pattern
                Pattern pattern = names.get(patternNumber);
                // DONE: Initialise mWorld using PackedWorld or ArrayWorld based on pattern world size
                if (pattern.getHeight()*pattern.getWidth() <= 64) {
                    mWorld = new PackedWorld(pattern);
                } else {
                    mWorld = new ArrayWorld(pattern);
                }
                mCachedWorlds.add(mWorld);
                print();
            } else if (response.equals("b")) {
                if (mWorld==null) System.out.println("Please select a pattern to play (l to list):");
                else {
                    previousGeneration();
                    print();
                }
            }

        }
    }

    public static void main(String args[]) throws IOException, PatternFormatException {
        args = new String[] {"http://www.cl.cam.ac.uk/teaching/current/OOProg/ticks/life.txt"};
        if (args.length!=1) {
            System.out.println("Usage: java FactsOfLife <path/url to store>");
            return;
        }

        try {
            PatternStore ps = new PatternStore(args[0]);
            GameOfLife gol = new GameOfLife(ps);
            gol.play();
        }
        catch (IOException ioe) {
            System.out.println("Failed to load pattern store");
        }


    }
}
