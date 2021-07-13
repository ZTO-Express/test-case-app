package com.zto.testcase.response;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.Data;

@Data
public class SysUserInfoData {
    private Integer id;

    private String userId;

    private String userName;

    private String nickName;

    private String oldUserPwd;

    private String userStatus;

    private String mobile;

    private String email;

    private String appId;

    private Integer loginErrorTimes;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime dateCreated;

    private LocalDateTime dateUpdated;

    private String org;

    public Date getDateCreated() {
        if (Optional.ofNullable(dateCreated).isPresent()) {
            return Date.from(dateCreated.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public Date getDateUpdated() {
        if (Optional.ofNullable(dateUpdated).isPresent()) {
            return Date.from(dateUpdated.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public Date getEndDate() {
        if (Optional.ofNullable(endDate).isPresent()) {
            return Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public Date getStartDate() {
        if (Optional.ofNullable(endDate).isPresent()) {
            return Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }
}
