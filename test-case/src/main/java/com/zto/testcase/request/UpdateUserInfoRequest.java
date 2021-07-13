package com.zto.testcase.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UpdateUserInfoRequest {

    /**
     * 登录密码
     **/
    @NotEmpty
    @Length(min = 0, max = 50)
    private String appId;
    /**
     * 用户编号
     **/
    @NotEmpty
    @Length(min = 0, max = 50)
    private String userId;
    /**
     * 手机号
     **/
    @Length(min = 0, max = 20)
    private String mobile;
    /**
     * 邮箱
     **/
    @Length(min = 0, max = 50)
    private String email;
    @Length(min = 0, max = 50)
    private String nickName;

    @Length(min = 0, max = 20)
    private String userStatus;

}
