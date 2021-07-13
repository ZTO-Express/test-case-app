package com.zto.testcase.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterUserRequest extends BaseUserRequest {
    /**
     * 用户编号
     **/
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

}
