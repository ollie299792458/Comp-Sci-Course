package uk.ac.cam.olb22.algo.Chinese.算法.排序;

/**
 * Created by olive on 03/04/2017.
 */
public class 算数排序机 extends 抽象排序机 {
    private static final String 名字 = "算数";

    public 算数排序机(int 长短, int 开始, int 结束, int 种) {
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

        int[] 算数 = new int[最大 + 1 - 最小];

        for (int 一 = 0; 一 < 啊.length; 一++) {
            算数[啊[一]-最小]++;
        }

        int 二 = 0;

        for (int 一 = 0; 一 < 算数.length; 一++) {
            while (算数[一] > 0) {
                啊[二] = 最小+一;
                二++;
                算数[一]--;
            }
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }

}
