package com.zto.testcase.model;

import java.util.Date;
import lombok.Data;

@Data
public class SysVersion {
	
	/**
	 * 序号
	 */
	private Integer id;
	/**
	 * 测试计划id
	 */
	private Integer tpId;
	/**
	   * 项目序号
	 */
	private Integer pid;
	/**
	   * 版本名称
	 */
	private String name;
	/**
	   * 状态：1-未发布，2-已发布，3-删除 4-归档，5-关闭
	 */
	private Integer status;
	/**
	   *  开始时间
	 */
	private Date startTime;
	/**
	   *  结束时间
	 */
	private Date endTime;
	/**
	   *   创建时间
	 */
	private Date createTime;
	
	/**
	   *  更新时间
	 */
	private Date updateTime;
	
	/**
	 * jira projectversion表序号
	 */
	private Long jpvId;
	
	/**
	   * 简易项目测试报告序号
	 */
	private Integer strId;
	/**
	   * 版本类型，0-常规版本，1-非常规版本
	 */
	private Integer type;
	/**
	   * 是否发生了变更，0-否，1-是，发生变更时（1）只允许发送准入，没有发生变更时发送准入不会同步pms
	 */
	private Integer needModify;
	/**
	 * 0-已确认版本计划 1-未确认
	 */
	private Integer editable;
	/**
	   * 外采项目测试报告序号
	 */
	private Integer extId;
	/**
	   * 开发开始时间
	 */
	private Date developStartDate;
	/**
	   * 开发结束时间
	 */
	private Date developEndDate;
	/**
	   * 测试开始时间
	 */
	private Date testStartDate;
	/**
	   * 测试结束时间
	 */
	private Date testEndDate;
	/**
	 * 用例标签
	 */
	private String caseTag;

	@Override
	public String toString() {
		return "QaVersion [id=" + id + ", tpId=" + tpId + ", pid=" + pid + ", name=" + name + ", status=" + status
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", jpvId=" + jpvId + ", strId=" + strId
				+ ", type=" + type
				+ ", needModify=" + needModify + ", editable=" + editable + ", extId=" + extId + ", developStartDate="
				+ developStartDate + ", developEndDate=" + developEndDate + ", testStartDate=" + testStartDate
				+ ", testEndDate=" + testEndDate + "]";
	}
	
	

}
