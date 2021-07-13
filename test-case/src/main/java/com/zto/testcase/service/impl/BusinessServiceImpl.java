package com.zto.testcase.service.impl;


import com.zto.testcase.mapper.testcase.BusinessMapper;
import com.zto.testcase.model.BusinessLinePO;
import com.zto.testcase.model.ProjectPO;
import com.zto.testcase.model.SysVersion;
import com.zto.testcase.service.BusinessService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private BusinessMapper businessMapper;

    @Override
    public BusinessLinePO getBusinessLineTree() {
        BusinessLinePO po = new BusinessLinePO();
        List<BusinessLinePO> list = businessMapper.getBusinessLineTree();
        //目录无限制
        for (BusinessLinePO business : list) {
            //第一层
            if (business.getParentid() == 0L) {
                po = business;
                po.setChildren(getSonMenuList(business.getId(), list));
            }
        }
        return po;
    }

    @Override
    public List<ProjectPO> getProjectListByBid(Long id) {
        return businessMapper.getProjectListByBid(id);
    }

    @Override
    public List<SysVersion> getVersionByPid(Integer pId) {
        return businessMapper.getVersionByPid(pId);
    }

    private List<BusinessLinePO> getSonMenuList(Long id, List<BusinessLinePO> allMenu) {
        List<BusinessLinePO> list = new ArrayList<>();
        for (BusinessLinePO po : allMenu) {
            if (String.valueOf(po.getParentid()).equals(String.valueOf(id))) {//找到父级相同的下级
                list.add(po);
            }
        }
        if (list.size() > 0) {
            for (BusinessLinePO vo : list) {
                vo.setChildren(getSonMenuList(vo.getId(), allMenu));
            }
        }
        return list;
    }

}
