package uk.ac.cam.olb22.算法.排序;

import java.util.LinkedList;

/**
 * Created by olive on 04/04/2017.
 */
public class 根数排序机 extends 抽象排序机 {
    private static final String 名字 = "根数";

    public 根数排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

        int 最大 = Integer.MIN_VALUE;
        for (int 数字 : 啊) {
            if (数字 > 最大) {
                最大 = 数字;
            }
        }

        int 根数 = 1;
        LinkedList<Integer>[] 算数们;
        int 根数最小 = -9;

        while (根数 < 最大 * 10) {
            算数们 = new LinkedList[20];

            for (int 一 = 0; 一 < 算数们.length; 一++) {
                算数们[一] = new LinkedList<>();
            }

            for (int 一 = 0; 一 < 啊.length; 一++) {
                int 数字 = (啊[一] / 根数) % 10;
                算数们[数字 - 根数最小].add(啊[一]);
            }

            int 二 = 0;

            for (int 一 = 0; 一 < 算数们.length; 一++) {
                while (!算数们[一].isEmpty()) {
                    啊[二] = 算数们[一].getFirst();
                    二++;
                    算数们[一].removeFirst();
                }
            }

            根数 = 根数 * 10;
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
