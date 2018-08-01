package uk.ac.cam.olb22.tpppubquiz;

public class MainQuestion7 {

    public static void main(String[] args) {
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                for (int z = 0; z < 100; z++) {
                    if (x*x+y*y==z*z) {
                        for (int i = 1; i < 10; i++) {
                            int X = Integer.parseInt(Integer.toString(i)+Integer.toString(x));
                            int Y = Integer.parseInt(Integer.toString(i)+Integer.toString(y));
                            int Z = Integer.parseInt(Integer.toString(i)+Integer.toString(z));
                            if (X*X+Y*Y==Z*Z) {
                                System.out.println("x:"+x+" y:"+y+" z:"+z+" i:"+i);
                            }
                        }
                    }
                }
            }
        }
    }
}
