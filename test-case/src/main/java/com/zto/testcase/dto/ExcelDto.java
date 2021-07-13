package com.zto.testcase.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class ExcelDto implements Serializable {

    private static final long serialVersionUID = 6133772627258154184L;

    /**
     * 表头
     */
    private List<String> titles;

    /**
     * 数据
     */
    private List<List<Object>> rows;

    /**
     * 页签名称
     */
    private String name;
}
