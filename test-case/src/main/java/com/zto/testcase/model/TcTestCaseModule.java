package com.zto.testcase.model;

import java.util.List;

import lombok.Data;

@Data
public class TcTestCaseModule extends BasePO {

    private Integer id;

    private Integer parentId;

    private String name;

    private String comment;

    private String pkey;

    private Byte status;

    private Integer level;

    private Integer sort;

    private List<TcTestCaseModule> children;

}