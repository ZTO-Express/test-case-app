package com.zto.testcase.model;

import lombok.Data;

@Data
public class TcFile  extends BasePO {
    private Integer id;

    private String path;

    private String name;

    private Integer relationId;

    private Byte type;

}