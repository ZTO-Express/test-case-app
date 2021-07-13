package com.zto.testcase.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TcTestStage extends BasePO {
    private Integer id;

    private Byte status;

    private String name;

}