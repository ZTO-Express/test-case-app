package com.zto.testcase.service;

import com.github.pagehelper.PageInfo;
import com.zto.testcase.model.TcPlan;
import com.zto.testcase.model.TcTestCaseModule;
import com.zto.testcase.request.TcPlanAssociateReq;
import com.zto.testcase.request.TcPlanBatchEditReq;
import com.zto.testcase.request.TcPlanCaseDetailReq;
import com.zto.testcase.request.TcPlanCaseEditReq;
import com.zto.testcase.request.TcPlanCaseStatisticReq;
import com.zto.testcase.request.TcPlanDetailReq;
import com.zto.testcase.request.TcPlanListReq;
import com.zto.testcase.response.Result;
import com.zto.testcase.response.TcPlanCaseDetailRes;
import com.zto.testcase.response.TcPlanDetailRes;
import com.zto.testcase.response.TcPlanListRes;
import java.util.List;
import java.util.Map;

public interface TcPlanService {

    /**
     * 新建/编辑执行计划
     *
     * @param tcPlan
     * @return
     */
    Result<Boolean> addOrUpdatePlan(TcPlan tcPlan);

    /**
     * 删除执行计划
     *
     * @param id
     * @return
     */
    Result<Boolean> deletePlan(Integer id);

    /**
     * 执行计划列表
     *
     * @param tcPlanListReq
     * @return
     */
    Result<PageInfo<TcPlanListRes>> planList(TcPlanListReq tcPlanListReq);

    Result<TcPlanListRes> getPlanCount(TcPlanListReq tcPlanListReq);

    /**
     * 执行计划详情
     *
     * @param tcPlanDetailReq
     * @return
     */
    Result<TcPlanDetailRes> planDetail(TcPlanDetailReq tcPlanDetailReq);

    /**
     * 执行计划详情(质控调用)
     *
     * @param tcPlanDetailReq
     * @return
     */
    Result<TcPlanDetailRes> planDetailToQa(TcPlanDetailReq tcPlanDetailReq);


    /**
     * 批量更新执行人/执行结果
     *
     * @param tcPlanBatchEditReq
     * @return
     */
    Result<Boolean> batchEdit(TcPlanBatchEditReq tcPlanBatchEditReq);

    /**
     * 执行计划 关联/移除 测试用例
     *
     * @param tcPlanAssociateReq
     * @return
     */
    Result<Boolean> associateCase(TcPlanAssociateReq tcPlanAssociateReq);

    /**
     * 执行计划-测试用例-步骤结果详情
     *
     * @param tcPlanCaseDetailReq
     * @return
     */
    Result<TcPlanCaseDetailRes> caseDetail(TcPlanCaseDetailReq tcPlanCaseDetailReq);

    /**
     * 维护 执行计划-测试用例-步骤结果
     *
     * @param tcPlanCaseEditReq
     * @return
     */
    Result<Boolean> caseEdit(TcPlanCaseEditReq tcPlanCaseEditReq);

    /**
     * 同步步骤结果
     *
     * @param caseId
     * @return
     */
    Result<Boolean> syncStepResult(Integer caseId);

    /**
     * 更新执行计划测试状态
     *
     * @param tcPlanDetailReq
     * @return
     */
    Result<Boolean> planState(TcPlanDetailReq tcPlanDetailReq);

    /**
     * 删除用例时关联操作
     *
     * @param caseIds
     * @return
     */
    Result<Boolean> deleteCaseEdit(List<Integer> caseIds);

    /**
     * 获取集成测试列表
     * @return
     */
    List<TcTestCaseModule> getIntegrationTestToQa(Integer parentId);

    /**
     * 统计一不同时间内创建计划数或者不同人创建计划数
     *
     * @param tcPlanCaseStatisticReq
     * @return
     */
    List<Map<String,Long>> planCount(TcPlanCaseStatisticReq tcPlanCaseStatisticReq);


    Map<String,Long>  planAndCaseCount(TcPlanCaseStatisticReq tcPlanCaseStatisticReq);
}
