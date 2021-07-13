package com.zto.testcase.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zto.testcase.enums.CommonStatusEnum;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.model.RoleInfo;
import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.request.LoginRequest;
import com.zto.testcase.request.RoleRequest;
import com.zto.testcase.response.Result;
import com.zto.testcase.response.UserResponse;
import com.zto.testcase.service.LoginService;
import com.zto.testcase.service.RoleService;
import com.zto.testcase.service.SysUserInfoService;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private SysUserInfoService sysUserInfoService;

    @Resource
    private RoleService roleService;

    @Override
    public Result login(LoginRequest loginRequest) {
        Result resultBean = new Result();
        if (StringUtils.isNotBlank(loginRequest.getUserPwd())) {
            loginRequest.setUserPwd(new String(Base64.getDecoder().decode(loginRequest.getUserPwd())));
        }

        SysUserInfo sysUserInfo = new SysUserInfo();
        sysUserInfo.setUserName(loginRequest.getUserName());
        sysUserInfo.setUserPwd(loginRequest.getUserPwd());
        sysUserInfo.setAppId(loginRequest.getAppId());
        UserResponse response = sysUserInfoService.login(sysUserInfo);
        log.info("get userInfo : {}", response.toString());
        if (!response.getRespCode().equals(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode())) {
            resultBean.setCode(response.getRespCode());
            resultBean.setMsg(response.getRespMessage());
            return resultBean;
        }

        String userInfoJson = response.getData();
        Map<String, Object> sysUserInfoData = JSONObject.parseObject(userInfoJson, Map.class);

        // 获取角色
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setAppId(loginRequest.getAppId());
        roleRequest.setUserId((String) sysUserInfoData.get("userId"));
        List<RoleInfo> rolesByUserId = roleService.findByUserId(roleRequest);
        if (null == rolesByUserId || rolesByUserId.size() == 0) {
            log.error("User[{}] do not have a valid role", sysUserInfoData.get("userName"));
            throw new BaseException(ErrorCodeEnum.ERROR_USER_HAS_NO_ROLE); // 没有分配角色
        }

        boolean hasActiveRole = false;
        for (RoleInfo roleInfo : rolesByUserId) {
            if (CommonStatusEnum.ACTIVE.getKey().equals(roleInfo.getStatus())) {
                hasActiveRole = true;
            }
        }

        if (!hasActiveRole) {
            log.error("用户[{}]角色状态不可用", sysUserInfoData.get("userName"));
            throw new BaseException(ErrorCodeEnum.ERROR_USER_HAS_NO_ROLE);
        }

        Map<String, Object> resultMap = new HashMap<>();
        sysUserInfoData.put("roles", rolesByUserId);
        resultMap.put("userInfo", sysUserInfoData); // 设置用户信息
        resultMap.put("access_token", "acst:" + sysUserInfoData.get("userId") + ":73f1973280b2f2bf1af217ba121983d0");
        resultMap.put("session_name", "login-session");
        resultMap.put("auth_token", "ce95781c90ffec7eecde93dbecebe2d2");
        resultMap.put("auth_next_step", "auth_verify");
        resultMap.put("auth_max_age", 300000);
        resultBean.setCode(response.getRespCode());
        resultBean.setMsg(response.getRespMessage());
        resultBean.setData(resultMap);
        return resultBean;
    }

//    @Override
//    public Map<String, Object> login(LoginRequest loginRequest) {
//        if(StringUtils.isNotBlank(loginRequest.getUserPwd())){
//            loginRequest.setUserPwd(new String(Base64.getDecoder().decode(loginRequest.getUserPwd())));
//        }
//
//        SysUserInfo sysUserInfo = new SysUserInfo();
//        sysUserInfo.setUserName(loginRequest.getUserName());
//        sysUserInfo.setUserPwd(loginRequest.getUserPwd());
//        sysUserInfo.setAppId(loginRequest.getAppId());
//        UserResponse response = sysUserInfoService.login(sysUserInfo);
//        log.info("get userInfo : {}", response);
//        if (!response.getRespCode().equals(ResultCode.SUCCESS.val())) {
//            throw new BizException(response.getRespCode(), response.getRespMessage());
//        }
//
//        String userInfoJson = response.getData();
//        Map<String, Object> sysUserInfoData = JSONObject.parseObject(userInfoJson, Map.class);
//
//        // 获取角色
//        RoleRequest roleRequest = new RoleRequest();
//        roleRequest.setAppId(loginRequest.getAppId());
//        roleRequest.setUserId((String) sysUserInfoData.get("userId"));
//        List<RoleInfo> rolesByUserId = roleService.findByUserId(roleRequest);
//        if (null == rolesByUserId || rolesByUserId.size() == 0) {
//            log.error("User[{}] do not have a valid role", sysUserInfoData.get("userName"));
//            throw new BizException(ResultCode.ERROR_USER_HAS_NO_ROLE); // 没有分配角色
//        }
//
//        boolean hasActiveRole = false;
//        for (RoleInfo roleInfo : rolesByUserId) {
//            if (CommonStatusEnum.ACTIVE.getKey().equals(roleInfo.getStatus())) {
//                hasActiveRole = true;
//            }
//        }
//
//        if (!hasActiveRole) {
//            log.error("用户[{}]角色状态不可用", sysUserInfoData.get("userName"));
//            throw new BizException(ResultCode.ERROR_USER_HAS_NO_ROLE);
//        }
//
//        Map<String, Object> resultMap = new HashMap<>();
//        sysUserInfoData.put("roles", rolesByUserId);
//        resultMap.put("userInfo", sysUserInfoData); // 设置用户信息
//        return resultMap;
//    }

    @Override
    public Map<String, Object> loginAuthorization(LoginRequest loginRequest) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("session_name", "login-session");
        resultMap.put("auth_token", "64265c505105e20668c1d0e71673893e");
        resultMap.put("auth_next_step", "login");
        resultMap.put("auth_max_age", 300000);
        return resultMap;
    }

    @Override
    public Map<String, Object> forgetPswAuthorization() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("auth_id", "8d991bd9f902fa625c535c2a3db17dee");
        resultMap.put("auth_max_age", 300000);
        resultMap.put("auth_next_step", "login_name_check");
        resultMap.put("auth_token", "ff2b40a351e8847e3aa32c434a934a81");
        resultMap.put("session_name", "forget-psw-certificate");
        return resultMap;
    }
}
