package uk.ac.cam.olb22.算法.排序;

import java.util.Random;

/**
 * Created by olive on 31/03/2017.
 */
public class 系列发生器 {
    public int[] 得随便数字系列(int 长短, long 种) {
        int[] 结果 = new int[长短];
        Random 个随便 = new Random(种);
        for (int 一 = 0; 一< 长短; 一++) {
            结果[一] = 个随便.nextInt(1000);
        }
        return 结果;
    }
}
