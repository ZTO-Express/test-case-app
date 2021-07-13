package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.TcTestStage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TcTestStageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TcTestStage record);

    int insertSelective(TcTestStage record);

    TcTestStage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TcTestStage record);

    int updateByPrimaryKey(TcTestStage record);

    List<TcTestStage> listAll();

    TcTestStage findStageByName(@Param(value="name") String name);

    int addStage(TcTestStage testStage);

    int updateStage(TcTestStage testStage);

    int changeStatus(TcTestStage testStage);
}