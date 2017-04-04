package uk.ac.cam.olb22.算法.排序;

/**
 * Created by olive on 02/04/2017.
 */
public class 堆排序机 extends 抽象排序机 {
    private static final String 名字 = "堆";

    public 堆排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

        for (int 一 = 啊.length / 2; 一 >= 0; 一--) {
            做堆(啊, 啊.length, 一);
        }

        for (int 一 = 啊.length; 一 >= 1; 一--) {
            交换(啊, 0, 一 - 1);
            做堆(啊, 一-1, 0);
        }
    }

    private void 做堆(int[] 啊, int 最后, int 本) {
        int 左孩 = 2*本+1;
        int 右孩 = 2*本+2;

        if (((左孩 >= 最后)||(啊[本] >= 啊[左孩]))&&((右孩 >= 最后)||(啊[本] >= 啊[右孩]))) {
            return;
        } else {
            int 最大的孩子 = Integer.MIN_VALUE;

            if (左孩 < 最后) {
                最大的孩子 = 左孩;
            }
            if (右孩 < 最后) {
                if (啊[右孩] > 啊[最大的孩子]) {
                    最大的孩子 = 右孩;
                }
            }
            交换(啊, 本, 最大的孩子);
            做堆(啊, 最后, 最大的孩子);
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
