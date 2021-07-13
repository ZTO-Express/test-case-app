package com.zto.testcase.dto;

import java.util.List;

import com.zto.testcase.model.TcFile;
import com.zto.testcase.model.TcTestcaseStep;
import com.zto.testcase.request.PageReq;

import lombok.Data;

@Data
public class TcTestCaseDto extends PageReq {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String name;

    private Integer priority;

    private Integer status;

    private Integer type;

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
}
