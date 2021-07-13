package com.zto.testcase.response;

import com.github.pagehelper.PageInfo;
import com.zto.testcase.dto.TcPlanCaseListDTO;
import com.zto.testcase.model.TcTestCaseModule;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class TcPlanDetailRes {

    /**
     * 执行计划主键id
     */
    private Integer id;

    /**
     * 测试状态，0-未开始，1-进行中，2-已完成
     */
    private Byte state;

    /**
     * 通过率
     */
    private BigDecimal passRate;

    /**
     * 已测数量
     */
    private Integer executeNum;

    /**
     * 总数量
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
     * 用例列表
     */
    private PageInfo<TcPlanCaseListDTO> caseList;

    /**
     * 模块树列表
     */
    private Set<TcTestCaseModule> moduleList;
}
