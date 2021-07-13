package com.zto.testcase.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "用户名不能为空", groups = {login.class})
    private String userName;

    @NotBlank(message = "密码不能为空", groups = {login.class})
    private String userPwd;

    @NotBlank(message = "系统编号不能为空", groups = {login.class})
//    @InEnum(message = "系统编号必须合法", enumClass = AppIpEnum.class, attribute = "appId", groups = {login.class})
    private String appId;

    interface login {

    }

}
