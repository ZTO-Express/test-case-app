package com.zto.testcase.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class SysProject implements Serializable {

    private Integer id;

    /**
     * 业务线id
     */
    private Integer bIds;

    /**
     * 项目名
     */
    private String pname;

    /**
     * 产品负责人
     */
    private String prdLead;

    /**
     * 开发负责人
     */
    private String devLead;

    /**
     * 测试负责人
     */
    private String qaLead;

    /**
     * 描述
     */
    private String description;

    /**
     * 项目简称
     */
    private String pkey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态：0-删除，1-正常
     */
    private Byte status;

    /**
     * 项目类型：0-研发类，1-基础平台
     */
    private Byte type;

    /**
     * 通知邮件收件人，多个邮件地址之间分号分割
     */
    private String notifyEmailTo;

    /**
     * 通知邮件抄送人，多个邮件地址之间分号分割
     */
    private String notifyEmailCc;

    private Long jpId;

    /**
     * 项目类别：3-中心重点项目，4-中心级项目，5-其他项目
     */
    private Byte category;

    /**
     * 项目总人天：手动在数据库中添加 （只有基础平台类的才会有值）
     */
    private BigDecimal totalManDay;

    /**
     * testlink项目id
     */
    private Integer tlpId;

    /**
     * 用户体验通知邮件收件人，多个邮件地址之间分号分割
     */
    private String userExpNotifyEmailTo;

    /**
     * 用户体验通知邮件抄送人，多个邮件地址之间分号分割
     */
    private String userExpNotifyEmailCc;

    /**
     * 父项目id
     */
    private Integer fPId;

    /**
     * pms中project主键id
     */
    private Integer pmsProjectId;

    /**
     * pms中project名称
     */
    private String pmsProjectName;

    /**
     * pms中所属部门(产品线)主键id
     */
    private Integer pmsDepartmentId;

    /**
     * 是否启用 1是0否
     */
    private Byte enable;

    /**
     * 是否禁用（1:是，0:否）
     */
    private Byte forbidden;

    /**
     * 组织架构id
     */
    private Long bId;

    private static final long serialVersionUID = 1L;
}