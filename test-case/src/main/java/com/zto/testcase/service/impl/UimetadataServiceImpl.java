package com.zto.testcase.service.impl;

import com.zto.testcase.mapper.testcase.PermissionMapper;
import com.zto.testcase.mapper.testcase.SysUimetadataMapper;
import com.zto.testcase.model.SysUimetadata;
import com.zto.testcase.request.UimetadataRequest;
import com.zto.testcase.service.UimetadataService;
import com.zto.testcase.util.BeanUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UimetadataServiceImpl implements UimetadataService {
    @Resource
    SysUimetadataMapper sysUimetadataMapper;
    @Resource
    PermissionMapper permissionMapper;

    @Override
    public long insert(UimetadataRequest uimetadataRequest) {
        SysUimetadata sysUimetadata = new SysUimetadata();
        BeanUtil.copyAttributeValue(uimetadataRequest, sysUimetadata);

        if (sysUimetadata.getIsdeleted() == null) {
            sysUimetadata.setIsdeleted(0);
        }
        sysUimetadata.setId(null);
        sysUimetadataMapper.insert(sysUimetadata);

        return sysUimetadata.getId();
    }

    @Override
    public List select(UimetadataRequest uimetadataRequest) {
        SysUimetadata sysUimetadata = new SysUimetadata();
        BeanUtil.copyAttributeValue(uimetadataRequest, sysUimetadata);
        List<SysUimetadata> result = sysUimetadataMapper.select(sysUimetadata);
        return result;
    }

    @Transactional
    @Override
    public int deleteById(UimetadataRequest uimetadataRequest) {
        permissionMapper.deleteByUiMetaId(String.valueOf(uimetadataRequest.getId()), uimetadataRequest.getAppId());

        SysUimetadata sysUimetadata = new SysUimetadata();
        sysUimetadata.setAppId(uimetadataRequest.getAppId());
        sysUimetadata.setIsdeleted(1);
        sysUimetadata.setId(uimetadataRequest.getId());

        return sysUimetadataMapper.updateById(sysUimetadata);
    }

    @Override
    public int updateById(UimetadataRequest uimetadataRequest) {
        SysUimetadata sysUimetadata = new SysUimetadata();
        BeanUtil.copyAttributeValue(uimetadataRequest, sysUimetadata);

        sysUimetadata.setIsdeleted(null);

        return sysUimetadataMapper.updateById(sysUimetadata);
    }

    @Override
    public Map tree(UimetadataRequest uimetadataRequest) {
        SysUimetadata sysUimetadata = new SysUimetadata();
        sysUimetadata.setAppId(uimetadataRequest.getAppId());
        sysUimetadata.setRoleCode(uimetadataRequest.getRoleCode());
        sysUimetadata.setIsdeleted(0);

        List<SysUimetadata> list = sysUimetadataMapper.select(sysUimetadata);
        Map<String, List> tree = new HashMap<>();
        for (SysUimetadata uimetadata : list) {
            List<SysUimetadata> uimetadatas = tree.get(uimetadata.getMenuCode());
            if (uimetadatas == null) {
                uimetadatas = new ArrayList<>();
                tree.put(uimetadata.getMenuCode(), uimetadatas);
            }
            uimetadatas.add(uimetadata);
        }

        return tree;
    }

}
