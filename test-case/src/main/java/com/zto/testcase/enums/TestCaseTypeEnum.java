package com.zto.testcase.enums;

import org.apache.commons.lang3.StringUtils;

public enum TestCaseTypeEnum {


    FUNCTION_TEST(1, "功能测试"),
    SMOKE_TEST(2, "冒烟用例"),
    PERFORMANCE_TEST(3, "性能测试"),
    AUTO_TEST(4, "自动化测试"),
    INTERFACE_TEST(5, "接口测试"),
    INSTALL_TEST(6, "安装部署"),
    CONFIGURATION_TEST(7, "配置相关"),
    SAFETY_TEST(8, "安全相关"),
    OTHER_TEST(9, "其它"),
    TYPICAL_CASE(10, "典型用例"),
    ;

    private Integer code;

    private String desc;

    TestCaseTypeEnum(Integer code, String desc) {
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
        TestCaseTypeEnum[] enums = TestCaseTypeEnum.values();
        for (TestCaseTypeEnum enumObj : enums) {
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
        TestCaseTypeEnum[] enums = TestCaseTypeEnum.values();
        for (TestCaseTypeEnum enumObj : enums) {
            if (StringUtils.equals(StringUtils.trim(desc), enumObj.desc)) {
                return enumObj.code;
            }
        }
        return null;
    }

}
