package com.zto.testcase.response;

import com.zto.testcase.model.RoleInfo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SysUserInfoJsonData {

    private Integer id;

    private String userId;

    private String userName;

    private String nickName;

    private String userStatus;

    private String mobile;

    private String email;

    private String appId;

    private String userPwd;

    private Integer loginErrorTimes;

    private Date startDate;

    private Date endDate;

    private Date dateCreated;

    private Date dateUpdated;

    private List<RoleInfo> roles;

    public void setStartDate(Object startDate) {
        if (startDate instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) startDate;
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            this.startDate = Date.from(instant);
        } else {
            Date date = new Date(Long.valueOf(startDate + ""));
            this.startDate = date;
        }
    }

    public void setDateCreated(Object dateCreated) {
        if (dateCreated instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) dateCreated;
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            this.dateCreated = Date.from(instant);
        } else {
            Date date = new Date(Long.valueOf(dateCreated + ""));
            this.dateCreated = date;
        }
    }

    public void setDateUpdated(Object dateUpdated) {
        if (dateUpdated instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) dateUpdated;
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            this.dateUpdated = Date.from(instant);
        } else {
            Date date = new Date(Long.valueOf(dateUpdated + ""));
            this.dateUpdated = date;
        }
    }

    public void setEndDate(Object endDate) {
        if (endDate instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) endDate;
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            this.endDate = Date.from(instant);
        } else {
            Date date = new Date(Long.valueOf(endDate + ""));
            this.endDate = date;
        }
    }


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
