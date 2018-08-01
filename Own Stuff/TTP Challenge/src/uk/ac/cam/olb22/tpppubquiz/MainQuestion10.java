package uk.ac.cam.olb22.tpppubquiz;

public class MainQuestion10 {
    public static void main(String[] args) {
        for (int s = 0; s < 10; s++) {
            for (int y = 0; y < 10; y++) {
                if (y != s) {
                    if (y == (s + s) % 10) {
                        for (int e = 0; e < 10; e++) {
                            if (e != y && e != s) {
                                for (int r = 0; r < 10; r++) {
                                    if (r != e && r != y && r != s) {
                                        if (r * 10 + y == (s + s + (e + e)*10) % 10) {
                                            for (int o = 0; o < 10; o++) {
                                                if (o != e && o != r && 0 != y && o !=s) {
                                                    for (int n = 0; n < 10; n++) {
                                                        if (n != o && n != e && n != r && n != s && n != y) {
                                                            for (int a = 0; a < 10; a++) {

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
