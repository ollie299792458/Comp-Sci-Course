package uk.ac.cam.olb22.算法.分类;

/**
 * Created by olive on 31/03/2017.
 */
public class 二进插入排序机 extends 抽象排序机 {
    private static final String 名字 = "二进插入";

    public 二进插入排序机(int 种) {
        super(种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

        for (int 一= 1; 一 < 啊.length; 一++) {

            int 最小 = 0;
            int 最大 = 一 - 1;
            int 位置 = -1;

            while (最大 >= 最小) {

                位置 = (最大+最小) / 2;

                if (啊[一] < 啊[位置]) {

                    最大 = 位置 - 1;

                } else if (啊[一] >= 啊[位置]) {

                    最小 = 位置 + 1;

                } else {

                    break;
                }
            }

            if (位置 != 一) {

                int 暂时 = 啊[一];

                for (int 二 = 一 - 1; 二 > 位置 - 1; 二--) {
                    啊[二+1] = 啊[二];
                }
                啊[位置] = 暂时;
            }
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
