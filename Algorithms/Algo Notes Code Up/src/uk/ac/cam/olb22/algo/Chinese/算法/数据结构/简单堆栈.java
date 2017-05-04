package uk.ac.cam.olb22.algo.Chinese.算法.数据结构;

/**
 * Created by olive on 08/04/2017.
 */
public class 简单堆栈 implements 堆栈 {
    private 堆栈分子 上面;

    @Override
    public boolean 是空的() {
        return 上面 != null;
    }

    @Override
    public void 推进(Object 东西) {
        堆栈分子 新上面 = new 堆栈分子();
        新上面.下面 = 上面;
        上面 = 新上面;
    }

    @Override
    public Object 弹出() {
        Object 旧上面分子 = 上面.分子;
        上面 = 上面.下面;
        return 旧上面分子;
    }

    @Override
    public Object 上面() {
        return 上面.分子;
    }

    private class 堆栈分子 {
        Object 分子;
        堆栈分子 下面;
    }
}
