package com.zto.testcase.mapper.testcase;

import com.zto.testcase.dto.TcPlanStepListDTO;
import com.zto.testcase.model.TcPlanCaseStepResult;
import com.zto.testcase.request.TcPlanCaseDetailReq;

import java.util.List;

public interface TcPlanCaseStepResultMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TcPlanCaseStepResult record);

    int insertSelective(TcPlanCaseStepResult record);

    TcPlanCaseStepResult selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TcPlanCaseStepResult record);

    int updateByPrimaryKey(TcPlanCaseStepResult record);

    /**
     * 获取实际应存步骤结果
     * @param planId
     * @return
     */
    List<TcPlanCaseStepResult> getActualStepListByPlanId(Integer planId);

    /**
     * 获取当前步骤结果表结果
     * @param planId
     * @return
     */
    List<TcPlanCaseStepResult> getSavedStepListByPlanId(Integer planId);

    /**
     * 通过主键id列表删除
     * @param list
     * @return
     */
    int deleteByIds(List<Long> list);

    /**
     * 通过planId和caseId获取步骤结果
     * @param tcPlanCaseDetailReq
     * @return
     */
    List<TcPlanStepListDTO> getResultByPlanIdAndCaseId(TcPlanCaseDetailReq tcPlanCaseDetailReq);

}