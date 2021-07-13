package com.zto.testcase.service.impl;

import com.baomidou.mybatisplus.toolkit.SystemClock;
import com.zto.testcase.enums.CommonStatusEnum;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.mapper.testcase.MenuRoleRelationMapper;
import com.zto.testcase.mapper.testcase.RoleInfoMapper;
import com.zto.testcase.model.MenuInfo;
import com.zto.testcase.model.MenuRoleRelation;
import com.zto.testcase.model.RoleInfo;
import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.request.MenuRequest;
import com.zto.testcase.request.MenuRoleRelationRequest;
import com.zto.testcase.request.RoleRequest;
import com.zto.testcase.response.RoleQueryResponse;
import com.zto.testcase.response.RoleResponse;
import com.zto.testcase.service.MenuRoleRelationService;
import com.zto.testcase.service.MenuService;
import com.zto.testcase.service.RoleService;
import com.zto.testcase.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleInfoMapper roleInfoMapper;
    @Resource
    private MenuRoleRelationService menuRoleRelationService;
    @Resource
    private MenuService menuService;
    @Resource
    private MenuRoleRelationMapper menuRoleRelationMapper;
    @Resource
    private UserService userService;

    @Transactional
    @Override
    public String create(RoleRequest roleRequest) {
        String appId = roleRequest.getAppId();
        String roleName = roleRequest.getRoleName();
        String status = roleRequest.getStatus();

        SysUserInfo userInfoByUserId = userService.getUserInfoByUserId(roleRequest.getUserId());
        if (userInfoByUserId == null) {
            throw new BaseException(ErrorCodeEnum.ERROR_USER_ID_NOT_EXITS);
        }

        List<RoleInfo> byUserId = findByUserId(roleRequest);
        RoleInfo creatorRole = byUserId.get(0);
        if (creatorRole == null || creatorRole.getStatus().equals(CommonStatusEnum.INACTIVE.getKey())) {
            throw new BaseException(ErrorCodeEnum.ERROR_USER_HAS_NO_ROLE);
        }

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleName(roleName);
        roleInfo.setSystemRole("0"); // 默认非系统角色
        roleInfo.setAppId(appId);
        roleInfo.setStatus(status);
        roleInfo.setCreateTime(new Date(SystemClock.now()));
        roleInfo.setCreatorChain(creatorRole.getCreatorChain() + "_" + creatorRole.getRoleCode());

        try {
            roleInfoMapper.insert(roleInfo);
        } catch (DuplicateKeyException e) {
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_EXITS);
        }
        roleInfo.setRoleCode("1" + String.format("%05d", roleInfo.getId()));
        roleInfoMapper.updateByPrimaryKey(roleInfo);
        return roleInfo.getRoleCode();
    }

    @Override
    public String delete(RoleRequest roleRequest) {
        RoleInfo roleInfoByRoleCode = findByRoleCode(roleRequest);
        if (null == roleInfoByRoleCode) { // 角色不存在
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_NOT_EXITS);
        }

        // 角色下有用户存在不允许删除
        if (countRoleUsers(roleRequest) > 0) {
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_HAS_USER);
        }

        // 角色下有子角色不允许删除
        if (countSubRole(roleInfoByRoleCode) > 0) {
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_HAS_SUBROLE);
        }

        roleInfoMapper.deleteByPrimaryKey(roleInfoByRoleCode.getId());
        return roleInfoByRoleCode.getRoleCode();
    }

    @Override
    public RoleResponse updateByRoleCode(RoleRequest roleRequest) {
        RoleInfo roleInfoByRoleCode = findByRoleCode(roleRequest);
        if (null == roleInfoByRoleCode) // 角色码不存在
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_NOT_EXITS);
        RoleResponse roleResponse = RoleResponse.builder()
                .roleCode(roleRequest.getRoleCode())
                .oldRoleName(roleInfoByRoleCode.getRoleName())
                .roleName(roleRequest.getRoleName())
                .status(roleInfoByRoleCode.getStatus())
                .build();

        if (StringUtils.isNotBlank(roleRequest.getRoleName())) {
            roleInfoByRoleCode.setRoleName(roleRequest.getRoleName());
        }

        if (StringUtils.isNotBlank(roleRequest.getStatus())) {
            roleInfoByRoleCode.setStatus(roleRequest.getStatus());
            roleResponse.setStatus(roleRequest.getStatus());
        }

        try {
            roleInfoMapper.updateByPrimaryKey(roleInfoByRoleCode);
        } catch (DuplicateKeyException e) {
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_EXITS);
        }
        return roleResponse;
    }

    @Override
    public List<RoleQueryResponse> all(RoleRequest roleRequest) {

        SysUserInfo userInfoByUserId = userService.getUserInfoByUserId(roleRequest.getUserId());
        if (userInfoByUserId == null) {
            throw new BaseException(ErrorCodeEnum.ERROR_USER_ID_NOT_EXITS);
        }

        List<RoleInfo> byUserId = findByUserId(roleRequest);
        RoleInfo creatorRole = byUserId.get(0);
        if (creatorRole == null || creatorRole.getStatus().equals(CommonStatusEnum.INACTIVE.getKey())) {
            throw new BaseException(ErrorCodeEnum.ERROR_USER_HAS_NO_ROLE);
        }

        log.info(creatorRole.toString());
        roleRequest.setRoleCode(creatorRole.getRoleCode());
        List<RoleInfo> all = findSubRoles(roleRequest);

        all.addAll(byUserId);

        List<RoleQueryResponse> roleResponseList = new ArrayList<>();
        RoleQueryResponse roleResponse;
        for (RoleInfo roleInfo : all) {
            roleRequest.setRoleCode(roleInfo.getRoleCode());
            roleResponse = RoleQueryResponse.builder()
                    .roleCode(roleInfo.getRoleCode())
                    .roleName(roleInfo.getRoleName())
                    .status(roleInfo.getStatus())
                    .createTime(roleInfo.getCreateTime().getTime())
                    .userCount(countRoleUsers(roleRequest))
                    .isSystemRole(roleInfo.getSystemRole())
                    .build();

            // 判断是否是自己的角色
            if (byUserId.contains(roleInfo)) {
                roleResponse.setIsMyRole("1");
            } else {
                roleResponse.setIsMyRole("0");
            }

            // 过滤管理员
            if (!"1".equals(roleResponse.getIsSystemRole())) {
                roleResponseList.add(roleResponse);
            }
        }
        return roleResponseList;
    }

    @Override
    public List<RoleInfo> findByUserId(RoleRequest roleRequest) {
        return roleInfoMapper.findByUserId(roleRequest);
    }

    @Override
    public RoleInfo findByRoleCode(RoleRequest roleRequest) {
        return roleInfoMapper.findByRoleCode(roleRequest);
    }

    @Override
    public RoleInfo findByRoleName(RoleRequest roleRequest) {
        return roleInfoMapper.findByRoleName(roleRequest);
    }

    @Transactional
    @Override
    public String batchBandMenu(RoleRequest roleRequest) {

        if (null == findByRoleCode(roleRequest)) {
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_NOT_EXITS);
        }

        List<String> menuCodes = roleRequest.getMenuCodes();
        log.info("==========================menuCodes size{}", menuCodes.size());
        // 校验菜单编码正确性
        for (String menuCode : menuCodes) {
            List<MenuInfo> select = menuService.select(MenuRequest.builder()
                    .appId(roleRequest.getAppId())
                    .menuCode(menuCode)
                    .build());
            if (null == select || select.size() == 0) {
                log.error("没找到对应的菜单编码>>>>{}", menuCode);
                throw new BaseException(ErrorCodeEnum.SYSTEM_EXCEPTION);
            }
        }

        // 回收父级角色权限---子角色权限需要对应收回
        List<RoleInfo> subRoles = findSubRoles(roleRequest);
        if (subRoles.size() > 0) {
            for (RoleInfo subRole : subRoles) {
                MenuRoleRelation menuRoleRelation = new MenuRoleRelation();
                menuRoleRelation.setRoleCode(subRole.getRoleCode());
                menuRoleRelation.setAppId(roleRequest.getAppId());
                List<MenuRoleRelation> subRoleMenus = menuRoleRelationMapper.select(menuRoleRelation);
                List<String> subRoleMenuCodes = new ArrayList<>();
                Map<String, MenuRoleRelation> subMap = new HashMap<>();

                for (MenuRoleRelation subMenuRelation : subRoleMenus) {
                    subRoleMenuCodes.add(subMenuRelation.getMenuCode());
                    subMap.put(subMenuRelation.getMenuCode(), subMenuRelation);
                }
                subRoleMenuCodes.removeAll(menuCodes); // 取子减父之差集

                // 逐条删除差集
                if (subRoleMenuCodes.size() > 0) {
                    for (String toDelMenuCode : subRoleMenuCodes) {
                        menuRoleRelationMapper.deleteById(MenuRoleRelationRequest.builder()
                                .id(subMap.get(toDelMenuCode).getId())
                                .appId(roleRequest.getAppId())
                                .build());
                    }
                }
            }
        }

        // 清空旧数据
        MenuRoleRelation menuRoleRelation = new MenuRoleRelation();
        menuRoleRelation.setRoleCode(roleRequest.getRoleCode());
        menuRoleRelation.setAppId(roleRequest.getAppId());
        menuRoleRelationMapper.clearByRoleCode(menuRoleRelation);

        for (String menuCode : menuCodes) {
            menuRoleRelationService.insert(MenuRoleRelationRequest.builder().appId(roleRequest.getAppId())
                    .roleCode(roleRequest.getRoleCode())
                    .menuCode(menuCode)
                    .build());
        }
            return roleRequest.getRoleCode();
    }


    @Override
    public List<RoleInfo> findParentRoles(RoleRequest roleRequest) {
        RoleInfo byRoleCode = findByRoleCode(roleRequest);

        List<String> list = Arrays.asList(byRoleCode.getCreatorChain().split("_"));

        return roleInfoMapper.findRolesByRoleCodeArray(roleRequest.getAppId(), list);
    }

    @Override
    public List<RoleInfo> findSubRoles(RoleRequest roleRequest) {
        return roleInfoMapper.findSubRoles(roleRequest);
    }

    // 统计角色的USER数量
    private int countRoleUsers(RoleRequest roleRequest) {
        long l = roleInfoMapper.countUserByRoleCode(roleRequest);
        return (int) l;
    }

    private int countSubRole(RoleInfo roleInfo) {
        long l = roleInfoMapper.countSubRole(roleInfo.getAppId(), roleInfo.getCreatorChain() + "_" + roleInfo.getRoleCode());
        return (int) l;
    }


}
