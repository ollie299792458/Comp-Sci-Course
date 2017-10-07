package uk.ac.cam.olb22.fjava.tick0;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExternalSort {

    private static final int INTS_PER_CHUNK = 4;//(int) (Runtime.getRuntime().maxMemory() / 4) - 700000;
    private static final int CONCURRENT_FILES = 2;

    public static void sort(String afName, String bfName) throws FileNotFoundException, IOException {
        //Open the files
        RandomAccessFile af = new RandomAccessFile(afName, "rw");
        long fileLength = af.length();
        RandomAccessFile bf = new RandomAccessFile(bfName, "rw");

        //debug
        //writeOutFiles(afName, bfName);

        //debug
        System.out.println("Starting Quicksort Pass:"+af.length());

        //TODO maybe check the size of all numbers, then use a short array instead (if all < Short.MAX_VALUE)

        /* **** QUICKSORT PASS **** */
        //Prepare the file reading and writing objects for quicksort pass
        DataInputStream adi = new DataInputStream(new BufferedInputStream(new FileInputStream(af.getFD())));
        DataOutputStream bdo = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(bf.getFD())));

        //Create the data storage structure for quicksorts
        int[] currentlySorting = new int[INTS_PER_CHUNK];

        //counter for the total number of integers in file a
        long intsInFile = 0;

        //boolean for while loop
        boolean lastIteration = false;

        while (!lastIteration) {
            //check if last iteration
            if (intsInFile + INTS_PER_CHUNK > af.length()/Integer.BYTES) {
                //reduce the array if too big - aka last iteration due to end of file
                currentlySorting = null;
                System.gc();
                currentlySorting = new int[(int) (af.length()/Integer.BYTES-intsInFile)];
                lastIteration = true;

            }

            //Read the bytes and get the number actually read
            int length = readInts(currentlySorting, adi);

            //increment count
            intsInFile += length;

            //sort the bytes
            quickSort(currentlySorting);

            //write the bytes
            writeInts(currentlySorting, bdo);

        }

        //flush
        bdo.flush();
        System.gc();

        adi.close();
        bdo.close();

        //debug
        af = new RandomAccessFile(afName, "r");
        System.out.println("Starting Merge Passes:"+af.length());
        af.close();


        /* **** MERGE PASS **** */
        //point at which merge is complete
        int maxChunksInASegment = (int) (intsInFile / INTS_PER_CHUNK + (intsInFile % INTS_PER_CHUNK > 0 ? 1 : 0));

        //initially merge single chunks
        int chunksInASegment = 1;

        //initial input and output files
        String foName = bfName;
        String fiName = afName;

        //while all segments haven't been merged
        while (maxChunksInASegment > chunksInASegment) {

            //inner loop deals with not having enough concurrent files for all the segments that must be merged on earlier passes
            //end state for inner loop
            long maxOffset = intsInFile;

            //initially start at the begining of the file
            long currentOffset = 0;

            //file to output too
            DataOutputStream fo = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new RandomAccessFile(foName, "rw").getFD())));

            //while the whole file hasn't been merged
            while (currentOffset < maxOffset) {

                //arrays of random access files, integers remaining in each segment, and an arraylist 'data' as the buffer
                RandomAccessFile[] rafs = new RandomAccessFile[CONCURRENT_FILES];
                int[] intsRemaining = new int[CONCURRENT_FILES];
                //todo keep data sorted by first integer in list
                ArrayList<List<Integer>> data = new ArrayList<>(CONCURRENT_FILES);

                //open all the random access files, populate integers remaining, and return number of rafs opened (if not enough segments)
                int length = getFiles(rafs, fiName, intsRemaining, chunksInASegment, currentOffset);

                //if not enough segments, update arrays
                if (length != CONCURRENT_FILES) {
                    rafs = Arrays.copyOf(rafs, length);
                    intsRemaining = Arrays.copyOf(intsRemaining, length);
                    data = new ArrayList<>(length);
                }

                //load data into the buggers, makes use of the refill buffer method
                loadInitialData(data, rafs, intsRemaining, chunksInASegment);

                //count down for when all segments have been fully read
                int notNullCount = length;

                //while not all segments have been fully read
                while (notNullCount > 0) {

                    //find the smallest integer
                    int index = findSmallestIndex(data, intsRemaining);

                    //write the integer to the output file and remove it from the segments
                    fo.writeInt(data.get(index).get(0));
                    data.get(index).remove(0);
                    intsRemaining[index]--;

                    //if the buffer is now empty either:
                    if (data.get(index).size() == 0) {
                        //just refill it
                        int res = refillBuffer(data.get(index), rafs[index], intsRemaining[index], chunksInASegment);
                        //or, if the segment is empty, make this clear
                        if (res == 0) {
                            rafs[index] = null;
                            notNullCount--;
                            data.get(index).clear();
                        }
                    }
                }

                currentOffset += CONCURRENT_FILES*chunksInASegment;
            }
            fo.flush();
            fo.close();

            //swap files
            String temp = fiName;
            fiName = foName;
            foName = temp;

            //merge bigger segments
            chunksInASegment *= CONCURRENT_FILES;
        }

        //debug
        af = new RandomAccessFile(afName, "r");
        System.out.println("Copy pass:"+af.length());
        af.close();

        //if file
        if (!foName.equals(afName) || intsInFile < INTS_PER_CHUNK) {
            moveFileFromTo(bfName, afName, intsInFile);
        }

        //debug
        af = new RandomAccessFile(afName, "r");
        System.out.println("Done:"+af.length());
        af.close();
        //writeOutFiles(afName, bfName);
    }

    private static int findSmallestIndex(ArrayList<List<Integer>> data, int[] remaining) {
        int minVal = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            if (remaining[i] != 0) {
                if (data.get(i).get(0) < minVal) {
                    index = i;
                    minVal = data.get(i).get(0);
                }
            }
        }
        return index;
    }

    private static void loadInitialData(ArrayList<List<Integer>> data, RandomAccessFile[] rafs, int[] intsRemaining, int chunksInASegment) throws IOException {
        for (int i = 0; i < rafs.length; i++) {
            data.add(new LinkedList<>());
            int sucked = refillBuffer(data.get(i), rafs[i], intsRemaining[i], chunksInASegment);
        }
    }

    private static int refillBuffer(List<Integer> buffer, RandomAccessFile raf, int remaining, int chunksInASegment) throws IOException {
        int count = chunksInASegment*INTS_PER_CHUNK;

        if (remaining < chunksInASegment*INTS_PER_CHUNK) {
            count = remaining;
        }

        buffer.clear();
        for (int i = 0; i < count; i++) {
            buffer.add(raf.readInt());
        }
        return count;
    }

    private static int getFiles(RandomAccessFile[] rafs, String fiName, int[] intsRemaining, int chunksInASegment, long offset) throws IOException {
        long currentPostition = offset;
        RandomAccessFile file = new RandomAccessFile(fiName, "r");
        long flength = file.length()/4;

        for (int i = 0; i < rafs.length; i++) {

            file = new RandomAccessFile(fiName, "r");
            file.seek(currentPostition*Integer.BYTES);
            rafs[i] = file;
            intsRemaining[i] = chunksInASegment*INTS_PER_CHUNK;

            if (currentPostition + (long) chunksInASegment*INTS_PER_CHUNK >= flength) {
                long segmentlength = flength - currentPostition;
                intsRemaining[i] = (int) segmentlength;
                return i+1;
            }

            currentPostition += chunksInASegment*INTS_PER_CHUNK;
        }

        return CONCURRENT_FILES;
    }

    private static void moveFileFromTo(String from, String to, long length) throws IOException {
        DataInputStream fromS = new DataInputStream(new BufferedInputStream(new FileInputStream(new RandomAccessFile(from, "r").getFD())));
        DataOutputStream toS = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new RandomAccessFile(to, "rw").getFD())));

        long size = length*Integer.BYTES;

        for (long i = 0; i < size; i++) {
            toS.writeInt(fromS.readInt());
        }
        toS.flush();

        fromS.close();
        toS.close();
    }

    /**
     * Debug method
     * @param af
     * @param bf
     */
    private static void writeOutFiles(String af, String bf) throws IOException {
        DataInputStream a = new DataInputStream(new BufferedInputStream(new FileInputStream(new RandomAccessFile(af, "r").getFD())));
        DataInputStream b = new DataInputStream(new BufferedInputStream(new FileInputStream(new RandomAccessFile(bf, "r").getFD())));
        try {
            while (true) {
                System.out.print(a.readInt()+ ",");
            }
        } catch (EOFException e) {
            System.out.println("A read");
        }
        try {
            while (true) {
                System.out.print(b.readInt()+",");
            }
        } catch (EOFException e) {
            System.out.println("B read");
        }
        a.close();
        b.close();
    }

    /**
     * @param output ints to be written
     * @param dataOutputStream destination
     */
    private static void writeInts(int[] output, DataOutputStream dataOutputStream) throws IOException {
        for (int out : output) {
            dataOutputStream.writeInt(out);
        }
    }

    /**
     * @param currentlySorting Array to be sorted
     */
    private static void quickSort(int[] currentlySorting) {
        //uses the default implementation, mine would certainly be no faster
        Arrays.sort(currentlySorting);
    }

    /**
     * @param output must be preinitialised
     * @param dataInputStream refering to a valid file
     * @return number of lines actually read
     */
    private static int readInts(int[] output, DataInputStream dataInputStream) throws IOException {
        for (int i = 0; i < output.length; i++) {
            try {
                output[i] = dataInputStream.readInt();
            } catch (EOFException e) {
                return i; //i+1 is the current number of ints attempted, but last one was unsuccessful, so i
            }
        }
        return output.length;
    }

    private static String byteToHex(byte b) {
        String r = Integer.toHexString(b);
        if (r.length() == 8) {
            return r.substring(6);
        }
        return r;
    }

    public static String checkSum(String f) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream ds = new DigestInputStream(
                    new FileInputStream(f), md);
            byte[] b = new byte[512];
            while (ds.read(b) != -1)
                ;

            String computed = "";
            for(byte v : md.digest())
                computed += byteToHex(v);

            return computed;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<error computing checksum>";
    }

    public static void main(String[] args) throws Exception {
        String f1 = args[0];
        String f2 = args[1];
        sort(f1, f2);
        System.out.println("The checksum is: "+checkSum(f1));
    }
}