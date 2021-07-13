package com.zto.testcase.request;

import com.zto.testcase.validator.anno.InIntArray;
import com.zto.testcase.validator.anno.InStringArray;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UimetadataRequest {
    @NotBlank(message = "系统ID 不能为空", groups = {insert.class, deleteById.class, updateById.class, select.class, tree.class})
//    @InEnum(message = "系统编号必须合法", enumClass = AppIpEnum.class, attribute = "appId", groups = {insert.class, deleteById.class, updateById.class, select.class, tree.class})
    private String appId;

    @NotBlank(message = "菜单码 不能为空", groups = {insert.class, updateById.class})
    @Length(max = 20, message = "必须为 1 - 20 字符", groups = {insert.class, updateById.class})
    private String menuCode;

    @NotBlank(message = "控件ID 不能为空", groups = {insert.class})
    @Length(max = 50, message = "必须为 1 - 50 字符", groups = {insert.class, updateById.class})
    private String controlId;

    @NotBlank(message = "控件名称 不能为空", groups = {insert.class})
    @Length(max = 50, message = "必须为 1 - 20 字符", groups = {insert.class, updateById.class})
    private String controlName;

    @NotBlank(message = "控件类型 不能为空", groups = {insert.class})
    @InStringArray( message = "控件类型 field oper",array = {"field", "oper"}, groups = {insert.class})
    private String controlType;

    @NotNull(message = "控件状态 不能为空", groups = {})
    @InIntArray( message = "控件状态 1：无效 0：有效",array = {1, 0}, groups = {insert.class})
    private Integer isdeleted;

    @NotNull(message = "ID 不能为空", groups = {deleteById.class, updateById.class})
    private Long id;

    @NotBlank(message = "角色编码 不能为空", groups = {})
    @Length(max = 20, message = "1 - 20的字符串", groups = {})
    private String roleCode;

    interface insert {}
    interface deleteById {}
    interface updateById {}
    interface select {}
    interface tree {}

}
