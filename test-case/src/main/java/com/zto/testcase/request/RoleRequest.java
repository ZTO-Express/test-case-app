package com.zto.testcase.request;

import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class RoleRequest {

    //    @InEnum(message = "系统编号必须合法", enumClass = AppIpEnum.class, attribute = "appId", groups = {create.class, delete.class, updateByRoleCode.class, findAll.class, findByUserId.class,batchBandMenu.class})
    @NotBlank(message = "系统编号不能为空", groups = {create.class, delete.class, updateByRoleCode.class, findAll.class,
            findByUserId.class, batchBandMenu.class})
    private String appId;

    @NotBlank(message = "角色编号不能为空", groups = {delete.class, updateByRoleCode.class, batchBandMenu.class})
    private String roleCode;

    @NotBlank(message = "角色名称不能为空", groups = {create.class})
    @Length(min = 2, max = 16, message = "角色名称格式(2-16个字符)", groups = {create.class})
    private String roleName;

    @NotBlank(message = "角色状态值不能为空", groups = {create.class})
//    @InStringArray( message = "角色状态值 0不可用 1可用",array = {"1", "0"}, groups = {create.class, updateByRoleCode.class}) // CommonStatusEnum
    private String status;

    @NotBlank(message = "用户编号不能为空", groups = {findByUserId.class, findAll.class, create.class})
    private String userId;

    @NotEmpty(message = "菜单编号不能为空", groups = {batchBandMenu.class})
    private List<String> menuCodes;

    interface create {

    }

    interface delete {

    }

    interface updateByRoleCode {

    }

    interface findByUserId {

    }

    interface findAll {

    }

    interface batchBandMenu {

    }


}
