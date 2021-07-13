package com.zto.testcase.model;

import lombok.Data;

/**
 * @Author: wxc
 * @Description:excel列对象
 * @Date: Create in 下午1:43 2021/3/18
 */
@Data
public class ExcelColumn {
    //用例所属
    private String contains;
    //用例名称
    private String caseName;
    //优先级
    private String priority;
    //用例类型
    private String caseType;
    //用例标签
    private String tag;
    //前置条件
    private String precondition;
    //步骤描述
    private String steps;
    //预期结果
    private String expectResult;
    //备注
    private String remark;
}
