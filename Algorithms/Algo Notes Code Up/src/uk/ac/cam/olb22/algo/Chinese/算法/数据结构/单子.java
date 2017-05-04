package uk.ac.cam.olb22.algo.Chinese.算法.数据结构;

/**
 * Created by olive on 08/04/2017.
 */
public interface 单子 {
    /**
     * @return 在而且只在而且对战是空的时还true
     */
    boolean 是空的();

    /**
     * 活动: 还单子的头
     * 先决条件: 是空的() = false
     * @return 头
     */
    Object 头();

    /**
     * 活动: 把"东西"放在单子的前面
     * 后置条件: 是空的() = false
     * @param 东西 新的头
     */
    void 前加(Object 东西);

    /**
     * 活动: 还全单子除了头, 不会删头
     * 先决条件: 是空的() = false
     * @return 尾
     */
    单子 尾();

    /**
     * 活动: 除了头, 把全单子改成"新尾"
     * 先决条件: 是空的() = false
     * @param 新尾 新的尾
     */
    void 改尾(单子 新尾);
}
