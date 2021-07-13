package com.zto.testcase.model;

import java.util.List;
import lombok.Data;

@Data
public class BusinessLinePO extends BasePO {

    private Long id;

    /**
     * 业务线名称
     */
    private String bname;
    /**
     * 层级，以下每一层+1
     */
    private Long level;

    /**
     * 基础资料对应父部门主键id
     */
    private Long parentid;

    /**
     * 测试部名称
     */
    private Long tid;

    /**
     * 测试部名称
     */
    private String tname;

    private List<BusinessLinePO> children;

    /**
     * 收件人
     */
    private String notifyEmailTo;

    /**
     * 抄送人
     */
    private String notifyEmailCc;

    private Long bussinessId;

    private Long projectBid;

    private String emailTitle;

    @Override
    public String toString() {
        return "BusinessLinePO [bname=" + bname + ", level=" + level + ", parentid=" + parentid + ", tid=" + tid
                + ", tname=" + tname + ", children=" + children + ", notifyEmailTo=" + notifyEmailTo
                + ", notifyEmailCc=" + notifyEmailCc + ", bussinessId=" + bussinessId + "]";
    }


}
