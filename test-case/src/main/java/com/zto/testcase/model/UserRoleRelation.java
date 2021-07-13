package com.zto.testcase.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserRoleRelation implements Serializable {

    private Integer id;

    private String appId;

    private String userId;

    private String roleId;

    private static final long serialVersionUID = 1L;

}