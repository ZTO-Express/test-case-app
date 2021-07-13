package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.SysProject;
import java.util.List;

public interface SysProjectMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysProject record);

    int insertSelective(SysProject record);

    SysProject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysProject record);

    int updateByPrimaryKey(SysProject record);

    List<SysProject> selectByBid(Integer bId);
}