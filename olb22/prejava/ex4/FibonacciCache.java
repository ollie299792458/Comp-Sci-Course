package uk.ac.cam.olb22.prejava.ex4;

public class FibonacciCache {

   // Uninitialised array
   public static long[] fib = null;

   public static void store() throws Exception{
      if (fib == null) {
         throw new Exception("Fibonacci array null pointer");
      }
      
      for (int i = 0; i < fib.length; i++) {
         if (i == 0 || i == 1) {
            fib[i] = 1;
         } else {
            fib[i] = fib[i-1] + fib[i-2];
         }
      }
   }

   public static void reset(int cachesize) {
      fib = new long[cachesize];
      for (long l : fib) {
         l = 0;
      }
   }
 
   public static long get(int i) throws Exception {
      if (fib == null) {
         throw new Exception("Fibonacci array null pointer");
      }

      if (i < fib.length) {
         return fib[i];
      } else {
         throw new Exception("Fibonacci array index out of bounds");
      }
   }

   public static void main(String[] args) {
      try {
         reset(20);
         store();
         int i = Integer.decode(args[0]);
         System.out.println(get(i));
      } catch (Exception e){
         System.out.println(e.toString());
      }
   }
  
}