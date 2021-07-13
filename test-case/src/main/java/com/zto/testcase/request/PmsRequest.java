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
public class PmsRequest {

    @NotBlank(message = "系统ID 不能为空", groups = {deleteByUiMetaId.class, deleteByRoleCode.class, insert.class,
            batchInsert.class, deleteById.class, updateById.class, batchUpdateById.class, select.class,
            treeByRoleCode.class, topTree.class})
//    @InEnum(message = "系统编号必须合法", enumClass = AppIpEnum.class, attribute = "appId", groups = {deleteByUiMetaId.class, deleteByRoleCode.class, insert.class, batchInsert.class, deleteById.class, updateById.class, batchUpdateById.class, select.class, treeByRoleCode.class, topTree.class})
    private String appId;

    @NotBlank(message = "角色编码 不能为空", groups = {deleteByRoleCode.class, insert.class, batchInsert.class,
            treeByRoleCode.class})
    @Length(max = 20, message = "1 - 20的字符串", groups = {deleteByRoleCode.class, insert.class, batchInsert.class,
            updateById.class, batchUpdateById.class})
    private String roleCode;

    @NotBlank(message = "控件记录ID 不能为空", groups = {deleteByUiMetaId.class, insert.class, batchInsert.class})
    private String uiMetaId;

    @NotNull(message = "操作元素展现方式 不能为空", groups = {insert.class, batchInsert.class})
    private Integer operationShowType;

    @NotNull(message = "字段元素展现方式 不能为空", groups = {insert.class, batchInsert.class})
    private Integer fieldPermissionType;

    @NotNull(message = "ID 不能为空", groups = {updateById.class, batchUpdateById.class, deleteById.class})
    private Long id;

    interface insert {

    }

    interface updateById {

    }

    interface deleteById {

    }

    interface select {

    }

    interface treeByRoleCode {

    }

    interface topTree {

    }

    interface batchInsert {

    }

    interface batchUpdateById {

    }

    interface deleteByRoleCode {

    }

    interface deleteByUiMetaId {

    }
}
