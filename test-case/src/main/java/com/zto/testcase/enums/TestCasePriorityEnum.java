package com.zto.testcase.enums;

import org.apache.commons.lang3.StringUtils;

public enum TestCasePriorityEnum {


    LOW(1, "低"),
    MIDDLE(2, "中"),
    HIGH(3, "高"),
    ;

    private Integer code;

    private String desc;

    TestCasePriorityEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public String getDesc(String msg) {
        return desc + ":" + msg;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(Integer code) {
        if (null == code) {
            return "";
        }
        TestCasePriorityEnum[] enums = TestCasePriorityEnum.values();
        for (TestCasePriorityEnum enumObj : enums) {
            if (code == enumObj.code) {
                return enumObj.desc;
            }
        }
        return null;
    }

    public static Integer getCode(String desc) {
        if (StringUtils.isEmpty(desc)) {
            return null;
        }
        TestCasePriorityEnum[] enums = TestCasePriorityEnum.values();
        for (TestCasePriorityEnum enumObj : enums) {
            if (StringUtils.equals(StringUtils.trim(desc), enumObj.desc)) {
                return enumObj.code;
            }
        }
        return null;
    }

}
