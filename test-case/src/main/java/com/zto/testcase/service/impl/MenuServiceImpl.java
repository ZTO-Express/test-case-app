package com.zto.testcase.service.impl;

import com.zto.testcase.mapper.testcase.MenuInfoMapper;
import com.zto.testcase.mapper.testcase.MenuRoleRelationMapper;
import com.zto.testcase.model.MenuInfo;
import com.zto.testcase.request.MenuRequest;
import com.zto.testcase.service.MenuService;
import com.zto.testcase.util.BeanUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    private static String menuCodePrefix = "tc_";

    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Resource
    private MenuRoleRelationMapper menuRoleRelationMapper;

    @Transactional
    @Override
    public long insert(MenuRequest request) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtil.copyAttributeValue(request, menuInfo);
        menuInfo.setMenuCode(null);
        menuInfo.setId(null);
        menuInfoMapper.insert(menuInfo);
        MenuInfo menuInfoForUpd = new MenuInfo();
        menuInfoForUpd.setId(menuInfo.getId());
        menuInfoForUpd.setSrl(menuInfo.getId().intValue());
        menuInfoForUpd.setAppId(menuInfo.getAppId());
        menuInfoForUpd
                .setMenuCode(menuCodePrefix + "1" + String.format("%05d", menuInfo.getId()));
        menuInfoMapper.updateById(menuInfoForUpd);
        return menuInfo.getId();
    }

    @Override
    public int updateById(MenuRequest request) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtil.copyAttributeValue(request, menuInfo);

        menuInfo.setIsdeleted(null);
        menuInfo.setMenuCode(null);
        return menuInfoMapper.updateById(menuInfo);
    }

    @Transactional
    @Override
    public int deleteById(MenuRequest request) {
        menuRoleRelationMapper.deleteByMenuCode(request.getMenuCode(), request.getAppId());

        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setAppId(request.getAppId());
        menuInfo.setId(request.getId());
        menuInfo.setIsdeleted(1);
        menuInfo.setMenuCode(null);
        return menuInfoMapper.updateById(menuInfo);
    }

    @Override
    public List<MenuInfo> select(MenuRequest request) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtil.copyAttributeValue(request, menuInfo);

        return menuInfoMapper.select(menuInfo);
    }

    @Override
    public List tree(MenuRequest request) {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setAppId(request.getAppId());
        menuInfo.setIsdeleted(0);

        List<MenuInfo> result = menuInfoMapper.select(menuInfo);

        List<MenuInfo> tree = new ArrayList<>();
        Map<String, MenuInfo> idMap = new HashMap<>();
        for (MenuInfo item : result) {
            idMap.put(item.getMenuCode(), item);
        }
        for (MenuInfo item : result) {
            if ("root".equals(item.getParentCode())) {
                tree.add(item);
                continue;
            }
            MenuInfo father = idMap.get(item.getParentCode());
            if (father == null) {
                continue;
            }
            father.getChild().add(item);
        }

        sortMenuTree(tree);

        return tree;
    }

    @Transactional
    @Override
    public int updateSrl(MenuRequest request) {
        int row = 0;

        boolean havePre = StringUtils.isNotBlank(request.getPreMenuCode());
        boolean haveAft = StringUtils.isNotBlank(request.getAftMenuCode());

        if (!(havePre || haveAft)) {
            return updateSrl(request.getId(), request.getAppId(), request.getParentCode(), null);
        }

        MenuInfo menuInfoForSelect = new MenuInfo();
        menuInfoForSelect.setAppId(request.getAppId());
        menuInfoForSelect.setIsdeleted(0);
        menuInfoForSelect.setParentCode(request.getParentCode());

        List<MenuInfo> menuInfos = menuInfoMapper.select(menuInfoForSelect);
        menuInfos.sort(Comparator.comparingInt(MenuInfo::getSrl));

        int i = 0;
        for (MenuInfo menuInfo : menuInfos) {
            if (menuInfo.getMenuCode().equals(request.getMenuCode())) {
                continue;
            }

            if (havePre && request.getPreMenuCode().equals(menuInfo.getMenuCode())) {
                row += updateSrl(menuInfo.getId(), menuInfo.getAppId(), null, i);
                i++;
                row += updateSrl(request.getId(), request.getAppId(), request.getParentCode(), i);
                i++;
                continue;
            }

            if (haveAft && request.getAftMenuCode().equals(menuInfo.getMenuCode())) {
                row += updateSrl(request.getId(), request.getAppId(), request.getParentCode(), i);
                i++;
                row += updateSrl(menuInfo.getId(), menuInfo.getAppId(), null, i);
                i++;
                continue;
            }

            row += updateSrl(menuInfo.getId(), menuInfo.getAppId(), null, i);
            i++;
        }

        return row;
    }

    private int updateSrl(Long id, String appId, String parentCode, Integer srl) {
        MenuInfo menuInfoForUpdate = new MenuInfo();
        menuInfoForUpdate.setId(id);
        menuInfoForUpdate.setAppId(appId);
        menuInfoForUpdate.setSrl(srl);
        menuInfoForUpdate.setParentCode(parentCode);

        return menuInfoMapper.updateById(menuInfoForUpdate);
    }

    public static void sortMenuTree(List<MenuInfo> tree) {
        tree.sort(Comparator.comparingInt(MenuInfo::getSrl));
        tree.forEach((menuInfo) -> {
            if (!CollectionUtils.isEmpty(menuInfo.getChild())) {
                sortMenuTree(menuInfo.getChild());
            }
        });
    }

}
