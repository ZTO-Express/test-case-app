package com.zto.testcase.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequest {

    @NotBlank(message = "系统ID 不能为空", groups = {create.class, updateById.class, deleteById.class, query.class,
            tree.class, updateSrl.class})
//    @InEnum(message = "系统编号必须合法", enumClass = AppIpEnum.class, attribute = "appId", groups = {create.class, updateById.class, deleteById.class, query.class, tree.class, updateSrl.class})
    private String appId;

    @NotBlank(message = "菜单编码 不能为空", groups = {updateSrl.class})
    @Length(max = 20, message = "必须为 1 - 20 字符", groups = {create.class, updateById.class})
    private String menuCode;

    @NotBlank(message = "菜单名称 不能为空", groups = {create.class})
    @Length(max = 50, message = "必须为 1 - 50 字符", groups = {create.class, updateById.class})
    private String menuName;

    @NotBlank(message = "菜单图标 不能为空", groups = {})
    @Length(max = 50, message = "必须为 1 - 50 字符", groups = {create.class, updateById.class})
    private String icon;

    @NotBlank(message = "菜单路由 不能为空", groups = {})
    @Length(max = 50, message = "必须为 1 - 50 字符", groups = {create.class, updateById.class})
    private String url;

    @NotBlank(message = "父ID 不能为空", groups = {})
    @Length(max = 20, message = "必须为 1 - 20 字符", groups = {create.class, updateById.class})
    private String parentCode;

    @NotBlank(message = "菜单编码路径 不能为空", groups = {})
    private String treePath;

    @NotBlank(message = "菜单名称路径 不能为空", groups = {})
    private String treePathName;

    @NotBlank(message = "标题描述 不能为空", groups = {})
    private String titleDesc;

    @NotBlank(message = "菜单帮助 不能为空", groups = {})
    private String menuHelp;

    @NotBlank(message = "上一个菜单编码 不能为空", groups = {})
    private String preMenuCode;

    @NotBlank(message = "下一个菜单编码 不能为空", groups = {})
    private String aftMenuCode;

    @NotNull(message = "显示顺序 不能为空", groups = {})
    private Integer srl;

    @NotNull(message = "菜单状态 不能为空", groups = {})
//    @InIntArray( message = "控件状态 1：无效 0：有效",array = {1, 0}, groups = {create.class})
    private Integer isdeleted;

    @NotNull(message = "菜单是否默认勾选 不能为空", groups = {})
//    @InIntArray( message = "菜单是否默认勾选 1：是 0：否",array = {1, 0}, groups = {create.class})
    private Integer defaultSelectStatus;

    @NotNull(message = "菜单是否可更改勾选状态 不能为空", groups = {})
//    @InIntArray( message = "菜单是否可更改勾选状态 1：是 0：否",array = {1, 0}, groups = {create.class})
    private Integer changeable;

    @NotNull(message = "菜单ID 不能为空", groups = {updateById.class, deleteById.class, updateSrl.class})
    private Long id;

    interface create {

    }

    interface updateById {

    }

    interface deleteById {

    }

    interface query {

    }

    interface tree {

    }

    interface updateSrl {

    }

}
