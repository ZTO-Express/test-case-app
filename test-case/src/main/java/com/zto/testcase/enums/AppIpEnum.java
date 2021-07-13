package com.zto.testcase.enums;

import org.apache.commons.lang3.StringUtils;

public enum AppIpEnum {
    WALLET("wallet", "2"),
    BOSS("diablo", "2"),
    ;

    private String appId;

    private String startStatus;

    AppIpEnum(String appId, String startStatus) {
        this.appId = appId;
        this.startStatus = startStatus;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(String startStatus) {
        this.startStatus = startStatus;
    }

    public static String getStartStatus(String appId) {
        AppIpEnum[] appIpEnums = AppIpEnum.values();
        for (AppIpEnum appIpEnum : appIpEnums) {
            if (StringUtils.equals(StringUtils.trim(appId), appIpEnum.appId)) {
                return appIpEnum.startStatus;
            } else if (StringUtils.isNotBlank(appId)) {
                return "2";
            }
        }
        return null;
    }
}
