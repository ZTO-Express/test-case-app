package com.zto.testcase.controller.basic;

import com.zto.testcase.mapper.testcase.SysAppInfoMapper;
import com.zto.testcase.model.SysAppInfo;
import com.zto.testcase.request.AppInfoReq;
import com.zto.testcase.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "app信息")
@RestController
@RequestMapping("/v1/appInfo")
public class AppInfoController {

    @Resource
    private SysAppInfoMapper sysAppInfoMapper;

    @ApiOperation(value = "app信息列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<List<SysAppInfo>> findAppInfo(@RequestBody AppInfoReq appInfoReq) {
        return Result.success(sysAppInfoMapper.findList(appInfoReq));
    }

    @ApiOperation(value = "queryByAppId")
    @RequestMapping(value = "/queryByAppId", method = RequestMethod.GET)
    public Result<SysAppInfo> queryByAppId(@Param("appId") String appId) {
        return Result.success(sysAppInfoMapper.queryByAppId(appId));
    }
}
