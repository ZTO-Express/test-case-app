package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PermissionMapper {

    int insert(Permission record);

    List select(Permission permission);

    int updateById(Permission permission);

    int deleteById(Permission permission);

    int deleteByRoleCode(@Param("roleCode") String roleCode, @Param("appId") String appId);

    int deleteByUiMetaId(@Param("uiMetaId") String uiMetaId, @Param("appId") String appId);
}