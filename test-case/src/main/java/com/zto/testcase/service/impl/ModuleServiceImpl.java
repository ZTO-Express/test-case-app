package com.zto.testcase.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.zto.testcase.dto.RedisPrefixDto;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.mapper.testcase.TcTestCaseModuleMapper;
import com.zto.testcase.mapper.testcase.TcTestcaseMapper;
import com.zto.testcase.model.TcTestCaseModule;
import com.zto.testcase.model.TcTestcase;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.ModuleService;
import com.zto.testcase.util.RedisUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ModuleServiceImpl implements ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Value("${custom.level}")
    private Integer customLevel;


    @Resource
    private TcTestCaseModuleMapper tcTestCaseModuleMapper;

    @Resource
    private TcTestcaseMapper tcTestcaseMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取模块树
     */
    @Override
    public List<TcTestCaseModule> getModuleTree(Integer planId) {
        //新增缓存逻辑，planId为空并且缓存中存在数据，则直接返回缓存数据
        Object moduleTree = redisUtil.get(RedisPrefixDto.MODULE_TREE);
        List<TcTestCaseModule> tcTestCaseModuleTree = new ArrayList<>();
        if (moduleTree != null && !moduleTree.equals("[]") && planId == null) {
            logger.info("redis get key：" + RedisPrefixDto.MODULE_TREE);
            return JSONArray.parseArray(moduleTree.toString(), TcTestCaseModule.class);
        }
        //如果缓存中不存在或者planId不为空，则进行数据库查询 并 更新缓存
        List<TcTestCaseModule> rootTcTestCaseModules;
        if (planId == null) {
            logger.info("customLevel==>{}", customLevel);
            rootTcTestCaseModules = tcTestCaseModuleMapper.selectAllDataWithLevel(customLevel);
            logger.info("rootTcTestCaseModules Size==>{}", rootTcTestCaseModules.size());
        } else {
            rootTcTestCaseModules = getModuleListByPlanId(planId);
        }
        for (int i = 0; i < rootTcTestCaseModules.size(); i++) {
            TcTestCaseModule tcTestCaseModule = rootTcTestCaseModules.get(i);
            if (tcTestCaseModule.getParentId() == 0) {
                tcTestCaseModuleTree.add(tcTestCaseModule);
                break;
            }
        }
        for (int i = 0; i < tcTestCaseModuleTree.size(); i++) {
            TcTestCaseModule rootTcTestCaseModule = tcTestCaseModuleTree.get(i);
            List<TcTestCaseModule> child = getChild(rootTcTestCaseModule.getId(), rootTcTestCaseModules);
            rootTcTestCaseModule.setChildren(child);
        }
        if (planId == null) {
            redisUtil.set(RedisPrefixDto.MODULE_TREE, JSONArray.toJSONString(tcTestCaseModuleTree));
            logger.info("redis set key：" + RedisPrefixDto.MODULE_TREE);
        }
        return tcTestCaseModuleTree;
    }


    /**
     * 通过用例列表获取所属模块树
     *
     * @param planId
     * @return
     */
    private List<TcTestCaseModule> getModuleListByPlanId(Integer planId) {
        Set<TcTestCaseModule> moduleList = new HashSet<>();
        Set<TcTestCaseModule> newSet = tcTestCaseModuleMapper.selectByPlanId(planId);
        getTreeModule(moduleList, newSet);
        return new ArrayList<>(moduleList);
    }

    /**
     * 获取所有父节点树
     *
     * @param moduleSet
     */
    private void getTreeModule(Set<TcTestCaseModule> moduleSet, Set<TcTestCaseModule> newSet) {
        moduleSet.addAll(newSet);
        if (newSet.size() > 0) {
            Set<TcTestCaseModule> resultSet = new HashSet<>();
            for (TcTestCaseModule tcTestCaseModule : newSet) {
                Integer parentId = tcTestCaseModule.getParentId();
                if (parentId != 0) {
                    TcTestCaseModule tcTestCaseModule1 = tcTestCaseModuleMapper.selectByPrimaryKey(parentId);
                    resultSet.add(tcTestCaseModule1);
                    moduleSet.add(tcTestCaseModule1);
                }
            }
            getTreeModule(moduleSet, resultSet);
        }
    }

    /**
     * 递归获取下级模块
     *
     * @param parentId
     * @param tcTestCaseModuleList
     * @return
     */
    public List<TcTestCaseModule> getChild(int parentId, List<TcTestCaseModule> tcTestCaseModuleList) {
        List<TcTestCaseModule> childList = new ArrayList<>();
        for (int i = 0; i < tcTestCaseModuleList.size(); i++) {
            TcTestCaseModule tcTestCaseModule = tcTestCaseModuleList.get(i);
            if (parentId == tcTestCaseModule.getParentId().intValue()) {
                childList.add(tcTestCaseModule);
            }
        }

        for (int i = 0; i < childList.size(); i++) {
            TcTestCaseModule tcTestCaseModule = childList.get(i);
            List<TcTestCaseModule> child = getChild(tcTestCaseModule.getId().intValue(), tcTestCaseModuleList);
            tcTestCaseModule.setChildren(child);
        }
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }

    /**
     * 添加模块
     */
    @Override
    public TcTestCaseModule addModule(TcTestCaseModule tcTestCaseModule) {
        tcTestCaseModuleMapper.addModule(tcTestCaseModule);
        //更新redis缓存（1.moduleTree缓存 2.查询用例列表时所用的模块id列表缓存）
        refreshRedis(tcTestCaseModule);
        return tcTestCaseModule;
    }

    @Override
    public int deleteModule(TcTestCaseModule tcTestCaseModule) {
        int i = tcTestCaseModuleMapper.deleteModule(tcTestCaseModule);
        refreshRedis(tcTestCaseModule);
        return i;
    }

    @Override
    public List<TcTestCaseModule> querySubModule(TcTestCaseModule tcTestCaseModule) {
        List<TcTestCaseModule> rootTcTestCaseModuleList = tcTestCaseModuleMapper.querySubModule(tcTestCaseModule);

        if (null != rootTcTestCaseModuleList) {
            logger.info("querySubModule rootTcTestCaseModuleList Size==>{}", rootTcTestCaseModuleList.size());
        } else {
            logger.info("querySubModule rootTcTestCaseModuleList is null");
        }

        return rootTcTestCaseModuleList;
    }

    @Override
    public Result editModule(TcTestCaseModule tcTestCaseModule) {
        List<TcTestCaseModule> editRepeatList = tcTestCaseModuleMapper.editRepeatList(tcTestCaseModule);
        if (editRepeatList.size() > 0) {
            logger.info("存在同名模块==>{}", editRepeatList.get(0));
            return Result.error(ErrorCodeEnum.DATA_IS_EXIST.getErrorCode(), ErrorCodeEnum.DATA_IS_EXIST.getErrorMsg());
        } else {
            tcTestCaseModuleMapper.editModule(tcTestCaseModule);
            logger.info("修改成功");
        }
        refreshRedis(tcTestCaseModule);
        return Result.success();
    }

    @Override
    public List<TcTestCaseModule> getSubModuleTree(int currentModuleId) {
        List<TcTestCaseModule> rootTcTestCaseModules = tcTestCaseModuleMapper.selectAllData();
        logger.info("rootTcTestCaseModules Size==>{}", rootTcTestCaseModules.size());
        List<TcTestCaseModule> tcTestCaseModuleTree = new ArrayList<>();
        for (int i = 0; i < rootTcTestCaseModules.size(); i++) {
            TcTestCaseModule tcTestCaseModule = rootTcTestCaseModules.get(i);
            if (tcTestCaseModule.getParentId().intValue() == currentModuleId) {
                tcTestCaseModuleTree.add(tcTestCaseModule);
                break;
            }
        }

        for (int i = 0; i < tcTestCaseModuleTree.size(); i++) {
            TcTestCaseModule rootTcTestCaseModule = tcTestCaseModuleTree.get(i);
            List<TcTestCaseModule> child = getChild(rootTcTestCaseModule.getId().intValue(), rootTcTestCaseModules);
            rootTcTestCaseModule.setChildren(child);
        }

        return tcTestCaseModuleTree;
    }

    @Override
    public List<TcTestCaseModule> repeatCheckLevelName(TcTestCaseModule tcTestCaseModule) {
        List<TcTestCaseModule> repeatList = tcTestCaseModuleMapper.repeatList(tcTestCaseModule);
        return repeatList;
    }

    @Override
    public List<TcTestCaseModule> getNextModulesById(TcTestCaseModule tcTestCaseModule) {
        return tcTestCaseModuleMapper.getNextModulesById(tcTestCaseModule);
    }


    @Override
    public List<TcTestcase> queryTcTestcaseWithModuleId(TcTestCaseModule tcTestCaseModule) {
        Integer moduleId = tcTestCaseModule.getId();
        List<Integer> moduleIds = tcTestCaseModuleMapper.getChildModuleIdByProc(moduleId);
        return tcTestcaseMapper.queryTcTestcaseWithModuleId(moduleIds);
    }

    /**
     * 更新redis缓存
     *
     * @param tcTestCaseModule
     */
    private void refreshRedis(TcTestCaseModule tcTestCaseModule) {
        //删除模块树缓存
        redisUtil.del(RedisPrefixDto.MODULE_TREE);
        //删除模块id列表缓存
        List<TcTestCaseModule> moduleList = new ArrayList<>();
        getParentList(moduleList, tcTestCaseModule);
        for (TcTestCaseModule module : moduleList) {
            redisUtil.del(RedisPrefixDto.MODULE_IDS + module.getId());
        }
    }

    /**
     * 递归获取所有父节点
     *
     * @param moduleList
     * @param tcTestCaseModule
     */
    private void getParentList(List<TcTestCaseModule> moduleList, TcTestCaseModule tcTestCaseModule) {
        moduleList.add(tcTestCaseModule);
        if (tcTestCaseModule.getParentId() != null && tcTestCaseModule.getParentId() != 0) {
            TcTestCaseModule parentModule = tcTestCaseModuleMapper.selectByPrimaryKey(tcTestCaseModule.getParentId());
            getParentList(moduleList, parentModule);
        }
    }

}
