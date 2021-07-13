package com.zto.testcase.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SysUserInfo {
    public SysUserInfo() {
    }

    private Integer id;

    private String userId;

    private String userName;

    private String nickName;

    private String userPwd;

    private String oldUserPwd;

    private String userStatus;

    private String mobile;

    private String email;

    private String appId;

    private Integer loginErrorTimes;
    
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime dateCreated;

    private String validKey;

    public void setStartDate(Object startDate) {
        if (startDate instanceof Date) {
            Date date = ((Date) startDate);
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            this.startDate = LocalDateTime.ofInstant(instant, zone);
        } else {
            this.startDate = (LocalDateTime) startDate;
        }
    }

    public void setEndDate(Object endDate) {
        if (endDate instanceof Date) {
            Date date = ((Date) endDate);
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            this.endDate = LocalDateTime.ofInstant(instant, zone);
        } else {
            this.endDate = (LocalDateTime) endDate;
        }
    }

    public void setDateCreated(Object dateCreated) {
        if (dateCreated instanceof Date) {
            Date date = ((Date) dateCreated);
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            this.dateCreated = LocalDateTime.ofInstant(instant, zone);
        } else {
            this.dateCreated = (LocalDateTime) dateCreated;
        }
    }

    public void setDateUpdated(Object dateUpdated) {
        if (dateUpdated instanceof Date) {
            Date date = ((Date) dateUpdated);
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            this.dateUpdated = LocalDateTime.ofInstant(instant, zone);
        } else {
            this.dateUpdated = (LocalDateTime) dateUpdated;
        }
    }

    private LocalDateTime dateUpdated;

    public String getUserId() {
        if (StringUtils.isBlank(StringUtils.trim(userId))) {
            userId = null;
        }
        return userId;
    }

    public String getUserName() {
        if (StringUtils.isBlank(StringUtils.trim(userName))) {
            userName = null;
        }
        return userName;
    }

    public String getNickName() {
        if (StringUtils.isBlank(StringUtils.trim(nickName))) {
            nickName = null;
        }
        return nickName;
    }

    public String getUserPwd() {
        if (StringUtils.isBlank(StringUtils.trim(userPwd))) {
            userPwd = null;
        }
        return userPwd;
    }

    public String getOldUserPwd() {
        if (StringUtils.isBlank(StringUtils.trim(oldUserPwd))) {
            oldUserPwd = null;
        }
        return oldUserPwd;
    }

    public String getUserStatus() {
        if (StringUtils.isBlank(StringUtils.trim(userStatus))) {
            userStatus = null;
        }
        return userStatus;
    }

    public String getMobile() {
        if (StringUtils.isBlank(StringUtils.trim(mobile))) {
            mobile = null;
        }
        return mobile;
    }

    public String getEmail() {
        if (StringUtils.isBlank(StringUtils.trim(email))) {
            email = null;
        }
        return email;
    }

    public String getAppId() {
        if (StringUtils.isBlank(StringUtils.trim(appId))) {
            appId = null;
        }
        return appId;
    }
}