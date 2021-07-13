package com.zto.testcase.service;

import com.zto.testcase.dto.UserDto;
import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.model.UserPO;
import com.zto.testcase.response.UserListItem;
import java.util.List;
import java.util.Map;

public interface UserService {

    List<SysUserInfo> getAllTester();

    String create(UserDto request);

    /**
     * 用户删除
     *
     * @param userId
     * @return
     */
    String delete(String userId);

    /**
     * 通过用户编号获取用户信息
     *
     * @return
     */
    SysUserInfo getUserInfoByUserId(String userId);

    /**
     * @param UserDto
     * @return
     */
    String updateByUserId(UserDto UserDto);

    /**
     * 获取系统所有用户
     *
     * @param appId
     * @return
     */
    List<UserListItem> all(String appId);

    Map<String, Object> loadAppData(UserDto UserDto);

    List<UserListItem> findByRoleCode(UserDto UserDto);

    /**
     * 通过登录名
     *
     * @param UserDto
     * @return
     */
    UserListItem findByLoginName(UserDto UserDto);

    /**
     * 登出清除缓存接口
     *
     * @param UserDto
     * @return
     */
    String logout(UserDto UserDto);
}
