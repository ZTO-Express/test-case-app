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
public class RoleResponse implements Serializable {

    private String roleCode;

    private String oldRoleName;

    private String roleName;

    private String status;

}
