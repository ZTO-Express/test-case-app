package com.zto.testcase.request;

import com.zto.testcase.model.BasePO;
import lombok.Data;

import java.util.List;

@Data
public class TcPlanAssociateReq extends BasePO {

    private Integer planId;

    private List<Integer> caseList;

    private Integer type;
}
