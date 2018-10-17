package uk.ac.cam.olb22.own.tpp.twenty19;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuestionA {
    public QuestionA() {
        List<String> all = new LinkedList<>();
        for (int a = 0; a <= 9; a++) {
            for (int b = 0; b <= 9; b++) {
                if (b!=a) {
                    for (int c = 0; c <= 9; c++) {
                        if (c!=a&&c!=b) {
                            for (int d = 0; d <= 9; d++) {
                                if (d!=c&&d!=b&&d!=a) {
                                    for (int e = 0; e <= 9; e++) {
                                        if (e!=d&&e!=c&&e!=b&&e!=a) {
                                            for (int f = 0; f <= 9; f++) {
                                                if (f!=e&&f!=d&&f!=c&&f!=b&&f!=a) {
                                                    for (int g = 0; g <= 9; g++) {
                                                        if (g!=f&&g!=e&&g!=d&&g!=c&&g!=b&&g!=a) {
                                                            for (int h = 0; h <= 9; h++) {
                                                                if (h!=g&&h!=f&&h!=e&&h!=d&&h!=c&&h!=b&&h!=a) {
                                                                    for (int i = 0; i <= 9; i++) {
                                                                        if (i!=h&&i!=g&&i!=f&&i!=e&&i!=d&&i!=c&&i!=b&&i!=a) {
                                                                            for (int j = 0; j <= 9; j++) {
                                                                                if (j!=i&&j!=h&&j!=g&&j!=f&&j!=e&&j!=d&&j!=c&&j!=b&&j!=a) {
                                                                                    all.add(a+""+b+""+c+""+d+""+e+""+f+""+g+""+h+""+i+""+j);
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
                }
            }
        }
        System.out.println(all.get(1000000));
    }
}
