package com.zto.testcase.model;

import java.util.List;
import lombok.Data;

@Data
public class TcTestcase extends BasePO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer priority;

    private Integer status;

    private Integer type;

    private String tag;

    private Integer moduleId;

    private String comment;

    private String precondition;

    private List<Integer> ids;

    private List<Integer> fileIds;

    private List<TcFile> tcFileList;

    private List<TcTestcaseStep> tcTestcaseStepList;

    //是否显示子模块，1-显示 ，0-不显示
    private Integer showSubModule;

    private List<Integer> moduleIds;

    private String sortField;

    private String sortWay;

    private String stepDesc;

    private String expectResult;
}
