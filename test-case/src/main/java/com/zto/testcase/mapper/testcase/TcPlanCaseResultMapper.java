package com.zto.testcase.mapper.testcase;

import com.zto.testcase.dto.TcPlanCaseListDTO;
import com.zto.testcase.model.TcPlanCaseResult;
import com.zto.testcase.request.TcPlanBatchEditReq;
import com.zto.testcase.request.TcPlanCaseEditReq;
import com.zto.testcase.request.TcPlanDetailReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TcPlanCaseResultMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TcPlanCaseResult record);

    int insertSelective(TcPlanCaseResult record);

    TcPlanCaseResult selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TcPlanCaseResult record);

    int updateByPrimaryKey(TcPlanCaseResult record);

    List<TcPlanCaseListDTO> getCaseListByPlanDetail(TcPlanDetailReq tcPlanDetailReq);

    List<TcPlanCaseListDTO> getCaseListByPlanId(Integer planId);

    TcPlanCaseListDTO getCaseByPlanIdAndCaseId(@Param(value = "planId") Integer planId,@Param(value = "caseId")Integer caseId);

    int batchEdit(TcPlanBatchEditReq tcPlanBatchEditReq);

    /**
     * 通过caseId获取所有有关联的
     * @param caseId
     * @return
     */
    List<Integer> getDistinctPlanId(Integer caseId);

    /**
     * 通过planId与caseId删除记录
     * @param planId
     * @param caseId
     * @return
     */
    int deleteByPlanIdAndCaseId(@Param("planId")Integer planId, @Param("caseId")Integer caseId);

    /**
     * 通过caseId及planId更新计划用例结果表
     * @param tcPlanCaseEditReq
     * @return
     */
    int updateByCaseIdAndPlanId(TcPlanCaseEditReq tcPlanCaseEditReq);

    /**
     * 通过用例id反查所有未完成的执行计划
     * @param caseId
     * @return
     */
    List<Integer> getUncomplatePlan(Integer caseId);

    /**
     * 通过版本id查询所有用例数量
     * @param pmsVersionId
     * @return
     */
    Integer getTotalCaseNumByVersionId(Integer pmsVersionId);
}