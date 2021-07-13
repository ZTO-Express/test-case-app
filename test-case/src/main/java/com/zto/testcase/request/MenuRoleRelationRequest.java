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
public class MenuRoleRelationRequest {

    @NotBlank(message = " 不能为空", groups = {deleteByMenuCode.class, insert.class, updateById.class, deleteById.class,
            query.class, treeByRoleCode.class})
//    @InEnum(message = "系统编号必须合法", enumClass = AppIpEnum.class, attribute = "appId", groups = {deleteByMenuCode.class, insert.class, updateById.class, deleteById.class, query.class, treeByRoleCode.class})
    private String appId;

    @NotBlank(message = " 不能为空", groups = {deleteByMenuCode.class, insert.class})
    @Length(max = 50, message = "1 - 50的字符串", groups = {deleteByMenuCode.class, insert.class, updateById.class})
    private String menuCode;

    @NotBlank(message = " 不能为空", groups = {insert.class, treeByRoleCode.class})
    @Length(max = 20, message = "1 - 20的字符串", groups = {insert.class, updateById.class})
    private String roleCode;

    @NotNull(message = " 不能为空", groups = {updateById.class, deleteById.class})
    private Integer id;

    interface insert {

    }

    interface updateById {

    }

    interface deleteById {

    }

    interface query {

    }

    interface treeByRoleCode {

    }

    interface deleteByMenuCode {

    }
}