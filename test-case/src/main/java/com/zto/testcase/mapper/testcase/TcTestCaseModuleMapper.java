package com.zto.testcase.mapper.testcase;

import java.util.List;
import java.util.Set;

import com.zto.testcase.model.TcTestCaseModule;
import org.apache.ibatis.annotations.Param;

public interface TcTestCaseModuleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TcTestCaseModule record);

    int insertSelective(TcTestCaseModule record);

    TcTestCaseModule selectByPrimaryKey(Integer id);

    TcTestCaseModule getModuleByCondition(TcTestCaseModule tcTestCaseModule);

    int updateByPrimaryKeySelective(TcTestCaseModule record);

    int updateByPrimaryKey(TcTestCaseModule record);

    List<TcTestCaseModule> selectAllData();

    List<TcTestCaseModule> selectAllDataWithLevel(@Param(value = "customLevel") Integer customLevel);

    int addModule(TcTestCaseModule tcTestCaseModule);

    List<TcTestCaseModule> querySubModule(TcTestCaseModule tcTestCaseModule);

    int deleteModule(TcTestCaseModule tcTestCaseModule);

    int editModule(TcTestCaseModule tcTestCaseModule);

    /**
     * 递归获取所有子模块id
     *
     * @param moduleId
     * @return
     */
    List<Integer> getChildModuleId(Integer moduleId);

    List<TcTestCaseModule> repeatList(TcTestCaseModule tcTestCaseModule);

    List<TcTestCaseModule> getNextModulesById(TcTestCaseModule tcTestCaseModule);

    TcTestCaseModule selectByPkey(String pkey);

    List<TcTestCaseModule> editRepeatList(TcTestCaseModule tcTestCaseModule);

    List<Integer> getChildModuleIdByProc(Integer moduleId);

    TcTestCaseModule getRootModule();

    /**
     * 通过主键列表获取模块列表
     * @param planId
     * @return
     */
    Set<TcTestCaseModule> selectByPlanId(Integer planId);

    List<TcTestCaseModule> getIntegrationTestToQa(Integer parentId);

}
