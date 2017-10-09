package uk.ac.cam.olb22.precourse;

class PrimativeInt {
   public static void main(String[] args) {
		int i;                 //create a variable called i
		i = 85 & 0x2B;
		int j = 0105 ^ 0x2B;
		System.out.println(j--);
		System.out.println(j);	  //update i with the value 1
		System.out.println(i); //addSounds out the current value of i
   }
}