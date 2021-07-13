package com.zto.testcase.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TcPlanListRes {

    private Integer id;

    /**
     * 执行计划名称
     */
    private String planName;

    /**
     * 负责人id
     */
    private Integer executeUser;

    /**
     * 负责人姓名
     */
    private String executeUserName;

    /**
     * 测试状态：0-未开始，1-进行中，2-已完成
     */
    private Integer state;

    /**
     * 通过率
     */
    private BigDecimal passRate;

    /**
     * 失败率
     */
    private BigDecimal failRate;

    /**
     * 阻塞率
     */
    private BigDecimal blockRate;

    /**
     * 用例执行数量
     */
    private Integer executeNum;

    /**
     * 用例总数量
     */
    private Integer totalNum;

    /**
     * 通过数量
     */
    private Integer passNum;

    /**
     * 失败数量
     */
    private Integer failNum;

    /**
     * 阻塞数量
     */
    private Integer blockNum;

    /**
     * 跳过数量
     */
    private Integer skipNum;

    /**
     * 测试阶段
     */
    private String stageName;

    /**
     * 测试阶段id
     */
    private Integer stageId;

    /**
     * 关联部门
     */
    private String bname;

    /**
     * 关联项目
     */
    private String pname;

    /**
     * 关联版本
     */
    private String vname;

    /**
     * 部门id
     */
    private Integer bid;

    /**
     * 项目id
     */
    private Integer pid;

    /**
     * 版本id
     */
    private Integer vid;

    /**
     * 未开始数
     */
    private Integer initCount;

    /**
     * 进行中数
     */
    private Integer inHandCount;

    /**
     * 已完成数
     */
    private Integer doneCount;

    private String createUser;

    private String updateUser;

}
