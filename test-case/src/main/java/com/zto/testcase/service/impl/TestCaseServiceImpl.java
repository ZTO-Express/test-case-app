package com.zto.testcase.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zto.testcase.dto.ExcelDto;
import com.zto.testcase.dto.RedisPrefixDto;
import com.zto.testcase.dto.TcTestCaseDto;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.enums.TestCasePriorityEnum;
import com.zto.testcase.enums.TestCaseTypeEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.mapper.testcase.TcFileMapper;
import com.zto.testcase.mapper.testcase.TcTestCaseModuleMapper;
import com.zto.testcase.mapper.testcase.TcTestcaseMapper;
import com.zto.testcase.mapper.testcase.TcTestcaseStepMapper;
import com.zto.testcase.model.TcFile;
import com.zto.testcase.model.TcTestCaseModule;
import com.zto.testcase.model.TcTestcase;
import com.zto.testcase.model.TcTestcaseStep;
import com.zto.testcase.request.TcPlanCaseStatisticReq;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.TcPlanService;
import com.zto.testcase.service.TestCaseService;
import com.zto.testcase.util.DateUtils;
import com.zto.testcase.util.ExcelUtils;
import com.zto.testcase.util.RedisUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TestCaseServiceImpl implements TestCaseService {

    @Resource
    private RedisUtil redisUtil;

    private static final Logger logger = LoggerFactory.getLogger(TestCaseServiceImpl.class);

    @Resource
    private TcTestcaseMapper tcTestcaseMapper;

    @Resource
    private TcTestcaseStepMapper tcTestcaseStepMapper;

    @Resource
    private TcFileMapper tcFileMapper;

    @Resource
    private TcTestCaseModuleMapper tcTestCaseModuleMapper;

    @Resource
    private TcPlanService tcPlanService;

    @Override
    public TcTestcase addTestCase(TcTestcase tcTestcase) {

        // 1、保存用例信息
        int ret = tcTestcaseMapper.addTestCase(tcTestcase);

        if (ret > 0) {
            // 2、保存用例步骤信息
            List<TcTestcaseStep> tcTestcaseStepList = tcTestcase.getTcTestcaseStepList();
            for (TcTestcaseStep tcTestcaseStep : tcTestcaseStepList) {
                tcTestcaseStep.setCaseId(tcTestcase.getId());
                tcTestcaseStep.setUser(tcTestcase.getUser());
                tcTestcaseStepMapper.addTcTestcaseStep(tcTestcaseStep);
            }

            // 3、更新文件表关联用例
            List<Integer> fileIds = tcTestcase.getFileIds();
            if (null != fileIds && fileIds.size() > 0) {
                tcFileMapper.updateFileRelationship(fileIds, tcTestcase.getId());
            }

        }

        return tcTestcase;
    }

    @Override
    public TcTestcase addTestCaseToNewModule(TcTestcase sourceTcTestcase) {
        TcTestcase tcTestcase = copyTestCase(sourceTcTestcase.getModuleId(), sourceTcTestcase);
        // 1、保存用例信息
        int ret = tcTestcaseMapper.addTestCase(tcTestcase);

        if (ret > 0) {
            // 2、保存用例步骤信息
            List<TcTestcaseStep> tcTestcaseStepList = sourceTcTestcase.getTcTestcaseStepList();
            for (TcTestcaseStep tcTestcaseStep : tcTestcaseStepList) {
                tcTestcaseStep.setId(null);
                tcTestcaseStep.setCaseId(tcTestcase.getId());
                tcTestcaseStep.setUser(tcTestcase.getUser());
                tcTestcaseStep.setCreateUser(tcTestcase.getUser());
                tcTestcaseStep.setUpdateUser(tcTestcase.getUser());
                TcTestcaseStep newStep = copyStep(tcTestcase.getId(), tcTestcaseStep);
                tcTestcaseStepMapper.copyAddTcTestcaseStep(newStep);
            }

            // 3、更新文件表关联用例
            List<Integer> fileIds = tcTestcase.getFileIds();
            if (null != fileIds && fileIds.size() > 0) {
                tcFileMapper.updateFileRelationship(fileIds, tcTestcase.getId());
            }
        }
        return tcTestcase;
    }


    @Override
    public int deleteTestCase(TcTestcase tcTestcase) {
        List<Integer> ids = tcTestcase.getIds();
        int ret = -1;
        if (null != ids && ids.size() > 0) {
            ret = tcTestcaseMapper.deleteTestCase(ids);
        }
        //删除执行计划关联信息
        tcPlanService.deleteCaseEdit(ids);
        return ret;
    }

    public static Set<Integer> getModuleIdSet(List<TcTestCaseModule> moduleList, Integer pid) {
        Set<Integer> moduleSet = new HashSet<>();
        for (TcTestCaseModule module : moduleList) {
            // 遍历出父id等于参数的id，add进子节点集合
            if (module.getParentId().intValue() == pid.intValue()) {
                // 递归遍历下一级
                moduleSet.add(module.getId());
                // childMenu.add(module);
                getModuleIdSet(moduleList, module.getId());
            }

        }
        return moduleSet;
    }

    public List<TcTestCaseModule> getChild(Set<Integer> set, Integer parentId, List<TcTestCaseModule> list) {
        List<TcTestCaseModule> childList = new ArrayList<>();
        for (TcTestCaseModule t : list) {
            if (parentId.intValue() == t.getParentId().intValue()) {
                childList.add(t);
                set.add(t.getId());
            }
        }
        for (TcTestCaseModule tcTestCaseModule : childList) {
            List<TcTestCaseModule> child = getChild(set, tcTestCaseModule.getId(), list);
            set.add(tcTestCaseModule.getId());
            tcTestCaseModule.setChildren(child);
        }
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }

    @Override
    public Result<PageInfo<TcTestcase>> getTestCaseList(TcTestCaseDto tcTestCaseDto) {
        List<TcTestcase> tcTestcaseList;
        Integer moduleId = tcTestCaseDto.getModuleId();
        if (null == moduleId) {
            logger.info("====查所有用例=============");
            tcTestCaseDto.setModuleIds(null);
            PageHelper.startPage(tcTestCaseDto.getPageNo(), tcTestCaseDto.getPageSize());
            tcTestcaseList = tcTestcaseMapper.getTestCaseList(tcTestCaseDto);
            PageInfo<TcTestcase> pageInfo = new PageInfo<>(tcTestcaseList);
            return Result.success(pageInfo);
        }

        TcTestCaseModule dbRootModule = tcTestCaseModuleMapper.getRootModule();
        if (dbRootModule.getId().intValue() == moduleId.intValue()) {
            logger.info("====查所有用例=============");
            PageHelper.startPage(tcTestCaseDto.getPageNo(), tcTestCaseDto.getPageSize());
            tcTestcaseList = tcTestcaseMapper.getTestCaseList(tcTestCaseDto);
            PageInfo<TcTestcase> pageInfo = new PageInfo<>(tcTestcaseList);
            return Result.success(pageInfo);
        }

        List<Integer> moduleIds = new ArrayList<>();

        if (tcTestCaseDto.getShowSubModule() == 1) {
            logger.info("显示子模块");
            Object moduleIdsFromRedis = redisUtil.get(RedisPrefixDto.MODULE_IDS + moduleId);
            if (moduleIdsFromRedis == null) {
                log.info("redis set value:" + RedisPrefixDto.MODULE_IDS + moduleId);
                moduleIds = tcTestCaseModuleMapper.getChildModuleIdByProc(moduleId);
                redisUtil.set(RedisPrefixDto.MODULE_IDS + moduleId, JSONArray.toJSONString(moduleIds));
            } else {
                log.info("redis get value:" + RedisPrefixDto.MODULE_IDS + moduleId);
                moduleIds = JSONObject.parseArray(moduleIdsFromRedis.toString(), Integer.class);
            }
            tcTestCaseDto.setModuleIds(moduleIds);
            PageHelper.startPage(tcTestCaseDto.getPageNo(), tcTestCaseDto.getPageSize());

            tcTestcaseList = tcTestcaseMapper.getTestCaseList(tcTestCaseDto);
            logger.info("显示子模块用例总数:{}", tcTestcaseList.size());

        } else {
            logger.info("不显示子模块");
            moduleIds.add(moduleId);
            tcTestCaseDto.setModuleIds(moduleIds);
            PageHelper.startPage(tcTestCaseDto.getPageNo(), tcTestCaseDto.getPageSize());
            tcTestcaseList = tcTestcaseMapper.getTestCaseList(tcTestCaseDto);
            logger.info("不显示子模块用例总数:{}", tcTestcaseList.size());
        }

        PageInfo<TcTestcase> pageInfo = new PageInfo<>(tcTestcaseList);
        return Result.success(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int editTestCase(TcTestcase tcTestcase) {
        // 1、编辑用例信息
        int ret = tcTestcaseMapper.editTestCase(tcTestcase);

        // 2、删除用例步骤
        tcTestcaseStepMapper.deleteTestcaseStep(tcTestcase.getId());

        // 3、添加用例步骤
        List<TcTestcaseStep> tcTestcaseStepList = tcTestcase.getTcTestcaseStepList();
        for (TcTestcaseStep tcTestcaseStep : tcTestcaseStepList) {
            tcTestcaseStep.setCaseId(tcTestcase.getId());
            tcTestcaseStep.setUser(tcTestcase.getUser());
            tcTestcaseStepMapper.addTcTestcaseStep(tcTestcaseStep);
        }
        // 4、更新对应的步骤结果表
        tcPlanService.syncStepResult(tcTestcase.getId());
        return ret;
    }

    @Override
    public List<TcFile> getFileListByCaseId(TcTestcase tcTestcase) {
        Integer caseId = tcTestcase.getId();
        if (caseId > 0) {
            return tcFileMapper.getFileListByCaseId(caseId);
        }
        return null;
    }

    @Override
    public List<TcTestcaseStep> getTestcaseStepListByCaseId(TcTestcase tcTestcase) {
        Integer caseId = tcTestcase.getId();
        if (caseId > 0) {
            return tcTestcaseStepMapper.getTestcaseStepListByCaseId(caseId);
        }
        return null;
    }

    @Override
    public int copyTestCase(TcTestcase tcTestcase) {
        Integer moduleId = tcTestcase.getModuleId();
        List<Integer> ids = tcTestcase.getIds();
        List<TcTestcase> tcTestcaseList = tcTestcaseMapper.getTcTestcaseListByIds(ids);
        List<TcTestcase> retCopyTestcaseList = new ArrayList<>();

        for (TcTestcase testcase : tcTestcaseList) {
            Integer caseId = testcase.getId();

            TcTestcase newTcTestcase = copyTestCase(moduleId, testcase);

            newTcTestcase.setCreateUser(tcTestcase.getUser());
            newTcTestcase.setUpdateUser(tcTestcase.getUser());
            tcTestcaseMapper.copyAddTestCase(newTcTestcase);

            Integer newCaseId = newTcTestcase.getId();

            List<TcTestcaseStep> tcTestcaseStepList = tcTestcaseStepMapper.getTestcaseStepListByCaseId(caseId);

            for (TcTestcaseStep t : tcTestcaseStepList) {
                TcTestcaseStep newStep = copyStep(newCaseId, t);
                tcTestcaseStepMapper.copyAddTcTestcaseStep(newStep);
            }

            List<TcFile> tcFileList = tcFileMapper.getFileListByCaseId(caseId);
            for (TcFile f : tcFileList) {
                TcFile newTcFile = copyFile(newCaseId, f);
                tcFileMapper.copyInsert(newTcFile);
            }

            retCopyTestcaseList.add(newTcTestcase);

        }

        return retCopyTestcaseList.size();
    }

    @Override
    public Result moveTestCase(TcTestcase tcTestcase) {
        tcTestcaseMapper.updateTestCaseByIds(tcTestcase);
        return Result.success();
    }

    @Override
    public void exportTestCase(TcTestCaseDto tcTestCaseDto, HttpServletResponse response) throws Exception {
        Integer moduleId = tcTestCaseDto.getModuleId();
        if (null == moduleId) {
            throw new BaseException(ErrorCodeEnum.REQUEST_PARAM_ERROR.getErrorCode(),
                    ErrorCodeEnum.REQUEST_PARAM_ERROR.getErrorMsg());
        }
        TcTestCaseModule dbRootModule = tcTestCaseModuleMapper.getRootModule();
        List<TcTestcase> tcTestcaseList;
        if (dbRootModule.getId().intValue() == moduleId.intValue()) {
            throw new BaseException(ErrorCodeEnum.CANNOT_EXPORT_ROOT_MODULE.getErrorCode(),
                    ErrorCodeEnum.CANNOT_EXPORT_ROOT_MODULE.getErrorMsg());
        }
        List<Integer> moduleIds = new ArrayList<>();
        if (tcTestCaseDto.getShowSubModule().intValue() == 1) {
            logger.info("显示子模块");
            long startTime = System.currentTimeMillis();
            Object moduleIdsFromRedis = redisUtil.get(RedisPrefixDto.MODULE_IDS + moduleId);
            if (moduleIdsFromRedis == null) {
                log.info("redis set value:" + RedisPrefixDto.MODULE_IDS + moduleId);
                moduleIds = tcTestCaseModuleMapper.getChildModuleIdByProc(moduleId);
                redisUtil.set(RedisPrefixDto.MODULE_IDS + moduleId, JSONArray.toJSONString(moduleIds));
            } else {
                log.info("redis get value:" + RedisPrefixDto.MODULE_IDS + moduleId);
                moduleIds = JSONObject.parseArray(moduleIdsFromRedis.toString(), Integer.class);
            }
            tcTestCaseDto.setModuleIds(moduleIds);
            tcTestcaseList = tcTestcaseMapper.getTestCaseAndStepsList(tcTestCaseDto);
            logger.info("显示子模块用例总数:{},耗时 ：{} ms", tcTestcaseList.size(), System.currentTimeMillis() - startTime);
        } else {
            logger.info("不显示子模块");
            moduleIds.add(moduleId);
            tcTestCaseDto.setModuleIds(moduleIds);
            tcTestcaseList = tcTestcaseMapper.getTestCaseAndStepsList(tcTestCaseDto);
            logger.info("不显示子模块用例总数:{}", tcTestcaseList.size());
        }

        String finalName = "导出用例" + DateUtils.getTimeFormat() + ".xlsx";
        ExcelDto data = new ExcelDto();
        data.setName(finalName);
        List<String> titles = new ArrayList<>();
        titles.add("用例编号");
        titles.add("用例名称");
        titles.add("优先级");
        titles.add("用例类型");
        titles.add("备注");
        titles.add("前置条件");
        titles.add("步骤描述");
        titles.add("预期结果");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList<>();
        logger.info("create rows start.");
        long startTime = System.currentTimeMillis();
        for (int i = 0, length = tcTestcaseList.size(); i < length; i++) {
            TcTestcase tcTestcase = tcTestcaseList.get(i);
            List<Object> row = new ArrayList<>();
            row.add(tcTestcase.getId());
            row.add(tcTestcase.getName());
            row.add(TestCasePriorityEnum.getDesc(tcTestcase.getPriority()));
            row.add(TestCaseTypeEnum.getDesc(tcTestcase.getType()));
            row.add(tcTestcase.getComment());
            row.add(tcTestcase.getPrecondition());
            row.add(tcTestcase.getStepDesc());
            row.add(tcTestcase.getExpectResult());
            rows.add(row);
        }
        logger.info("create rows use : {} ms", System.currentTimeMillis() - startTime);
        data.setRows(rows);
        long exportExcelStartTime = System.currentTimeMillis();
        logger.info("exportExcel start.");
        ExcelUtils.exportExcel(response, finalName, data);
        logger.info("exportExcel end.耗时 ：{} ms", System.currentTimeMillis() - exportExcelStartTime);
    }

    private TcFile copyFile(Integer newCaseId, TcFile f) {
        TcFile newTcFile = new TcFile();
        newTcFile.setPath(f.getPath());
        newTcFile.setName(f.getName());
        newTcFile.setRelationId(newCaseId);
        newTcFile.setType(f.getType());
        newTcFile.setCreateTime(f.getCreateTime());
        newTcFile.setUpdateTime(f.getUpdateTime());
        newTcFile.setCreateUser(f.getCreateUser());
        newTcFile.setUpdateUser(f.getUpdateUser());
        return newTcFile;
    }

    private TcTestcaseStep copyStep(Integer newCaseId, TcTestcaseStep t) {
        TcTestcaseStep newStep = new TcTestcaseStep();
        newStep.setStepNumber(t.getStepNumber());
        newStep.setStepDesc(t.getStepDesc());
        newStep.setExpectResult(t.getExpectResult());
        newStep.setCaseId(newCaseId);
        newStep.setCreateTime(t.getCreateTime());
        newStep.setUpdateTime(t.getUpdateTime());
        newStep.setCreateUser(t.getCreateUser());
        newStep.setUpdateUser(t.getUpdateUser());
        return newStep;
    }

    private TcTestcase copyTestCase(Integer moduleId, TcTestcase testcase) {
        TcTestcase newTestcase = new TcTestcase();
        newTestcase.setName(testcase.getName());
        newTestcase.setPriority(testcase.getPriority());
        newTestcase.setStatus(testcase.getStatus());
        newTestcase.setPrecondition(testcase.getPrecondition());
        newTestcase.setType(testcase.getType());
        newTestcase.setModuleId(moduleId);
        newTestcase.setComment(testcase.getComment());
        newTestcase.setCreateTime(testcase.getCreateTime());
        newTestcase.setUpdateTime(testcase.getUpdateTime());
        newTestcase.setCreateUser(testcase.getUser());
        newTestcase.setUpdateUser(testcase.getUser());
        newTestcase.setUser(testcase.getUser());
        return newTestcase;
    }

    @Override
    public List<Map<String, Long>> caseCount(TcPlanCaseStatisticReq tcPlanCaseStatisticReq) {
        if(tcPlanCaseStatisticReq.getGroupType()!=null&&tcPlanCaseStatisticReq.getGroupType()==1){
            return tcTestcaseMapper.getCreatedCaseCountByCreator(tcPlanCaseStatisticReq);
        }else{
            return tcTestcaseMapper.getCreatedCaseCountByDate(tcPlanCaseStatisticReq);
        }

    }
}
