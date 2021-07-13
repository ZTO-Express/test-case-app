package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.SysBusinessLine;
import java.util.List;

public interface SysBusinessLineMapper {

    int deleteByPrimaryKey(Integer bid);

    int insert(SysBusinessLine record);

    int insertSelective(SysBusinessLine record);

    SysBusinessLine selectByPrimaryKey(Integer bid);

    int updateByPrimaryKeySelective(SysBusinessLine record);

    int updateByPrimaryKey(SysBusinessLine record);

    List<SysBusinessLine> selectByParentId(Integer parentId);
}