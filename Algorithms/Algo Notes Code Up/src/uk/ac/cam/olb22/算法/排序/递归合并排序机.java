package uk.ac.cam.olb22.算法.排序;

import java.util.Arrays;

/**
 * Created by olive on 31/03/2017.
 */
public class 递归合并排序机 extends 抽象排序机 {
    private static final String 名字 = "递归合并";

    public 递归合并排序机(int 长短, int 开始, int 结束, int 种) {
        super(长短, 开始, 结束, 种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;
        排序了系列 = 合并排序(啊);
    }

    private int[] 合并排序(int[] 啊) {

        if (啊.length < 2) {
            return 啊;
        }

        int 哈 = 啊.length / 2;
        int[] 啊一部 = 合并排序(Arrays.copyOfRange(啊, 0, 哈));
        int[] 啊二部 = 合并排序(Arrays.copyOfRange(啊, 哈, 啊.length));

        int 一部一 = 0;
        int 二部一 = 0;
        int 全部一 = 0;

        while ((一部一 < 啊一部.length) || (二部一 < 啊二部.length)) {

            int 最小 = Integer.MAX_VALUE;

            int 一部最低 = Integer.MAX_VALUE;
            int 二部最低 = Integer.MAX_VALUE;

            if (一部一 < 啊一部.length) {

                一部最低 = 啊一部[一部一];
            }

            if (二部一 < 啊二部.length) {

                二部最低 = 啊二部[二部一];
            }

            if (一部最低 < 二部最低) {

                最小 = 一部最低;

                一部一++;

            } else {

                最小 = 二部最低;

                二部一++;
            }

            啊[全部一] = 最小;
            全部一++;
        }

        return 啊;
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
