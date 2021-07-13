package com.zto.testcase.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleQueryResponse implements Serializable {

    private String roleCode;

    private int userCount; // 用户数量

    private String roleName;

    private String status;

    private long createTime;

    private String isSystemRole; // 是否是系统角色， 0非， 1是

    private String isMyRole;  // 是否是自己的角色, 0非， 1是


}
