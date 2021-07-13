package com.zto.testcase.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class MenuRoleRelation implements Serializable {

    private Integer id;

    private String appId;

    private String menuCode;

    private String roleCode;

    private Date createdDate;

    private Date updateDate;

    private MenuInfo menuInfo;

    private static final long serialVersionUID = 1L;
}