package com.zto.testcase.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String roleCode;

    private String roleName;

    // 默认0， 0：否， 1：是
    private String systemRole;

    private String appId;

    private String status;

    private Date createTime;

    private Date updateTime;

    private String creatorChain; // 创建者层级链
}