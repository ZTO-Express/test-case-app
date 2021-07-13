package com.zto.testcase.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class MenuInfo implements Serializable {

    private Long id;

    private String appId;

    private String menuCode;

    private String menuName;

    private String icon;

    private String url;

    private String parentCode;

    private String treePath;

    private String treePathName;

    private Integer srl;

    private String titleDesc;

    private String menuHelp;

    private Integer isdeleted;

    private Integer defaultSelectStatus;

    private Integer changeable;

    private Date createdDate;

    private Date updateDate;

    private List<MenuInfo> child = new ArrayList<>();

    private static final long serialVersionUID = 1L;

}