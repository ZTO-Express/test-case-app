package com.zto.testcase.dto;

import lombok.Data;

@Data
public class TcPlanCaseListDTO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 用例id
     */
    private Integer caseId;

    /**
     * 执行计划id
     */
    private Integer planId;

    /**
     * 用例名称
     */
    private String name;

    /**
     * 维护人
     */
    private String updateUser;

    /**
     * 优先级,1 低 2 中 3 高
     */
    private Integer priority;

    /**
     * 用例类型：1-功能测试, 2-性能测试, 3-接口测试, 4-安装部署,5-配置相关,6安全相关, 7-其它
     */
    private Integer type;

    /**
     * 执行人
     */
    private String executeUserName;

    /**
     * 执行结果:0-未执行，1-通过，2-失败，3-阻塞，4-跳过
     */
    private Integer result;

    /**
     * 用例状态
     */
    private Integer status;

    /**
     * 所属模块id
     */
    private Integer moduleId;

    private String tag;
}
