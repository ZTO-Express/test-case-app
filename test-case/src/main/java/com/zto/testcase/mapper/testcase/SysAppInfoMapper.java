package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.SysAppInfo;
import com.zto.testcase.request.AppInfoReq;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysAppInfoMapper {

    List<SysAppInfo> findList(AppInfoReq appInfoReq);

    SysAppInfo queryByAppId(String appId);

}
