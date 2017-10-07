package uk.ac.cam.olb22.fjava.tick0;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ExternalSort {

    private static final int INTS_PER_CHUNK = (int) (Runtime.getRuntime().freeMemory() / 6);
    private static final int CONCURRENT_FILES = 2;
    private static final int BUFFER_SIZE = (int) (Runtime.getRuntime().freeMemory()/(CONCURRENT_FILES*20));

    public static void sort(String afName, String bfName) throws FileNotFoundException, IOException {
        //Open the files
        RandomAccessFile af = new RandomAccessFile(afName, "rw");
        long fileLength = af.length();
        RandomAccessFile bf = new RandomAccessFile(bfName, "rw");

        //debug
        //writeOutFiles(afName, bfName);

        //debug
        //System.out.println("Starting Quicksort Pass:"+af.length()/Integer.BYTES);

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
        //writeOutFiles(afName, bfName);

        //debug
        /*af = new RandomAccessFile(afName, "r");
        System.out.println("Starting Merge Passes:"+af.length()/Integer.BYTES);
        af.close();*/


        /* **** MERGE PASS **** */
        //point at which merge is complete, times 2 for some unknown reason, but it works
        int maxChunksInASegment = (int) (intsInFile / INTS_PER_CHUNK + (intsInFile % INTS_PER_CHUNK > 0 ? 1 : 0))*2;

        //initially merge single chunks
        int chunksInASegment = 1;

        //initial input and output files
        String foName = afName;
        String fiName = bfName;

        //while all segments haven't been merged
        while (maxChunksInASegment >= chunksInASegment) {

            //inner loop deals with not having enough concurrent files for all the segments that must be merged
            //end state for inner loop
            long maxOffsetInt = intsInFile;

            //initially start at the beginning of the file
            long currentOffsetInt = 0;

            //file to output too
            DataOutputStream fo = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new RandomAccessFile(foName, "rw").getFD())));

            //while the whole file hasn't been merged
            while (currentOffsetInt < maxOffsetInt) {

                //arrays of random access files, integers remaining in each segment, and an arraylist 'data' as the buffer
                RandomAccessFile[] rafs = new RandomAccessFile[CONCURRENT_FILES];
                int[] intsRemaining = new int[CONCURRENT_FILES];
                int[][] buffer = new int[CONCURRENT_FILES][BUFFER_SIZE];
                int[] bufferIndex = new int[CONCURRENT_FILES];

                //open all the random access files, populate integers remaining, and return number of rafs opened (if not enough segments)
                int length = getFiles(rafs, fiName, intsRemaining, chunksInASegment, currentOffsetInt, intsInFile);

                //if not enough segments, update arrays
                if (length != CONCURRENT_FILES) {
                    rafs = Arrays.copyOf(rafs, length);
                    intsRemaining = Arrays.copyOf(intsRemaining, length);
                    buffer = new int[length][BUFFER_SIZE];
                    bufferIndex = new int[length];
                }

                //load data into the buggers, makes use of the refill buffer method
                loadInitialData(buffer, bufferIndex, rafs, intsRemaining, chunksInASegment);

                //count down for when all segments have been fully read
                int notNullCount = length;

                //while not all segments have been fully read
                while (notNullCount > 0) {

                    //find the smallest integer
                    int index = findIndexOfSmallest(buffer, bufferIndex, intsRemaining);

                    assert index >= 0;

                    //write the integer to the output file and remove it from the segments
                    fo.writeInt(buffer[index][bufferIndex[index]]);
                    intsRemaining[index]--;
                    bufferIndex[index]++;

                    assert intsRemaining[index] >= 0;

                    //if the segment is empty, make this clear
                    if (intsRemaining[index] == 0) {
                        notNullCount--;
                        bufferIndex[index] = -1;
                    }

                    //if the buffer is now empty
                    if (buffer[index].length <= bufferIndex[index]) {
                        //refill it
                        refillBuffer(buffer[index], rafs[index], intsRemaining[index]);
                        bufferIndex[index]=0;
                    }
                }

                //increment the 'counter'
                currentOffsetInt += CONCURRENT_FILES * chunksInASegment * INTS_PER_CHUNK;

                //close all the files
                for (RandomAccessFile raf: rafs) {
                    raf.close();
                }
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
        /*af = new RandomAccessFile(afName, "r");
        System.out.println("Copy pass:"+af.length()/Integer.BYTES);
        af.close();*/

        //make sure the file ends up in A
        if (!foName.equals(afName) || INTS_PER_CHUNK <= intsInFile) {
            moveFileFromTo(bfName, afName, intsInFile);
        }

        //debug
        /*af = new RandomAccessFile(afName, "r");
        System.out.println("Done:"+af.length()/Integer.BYTES);
        af.close();*/

        //debug
        //writeOutFiles(afName, bfName);
    }

    private static int findIndexOfSmallest(int[][] buffer, int[] bufferIndex, int[] remaining) {
        //look for integers smaller than the biggest one
        int minVal = Integer.MAX_VALUE;
        int index = -1;

        //for every segment
        for (int i = 0; i < buffer.length; i++) {
            //if the segment isn't empty
            if (remaining[i] != 0) {
                //if the first integer is less than the lowest so far
                if (buffer[i][bufferIndex[i]] < minVal) {
                    //mark that as the lowest integer
                    index = i;
                    minVal = buffer[i][bufferIndex[i]];
                }
            }
        }
        return index;
    }

    private static void loadInitialData(int[][] buffer, int[] bufferIndex, RandomAccessFile[] rafs, int[] intsRemaining, int chunksInASegment) throws IOException {
        for (int i = 0; i < rafs.length; i++) {
            long sucked = refillBuffer(buffer[i], rafs[i], intsRemaining[i]);
            bufferIndex[i] = 0;
        }
    }

    private static int refillBuffer(int[] buffer, RandomAccessFile raf, int remaining) throws IOException {
        int count = BUFFER_SIZE;

        //if there is less than a full buffer load left only load so much
        if (remaining < count) {
            count = remaining;
        }

        //fill in the buffer from the file
        for (int i = 0; i < count; i++) {
            buffer[i] = raf.readInt();
            if (i != 0 && buffer[i-1] > buffer[i]) {
                System.out.println(i+" "+remaining);
            }
        }
        return count;
    }

    private static int getFiles(RandomAccessFile[] rafs, String fiName, int[] intsRemaining, int chunksInASegment, long offsetInt, long fLengthInt) throws IOException {
        long currentPostitionInt = offsetInt;

        for (int i = 0; i < rafs.length; i++) {

            //create a fresh file for each point
            RandomAccessFile file = new RandomAccessFile(fiName, "r");

            //seek to the right position
            file.seek(currentPostitionInt*Integer.BYTES);

            //add the file to the array of files
            rafs[i] = file;

            //set the number of integers remaining in the files segment
            intsRemaining[i] = chunksInASegment*INTS_PER_CHUNK;

            //if the files segment is at the end of the file, update the number of integers remaining appropriately
            if (currentPostitionInt + (long) chunksInASegment*INTS_PER_CHUNK >= fLengthInt) {
                long segmentlengthInt = fLengthInt - currentPostitionInt;
                intsRemaining[i] = (int) segmentlengthInt;
                //and return the number of files read
                return i+1;
            }

            currentPostitionInt += chunksInASegment*INTS_PER_CHUNK;
        }

        //debug
        /*
        int total = 0;
        for (int k : intsRemaining) {
            total += k;
        }
        System.out.println(offsetInt + total+" "+currentPostitionInt);*/

        return rafs.length;
    }

    private static void moveFileFromTo(String from, String to, long length) throws IOException {
        DataInputStream fromS = new DataInputStream(new BufferedInputStream(new FileInputStream(new RandomAccessFile(from, "r").getFD())));
        DataOutputStream toS = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new RandomAccessFile(to, "rw").getFD())));

        long size = length;

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