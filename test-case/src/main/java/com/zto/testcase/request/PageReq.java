package com.zto.testcase.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageReq implements Serializable  {
    private static final long serialVersionUID = 1L;

    private Integer pageNo;

    private Integer pageSize;

    private String user;
}
