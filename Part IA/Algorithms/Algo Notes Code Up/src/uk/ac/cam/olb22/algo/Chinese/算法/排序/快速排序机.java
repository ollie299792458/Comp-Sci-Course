package uk.ac.cam.olb22.algo.Chinese.算法.排序;

/**
 * Created by olive on 02/04/2017.
 */
public class 快速排序机 extends 抽象排序机{

    private static final String 名字 = "快速";

    public 快速排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;
        快速排序(啊, 0, 啊.length);
    }

    private void 快速排序(int[] 啊, int 开始, int 结束) {
        if (开始 + 1 >= 结束) {
            return;
        }

        int 指针 = 结束 - 1;
        int 一左边 = 开始;
        int 一右边 = 指针 - 1;

        while (一左边 < 一右边) {
            boolean 细分指针改了 = true;

            while (细分指针改了&&一左边 != 一右边) {
                细分指针改了 = false;

                if (啊[一左边] < 啊[指针]) {
                    一左边++;
                    细分指针改了 = true;
                }

                if (啊[一右边] > 啊[指针]) {
                    一右边--;
                    细分指针改了 = true;
                }
            }

            交换(啊, 一右边, 一左边);
        }

        交换(啊, 一左边, 指针);

        快速排序(啊, 开始, 一左边);
        快速排序(啊,一右边 +1, 结束);
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
