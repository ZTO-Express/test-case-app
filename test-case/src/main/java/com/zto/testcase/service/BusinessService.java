package com.zto.testcase.service;

import com.zto.testcase.model.BusinessLinePO;
import com.zto.testcase.model.ProjectPO;
import com.zto.testcase.model.SysVersion;
import java.util.List;

public interface BusinessService {

//    List<BusinessLinePO> getBusinessLineList();

    BusinessLinePO getBusinessLineTree();

    List<ProjectPO> getProjectListByBid(Long id);

    List<SysVersion> getVersionByPid(Integer pId);

}
