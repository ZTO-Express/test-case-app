package com.zto.testcase.dto;

import lombok.Data;

@Data
public class TcPlanStepListDTO {

    /**
     * 步骤结果表主键id
     */
    private Long id;

    /**
     * 步骤描述
     */
    private String stepDesc;

    /**
     * 预期结果
     */
    private String expectResult;

    /**
     * 实际结果
     */
    private String actualResult;

    /**
     * 执行结果，0-未执行，1-通过，2-失败，3-阻塞，4-跳过
     */
    private Integer result;

    /**
     * 步骤序号
     */
    private Integer stepNumber;
}
