package com.zto.testcase.model;

import lombok.Data;

@Data
public class TcPlanCaseResult extends BasePO {
    private Long id;

    private Integer planId;

    private Integer caseId;

    private Byte result;

    private Integer executeUser;

}