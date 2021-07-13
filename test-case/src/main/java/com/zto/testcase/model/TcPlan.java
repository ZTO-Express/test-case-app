package com.zto.testcase.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TcPlan extends BasePO {
    private Integer id;

    private String planName;

    private Byte state;

    private Integer vid;

    private Integer stageId;

    private Integer executeUser;

    private Integer totalNum;

    private Integer executeNum;

    private Integer passNum;

    private Integer failNum;

    private Integer blockNum;

    private Integer skipNum;

    private BigDecimal passRate;

    private Byte status;


}