package com.zto.testcase.mapper.testcase;

import java.util.List;

import com.zto.testcase.model.TcTestcaseStep;

public interface TcTestcaseStepMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TcTestcaseStep record);

    int insertSelective(TcTestcaseStep record);

    TcTestcaseStep selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TcTestcaseStep record);

    int updateByPrimaryKey(TcTestcaseStep record);

	int addTcTestcaseStep(TcTestcaseStep tcTestcaseStep);

	void deleteTestcaseStep(Integer caseId);

	List<TcTestcaseStep> getTestcaseStepListByCaseId(Integer caseId);

	int copyAddTcTestcaseStep(TcTestcaseStep newStep);

    int deleteByCaseId(Integer caseId);
}