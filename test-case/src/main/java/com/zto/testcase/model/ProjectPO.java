package com.zto.testcase.model;

import lombok.Data;

/**
 * 项目实体类
 */
@Data
public class ProjectPO extends BasePO {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 项目名称名称 同 jira项目名称
	 */
	private String pname;

	/**
	 * 部门名称
	 */
	private String bname;

	/**
	 * 部门id
	 */
	private Long bid;

	/**
	 * 通知邮件收件人，多个邮件地址之间分号分割
	 */
	private String notifyEmailTo;

	/**
	 * 通知邮件抄送人，多个邮件地址之间分号分割
	 */
	private String notifyEmailCc;

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
	 * 状态：0-删除，1-正常 2-停用
	 */
	private Long status;

	/**
	 * pms项目Id
	 */
	private Long pmsPid;

	/**
	 * jira项目Id
	 */
	private Long jpid;

	/**
	 * pms项目名称
	 */
	private String pmsPname;

	/**
	 * pms产品线id
	 */
	private Integer productLineId;

}



