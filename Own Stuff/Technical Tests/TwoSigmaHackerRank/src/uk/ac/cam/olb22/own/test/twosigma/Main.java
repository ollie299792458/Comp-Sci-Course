package uk.ac.cam.olb22.own.test.twosigma;

public class Main {

    public static void main(String[] args) {
        // write your code here
        printSubstrings("hi");
    }

    public static void printSubstrings(String string) {
        //useful arrays
        char[] vowels = "aeiou".toCharArray();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        //get as chars
        char[] s = string.toCharArray();


        //get first
        int i = 0;
        String substring = "";

        for (int c = 0; c < alphabet.length; c++) {
            if (substring.length() > 0 || contains(vowels, c)) {
               //TODO I give up
            }
        }

        //remove from start until vowel

        //remove from end until consonant

        System.out.println("a".compareTo("b")+""+"aab".compareTo("aabb"));
    }

    private static boolean contains(char[] chars, int c) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return true;
            }
        }
        return false;
    }
}
