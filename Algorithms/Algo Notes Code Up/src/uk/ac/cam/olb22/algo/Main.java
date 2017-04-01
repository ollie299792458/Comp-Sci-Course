package uk.ac.cam.olb22.algo;

import uk.ac.cam.olb22.算法.排序.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<抽象排序机> 算法们 = new LinkedList<>();
        算法们.add(new 插入排序机(432));
        算法们.add(new 选择排序机(231));
        算法们.add(new 泡沫排序机(692));
        算法们.add(new 二进插入排序机(293));
        算法们.add(new 递归合并排序机(432));
        算法们.add(new 从下合并排序机(789));

        for (抽象排序机 算法 : 算法们) {
            打印结果(算法);
        }
    }

    private static void 打印结果(抽象排序机 排序机) {
        System.out.println(排序机.得名字());
        排序机.排序();
        排序机.改长短(3000);
        System.out.println("“"+ Arrays.toString(排序机.原来系列) +"”");
        System.out.println("变成：");
        System.out.println("”"+ Arrays.toString(排序机.排序了系列) +"“");
        System.out.println();
    }
}
