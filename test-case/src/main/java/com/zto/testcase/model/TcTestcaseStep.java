package com.zto.testcase.model;

import lombok.Data;

@Data
public class TcTestcaseStep extends BasePO {
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer stepNumber;

    private String stepDesc;

    private String expectResult;

    private Integer caseId;

}