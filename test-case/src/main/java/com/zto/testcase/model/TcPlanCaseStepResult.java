package com.zto.testcase.model;

import lombok.Data;

@Data
public class TcPlanCaseStepResult  extends BasePO {
    private Long id;

    private Integer planId;

    private Integer caseId;

    private Integer stepId;

    private Integer result;

    private String actualResult;

}