package com.zto.testcase.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Api(value = "人员信息")
public class UserPO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String createUser;

    private String updateUser;
    /**
     * 部门id
     */
    private Long did;

    /**
     * 小组id
     */
    private Long tid;

    /**
     * 钉钉id
     */
    private String dingId;

    /**
     * 部门
     */
    private String dname;

    /**
     * 用户名称(英文,登录用)
     */
    private String uname;

    /**
     * 用户名称(中文)
     */
    private String displayName;

    /**
     * 状态：0-删除，1-正常
     */
    private Integer status;

    /**
     * 邮件地址
     */
    private String email;

    /**
     * 返回结果
     */
    private String returnResult;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 组名
     */
    private String tname;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 用户唯一标识
     */
    private String openId;

    /**
     * 员工编号
     */
    private String empNumber;
}
