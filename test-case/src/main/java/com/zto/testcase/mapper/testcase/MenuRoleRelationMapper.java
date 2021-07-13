package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.MenuRoleRelation;
import com.zto.testcase.request.MenuRoleRelationRequest;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MenuRoleRelationMapper {

    int insert(MenuRoleRelation record);

    int updateById(MenuRoleRelation menuRoleRelation);

    int deleteById(MenuRoleRelationRequest menuRoleRelationRequest);

    List<MenuRoleRelation> select(MenuRoleRelation menuRoleRelation);

    int clearByRoleCode(MenuRoleRelation menuRoleRelation);

    int deleteByMenuCode(@Param("menuCode") String menuCode, @Param("appId") String appId);
}