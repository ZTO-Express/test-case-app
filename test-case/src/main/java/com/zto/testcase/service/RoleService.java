package com.zto.testcase.service;

import com.zto.testcase.model.RoleInfo;
import com.zto.testcase.request.RoleRequest;
import com.zto.testcase.response.RoleQueryResponse;
import com.zto.testcase.response.RoleResponse;
import java.util.List;

public interface RoleService {

    /**
     * 添加角色
     *
     * @param roleRequest
     * @return
     */
    String create(RoleRequest roleRequest);

    /**
     * 删除角色
     *
     * @return
     */
    String delete(RoleRequest roleRequest);

    /**
     * 更新角色
     *
     * @param roleRequest
     * @return
     */
    RoleResponse updateByRoleCode(RoleRequest roleRequest);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<RoleQueryResponse> all(RoleRequest roleRequest);

    List<RoleInfo> findByUserId(RoleRequest roleRequest);

    RoleInfo findByRoleCode(RoleRequest roleRequest);

    RoleInfo findByRoleName(RoleRequest roleRequest);

    /**
     * 批量绑定菜单
     *
     * @param roleRequest
     * @return
     */
    String batchBandMenu(RoleRequest roleRequest);

    /**
     * 找父角色
     *
     * @param roleRequest appId， roleCode 必须
     * @return
     */
    List<RoleInfo> findParentRoles(RoleRequest roleRequest);

    /**
     * 找子角色
     *
     * @param roleRequest appId， roleCode 必须
     * @return
     */
    List<RoleInfo> findSubRoles(RoleRequest roleRequest);
}
