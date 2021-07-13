package com.zto.testcase.service;

import com.zto.testcase.request.PmsRequest;
import java.util.List;
import java.util.Map;


public interface PmsService {

    long insert(PmsRequest pmsRequest);

    List select(PmsRequest pmsRequest);

    int updateById(PmsRequest pmsRequest);

    int deleteById(PmsRequest pmsRequest);

    Map treeByRoleCode(PmsRequest pmsRequest);

    Map topTree(PmsRequest pmsRequest);

    List batchInsert(List<PmsRequest> pmsRequests);

    int batchUpdateById(List<PmsRequest> pmsRequests);

    int deleteByRoleCode(PmsRequest pmsRequest);

    int deleteByUiMetaId(PmsRequest pmsRequest);
}
