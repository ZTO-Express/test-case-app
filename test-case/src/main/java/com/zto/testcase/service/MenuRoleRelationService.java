package com.zto.testcase.service;


import com.zto.testcase.model.MenuRoleRelation;
import com.zto.testcase.request.MenuRoleRelationRequest;
import java.util.List;

public interface MenuRoleRelationService {

    int insert(MenuRoleRelationRequest menuRoleRelationRequest);

    int updateById(MenuRoleRelationRequest menuRoleRelationRequest);

    int deleteById(MenuRoleRelationRequest menuRoleRelationRequest);

    List<MenuRoleRelation> select(MenuRoleRelationRequest menuRoleRelationRequest);

    List treeByRoleCode(MenuRoleRelationRequest menuRoleRelationRequest);

    int deleteByMenuCode(MenuRoleRelationRequest menuRoleRelationRequest);
}
