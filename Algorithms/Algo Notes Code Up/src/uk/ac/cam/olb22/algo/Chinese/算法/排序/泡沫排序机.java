package uk.ac.cam.olb22.algo.Chinese.算法.排序;

/**
 * Created by olive on 31/03/2017.
 */
public class 泡沫排序机 extends 抽象排序机 {
    private static final String 名字 = "泡沫";

    public 泡沫排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

        boolean 交换了 = true;

        while (交换了) {

            交换了 = false;

            for (int 一 = 0; 一 < 啊.length-1; 一++){
                if (啊[一] > 啊[一+1]) {
                    交换(啊, 一, 一 + 1);
                    交换了 = true;
                }
            }
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
