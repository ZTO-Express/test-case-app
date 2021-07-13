package com.zto.testcase.service;

import com.zto.testcase.model.TcCaseType;
import com.zto.testcase.model.TcTestStage;

import java.util.List;

/**
 * @Author: wxc
 * @Description:
 * @Date: Create in 下午3:02 2021/3/10
 */
public interface TestConfigService {
    /**
     * 查询全部标签
     *
     * @return
     */
    List<TcCaseType> listAllType();

    /**
     * 查询用例tag
     *
     * @return
     */
    List<TcCaseType> listAllTag();

    /**
     * 查询全部阶段
     *
     * @return
     */
    List<TcTestStage> listAllStage();

    /***
     * 保存或者更新标签
     * @param caseType
     * @return
     */
    int addTypeOrUpdate(TcCaseType caseType);

    /***
     * 保存或者更新阶段
     * @param testStage
     * @return
     */
    int addStageOrUpdate(TcTestStage testStage);

    int changeTypeStatus(TcCaseType caseType);

    int changeStageStatus(TcTestStage testStage);
    /**
     * 根据标签名称查询
     * @param name
     * @return
     */
    TcCaseType findTypeByName(String name);

    /**
     * 根据测试阶段名称查询
     * @param name
     * @return
     */
    TcTestStage findStageByName(String name);

    TcCaseType findTypeById(Integer id);
}
