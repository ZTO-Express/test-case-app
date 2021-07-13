package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.BusinessLinePO;
import com.zto.testcase.model.ProjectPO;
import com.zto.testcase.model.SysVersion;
import java.util.List;

public interface BusinessMapper {

//    List<BusinessLinePO> getBusinessLineList();

    List<BusinessLinePO> getBusinessLineTree();

    List<ProjectPO> getProjectListByBid(Long id);

    List<SysVersion> getVersionByPid(Integer pId);

//    List<BusinessLinePO> getBusinessLineTreeNew();

//    BusinessLinePO getBusinessLineById(Integer id);

//	List<BusinessLinePO> findChildBusinessLinePOByParentId(List<Long> list);

    /**
     * 通过父部门查询子部门
     * @param ids
     * @return
     */
//    List<Long> findByParentIds(List<Long> ids);
}
