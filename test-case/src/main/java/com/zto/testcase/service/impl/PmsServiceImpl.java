package com.zto.testcase.service.impl;

import com.zto.testcase.mapper.testcase.PermissionMapper;
import com.zto.testcase.mapper.testcase.SysUimetadataMapper;
import com.zto.testcase.model.Permission;
import com.zto.testcase.model.RoleInfo;
import com.zto.testcase.model.SysUimetadata;
import com.zto.testcase.request.PmsRequest;
import com.zto.testcase.request.RoleRequest;
import com.zto.testcase.service.PmsService;
import com.zto.testcase.service.RoleService;
import com.zto.testcase.util.BeanUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * <p>
 * User: xie pengqi
 * Date: 2018-04-07
 * Time: 22:44
 */
@Slf4j
@Service
public class PmsServiceImpl implements PmsService {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private SysUimetadataMapper sysUimetadataMapper;

    @Resource
    private RoleService roleService;

    public static Map<String, String> permMap = new HashMap();

    static {
        permMap.put("oper-0", "view");
        permMap.put("field-0", "edit");
        permMap.put("field-2", "view");

        permMap.put("oper", "view");
        permMap.put("field", "edit");

        //减权限映射
        permMap.put("oper-view-del", "del");
        permMap.put("field-view-del", "del");
        permMap.put("field-edit-del", "2");
    }

    @Override
    public long insert(PmsRequest pmsRequest) {
        Permission permission = new Permission();

        BeanUtil.copyAttributeValue(pmsRequest, permission);
        try {
            permissionMapper.insert(permission);
        } catch (DuplicateKeyException e) {
            Permission permissionForSelect = new Permission();
            permissionForSelect.setAppId(pmsRequest.getAppId());
            permissionForSelect.setRoleCode(pmsRequest.getRoleCode());
            permissionForSelect.setUiMetaId(pmsRequest.getUiMetaId());
            List<Permission> selects = permissionMapper.select(permissionForSelect);

            permission.setId(selects.get(0).getId());
            permissionMapper.updateById(permission);
        }
        return permission.getId();
    }

    @Override
    public List select(PmsRequest pmsRequest) {
        Permission permission = new Permission();
        BeanUtil.copyAttributeValue(pmsRequest, permission);

        return permissionMapper.select(permission);
    }

    @Override
    public int updateById(PmsRequest pmsRequest) {
        Permission permission = new Permission();
        BeanUtil.copyAttributeValue(pmsRequest, permission);
        return permissionMapper.updateById(permission);
    }

    @Override
    public int deleteById(PmsRequest pmsRequest) {
        Permission permission = new Permission();
        permission.setAppId(pmsRequest.getAppId());
        permission.setId(pmsRequest.getId());
        return permissionMapper.deleteById(permission);
    }

    @Override
    public Map treeByRoleCode(PmsRequest pmsRequest) {
        Permission permission = new Permission();
        permission.setAppId(pmsRequest.getAppId());
        permission.setRoleCode(pmsRequest.getRoleCode());

        List<Permission> results = permissionMapper.select(permission);

        Map<String, Object> perms = new HashMap<>();
        for (Permission result : results) {
            SysUimetadata sysUimetadata = result.getSysUimetadata();

            sysUimetadata.setId(Long.parseLong(result.getUiMetaId()));

            Map<String, String> typeMap = new HashMap();
            typeMap.put("oper", "oper-" + result.getOperationShowType());
            typeMap.put("field", "field-" + result.getFieldPermissionType());

            String permKey = permMap.get(typeMap.get(sysUimetadata.getControlType()));
            if (StringUtils.isNotBlank(permKey)) {
                Map<String, String> item = new HashMap<>();
                item.put("permId", String.valueOf(result.getId()));
                item.put("permType", permKey);
                item.put("menuCode", sysUimetadata.getMenuCode());
                item.put("uiMetaId", String.valueOf(sysUimetadata.getId()));

                perms.put(sysUimetadata.getControlId(), item);
            }
        }

        return perms;
    }

    @Override
    public Map topTree(PmsRequest pmsRequest) {
        SysUimetadata sysUimetadata = new SysUimetadata();
        sysUimetadata.setIsdeleted(0);
        sysUimetadata.setAppId(pmsRequest.getAppId());

        Map<String, Object> perms = new HashMap<>();
        List<SysUimetadata> sysuimetadatas = sysUimetadataMapper.select(sysUimetadata);

        for (SysUimetadata elem : sysuimetadatas) {
            Map<String, String> item = new HashMap<>();
            item.put("permId", null);
            item.put("permType", permMap.get(elem.getControlType()));
            item.put("menuCode", elem.getMenuCode());
            item.put("uiMetaId", String.valueOf(elem.getId()));

            perms.put(elem.getControlId(), item);
        }

        return perms;
    }

    @Override
    @Transactional
    public List batchInsert(List<PmsRequest> pmsRequests) {
        List<Long> result = new ArrayList<Long>();

        if (pmsRequests.size() == 0) {
            return result;
        }

        for (PmsRequest pmsRequest : pmsRequests) {
            result.add(this.insert(pmsRequest));
        }

        // 回收父级角色权限---子角色权限需要对应收回
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setAppId(pmsRequests.get(0).getAppId());
        roleRequest.setRoleCode(pmsRequests.get(0).getRoleCode());
        List<RoleInfo> subRoles = roleService.findSubRoles(roleRequest);
        if (subRoles.size() > 0) {
            Permission pmsReq = new Permission();
            pmsReq.setRoleCode(pmsRequests.get(0).getRoleCode());
            pmsReq.setAppId(pmsRequests.get(0).getAppId());
            List<Permission> fatherPms = permissionMapper.select(pmsReq);

            Map<String, Long> fatherPmsKey = getPmsKey(fatherPms);

            for (RoleInfo subRole : subRoles) {
                Permission permission = new Permission();
                permission.setRoleCode(subRole.getRoleCode());
                permission.setAppId(roleRequest.getAppId());
                List<Permission> subRolePms = permissionMapper.select(permission);
                Map<String, Long> subRolePmsKey = getPmsKey(subRolePms);

                for (String pmsKey : subRolePmsKey.keySet()) {
                    if (fatherPmsKey.containsKey(pmsKey)) {
                        continue;
                    }

                    Permission permissionUpd = new Permission();
                    String[] strs = pmsKey.split("-");

                    permissionUpd.setId(subRolePmsKey.get(pmsKey));
                    permissionUpd.setAppId(roleRequest.getAppId());
                    String pmsValue = permMap.get(strs[1] + "-" + strs[2] + "-del");
                    if ("del".equals(pmsValue)) {
                        permissionMapper.deleteById(permissionUpd);
                        continue;
                    }

                    if (StringUtils.isNotBlank(pmsValue)) {
                        permissionUpd.setFieldPermissionType(new Integer(pmsValue));
                        permissionMapper.updateById(permissionUpd);
                    }
                }
            }
        }

        return result;
    }

    private Map<String, Long> getPmsKey(List<Permission> pmss) {
        Map<String, Long> pmsKey = new HashMap<>();
        for (Permission pms : pmss) {
            Map<String, String> typeMap = new HashMap();
            typeMap.put("oper", "oper-" + pms.getOperationShowType());
            typeMap.put("field", "field-" + pms.getFieldPermissionType());

            String controlType = pms.getSysUimetadata().getControlType();
            String permKey = permMap.get(typeMap.get(controlType));

            if (StringUtils.isNotBlank(permKey)) {
                pmsKey.put(pms.getUiMetaId() + "-" + pms.getSysUimetadata().getControlType() + "-" + permKey,
                        pms.getId());
                if (permKey.equals("edit")) {
                    pmsKey.put(pms.getUiMetaId() + "-" + pms.getSysUimetadata().getControlType() + "-view",
                            pms.getId());
                }
            }
        }
        return pmsKey;
    }

    @Override
    @Transactional
    public int batchUpdateById(List<PmsRequest> pmsRequests) {
        int row = 0;

        for (PmsRequest pmsRequest : pmsRequests) {
            row += this.updateById(pmsRequest);
        }

        return row;
    }

    @Override
    public int deleteByRoleCode(PmsRequest pmsRequest) {
        return permissionMapper.deleteByRoleCode(pmsRequest.getRoleCode(), pmsRequest.getAppId());
    }

    @Override
    public int deleteByUiMetaId(PmsRequest pmsRequest) {
        return permissionMapper.deleteByUiMetaId(pmsRequest.getUiMetaId(), pmsRequest.getAppId());
    }

}
