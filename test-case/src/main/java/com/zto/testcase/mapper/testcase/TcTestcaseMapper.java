package com.zto.testcase.mapper.testcase;

import com.zto.testcase.dto.TcTestCaseDto;
import com.zto.testcase.model.TcTestcase;
import com.zto.testcase.request.TcPlanCaseDetailReq;
import com.zto.testcase.request.TcPlanCaseStatisticReq;
import com.zto.testcase.response.TcPlanCaseDetailRes;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TcTestcaseMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TcTestcase record);

    int insertSelective(TcTestcase record);

    TcTestcase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TcTestcase record);

    int updateByPrimaryKeyWithBLOBs(TcTestcase record);

    int updateByPrimaryKey(TcTestcase record);

    int addTestCase(TcTestcase tcTestcase);

    int deleteTestCase(@Param(value = "ids") List<Integer> ids);

    List<TcTestcase> getTestCaseList(TcTestCaseDto tcTestCaseDto);

    List<TcTestcase> getTestCaseAndStepsList(TcTestCaseDto tcTestCaseDto);

    int editTestCase(TcTestcase tcTestcase);

    List<TcTestcase> getTcTestcaseListByIds(@Param(value = "ids") List<Integer> ids);

    int copyAddTestCase(TcTestcase newTcTestcase);

    TcPlanCaseDetailRes getCaseDetail(TcPlanCaseDetailReq tcPlanCaseDetailReq);

    int updateTestCaseByIds(TcTestcase record);

    List<TcTestcase> queryTcTestcaseWithModuleId(@Param(value = "moduleIds") List<Integer> moduleIds);

    List<TcTestcase> getAllTagCase();

    List<Map<String, Long>> getCreatedCaseCountByDate(TcPlanCaseStatisticReq tcPlanCaseStatisticReq);

    List<Map<String, Long>> getCreatedCaseCountByCreator(TcPlanCaseStatisticReq tcPlanCaseStatisticReq);

}
