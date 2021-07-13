package com.zto.testcase.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BasePO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date createTime;

    private Date updateTime;

    private String createUser;

    private String updateUser;

    private String user;

}
