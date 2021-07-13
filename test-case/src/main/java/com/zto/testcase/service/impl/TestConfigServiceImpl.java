package com.zto.testcase.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zto.testcase.mapper.testcase.TcCaseTypeMapper;
import com.zto.testcase.mapper.testcase.TcTestStageMapper;
import com.zto.testcase.model.TcCaseType;
import com.zto.testcase.model.TcTestStage;
import com.zto.testcase.service.TestConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: wxc
 * @Description:
 * @Date: Create in 下午3:08 2021/3/10
 */
@Service
@Slf4j
public class TestConfigServiceImpl implements TestConfigService {

    @Resource
    private TcCaseTypeMapper caseTypeMapper;

    @Resource
    private TcTestStageMapper testStageMapper;

    @Override
    public List<TcCaseType> listAllType() {
        return caseTypeMapper.listAll();
    }

    @Override
    public List<TcCaseType> listAllTag() {
        return caseTypeMapper.listAllTag();
    }

    @Override
    public List<TcTestStage> listAllStage() {
        return testStageMapper.listAll();
    }

    @Override
    public int addTypeOrUpdate(TcCaseType caseType) {
        if(caseType.getId()==null){
            //新增标签
            return caseTypeMapper.addType(caseType);
        }else{
            //更新标签
            return caseTypeMapper.updateType(caseType);
        }
    }

    @Override
    public int addStageOrUpdate(TcTestStage testStage) {
        if(testStage.getId()==null){
            //新增阶段
            return testStageMapper.addStage(testStage);
        }else{
            //更新阶段
            return testStageMapper.updateStage(testStage);
        }
    }

    @Override
    public int changeTypeStatus(TcCaseType caseType) {
        return caseTypeMapper.changeStatus(caseType);
    }

    @Override
    public int changeStageStatus(TcTestStage testStage) {
        return testStageMapper.changeStatus(testStage);
    }

    @Override
    public TcCaseType findTypeByName(String name) {
        return caseTypeMapper.findTypeByName(name);
    }

    @Override
    public TcTestStage findStageByName(String name) {
        return testStageMapper.findStageByName(name);
    }

    @Override
    public TcCaseType findTypeById(Integer id) {
        return caseTypeMapper.findTypeById(id);
    }
}
