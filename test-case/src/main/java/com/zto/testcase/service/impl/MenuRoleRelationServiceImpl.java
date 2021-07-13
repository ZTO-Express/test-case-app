package com.zto.testcase.service.impl;

import com.zto.testcase.mapper.testcase.MenuRoleRelationMapper;
import com.zto.testcase.model.MenuInfo;
import com.zto.testcase.model.MenuRoleRelation;
import com.zto.testcase.request.MenuRoleRelationRequest;
import com.zto.testcase.service.MenuRoleRelationService;
import com.zto.testcase.util.BeanUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MenuRoleRelationServiceImpl implements MenuRoleRelationService {

    @Resource
    MenuRoleRelationMapper menuRoleRelationMapper;

    @Override
    public int insert(MenuRoleRelationRequest menuRoleRelationRequest) {
        MenuRoleRelation menuRoleRelation = new MenuRoleRelation();
        BeanUtil.copyAttributeValue(menuRoleRelationRequest, menuRoleRelation);
        menuRoleRelationMapper.insert(menuRoleRelation);
        log.info("=======menuRoleRelation.getId():{}=======", menuRoleRelation.getId());
        return menuRoleRelation.getId();
    }

    @Override
    public int updateById(MenuRoleRelationRequest menuRoleRelationRequest) {
        MenuRoleRelation menuRoleRelation = new MenuRoleRelation();
        BeanUtil.copyAttributeValue(menuRoleRelationRequest, menuRoleRelation);

        return menuRoleRelationMapper.updateById(menuRoleRelation);
    }

    @Override
    public int deleteById(MenuRoleRelationRequest menuRoleRelationRequest) {
        MenuRoleRelation menuRoleRelation = new MenuRoleRelation();
        menuRoleRelation.setId(menuRoleRelationRequest.getId());
        menuRoleRelation.setAppId(menuRoleRelationRequest.getAppId());
        return menuRoleRelationMapper.deleteById(menuRoleRelationRequest);
    }

    @Override
    public List<MenuRoleRelation> select(MenuRoleRelationRequest menuRoleRelationRequest) {
        MenuRoleRelation menuRoleRelation = new MenuRoleRelation();
        BeanUtil.copyAttributeValue(menuRoleRelationRequest, menuRoleRelation);
        return menuRoleRelationMapper.select(menuRoleRelation);
    }

    @Override
    public List treeByRoleCode(MenuRoleRelationRequest menuRoleRelationRequest) {
        MenuRoleRelation menuRoleRelation = new MenuRoleRelation();
        menuRoleRelation.setAppId(menuRoleRelationRequest.getAppId());
        menuRoleRelation.setRoleCode(menuRoleRelationRequest.getRoleCode());
        List<MenuRoleRelation> result = menuRoleRelationMapper.select(menuRoleRelation);

        List<MenuInfo> tree = new ArrayList<>();
        Map<String, MenuInfo> idMap = new HashMap<>();
        for (MenuRoleRelation roleRelation : result) {
            MenuInfo menuInfo = roleRelation.getMenuInfo();
            menuInfo.setMenuCode(roleRelation.getMenuCode());
            idMap.put(menuInfo.getMenuCode(), menuInfo);
        }
        for (MenuRoleRelation roleRelation : result) {
            MenuInfo menuInfo = roleRelation.getMenuInfo();
            if ("root".equals(menuInfo.getParentCode())) {
                tree.add(menuInfo);
                continue;
            }
            MenuInfo father = idMap.get(menuInfo.getParentCode());
            if (father == null) {
                continue;
            }
            father.getChild().add(menuInfo);
        }

        MenuServiceImpl.sortMenuTree(tree);

        return tree;
    }

    @Override
    public int deleteByMenuCode(MenuRoleRelationRequest menuRoleRelationRequest) {
        return menuRoleRelationMapper
                .deleteByMenuCode(menuRoleRelationRequest.getMenuCode(), menuRoleRelationRequest.getAppId());
    }
}
