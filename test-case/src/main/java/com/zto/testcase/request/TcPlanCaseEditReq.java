package com.zto.testcase.request;

import com.zto.testcase.dto.TcPlanStepListDTO;
import com.zto.testcase.model.BasePO;
import lombok.Data;

import java.util.List;

@Data
public class TcPlanCaseEditReq extends BasePO {

    private List<TcPlanStepListDTO> stepList;

    private Integer planId;

    private Integer caseId;

    private Integer result;

    //执行结果备注
    private String resultComment;
}
