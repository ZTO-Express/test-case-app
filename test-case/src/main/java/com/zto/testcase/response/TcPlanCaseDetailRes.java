package com.zto.testcase.response;

import com.zto.testcase.dto.TcPlanStepListDTO;
import com.zto.testcase.model.TcFile;
import lombok.Data;

import java.util.List;

@Data
public class TcPlanCaseDetailRes {

    /**
     * 用例名称
     */
    private String name;

    /**
     * 优先级:1 低 2 中 3 高
     */
    private Integer priority;

    /**
     * 用例类型：1-功能测试, 2-性能测试, 3-接口测试, 4-安装部署,5-配置相关,6安全相关, 7-其它
     */
    private Integer type;

    /**
     * 所属模块id
     */
    private String moduleId;

    /**
     * 所属模块
     */
    private String moduleName;

    /**
     * 执行结果，0-未执行，1-通过，2-失败，3-阻塞，4-跳过
     */
    private Integer result;

    /**
     * 备注
     */
    private String comment;

    /**
     * 执行结果备注
     */
    private String resultComment;

    /**
     * 文件列表
     */
    private List<TcFile> fileList;

    /**
     * 用例执行结果文件列表
     */
    private List<TcFile> resultFileList;

    /**
     * 步骤结果列表
     */
    private List<TcPlanStepListDTO> stepList;
}
