package com.zto.testcase.dto;

import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserDto {

    @NotBlank(message = "系统编号不能为空", groups = {create.class, findAll.class, updateByUserId.class, loadAppData.class,
            findByRoleCode.class})
    private String appId;

    @NotBlank(message = "用户编号不能为空", groups = {delete.class, updateByUserId.class, loadAppData.class, logout.class})
    private String userId;

    // 角色编号
    @NotBlank(message = "角色编号不能为空", groups = {create.class, findByRoleCode.class})
    private String roleCode;

    // 用户名
    @NotBlank(message = "登录名不能为空", groups = {create.class})
    @Length(min = 4, max = 20, message = "登录名长度不合法（4-20个字符）", groups = {create.class})
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "登录名格式不合法", groups = {create.class, updateByUserId.class})
    private String userName;

    @NotBlank(message = "密码不能为空", groups = {create.class})
    @Length(max = 50, message = "密码格式(最少6位)", groups = {create.class, updateByUserId.class})
    private String userPwd;

    // 姓名
    @Length(max = 10, message = "姓名长度不合法(2~10个字符)", groups = {create.class, updateByUserId.class})
    private String nickName;

    @Pattern(regexp = "^(1[3|4|5|7|8|9][0-9]\\d{8})?$", message = "手机号不合法", groups = {create.class,
            updateByUserId.class})
    private String mobile;

    @Email(message = "必须满足邮箱格式", groups = {create.class, updateByUserId.class})
    @Length(max = 50, message = "邮箱长度必须少于50", groups = {create.class, updateByUserId.class})
    private String email;

    // 用户状态"2","正常" "3","锁定"
    @NotBlank(message = "账号状态值不能为空", groups = {create.class})
    private String userStatus;

    @NotBlank(message = "登录用户名不能为空", groups = {findByLoginName.class})
    private String loginName;

    /**
     * 以下为校验分组标记
     **/
    interface create {

    }

    interface delete {

    }

    interface updateByUserId {

    }

    interface findAll {

    }

    interface loadAppData {

    }

    interface findByRoleCode {

    }

    interface findByLoginName {

    }

    interface logout {

    }
}
