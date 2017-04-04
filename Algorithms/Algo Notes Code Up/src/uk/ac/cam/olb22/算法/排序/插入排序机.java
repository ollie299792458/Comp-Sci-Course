package uk.ac.cam.olb22.算法.排序;

/**
 * Created by olive on 31/03/2017.
 */
public class 插入排序机 extends 抽象排序机 {
    public static final String 名字 = "插入";

    public 插入排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

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
