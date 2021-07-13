package com.zto.testcase.enums;

import org.apache.commons.lang3.StringUtils;

public enum FieldChinaeseEnum {
    APP_ID("appId", "appId不能为空", "F40106"),
    USER_NAME("userName", "用户名格式错误", "F40102"),
    USER_PWD("userPwd", "密码格式错误", "F40105"),
    OLD_USER_PWD("oldUserPwd", "原密码格式错误", "F40305"),
    NICK_NAME("nickName", "用户昵称格式错误", "F40107"),
    EMAIL("email", "邮箱格式错误", "F40104"),
    MOBILE("mobile", "手机号格式错误", "F40103"),
    USER_STATUS("userStatus", "用户状态格式错误", "F40404"),
    USER_ID("userId", "用户号格式错误", "F40401"),
    ROLE_NAME("roleName", "角色名称有误", "F40501"),
    ROLE_CODE("roleCode", "角色编号有误", "F40502"),

    ;
    private String field;
    private String chinaese;
    private String errorCode;
    FieldChinaeseEnum(String field, String chinaese, String errorCode){
        this.field = field;
        this.chinaese = chinaese;
        this.errorCode = errorCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getChinaese() {
        return chinaese;
    }

    public void setChinaese(String chinaese) {
        this.chinaese = chinaese;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public static FieldChinaeseEnum getChinaese(String field){
        FieldChinaeseEnum[] fieldChinaeseEnums = FieldChinaeseEnum.values();
        for (FieldChinaeseEnum fieldChinaeseEnum : fieldChinaeseEnums){
            if(StringUtils.equals(StringUtils.trim(field), fieldChinaeseEnum.field)){
                return fieldChinaeseEnum;
            }
        }
        return null;
    }
}
