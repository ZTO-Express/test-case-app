package com.zto.testcase.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class Permission implements Serializable {

    private Long id;

    private String appId;

    private String roleCode;

    private String uiMetaId;

    private Integer operationShowType;

    private Integer fieldPermissionType;

    private Date createdDate;

    private Date updateDate;

    private static final long serialVersionUID = 1L;


    private SysUimetadata sysUimetadata;

}