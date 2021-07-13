package com.zto.testcase.request;

import lombok.Data;

@Data
public class TcPlanListReq extends PageReq {

    private Integer listType;

    private Integer state;

    private String planName;

    private Integer pmsVersionId;

    private Integer stageId;

}
