package com.zto.testcase.controller.basic;

import com.zto.testcase.request.PmsRequest;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.PmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("权限管理")
@RestController
@RequestMapping("/v1/pms/control")
public class PmsController {

    @Resource
    private PmsService pmsService;

    @ApiOperation(value = "创建权限")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Long> insert (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.insert(pmsRequest));
    }

    @ApiOperation(value = "批量创建权限")
    @RequestMapping(value = "/batchCreate", method = RequestMethod.POST)
    public Result<List> batchInsert (@RequestBody List<PmsRequest> pmsRequests) {
        return Result.success(pmsService.batchInsert(pmsRequests));
    }

    @ApiOperation(value = "删除角色关联权限")
    @RequestMapping(value = "/deleteByRoleCode", method = RequestMethod.POST)
    public Result<Integer> deleteByRoleCode (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.deleteByRoleCode(pmsRequest));
    }

    @ApiOperation(value = "删除控件关联权限")
    @RequestMapping(value = "/deleteByUiMetaId", method = RequestMethod.POST)
    public Result<Integer> deleteByUiMetaId (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.deleteByUiMetaId(pmsRequest));
    }

    @ApiOperation(value = "查询权限")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Result<List> select (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.select(pmsRequest));
    }

    @ApiOperation(value = "修改权限")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Result<Integer> updateById (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.updateById(pmsRequest));
    }

    @ApiOperation(value = "批量修改权限")
    @RequestMapping(value = "/batchUpdateById", method = RequestMethod.POST)
    public Result<Integer> batchUpdateById (@RequestBody List<PmsRequest> pmsRequests) {
        return Result.success(pmsService.batchUpdateById(pmsRequests));
    }

    @ApiOperation(value = "删除权限")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public Result<Integer> deleteById (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.deleteById(pmsRequest));
    }

    @ApiOperation(value = "权限扁平树")
    @RequestMapping(value = "/treeByRoleCode", method = RequestMethod.POST)
    public Result<Map> treeByRoleCode (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.treeByRoleCode(pmsRequest));
    }

    @ApiOperation(value = "最高权限树")
    @RequestMapping(value = "/topTree", method = RequestMethod.POST)
    public Result<Map> topTree (@RequestBody PmsRequest pmsRequest) {
        return Result.success(pmsService.topTree(pmsRequest));
    }
}
