package com.zto.testcase.dto;

import lombok.Data;

/**
 * @Author: wxc
 * @Description:
 * @Date: Create in 下午4:45 2021/3/16
 */
@Data
public class TcFileDto {
    private String user;
    private Byte type;
    private Integer caseId;
    private Integer planId;
    private Integer planCaseId;
}
