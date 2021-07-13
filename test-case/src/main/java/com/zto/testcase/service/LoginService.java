package com.zto.testcase.service;

import com.zto.testcase.request.LoginRequest;
import com.zto.testcase.response.Result;
import java.util.Map;

public interface LoginService {

    /**
     * 登陆并获取权限信息
     *
     * @param loginRequest
     * @return
     */
//    Map<String, Object> login(LoginRequest loginRequest);

    Result login(LoginRequest loginRequest);

    Map<String, Object> loginAuthorization(LoginRequest loginRequest);

    Map<String, Object> forgetPswAuthorization();
}
