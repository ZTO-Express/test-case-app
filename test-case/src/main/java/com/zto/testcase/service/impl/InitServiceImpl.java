//package com.zto.testcase.service.impl;
//
//import com.zto.testcase.dto.TcTestCaseDto;
//import com.zto.testcase.mapper.testcase.SysBusinessLineMapper;
//import com.zto.testcase.mapper.testcase.SysProjectMapper;
//import com.zto.testcase.mapper.testcase.TcTestCaseModuleMapper;
//import com.zto.testcase.mapper.testcase.TcTestcaseMapper;
//import com.zto.testcase.mapper.testcase.TcTestcaseStepMapper;
//import com.zto.testcase.mapper.testlink.NodesHierarchyMapper;
//import com.zto.testcase.mapper.testlink.TcstepsMapper;
//import com.zto.testcase.mapper.testlink.TcversionsMapper;
//import com.zto.testcase.mapper.testlink.TestprojectsMapper;
//import com.zto.testcase.mapper.testlink.UsersMapper;
//import com.zto.testcase.model.PtSysBusinessLine;
//import com.zto.testcase.model.SysProject;
//import com.zto.testcase.model.TcTestCaseModule;
//import com.zto.testcase.model.TcTestcase;
//import com.zto.testcase.model.TcTestcaseStep;
//import com.zto.testcase.model.testlink.NodesHierarchy;
//import com.zto.testcase.model.testlink.Tcsteps;
//import com.zto.testcase.model.testlink.Tcversions;
//import com.zto.testcase.model.testlink.Testprojects;
//import com.zto.testcase.service.InitService;
//import java.util.ArrayList;
//import java.util.List;
//import javax.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Slf4j
//public class InitServiceImpl implements InitService {
//
//    @Resource
//    private TcTestCaseModuleMapper tcTestCaseModuleMapper;
//
//    @Resource
//    private NodesHierarchyMapper nodesHierarchyMapper;
//
//    @Resource
//    private TestprojectsMapper testprojectsMapper;
//
//    @Resource
//    private TcversionsMapper tcversionsMapper;
//
//    @Resource
//    private TcTestcaseMapper tcTestcaseMapper;
//
//    @Resource
//    private TcTestcaseStepMapper tcTestcaseStepMapper;
//
//    @Resource
//    private UsersMapper usersMapper;
//
//    @Resource
//    private TcstepsMapper tcStepsMapper;
//
//    @Resource
//    private SysBusinessLineMapper sysBusinessLineMapper;
//
//    @Resource
//    private SysProjectMapper sysProjectMapper;
//
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void initModule() {
//        PtSysBusinessLine ptSysBusinessLine = new PtSysBusinessLine();
//        ptSysBusinessLine.setId(0);
//        ptSysBusinessLine.setParentId(0);
//        saveModule(ptSysBusinessLine);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void initTestCase(String pkey) {
//
//        if (StringUtils.isEmpty(pkey)) {
//            List<TcTestCaseModule> tcTestCaseModuleList = tcTestCaseModuleMapper.selectAllData();
//            for (TcTestCaseModule tcTestCaseModule : tcTestCaseModuleList) {
//                pkey = tcTestCaseModule.getPkey();
//                if (StringUtils.isEmpty(pkey)) {
//                    continue;
//                }
//                Testprojects testprojects = testprojectsMapper.selectByPkey(pkey);
//                if (null == testprojects) {
//                    log.warn(">>>>>>test project is null.pkey = {}", pkey);
//                    continue;
//                }
//                int projectId = testprojects.getId();
//                NodesHierarchy nodesHierarchy = nodesHierarchyMapper.selectByPrimaryKey(projectId);
//                if (null == nodesHierarchy) {
//                    log.warn(">>>>>>nodes hierarchy is null.project id : {}", projectId);
//                    continue;
//                }
//                if (nodesHierarchy.getNodeTypeId() != 1) {
//                    log.warn(">>>>>>nodes hierarchy type is not test project.type : {}",
//                            nodesHierarchy.getNodeTypeId());
//                    continue;
//                }
//                saveChildren(projectId, tcTestCaseModule.getId(), tcTestCaseModule.getLevel(), null);
//            }
//        } else {
//            TcTestCaseModule tcTestCaseModule = tcTestCaseModuleMapper.selectByPkey(pkey);
//            if (null == tcTestCaseModule) {
//                log.warn("TcTestCaseModule is null.key : {}", pkey);
//                return;
//            }
//            List<Integer> moduleList = new ArrayList<>();
//            moduleList.add(tcTestCaseModule.getId());
//            delAll(moduleList, pkey);
//
//            Testprojects testprojects = testprojectsMapper.selectByPkey(pkey);
//            if (null == testprojects) {
//                log.warn(">>>>>>test project is null.pkey = {}", pkey);
//            }
//            int projectId = testprojects.getId();
//            NodesHierarchy nodesHierarchy = nodesHierarchyMapper.selectByPrimaryKey(projectId);
//            if (null == nodesHierarchy) {
//                log.warn(">>>>>>nodes hierarchy is null.project id : {}", projectId);
//            }
//            if (nodesHierarchy.getNodeTypeId() != 1) {
//                log.warn(">>>>>>nodes hierarchy type is not test project.type : {}",
//                        nodesHierarchy.getNodeTypeId());
//            }
//            saveChildren(projectId, tcTestCaseModule.getId(), tcTestCaseModule.getLevel(), null);
//        }
//
//    }
//
//    private void saveModule(PtSysBusinessLine ptSysBusinessLine) {
//        List<PtSysBusinessLine> ptSysBusinessLineList = sysBusinessLineMapper
//                .selectByParentId(ptSysBusinessLine.getParentId());
//        if (null == ptSysBusinessLineList || ptSysBusinessLineList.size() == 0) {
//            log.info(">>>>>>sys business line is null.id : {}", ptSysBusinessLine.getId());
//            return;
//        }
//        int parentId = ptSysBusinessLine.getId();
//        for (PtSysBusinessLine ptSysBusinessLine1 : ptSysBusinessLineList) {
//            TcTestCaseModule addTcTestCaseModule = new TcTestCaseModule();
//            addTcTestCaseModule.setLevel(ptSysBusinessLine1.getLevel() - 1);
//            addTcTestCaseModule.setName(ptSysBusinessLine1.getBname());
//            addTcTestCaseModule.setParentId(parentId);
//            addTcTestCaseModule.setUser("system");
//            tcTestCaseModuleMapper.addModule(addTcTestCaseModule);
//            ptSysBusinessLine1.setParentId(ptSysBusinessLine1.getId());
//            ptSysBusinessLine1.setId(addTcTestCaseModule.getId());
//            List<SysProject> ptSysProjectList = sysProjectMapper.selectByBid(ptSysBusinessLine1.getParentId());
//            for (SysProject ptSysProject : ptSysProjectList) {
//                TcTestCaseModule addProject = new TcTestCaseModule();
//                addProject.setLevel(ptSysBusinessLine1.getLevel());
//                addProject.setName(ptSysProject.getPname());
//                addProject.setParentId(addTcTestCaseModule.getId());
//                addProject.setUser("system");
//                addProject.setPkey(ptSysProject.getPkey());
//                tcTestCaseModuleMapper.addModule(addProject);
//            }
//            saveModule(ptSysBusinessLine1);
//        }
//    }
//
//    /**
//     * 递归删除模块下所有（模块、用例、步骤）
//     */
//    private void delAll(List<Integer> idList, String key) {
//        for (int id : idList) {
//            if (StringUtils.isEmpty(key)) {
//                log.info(">>>>>>删除模块开始。id : {}", id);
//                tcTestCaseModuleMapper.deleteByPrimaryKey(id);
//                log.info(">>>>>>删除模块结束。id : {}", id);
//            }
//            TcTestCaseDto tcTestCaseDto = new TcTestCaseDto();
//            tcTestCaseDto.setModuleIds(idList);
//            List<TcTestcase> tcTestcaseList = tcTestcaseMapper.getTestCaseList(tcTestCaseDto);
//            for (TcTestcase tctestcase : tcTestcaseList) {
//                int caseId = tctestcase.getId();
//                log.info(">>>>>>删除用例开始。testCaseId : {}", caseId);
//                tcTestcaseMapper.deleteByPrimaryKey(caseId);
//                log.info(">>>>>>删除用例结束。testCaseId : {}", caseId);
//                log.info(">>>>>>删除步骤开始。testCaseId : {}", caseId);
//                tcTestcaseStepMapper.deleteByCaseId(caseId);
//                log.info(">>>>>>删除步骤结束。testCaseId : {}", caseId);
//            }
//            TcTestCaseModule tcTestCaseModule = new TcTestCaseModule();
//            tcTestCaseModule.setId(id);
//            List<TcTestCaseModule> tcTestCaseModuleList = tcTestCaseModuleMapper.querySubModule(tcTestCaseModule);
//            if (null == tcTestCaseModuleList || tcTestCaseModuleList.size() == 0) {
//                log.info(">>>>>>There is no sub module.moduleId : {}", tcTestCaseModule.getId());
//                continue;
//            }
//            List<Integer> moduleList = new ArrayList<>();
//            for (TcTestCaseModule tcTestCaseModule1 : tcTestCaseModuleList) {
//                moduleList.add(tcTestCaseModule1.getId());
//            }
//            delAll(moduleList, null);
//        }
//
//    }
//
//    /**
//     * 递归保存所有（模块、用例、步骤）
//     *
//     * @param nodeId
//     * @param moduleId
//     * @param level
//     */
//    private void saveChildren(int nodeId, int moduleId, int level, String caseName) {
//        List<NodesHierarchy> nodesHierarchyList = nodesHierarchyMapper
//                .selectAllByParentId(nodeId);
//        if (null == nodesHierarchyList && nodesHierarchyList.size() == 0) {
//            log.warn(">>>>>>nodesHierarchyList is null.nodeId : {}", moduleId);
//            return;
//        }
//        for (NodesHierarchy node : nodesHierarchyList) {
//            // testplan
//            if (node.getNodeTypeId() == 5) {
//                continue;
//            }
//            // testsuite
//            if (node.getNodeTypeId() == 2) {
//                TcTestCaseModule addTcTestCaseModule = new TcTestCaseModule();
//                addTcTestCaseModule.setLevel(level);
//                addTcTestCaseModule.setName(node.getName());
//                addTcTestCaseModule.setParentId(moduleId);
//                addTcTestCaseModule.setUser("system");
//                tcTestCaseModuleMapper.addModule(addTcTestCaseModule);
//                saveChildren(node.getId(), addTcTestCaseModule.getId(), level + 1, null);
//                continue;
//            }
//            // testcase
//            if (node.getNodeTypeId() == 3) {
//                saveChildren(node.getId(), moduleId, level, node.getName());
//                continue;
//            }
//            // testcase_version
//            if (node.getNodeTypeId() == 4) {
//                Tcversions tcversions = tcversionsMapper.selectByPrimaryKey(node.getId());
//                if (null == tcversions) {
//                    log.warn(">>>>>>tcversions is null.nodeId : {}", node.getId());
//                    continue;
//                }
//                TcTestcase tcTestcase = new TcTestcase();
//                tcTestcase.setName(caseName);
//                tcTestcase.setModuleId(moduleId);
//                tcTestcase.setPriority(tcversions.getImportance());
//                tcTestcase.setStatus(1);
//                tcTestcase.setType(1);
//                tcTestcase.setPrecondition(tcversions.getPreconditions());
//                tcTestcase.setComment("");
//                String user = usersMapper.getUserNameByPrimaryKey(tcversions.getAuthorId());
//                tcTestcase.setUser(user != null ? user : "system");
//                tcTestcaseMapper.addTestCase(tcTestcase);
//                saveChildren(node.getId(), tcTestcase.getId(), level, null);
//                continue;
//            }
//            // testcase_step
//            if (node.getNodeTypeId() == 9) {
//                Tcsteps tcsteps = tcStepsMapper.selectByPrimaryKey(node.getId());
//                if (null == tcsteps) {
//                    log.warn(">>>>>>tc steps is null.nodeId : {}", node.getId());
//                    continue;
//                }
//                log.info(">>>>>>tcsteps id : {}", tcsteps.getId());
//                TcTestcaseStep tcTestcaseStep = new TcTestcaseStep();
//                tcTestcaseStep.setStepNumber(tcsteps.getStepNumber());
//                tcTestcaseStep.setCaseId(moduleId);
//                tcTestcaseStep.setStepDesc(tcsteps.getActions());
//                tcTestcaseStep.setExpectResult(tcsteps.getExpectedResults());
//                tcTestcaseStep.setUser("system");
//                log.info("ExpectedResults : {}", tcsteps.getExpectedResults());
//                tcTestcaseStepMapper.addTcTestcaseStep(tcTestcaseStep);
//                continue;
//            }
//        }
//    }
//
//}