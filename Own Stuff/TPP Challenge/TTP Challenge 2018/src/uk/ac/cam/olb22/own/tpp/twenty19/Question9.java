package uk.ac.cam.olb22.own.tpp.twenty19;

public class Question9 {
    public Question9() {
        System.out.println("Question 9");
        int MAX = 5000;
        for (int i = 0; i < MAX; i++) {
            int totalarea = i;
            double area = 0;//todo
            double p = area / (double) totalarea;
            if (p < 0.001) {
                System.out.println(i);
                break;
            }
        }
    }
}
