package com.zto.testcase.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zto.testcase.dto.RedisPrefixDto;
import com.zto.testcase.dto.TcPlanCaseListDTO;
import com.zto.testcase.dto.TcPlanStepListDTO;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.mapper.testcase.TcFileMapper;
import com.zto.testcase.mapper.testcase.TcPlanCaseResultMapper;
import com.zto.testcase.mapper.testcase.TcPlanCaseStepResultMapper;
import com.zto.testcase.mapper.testcase.TcPlanMapper;
import com.zto.testcase.mapper.testcase.TcTestCaseModuleMapper;
import com.zto.testcase.mapper.testcase.TcTestcaseMapper;
import com.zto.testcase.model.*;
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
import com.zto.testcase.service.TcPlanService;
import com.zto.testcase.util.RedisUtil;
import java.math.BigDecimal;
import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TcPlanServiceImpl implements TcPlanService {

    @Resource
    private TcPlanMapper tcPlanMapper;

    @Resource
    private TcPlanCaseResultMapper tcPlanCaseResultMapper;

    @Resource
    private TcPlanCaseStepResultMapper tcPlanCaseStepResultMapper;

    @Resource
    private TcTestcaseMapper tcTestcaseMapper;

    @Resource
    private TcFileMapper tcFileMapper;

    @Resource
    private TcTestCaseModuleMapper tcTestCaseModuleMapper;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public Result<Boolean> addOrUpdatePlan(TcPlan tcPlan) {
        Integer id = tcPlan.getId();
        if (id == null) {
            tcPlan.setCreateUser(tcPlan.getUser());
            tcPlan.setUpdateUser(tcPlan.getUser());
            tcPlanMapper.insertSelective(tcPlan);
            //通过标签关联相应测试用例
            String tags = tcPlanMapper.getCaseTag(tcPlan.getVid());
            if(StringUtils.isNotBlank(tags)){
                Set<Integer> caseSet = new HashSet<>();
                String[] versionTags = tags.split(",");
                //查询所有带tag的用例
                List<TcTestcase> caseList = tcTestcaseMapper.getAllTagCase();
                //三重嵌套
                for(TcTestcase tcTestcase : caseList){
                    boolean isExist = false;
                    String tag = tcTestcase.getTag();
                    String[] caseTags = tag.split(",");
                    for(String caseTag : caseTags){
                        for(String versionTag : versionTags){
                            if(caseTag.equals(versionTag)){
                                caseSet.add(tcTestcase.getId());
                                isExist = true;
                                break;
                            }
                        }
                        if(isExist){
                            break;
                        }
                    }
                }
                //关联对应用例
                TcPlanAssociateReq tcPlanAssociateReq = new TcPlanAssociateReq();
                tcPlanAssociateReq.setType(1);
                tcPlanAssociateReq.setPlanId(tcPlan.getId());
                tcPlanAssociateReq.setCaseList(new ArrayList<>(caseSet));
                tcPlanAssociateReq.setUser(tcPlan.getUser());
                associateCase(tcPlanAssociateReq);
            }
        } else {
            tcPlan.setCreateUser(null);
            tcPlan.setUpdateUser(tcPlan.getUser());
            tcPlanMapper.updateByPrimaryKeySelective(tcPlan);
        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean> deletePlan(Integer id) {
        TcPlan tcPlan = new TcPlan();
        tcPlan.setId(id);
        tcPlan.setStatus((byte) 0);
        tcPlanMapper.updateByPrimaryKeySelective(tcPlan);
        return Result.success(true);
    }

    @Override
    public Result<PageInfo<TcPlanListRes>> planList(TcPlanListReq tcPlanListReq) {
        PageHelper.startPage(tcPlanListReq.getPageNo(), tcPlanListReq.getPageSize());
        List<TcPlanListRes> tcPlanListResPage = tcPlanMapper.planList(tcPlanListReq);
        Integer totalCaseNum = 0;
        //质控调用时填充冗余字段
        if (tcPlanListReq.getPmsVersionId() != null) {
            //失败率 阻塞率
            for (TcPlanListRes tcPlanListRes : tcPlanListResPage) {
                Integer totalNum = tcPlanListRes.getTotalNum();
                Integer failNum = tcPlanListRes.getFailNum();
                Integer blockNum = tcPlanListRes.getBlockNum();
                if (totalNum == null || totalNum == 0) {
                    tcPlanListRes.setFailRate(BigDecimal.ZERO);
                    tcPlanListRes.setBlockRate(BigDecimal.ZERO);
                } else {
                    BigDecimal failRateNum = new BigDecimal(failNum)
                            .divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP);
                    tcPlanListRes.setFailRate(failRateNum.multiply(new BigDecimal(100)));

                    BigDecimal blockRateNum = new BigDecimal(blockNum)
                            .divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP);
                    tcPlanListRes.setBlockRate(blockRateNum.multiply(new BigDecimal(100)));
                }
            }
            //用例总数
            totalCaseNum = tcPlanCaseResultMapper.getTotalCaseNumByVersionId(tcPlanListReq.getPmsVersionId());
        }
        PageInfo<TcPlanListRes> pageInfo = new PageInfo<>(tcPlanListResPage);
        pageInfo.setNavigatePages(totalCaseNum);
        return Result.success(pageInfo);
    }

    @Override
    public Result<TcPlanListRes> getPlanCount(TcPlanListReq tcPlanListReq) {
        TcPlanListRes tcPlanListRes = new TcPlanListRes();
        List<HashMap<Integer, Object>> countMapList = tcPlanMapper.getPlanCount(tcPlanListReq);
        if (countMapList != null && !countMapList.isEmpty()) {
            for (HashMap<Integer, Object> hashMap : countMapList) {
                Integer key = null;
                Integer value = null;
                for (Map.Entry<Integer, Object> entry : hashMap.entrySet()) {
                    if ("key".equals(entry.getKey())) {
                        key = (Integer) entry.getValue();
                    } else if ("value".equals(entry.getKey())) {
                        value = ((Long)entry.getValue()).intValue();
                    }
                }
                if (key == 0) {
                    tcPlanListRes.setInitCount(value);
                }
                if (key == 1) {
                    tcPlanListRes.setInHandCount(value);
                }
                if (key == 2) {
                    tcPlanListRes.setDoneCount(value);
                }
            }
        }
        if(tcPlanListRes.getInitCount() == null){
            tcPlanListRes.setInitCount(0);
        }
        if(tcPlanListRes.getInHandCount() == null){
            tcPlanListRes.setInHandCount(0);
        }
        if(tcPlanListRes.getDoneCount() == null){
            tcPlanListRes.setDoneCount(0);
        }
        return Result.success(tcPlanListRes);
    }

    @Override
    public Result<TcPlanDetailRes> planDetail(TcPlanDetailReq tcPlanDetailReq) {
        //所查询模块列表
        List<Integer> moduleIdList = new ArrayList<>();
        if (tcPlanDetailReq.getModuleId() != null) {
            if (tcPlanDetailReq.getShowChildren() == 1) {
                //科技与信息中心不需要进行递归查询，直接查询全部记录
                TcTestCaseModule tcTestCaseModule = tcTestCaseModuleMapper
                        .selectByPrimaryKey(tcPlanDetailReq.getModuleId());
                if (tcTestCaseModule != null
                        && tcTestCaseModule.getParentId() != null
                        && !tcTestCaseModule.getParentId().equals(0)) {
                    //递归所有子节点
                    Object moduleIdsFromRedis = redisUtil.get(RedisPrefixDto.MODULE_IDS + tcPlanDetailReq.getModuleId());
                    if(moduleIdsFromRedis != null){
                        log.info("redis get value:" + RedisPrefixDto.MODULE_IDS + tcPlanDetailReq.getModuleId());
                        moduleIdList = JSONObject.parseArray(moduleIdsFromRedis.toString(),Integer.class);
                    } else {
                        moduleIdList = tcTestCaseModuleMapper.getChildModuleIdByProc(tcPlanDetailReq.getModuleId());
                    }
                }
            } else {
                moduleIdList.add(tcPlanDetailReq.getModuleId());
            }
        }
        tcPlanDetailReq.setModuleIdList(moduleIdList);
        //查询测试计划详情
        TcPlan tcPlan = tcPlanMapper.selectByPrimaryKey(tcPlanDetailReq.getPlanId());
        TcPlanDetailRes tcPlanDetailRes = new TcPlanDetailRes();
        BeanUtils.copyProperties(tcPlan, tcPlanDetailRes);
        //查询用例列表
        PageHelper.startPage(tcPlanDetailReq.getPageNo(), tcPlanDetailReq.getPageSize());
        List<TcPlanCaseListDTO> caseListByPlanId = tcPlanCaseResultMapper.getCaseListByPlanDetail(tcPlanDetailReq);
        PageInfo<TcPlanCaseListDTO> pageInfo = new PageInfo<>(caseListByPlanId);
        tcPlanDetailRes.setCaseList(pageInfo);
        //查询所属模块树
//        Set<TcTestCaseModule> moduleList = getModuleListByCaseList(caseListByPlanId);
//        tcPlanDetailRes.setModuleList(moduleList);
        return Result.success(tcPlanDetailRes);
    }

    @Override
    public Result<TcPlanDetailRes> planDetailToQa(TcPlanDetailReq tcPlanDetailReq) {
        //所查询模块列表
        List<Integer> moduleIdList = new ArrayList<>();
        if (tcPlanDetailReq.getModuleId() != null) {
            if (tcPlanDetailReq.getShowChildren() == 1) {
                //科技与信息中心不需要进行递归查询，直接查询全部记录
                TcTestCaseModule tcTestCaseModule = tcTestCaseModuleMapper
                        .selectByPrimaryKey(tcPlanDetailReq.getModuleId());
                if (tcTestCaseModule != null
                        && tcTestCaseModule.getParentId() != null
                        && !tcTestCaseModule.getParentId().equals(0)) {
                    //递归所有子节点
                    Object moduleIdsFromRedis = redisUtil.get(RedisPrefixDto.MODULE_IDS + tcPlanDetailReq.getModuleId());
                    if(moduleIdsFromRedis != null){
                        log.info("redis get value:" + RedisPrefixDto.MODULE_IDS + tcPlanDetailReq.getModuleId());
                        moduleIdList = JSONObject.parseArray(moduleIdsFromRedis.toString(),Integer.class);
                    } else {
                        moduleIdList = tcTestCaseModuleMapper.getChildModuleIdByProc(tcPlanDetailReq.getModuleId());
                    }
                }
            } else {
                moduleIdList.add(tcPlanDetailReq.getModuleId());
            }
        }
        tcPlanDetailReq.setModuleIdList(moduleIdList);
        //查询测试计划详情
        TcPlan tcPlan = tcPlanMapper.selectByPrimaryKey(tcPlanDetailReq.getPlanId());
        TcPlanDetailRes tcPlanDetailRes = new TcPlanDetailRes();
        BeanUtils.copyProperties(tcPlan, tcPlanDetailRes);
        //查询用例列表
        PageHelper.startPage(tcPlanDetailReq.getPageNo(), tcPlanDetailReq.getPageSize());
        List<TcPlanCaseListDTO> caseListByPlanId = tcPlanCaseResultMapper.getCaseListByPlanDetail(tcPlanDetailReq);
        PageInfo<TcPlanCaseListDTO> pageInfo = new PageInfo<>(caseListByPlanId);
        tcPlanDetailRes.setCaseList(pageInfo);
        //查询所属模块树
//        Set<TcTestCaseModule> moduleList = getModuleListByCaseList(caseListByPlanId);
//        tcPlanDetailRes.setModuleList(moduleList);
        return Result.success(tcPlanDetailRes);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> batchEdit(TcPlanBatchEditReq tcPlanBatchEditReq) {
        //批量更新执行人/执行结果
        tcPlanCaseResultMapper.batchEdit(tcPlanBatchEditReq);
        //通过用例列表执行结果更新执行计划
        if (tcPlanBatchEditReq.getResult() != null) {
            updatePlanResult(tcPlanBatchEditReq.getPlanId());
        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean> associateCase(TcPlanAssociateReq tcPlanAssociateReq) {
        //1.维护执行计划-用例结果表
        List<Integer> requestList = tcPlanAssociateReq.getCaseList();
        if (tcPlanAssociateReq.getType() == 1) {
            //关联用例
            TcPlan tcPlan = tcPlanMapper.selectByPrimaryKey(tcPlanAssociateReq.getPlanId());
            List<TcPlanCaseListDTO> caseListByPlanId = tcPlanCaseResultMapper
                    .getCaseListByPlanId(tcPlanAssociateReq.getPlanId());
            for (Integer requestId : requestList) {
                //如果已经存在，则直接跳过
                boolean flag = false;
                for (TcPlanCaseListDTO existRecord : caseListByPlanId) {
                    if (existRecord.getCaseId().equals(requestId)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                //插入记录
                TcPlanCaseResult tcPlanCaseResult = new TcPlanCaseResult();
                tcPlanCaseResult.setCaseId(requestId);
                tcPlanCaseResult.setPlanId(tcPlanAssociateReq.getPlanId());
                tcPlanCaseResult.setExecuteUser(tcPlan.getExecuteUser());
                tcPlanCaseResult.setCreateUser(tcPlanAssociateReq.getUser());
                tcPlanCaseResult.setUpdateUser(tcPlanAssociateReq.getUser());
                try {
                    tcPlanCaseResultMapper.insertSelective(tcPlanCaseResult);
                } catch (Exception e) {
                    log.warn("planId为：" + tcPlanAssociateReq.getPlanId() + " caseId为：" + requestId + " 的结果记录已存在！");
                }
            }

        } else if (tcPlanAssociateReq.getType() == 0) {
            //移除关联
            for (Integer requestId : requestList) {
                tcPlanCaseResultMapper.deleteByPlanIdAndCaseId(tcPlanAssociateReq.getPlanId(), requestId);
            }
        }
        //2.维护步骤结果表
        updateStepResult(tcPlanAssociateReq.getPlanId());
        //3.维护执行计划结果
        updatePlanResult(tcPlanAssociateReq.getPlanId());
        return Result.success(true);
    }

    /**
     * 根据当前执行计划下用例列表更新所关联步骤结果
     *
     * @param planId
     */
    private void updateStepResult(Integer planId) {
        //获取实际应存步骤结果
        List<TcPlanCaseStepResult> actualList = tcPlanCaseStepResultMapper.getActualStepListByPlanId(planId);
        //获取当前步骤结果表结果
        List<TcPlanCaseStepResult> savedList = tcPlanCaseStepResultMapper.getSavedStepListByPlanId(planId);
        //获取新增列表
        List<TcPlanCaseStepResult> addList = new ArrayList<>();
        for (TcPlanCaseStepResult actualRecord : actualList) {
            String actualCode =
                    actualRecord.getPlanId() + "-" + actualRecord.getCaseId() + "-" + actualRecord.getStepId();
            boolean flag = false;
            for (TcPlanCaseStepResult savedRecord : savedList) {
                String savedCode =
                        savedRecord.getPlanId() + "-" + savedRecord.getCaseId() + "-" + savedRecord.getStepId();
                if (actualCode.equals(savedCode)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                addList.add(actualRecord);
            }
        }
        //获取删除列表
        List<Long> deleteList = new ArrayList<>();
        for (TcPlanCaseStepResult savedRecord : savedList) {
            String savedCode = savedRecord.getPlanId() + "-" + savedRecord.getCaseId() + "-" + savedRecord.getStepId();
            boolean flag = false;
            for (TcPlanCaseStepResult actualRecord : actualList) {
                String actualCode =
                        actualRecord.getPlanId() + "-" + actualRecord.getCaseId() + "-" + actualRecord.getStepId();
                if (savedCode.equals(actualCode)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                deleteList.add(savedRecord.getId());
            }
        }
        //新增记录
        for (TcPlanCaseStepResult tcPlanCaseStepResult : addList) {
            try {
                tcPlanCaseStepResultMapper.insertSelective(tcPlanCaseStepResult);
            } catch (Exception e) {
                log.warn("planId为：" + tcPlanCaseStepResult.getPlanId()
                        + " caseId为：" + tcPlanCaseStepResult.getCaseId()
                        + " stePId为：" + tcPlanCaseStepResult.getStepId()
                        + " 的步骤结果记录已存在！");
            }
        }
        //删除记录
        if (!deleteList.isEmpty()) {
            tcPlanCaseStepResultMapper.deleteByIds(deleteList);
        }
    }

    @Override
    public Result<TcPlanCaseDetailRes> caseDetail(TcPlanCaseDetailReq tcPlanCaseDetailReq) {
        //用例详情及结果
        TcPlanCaseDetailRes tcPlanCaseDetailRes = tcTestcaseMapper.getCaseDetail(tcPlanCaseDetailReq);
        if (tcPlanCaseDetailRes == null) {
            return Result.error(ErrorCodeEnum.PLAN_DETAIL_NOT_EXIST);
        }
        //文件列表
//        List<String> filePathListByCaseId = tcFileMapper.getFilePathListByCaseId(tcPlanCaseDetailReq.getCaseId());
//        tcPlanCaseDetailRes.setFileList(filePathListByCaseId);
        List<TcFile> fileListByCaseId = tcFileMapper.getFileListByCaseId(tcPlanCaseDetailReq.getCaseId());
        tcPlanCaseDetailRes.setFileList(fileListByCaseId);

        //用例执行结果文件列表
        //获取tc_plan_case_result--id
//        TcPlanCaseListDTO caseInfo=tcPlanCaseResultMapper.getCaseByPlanIdAndCaseId(tcPlanCaseDetailReq.getPlanId(),tcPlanCaseDetailReq.getCaseId());
        //这里传的是relationId
        List<TcFile> resultFileListByCaseId = tcFileMapper.getFileListByCaseId(tcPlanCaseDetailReq.getId());
        tcPlanCaseDetailRes.setResultFileList(resultFileListByCaseId);

        //步骤结果
        List<TcPlanStepListDTO> resultList = tcPlanCaseStepResultMapper.getResultByPlanIdAndCaseId(tcPlanCaseDetailReq);
        tcPlanCaseDetailRes.setStepList(resultList);
        return Result.success(tcPlanCaseDetailRes);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> caseEdit(TcPlanCaseEditReq tcPlanCaseEditReq) {
        //更新执行计划-用例结果数据
        tcPlanCaseResultMapper.updateByCaseIdAndPlanId(tcPlanCaseEditReq);
        //更新步骤结果
        List<TcPlanStepListDTO> stepList = tcPlanCaseEditReq.getStepList();
        if (stepList != null) {
            for (TcPlanStepListDTO tcPlanStepListDTO : stepList) {
                TcPlanCaseStepResult tcPlanCaseStepResult = new TcPlanCaseStepResult();
                BeanUtils.copyProperties(tcPlanStepListDTO, tcPlanCaseStepResult);
                tcPlanCaseStepResult.setUpdateUser(tcPlanCaseEditReq.getUser());
                tcPlanCaseStepResultMapper.updateByPrimaryKeySelective(tcPlanCaseStepResult);
            }
        }
        //更新执行计划
        updatePlanResult(tcPlanCaseEditReq.getPlanId());
        return Result.success(true);
    }

    @Override
    public Result<Boolean> syncStepResult(Integer caseId) {
        List<Integer> uncomplatePlan = tcPlanCaseResultMapper.getUncomplatePlan(caseId);
        for (Integer planId : uncomplatePlan) {
            updateStepResult(planId);
        }
        return Result.success(true);
    }

    @Override
    public Result<Boolean> planState(TcPlanDetailReq tcPlanDetailReq) {
        TcPlan tcPlan = new TcPlan();
        tcPlan.setId(tcPlanDetailReq.getId());
        tcPlan.setState(tcPlanDetailReq.getState());
        tcPlan.setUpdateUser(tcPlanDetailReq.getUser());
        tcPlanMapper.updateByPrimaryKeySelective(tcPlan);
        return Result.success(true);
    }

    @Override
    public Result<Boolean> deleteCaseEdit(List<Integer> caseIds) {
        for (Integer caseId : caseIds) {
            List<Integer> distinctPlanId = tcPlanCaseResultMapper.getDistinctPlanId(caseId);
            List<Integer> caseList = new ArrayList<>(1);
            caseList.add(caseId);
            for (Integer planId : distinctPlanId) {
                TcPlanAssociateReq tcPlanAssociateReq = new TcPlanAssociateReq();
                tcPlanAssociateReq.setCaseList(caseList);
                tcPlanAssociateReq.setPlanId(planId);
                tcPlanAssociateReq.setType(0);
                associateCase(tcPlanAssociateReq);
            }
        }
        return Result.success(true);
    }

    @Override
    public List<TcTestCaseModule> getIntegrationTestToQa(Integer parentId) {
        return tcTestCaseModuleMapper.getIntegrationTestToQa(parentId);
    }

    /**
     * 通过用例列表执行结果更新执行计划
     *
     * @param planId
     */
    private void updatePlanResult(Integer planId) {
        int totalNum;
        int executeNum = 0;
        int passNum = 0;
        int failNum = 0;
        int blockNum = 0;
        int skipNum = 0;
        BigDecimal passRate;
        List<TcPlanCaseListDTO> caseListByPlanId = tcPlanCaseResultMapper.getCaseListByPlanId(planId);
        totalNum = caseListByPlanId.size();
        for (TcPlanCaseListDTO tcPlanCaseListDTO : caseListByPlanId) {
            //通过
            if (tcPlanCaseListDTO.getResult() == 1) {
                passNum++;
            }
            //失败
            if (tcPlanCaseListDTO.getResult() == 2) {
                failNum++;
            }
            //阻塞
            if (tcPlanCaseListDTO.getResult() == 3) {
                blockNum++;
            }
            //跳过
            if (tcPlanCaseListDTO.getResult() == 4) {
                skipNum++;
            }
            //执行数量
            if (tcPlanCaseListDTO.getResult() != 0) {
                executeNum++;
            }
        }
        if (totalNum > 0) {
            BigDecimal passRateNum = new BigDecimal(passNum)
                    .divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP);
            passRate = passRateNum.multiply(new BigDecimal(100));
        } else {
            passRate = BigDecimal.ZERO;
        }
        TcPlan tcPlan = new TcPlan();
        tcPlan.setId(planId);
        tcPlan.setTotalNum(totalNum);
        tcPlan.setExecuteNum(executeNum);
        tcPlan.setPassNum(passNum);
        tcPlan.setFailNum(failNum);
        tcPlan.setBlockNum(blockNum);
        tcPlan.setSkipNum(skipNum);
        tcPlan.setPassRate(passRate);
        //通过运行数量自动变更执行计划状态
        if(passNum == totalNum){
            tcPlan.setState((byte)2);
        } else if(executeNum != 0){
            tcPlan.setState((byte)1);
        }
        tcPlanMapper.updateByPrimaryKeySelective(tcPlan);
    }

    /**
     * 递归获取所有子模块id
     *
     * @param moduleId
     * @return
     */
    private List<Integer> getChildModuleList(Integer moduleId, List<Integer> list) {
        list.add(moduleId);
        List<Integer> childModuleId = tcTestCaseModuleMapper.getChildModuleId(moduleId);
        for (Integer id : childModuleId) {
            getChildModuleList(id, list);
        }
//        log.info(list.size() + "");
        return list;
    }

    @Override
    public List<Map<String, Long>> planCount(TcPlanCaseStatisticReq tcPlanCaseStatisticReq) {
        if(tcPlanCaseStatisticReq.getGroupType()!=null&&tcPlanCaseStatisticReq.getGroupType()==1){
            return tcPlanMapper.getCreatedPlanCountByCreator(tcPlanCaseStatisticReq);
        }else{
            return tcPlanMapper.getCreatedPlanCountByDate(tcPlanCaseStatisticReq);
        }

    }

    @Override
    public Map<String, Long> planAndCaseCount(TcPlanCaseStatisticReq tcPlanCaseStatisticReq) {
        List<Map<String,Long>> newPlanList=tcPlanMapper.getCreatedPlanCountByCreator(tcPlanCaseStatisticReq);
        long newPlan=(newPlanList!=null&&!newPlanList.isEmpty())?newPlanList.get(0).get("value"):0;
        List<Map<String,Long>> newCaseList=tcTestcaseMapper.getCreatedCaseCountByCreator(tcPlanCaseStatisticReq);
        long newCase=(newCaseList!=null&&!newCaseList.isEmpty())?newCaseList.get(0).get("value"):0;
        TcPlanCaseStatisticReq req=new TcPlanCaseStatisticReq();
        req.setCreateUser(tcPlanCaseStatisticReq.getCreateUser());
        List<Map<String,Long>> totalPlanList=tcPlanMapper.getCreatedPlanCountByCreator(req);
        long totalPlan=(totalPlanList!=null&&!totalPlanList.isEmpty())?totalPlanList.get(0).get("value"):0;
        List<Map<String,Long>> totalCaseList=tcTestcaseMapper.getCreatedCaseCountByCreator(req);
        long totalCase=(totalCaseList!=null&&!totalCaseList.isEmpty())?totalCaseList.get(0).get("value"):0;
        Map<String, Long> result=new HashMap<>();
        result.put("totalPlan",totalPlan);
        result.put("totalCase",totalCase);
        result.put("newPlan",newPlan);
        result.put("newCase",newCase);
        return result;
    }
}
