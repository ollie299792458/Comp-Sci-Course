package uk.ac.cam.olb22.algo.数据结构;

/**
 * Created by olive on 09/04/2017.
 */
public class 简单单子 implements 单子 {
    private 单子分子 头;

    private 简单单子(单子分子 单子分子们) {
        头 = 单子分子们;
    }

    @Override
    public boolean 是空的() {
        return 头 == null;
    }

    @Override
    public Object 头() {
        return 头.分子;
    }

    @Override
    public void 前加(Object 东西) {
        单子分子 新分子 = new 单子分子();
        新分子.分子 = 东西;
        新分子.下个 = 头;
        头 = 新分子;
    }

    @Override
    public 单子 尾() {
        return new 简单单子(头.下个);
    }

    @Override
    public void 改尾(单子 新尾) {
        if (新尾 instanceof 简单单子) {
            头.下个 = ((简单单子) 新尾).头;
        } else {
            throw new ListException("Unsupported tail type");
        }
    }

    private class 单子分子 {
        Object 分子;
        单子分子 下个;
    }

    private class ListException extends RuntimeException {
        ListException(String s) {
            super(s);
        }
    }
}
