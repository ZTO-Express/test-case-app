package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.SysUimetadata;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

public interface SysUimetadataMapper {
    int insert(SysUimetadata sysUimetadata);

    List<SysUimetadata> select(SysUimetadata sysUimetadata);

    int updateById(SysUimetadata sysUimetadata);

}