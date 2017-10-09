package uk.ac.cam.olb22.algo.Chinese.算法.动态规划;

import java.util.Arrays;

/**
 * Created by olive on 07/04/2017.
 */
public class 斐波那契数字 {
    int[] 动态规划表格 = new int[0];

    public String 打印到一千() {
        StringBuilder 结果 = new StringBuilder("");
        for (int 一 = 0; 一 <= 1000; 一++) {
            结果.append(" ").append(Integer.toString(得(一)));
        }
        return 结果.toString();
    }

    private int 得(int 一) {
        if (一 == 0 || 一 == 1) {
            return 1;
        }

        if (一 >= 动态规划表格.length) {
            动态规划表格 = Arrays.copyOf(动态规划表格, 一*2);
        }

        if (动态规划表格[一] == 0) {
            动态规划表格[一] = 得(一 - 2) + 得(一 - 1);
        }
        return 动态规划表格[一];
    }
}
