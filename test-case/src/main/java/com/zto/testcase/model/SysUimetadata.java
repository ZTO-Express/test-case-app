package com.zto.testcase.model;

import java.util.Date;
import lombok.Data;

@Data
public class SysUimetadata {

    private Long id;

    private String appId;

    private String menuCode;

    private String controlId;

    private String controlName;

    private String controlType;

    private Integer isdeleted;

    private String roleCode;

    private Date createdDate;

    private Date updateDate;

}