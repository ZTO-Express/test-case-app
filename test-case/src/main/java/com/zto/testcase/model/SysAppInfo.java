package com.zto.testcase.model;

import java.util.Date;
import lombok.Data;

@Data
public class SysAppInfo {
    private Long id;
    private String appId;
    private String domain;
    private String appName;
    private Integer isDeleted;
    private Date createdDate;
    private Date updateDate;
    private String smsExt;
}
