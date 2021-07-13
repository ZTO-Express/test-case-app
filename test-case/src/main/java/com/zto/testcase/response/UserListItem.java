package com.zto.testcase.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListItem {

    private String userId;

    private String userName;

    private List<RoleItem> roles;

    private String nickName;

    private String mobile;

    private String email;

    private Date createTime;

    private String status;

}
