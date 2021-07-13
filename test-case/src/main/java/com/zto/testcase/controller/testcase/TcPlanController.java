package com.zto.testcase.controller.testcase;

import com.github.pagehelper.PageInfo;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.model.TcPlan;
import com.zto.testcase.model.TcTestCaseModule;
import com.zto.testcase.request.*;
import com.zto.testcase.response.Result;
import com.zto.testcase.response.TcPlanCaseDetailRes;
import com.zto.testcase.response.TcPlanDetailRes;
import com.zto.testcase.response.TcPlanListRes;
import com.zto.testcase.service.TcPlanService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/plan")
public class TcPlanController {

    @Resource
    private TcPlanService tcPlanService;

    /**
     * 新建/编辑执行计划
     *
     * @param tcPlan
     * @return
     */
    @PostMapping("/addOrUpdatePlan")
    public Result<Boolean> addOrUpdatePlan(@RequestBody TcPlan tcPlan) {
        if (StringUtils.isBlank(tcPlan.getPlanName())
                || tcPlan.getVid() == null
                || tcPlan.getExecuteUser() == null
                || tcPlan.getStageId() == null
                || StringUtils.isBlank(tcPlan.getUser())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.addOrUpdatePlan(tcPlan);
    }

    /**
     * 删除执行计划
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deletePlan/{id}")
    public Result<Boolean> deletePlan(@PathVariable("id") Integer id) {
        return tcPlanService.deletePlan(id);
    }

    /**
     * 执行计划列表
     *
     * @param tcPlanListReq
     * @return
     */
    @GetMapping("/planList")
    public Result<PageInfo<TcPlanListRes>> planList(TcPlanListReq tcPlanListReq) {
        if (tcPlanListReq.getPageSize() == null
                || tcPlanListReq.getPageNo() == null
                || tcPlanListReq.getListType() == null
                || StringUtils.isBlank(tcPlanListReq.getUser())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.planList(tcPlanListReq);
    }

    /**
     * 获取执行计划状态数量
     *
     * @param tcPlanListReq
     * @return
     */
    @GetMapping("/getPlanCount")
    public Result<TcPlanListRes> getPlanCount(TcPlanListReq tcPlanListReq) {
        if (tcPlanListReq.getListType() == null) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.getPlanCount(tcPlanListReq);
    }

    /**
     * 执行计划详情
     * @param tcPlanDetailReq
     * @return
     */
    @GetMapping("/planDetail")
    public Result<TcPlanDetailRes> planDetail(TcPlanDetailReq tcPlanDetailReq) {
        if (tcPlanDetailReq.getPlanId() == null
                || tcPlanDetailReq.getShowChildren() == null
                || StringUtils.isBlank(tcPlanDetailReq.getOrderBy())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.planDetail(tcPlanDetailReq);
    }

    /**
     * 执行计划详情(质控调用)
     * @param tcPlanDetailReq
     * @return
     */
    @GetMapping("/planDetailToQa")
    public Result<TcPlanDetailRes> planDetailToQa(TcPlanDetailReq tcPlanDetailReq) {
        if (tcPlanDetailReq.getPlanId() == null
                || tcPlanDetailReq.getShowChildren() == null
                || StringUtils.isBlank(tcPlanDetailReq.getOrderBy())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.planDetailToQa(tcPlanDetailReq);
    }

    /**
     * 更新执行计划测试状态
     *
     * @param tcPlanDetailReq
     * @return
     */
    @PostMapping("/planState")
    public Result<Boolean> planState(@RequestBody TcPlanDetailReq tcPlanDetailReq) {
        if (tcPlanDetailReq.getId() == null
                || tcPlanDetailReq.getState() == null
                || StringUtils.isEmpty(tcPlanDetailReq.getUser())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.planState(tcPlanDetailReq);
    }

    /**
     * 批量更新执行人/执行结果
     *
     * @param tcPlanBatchEditReq
     * @return
     */
    @PostMapping("/batchEdit")
    public Result<Boolean> batchEdit(@RequestBody TcPlanBatchEditReq tcPlanBatchEditReq) {
        if (tcPlanBatchEditReq.getPlanId() == null
                || tcPlanBatchEditReq.getCaseList() == null
                || tcPlanBatchEditReq.getCaseList().isEmpty()
                || (tcPlanBatchEditReq.getExecuteUser() == null && tcPlanBatchEditReq.getResult() == null)
                || StringUtils.isBlank(tcPlanBatchEditReq.getUser())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.batchEdit(tcPlanBatchEditReq);
    }

    /**
     * 执行计划 关联/移除 测试用例
     *
     * @param tcPlanAssociateReq
     * @return
     */
    @PostMapping("/associateCase")
    public Result<Boolean> associateCase(@RequestBody TcPlanAssociateReq tcPlanAssociateReq) {
        if (tcPlanAssociateReq.getCaseList() == null
                || tcPlanAssociateReq.getCaseList().isEmpty()
                || tcPlanAssociateReq.getPlanId() == null
                || tcPlanAssociateReq.getType() == null
                || StringUtils.isBlank(tcPlanAssociateReq.getUser())) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.associateCase(tcPlanAssociateReq);
    }

    /**
     * 执行计划-测试用例-步骤结果详情
     *
     * @param tcPlanCaseDetailReq
     * @return
     */
    @GetMapping("/caseDetail")
    public Result<TcPlanCaseDetailRes> caseDetail(TcPlanCaseDetailReq tcPlanCaseDetailReq) {
        if (tcPlanCaseDetailReq.getCaseId() == null || tcPlanCaseDetailReq.getPlanId() == null) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.caseDetail(tcPlanCaseDetailReq);
    }

    /**
     * 维护 执行计划-测试用例-步骤结果
     *
     * @param tcPlanCaseEditReq
     * @return
     */
    @PostMapping("/caseEdit")
    public Result<Boolean> caseEdit(@RequestBody TcPlanCaseEditReq tcPlanCaseEditReq) {
        if (tcPlanCaseEditReq.getPlanId() == null
                || tcPlanCaseEditReq.getCaseId() == null
                || tcPlanCaseEditReq.getResult() == null) {
            return Result.error(ErrorCodeEnum.REQUEST_PARAM_ERROR);
        }
        return tcPlanService.caseEdit(tcPlanCaseEditReq);
    }

    /**
     * 提供给质控 - 获取集成测试列表
     * @return
     */
    @GetMapping("/integrationTestToQa")
    public Result<List<TcTestCaseModule>> getIntegrationTestToQa(@RequestParam("parentId")Integer parentId){
        return Result.success(tcPlanService.getIntegrationTestToQa(parentId));
    }


    /**
     * 统计一不同时间内创建计划数或者不同人创建计划数
     *
     * @param tcPlanCaseStatisticReq
     * @return
     */
    @PostMapping("/planCount")
    public Result<List<Map<String,Long>>> planCount(@RequestBody TcPlanCaseStatisticReq tcPlanCaseStatisticReq) {
        return Result.success(tcPlanService.planCount(tcPlanCaseStatisticReq));
    }

    /**
     * 统计一定查询时间内，所有或者特定的测试负责人所创建的计划数，用例数，及当前系统中总计划数，用例数
     *
     * @param tcPlanCaseStatisticReq
     * @return
     */
    @PostMapping("/planAndCaseCount")
    public Result<Map<String,Long>> planAndCaseCount(@RequestBody TcPlanCaseStatisticReq tcPlanCaseStatisticReq) {
        return Result.success(tcPlanService.planAndCaseCount(tcPlanCaseStatisticReq));
    }
}
