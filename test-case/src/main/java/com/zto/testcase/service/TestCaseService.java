package com.zto.testcase.service;

import com.zto.testcase.request.TcPlanCaseStatisticReq;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.zto.testcase.dto.TcTestCaseDto;
import com.zto.testcase.model.TcFile;
import com.zto.testcase.model.TcTestcase;
import com.zto.testcase.model.TcTestcaseStep;
import com.zto.testcase.response.Result;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public interface TestCaseService {

    TcTestcase addTestCase(TcTestcase tcTestcase);

    int deleteTestCase(TcTestcase tcTestcase);

    Result<PageInfo<TcTestcase>> getTestCaseList(TcTestCaseDto tcTestCaseDto);

    int editTestCase(TcTestcase tcTestcase);

    List<TcFile> getFileListByCaseId(TcTestcase tcTestcase);

    List<TcTestcaseStep> getTestcaseStepListByCaseId(TcTestcase tcTestcase);

    int copyTestCase(TcTestcase tcTestcase);

    Result moveTestCase(TcTestcase tcTestcase);

    void exportTestCase(TcTestCaseDto tcTestCaseDto, HttpServletResponse response) throws Exception;

	TcTestcase addTestCaseToNewModule(TcTestcase tcTestcase);

    /**
     * 统计一不同时间内创建用例数或者不同人创建用例数
     *
     * @param tcPlanCaseStatisticReq
     * @return
     */
    List<Map<String,Long>> caseCount(TcPlanCaseStatisticReq tcPlanCaseStatisticReq);
}
