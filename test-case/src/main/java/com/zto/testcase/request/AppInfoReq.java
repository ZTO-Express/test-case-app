package com.zto.testcase.request;

import lombok.Data;

@Data
public class AppInfoReq {
    private Long id;
    private String appName;
    private String domain;
    private Integer isDeleted = 0;
}
