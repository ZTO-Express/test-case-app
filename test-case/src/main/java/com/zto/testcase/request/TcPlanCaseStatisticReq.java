package com.zto.testcase.request;
import lombok.Data;

@Data
public class TcPlanCaseStatisticReq {

    /**
     * 分组类型，0：周期，1：负责人
     * */
    private Integer groupType;

    private String startDate;

    private String endDate;

    private String createUser;

}
