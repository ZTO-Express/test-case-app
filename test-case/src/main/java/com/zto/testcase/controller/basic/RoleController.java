package com.zto.testcase.controller.basic;

import com.zto.testcase.model.RoleInfo;
import com.zto.testcase.request.RoleRequest;
import com.zto.testcase.response.Result;
import com.zto.testcase.response.RoleQueryResponse;
import com.zto.testcase.response.RoleResponse;
import com.zto.testcase.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("角色管理")
@RestController
@RequestMapping("/v1/role")
@Slf4j
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation(value = "创建角色")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<String> create(@RequestBody RoleRequest roleRequest) {
        return Result.success(roleService.create(roleRequest));
    }

    @ApiOperation("删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result<String> delete(@RequestBody RoleRequest roleRequest) {
        return Result.success(roleService.delete(roleRequest));
    }

    @ApiOperation("更新角色")
    @RequestMapping(value = "/updateByRoleCode", method = RequestMethod.POST)
    public Result<RoleResponse> updateByRoleCode(@RequestBody RoleRequest roleRequest) {
        return Result.success(roleService.updateByRoleCode(roleRequest));
    }

    @ApiOperation("获取所有的角色信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public Result<List<RoleQueryResponse>> findAll(@RequestBody RoleRequest roleRequest) {
        return Result.success(roleService.all(roleRequest));
    }

    @ApiOperation("通过用户ID获取角色信息")
    @RequestMapping(value = "/findByUserId", method = RequestMethod.POST)
    public Result<List<RoleInfo>> findByUserId(@RequestBody RoleRequest roleRequest) {
        return Result.success(roleService.findByUserId(roleRequest));
    }

    @ApiOperation("批量绑定菜单")
    @RequestMapping(value = "/bandMenu", method = RequestMethod.POST)
    public Result<String> bandMenu (@RequestBody RoleRequest roleRequest) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>{}", roleRequest.toString());
        return Result.success(roleService.batchBandMenu(roleRequest));
    }
}
