package com.zto.testcase.request;

import lombok.Data;

import java.util.List;

@Data
public class TcPlanDetailReq extends PageReq {

    /**
     * 执行计划主键id
     */
    private Integer planId;

    /**
     * 模块id
     */
    private Integer moduleId;

    /**
     * 模块id列表
     */
    private List<Integer> moduleIdList;

    /**
     * 是否显示子模块，0-否，1-是
     */
    private Integer showChildren;

    /**
     * 排序
     */
    private String orderBy;

    /**
     * 筛选框：ID
     */
    private Integer id;

    /**
     * 筛选框：用例名称
     */
    private String caseName;

    /**
     * 优先级,1 低 2 中 3 高
     */
    private Integer priority;

    /**
     * 执行结果:0-未执行，1-通过，2-失败，3-阻塞，4-跳过
     */
    private Integer result;

    /**
     * 测试状态，0-未开始，1-进行中，2-已完成
     */
    private Byte state;

    /**
     * 更新人
     */
    private String user;

    /**
     * 用例类型
     */
    private Integer type;

}
