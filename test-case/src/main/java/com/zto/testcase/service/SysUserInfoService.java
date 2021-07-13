package com.zto.testcase.service;

import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.response.UserResponse;

public interface SysUserInfoService {

    /**
     * 注册
     *
     * @param sysUserInfo 用户信息
     * @return
     */
    public UserResponse register(SysUserInfo sysUserInfo);

    /**
     * 登录
     *
     * @param sysUserInfo 用户信息
     * @return
     */
    public UserResponse login(SysUserInfo sysUserInfo);

    /**
     * 解锁用户
     *
     * @param userId
     * @param appId
     * @return
     */
    UserResponse unLocking(String userId, String appId);

    UserResponse updateUserPwd(SysUserInfo sysUserInfo);

    UserResponse getUserInfoByUserName(SysUserInfo sysUserInfo);

    UserResponse getUserDynamic(SysUserInfo sysUserInfo);

    UserResponse update(SysUserInfo sysUserInfo);
}
