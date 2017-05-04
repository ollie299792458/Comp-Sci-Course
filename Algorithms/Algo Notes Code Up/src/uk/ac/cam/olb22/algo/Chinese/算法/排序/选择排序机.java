package uk.ac.cam.olb22.algo.Chinese.算法.排序;

/**
 * Created by olive on 31/03/2017.
 */
public class 选择排序机 extends 抽象排序机 {
    public static final String 名字 = "选择";

    public 选择排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

        for (int 一 = 0; 一 < 啊.length; 一++) {

            int 最小 = 一;

            for (int 二 = 最小 + 1; 二 < 啊.length; 二++) {

                if (啊[二] < 啊[最小]) {
                    最小 = 二;
                }
            }
            交换(啊,一,最小);
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
