package uk.ac.cam.olb22.algo.Chinese;

import uk.ac.cam.olb22.algo.Chinese.算法.动态规划.斐波那契数字;
import uk.ac.cam.olb22.algo.Chinese.算法.排序.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<抽象排序机> 算法们 = new LinkedList<>();
        算法们.add(new 插入排序机(20, -1000, 1000, 432));
        算法们.add(new 选择排序机(20, -1000, 1000, 231));
        算法们.add(new 泡沫排序机(20, -1000, 1000, 692));
        算法们.add(new 二进插入排序机(20, -1000, 1000, 293));
        算法们.add(new 递归合并排序机(20, -1000, 1000, 432));
        算法们.add(new 从下合并排序机(20, -1000, 1000, 789));
        算法们.add(new 快速排序机(20, -1000, 1000, 892));
        算法们.add(new 堆排序机(20, -1000, 1000, 923));
        算法们.add(new 算数排序机(20, -5, 5, 129));
        算法们.add(new 水桶排序机(20, -1000, 1000, 823));
        算法们.add(new 根数排序机(20, -1000, 1000, 328));

        for (抽象排序机 算法 : 算法们) {
            打印结果(算法);
        }

        斐波那契数字 个斐波那契数子 = new 斐波那契数字();
        System.out.println("动态规划 斐波那契数子");
        System.out.println(个斐波那契数子.打印到一千());
        System.out.println();
    }

    private static void 打印结果(抽象排序机 排序机) {
        System.out.println(排序机.得名字());
        排序机.排序();
        System.out.println("“"+ Arrays.toString(排序机.原来系列) +"”");
        System.out.println("变成：");
        System.out.println("”"+ Arrays.toString(排序机.排序了系列) +"“");
        System.out.println();
    }
}
