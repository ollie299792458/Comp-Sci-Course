package uk.ac.cam.olb22.algo.数据结构;

/**
 * Created by olive on 08/04/2017.
 */
public interface 堆栈 {
    /**
     * @return 在而且只在而且对战是空的时还true
     */
    boolean 是空的();

    /**
     * 活动: 把"东西"放在堆栈的上面
     * 先决条件: 是空的() = false
     * 后置条件: 上面() = 东西
     * @param 东西 新的上面
     */
    void 推进(Object 东西);

    /**
     * 活动: 还"上面()", 把上面删掉
     * 先决条件: 是空的() = false
     * @return 上面
     */
    Object 弹出();

    /**
     * 活动: 还"上面()" (不删)
     * 先决条件: 是空的() = false
     * @return 上面
     */
    Object 上面();
}
