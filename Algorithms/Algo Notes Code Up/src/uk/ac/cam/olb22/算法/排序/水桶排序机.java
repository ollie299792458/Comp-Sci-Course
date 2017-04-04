package uk.ac.cam.olb22.算法.排序;

import java.util.LinkedList;

/**
 * Created by olive on 04/04/2017.
 */
public class 水桶排序机 extends 抽象排序机{
    private static final String 名字 = "水桶";

    public 水桶排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

        int 最小 = Integer.MAX_VALUE;
        int 最大 = Integer.MIN_VALUE;

        for (int 一 = 0; 一 < 啊.length; 一++) {
            if (啊[一] > 最大) {
                最大 = 啊[一];
            }
            if (啊[一] < 最小) {
                最小 = 啊[一];
            }
        }

        double 桶大小 = (double) (最大+1-最小)/(double) 啊.length;

        LinkedList<Integer>[] 桶们 = new LinkedList[啊.length];
        for (int 一 = 0; 一 < 桶们.length; 一++) {
            桶们[一] = new LinkedList<>();
        }

        for (int 一 = 0; 一 < 啊.length; 一++) {
            int 位置 = (int) Math.floor((啊[一]-最小)/桶大小);
            桶们[位置].add(啊[一]);
        }

        int 三 = 0;

        for (int 一 = 0; 一 < 桶们.length; 一++) {
            LinkedList<Integer> 桶 = 桶们[一];

            for (int 数 : 桶) {
                啊[三] = 数;
                三++;
            }
        }

        for (int 一 = 1; 一 < 啊.length; 一++) {

            int 二 = 一 - 1;

            while (二 >= 0 && 啊[二] > 啊[二+1]) {

                交换(啊, 二,二 + 1);

                二 = 二 - 1;
            }
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
