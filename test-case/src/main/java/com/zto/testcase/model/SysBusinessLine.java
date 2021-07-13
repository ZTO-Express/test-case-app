package com.zto.testcase.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SysBusinessLine implements Serializable {

    /**
     * id
     */
    private Integer bid;

    /**
     * 部门名称
     */
    private String bname;

    /**
     * 基础资料主键id
     */
    private Integer id;

    /**
     * GROUP(10,"集团"),COMPANY(11,"直营公司"),
     * CENTER(13,"中心"),BRANCH(14,"网点"),SUBSECTION(15,"分部(承包区)"),
     * FUNCTIONAL_DEPT(16,"职能部门"), DEPT(30,"部门"),OTHER(90,"其它")
     */
    private Integer type;

    /**
     * 基础资料对应父部门主键id
     */
    private Integer parentId;

    /**
     * 部门人员
     */
    private Integer userCount;

    /**
     * 层级，科技与信息中心为1，以下每一层+1
     */
    private Integer level;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否可见，0-不可见，1-可见
     */
    private Byte visibility;

    /**
     * 状态：0-删除，1-正常
     */
    private Byte status;

    /**
     * 小組id
     */
    private Integer tId;

    /**
     * 通知邮件收件人，多个邮件地址之间分号分割
     */
    private String notifyEmailTo;

    /**
     * 通知邮件收件人，多个邮件地址之间分号分割
     */
    private String notifyEmailCc;

    private static final long serialVersionUID = 1L;
}