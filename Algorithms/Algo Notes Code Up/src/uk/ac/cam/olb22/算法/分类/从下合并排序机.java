package uk.ac.cam.olb22.算法.分类;

import java.util.Arrays;

/**
 * Created by olive on 01/04/2017.
 */
public class 从下合并排序机 extends 抽象排序机 {
    private static final String 名字 = "从下合并";

    public 从下合并排序机(int 种) {
        super(种);
    }

    @Override
    public void 排序() {
        int[] 啊 = 排序了系列;

        int 块大小 = 2;

        while (块大小 <= 啊.length) {

            int 多少块 = (啊.length + 块大小 - 1) / 块大小;

            for (int 块 = 0; 块 < 多少块; 块++) {

                int 一部一 = 0;
                int 二部一 = 0;
                int 全部一 = 块*块大小;

                int 快结束点 = Math.min(全部一 + 块大小, 啊.length);
                int 快的一半 = Math.min(全部一 + 块大小/2, (啊.length + 全部一) / 2);

                int[] 啊一部 = Arrays.copyOfRange(啊, 全部一, 快的一半);
                int[] 啊二部 = Arrays.copyOfRange(啊, 快的一半, 快结束点);

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
            }

            块大小 = 块大小 * 2;
        }
    }

    @Override
    public String 得名字() {
        return 名字;
    }
}
