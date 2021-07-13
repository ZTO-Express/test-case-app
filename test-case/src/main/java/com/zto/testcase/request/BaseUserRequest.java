package com.zto.testcase.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class BaseUserRequest {

    /**
     * 登录名
     **/
    @Length(min = 0, max = 50)
    private String userName;

    /**
     * 登录密码
     **/
    @NotEmpty
    @Length(min = 0, max = 50)
    private String userPwd;

    @NotEmpty
    @Length(min = 0, max = 50)
    private String appId;

}
