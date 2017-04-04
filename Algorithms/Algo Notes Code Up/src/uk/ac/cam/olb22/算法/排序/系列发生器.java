package uk.ac.cam.olb22.算法.排序;

import java.util.Random;

/**
 * Created by olive on 31/03/2017.
 */
public class 系列发生器 {
    public int[] 得随便数字系列(int 长短, int 开始, int 结束, long 种) {
        int[] 结果 = new int[长短];
        Random 个随便 = new Random(种);
        for (int 一 = 0; 一< 长短; 一++) {
            结果[一] = 开始 + 个随便.nextInt(结束-开始);
        }
        return 结果;
    }
}
