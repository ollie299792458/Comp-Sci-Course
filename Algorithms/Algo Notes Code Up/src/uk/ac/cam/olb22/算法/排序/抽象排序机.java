package uk.ac.cam.olb22.算法.排序;

import java.util.Arrays;

/**
 * Created by olive on 31/03/2017.
 */
public abstract class 抽象排序机 {
    public 系列发生器 个系列发生器;
    public int[] 原来系列;
    public int[] 排序了系列;

    public void 交换(int[] 啊, int 一, int 二) {
        int 暂时 = 啊[一];
        啊[一] = 啊[二];
        啊[二] = 暂时;
    }

    public 抽象排序机(int 长短, int 开始, int 结束, int 种) {
        个系列发生器 = new 系列发生器();
        原来系列 = 个系列发生器.得随便数字系列(长短, 开始, 结束, 种);
        排序了系列 = Arrays.copyOf(原来系列, 原来系列.length);
    }

    public abstract void 排序();

    public abstract String 得名字();
}
