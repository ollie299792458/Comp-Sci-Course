package uk.ac.cam.olb22.oop.tick3star;

import java.io.*;
import java.util.*;

/**
 * Created by oliver on 14/11/16.
 */
public class FactsOfLife {

    private World mWorld;
    private PatternStore mStore;
    private PatternRecord largestPreLoop = new PatternRecord();
    private PatternRecord longestCycle = new PatternRecord();
    private PatternRecord biggestGrowthRate = new PatternRecord();
    private PatternRecord biggestDeathRate = new PatternRecord();
    private PatternRecord largestPopulation = new PatternRecord();

    public FactsOfLife(PatternStore ps) {
        mStore = ps;
    }

    public String worldToString() {
        String result = "";
        for (int row = 0; row < mWorld.getHeight(); row++) {
            for (int col = 0; col < mWorld.getWidth(); col++) {
                result = result +(mWorld.getCell(col, row) ? "#" : "_");
            }
            result = result +"\\n";
        }
        return result;
    }

    public void play() throws IOException, PatternFormatException {
        List<Pattern> patterns = mStore.getPatternsNameSorted();

        Map<String, Integer> prevGens = new HashMap<>();

        for (Pattern pattern: patterns) {

            System.out.println("Now testing: " +pattern.getName());

            if (pattern.getHeight()*pattern.getWidth() <= 64) {
                mWorld = new PackedWorld(pattern);
            } else {
                mWorld = new ArrayWorld(pattern);
            }

            prevGens.clear();

            int lastPop = getWorldPopulation();
            int thisPop = getWorldPopulation();
            String thisWorldToString = worldToString();

            while (!prevGens.containsKey(thisWorldToString)) {

                double growthRate = (((double)thisPop) - ((double)lastPop))/((double)lastPop);
                double deathRate = (((double)lastPop) - ((double)thisPop))/((double)lastPop);

                if (biggestDeathRate.doubleVal < deathRate) {
                    biggestDeathRate.doubleVal = deathRate;
                    biggestDeathRate.pattern = pattern;
                }

                if (biggestGrowthRate.doubleVal < growthRate) {
                    biggestGrowthRate.doubleVal = growthRate;
                    biggestGrowthRate.pattern = pattern;
                }

                if (largestPopulation.intVal < thisPop) {
                    largestPopulation.intVal = thisPop;
                    largestPopulation.pattern = pattern;
                }

                prevGens.put(thisWorldToString, mWorld.getGenerationCount());

                mWorld.nextGeneration();
                lastPop = thisPop;
                thisPop = getWorldPopulation();
                thisWorldToString = worldToString();
            }

            int preLoop = prevGens.get(thisWorldToString);
            int endLoop = mWorld.getGenerationCount();

            if (largestPreLoop.intVal < preLoop) {
                largestPreLoop.pattern = pattern;
                largestPreLoop.intVal = preLoop;
            }

            int loop = endLoop-preLoop;
            if (longestCycle.intVal < loop) {
                longestCycle.pattern = pattern;
                longestCycle.intVal = loop;
            }
        }

        /*System.out.println(largestPreLoop.pattern.getName());
        System.out.println(longestCycle.pattern.getName());
        System.out.println(biggestGrowthRate.pattern.getName());
        System.out.println(biggestDeathRate.pattern.getName());
        System.out.println(largestPopulation.pattern.getName());*/

        FileWriter fileWriter = new FileWriter("stats.txt");
        PrintWriter printWrite = new PrintWriter(fileWriter);
        printWrite.println(largestPreLoop.pattern.getName());
        printWrite.println(longestCycle.pattern.getName());
        printWrite.println(biggestGrowthRate.pattern.getName());
        printWrite.println(biggestDeathRate.pattern.getName());
        printWrite.print(largestPopulation.pattern.getName());
        printWrite.close();
    }

    private int getWorldPopulation() {
        int result = 0;
        for (int row = 0; row < mWorld.getHeight(); row++) {
            for (int col = 0; col < mWorld.getWidth(); col++) {
                if (mWorld.getCell(col, row)) {
                    result++;
                }
            }
        }
        return result;
    }

    public static void main(String args[]) throws IOException, PatternFormatException {
        args = new String[] {"http://www.cl.cam.ac.uk/teaching/current/OOProg/ticks/life.txt"};
        if (args.length!=1) {
            System.out.println("Usage: java FactsOfLife <path/url to store>");
            return;
        }

        try {
            PatternStore ps = new PatternStore(args[0]);
            FactsOfLife gol = new FactsOfLife(ps);
            gol.play();
        }
        catch (IOException ioe) {
            System.out.println("Failed to load pattern store");
        }


    }

    private class PatternRecord {
        public Pattern pattern;
        public int intVal;
        public double doubleVal;
    }
}
