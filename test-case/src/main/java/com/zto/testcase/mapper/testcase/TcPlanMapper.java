package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.TcPlan;
import com.zto.testcase.request.TcPlanCaseStatisticReq;
import com.zto.testcase.request.TcPlanListReq;
import com.zto.testcase.response.TcPlanListRes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TcPlanMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TcPlan record);

    int insertSelective(TcPlan record);

    TcPlan selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TcPlan record);

    int updateByPrimaryKey(TcPlan record);

    List<TcPlanListRes> planList(TcPlanListReq tcPlanListReq);

    List<HashMap<Integer, Object>> getPlanCount(TcPlanListReq tcPlanListReq);

    String getCaseTag(Integer vid);

    List<Map<String, Long>> getCreatedPlanCountByCreator(TcPlanCaseStatisticReq tcPlanCaseStatisticReq);

    List<Map<String, Long>> getCreatedPlanCountByDate(TcPlanCaseStatisticReq tcPlanCaseStatisticReq);
}
