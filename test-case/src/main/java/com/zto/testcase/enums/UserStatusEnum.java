package com.zto.testcase.enums;

public enum UserStatusEnum {
    /**
     * "2","正常"
     **/
    NOMARL("2", "正常"),
    /**
     * "3","锁定"
     **/
    LOCKING("3", "锁定"),
    /**
     * "4","停用"
     **/
    DISABLED("4", "停用"),

    ;

    UserStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
