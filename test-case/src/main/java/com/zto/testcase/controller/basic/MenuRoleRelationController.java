package com.zto.testcase.controller.basic;

import com.zto.testcase.request.MenuRoleRelationRequest;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.MenuRoleRelationService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pms/menu")
public class MenuRoleRelationController {
    @Resource
    MenuRoleRelationService menuRoleRelationService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Integer> insert (@RequestBody MenuRoleRelationRequest menuRoleRelationRequest) {
        return Result.success(menuRoleRelationService.insert(menuRoleRelationRequest));
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Result<Integer> updateById (@RequestBody MenuRoleRelationRequest menuRoleRelationRequest) {
        return Result.success(menuRoleRelationService.updateById(menuRoleRelationRequest));
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public Result<Integer> deleteById (@RequestBody MenuRoleRelationRequest menuRoleRelationRequest) {
        return Result.success(menuRoleRelationService.deleteById(menuRoleRelationRequest));
    }

    @RequestMapping(value = "/deleteByMenuCode", method = RequestMethod.POST)
    public Result<Integer> deleteByMenuCode (@RequestBody MenuRoleRelationRequest menuRoleRelationRequest) {
        return Result.success(menuRoleRelationService.deleteByMenuCode(menuRoleRelationRequest));
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Result<List> query (@RequestBody MenuRoleRelationRequest menuRoleRelationRequest) {
        return Result.success(menuRoleRelationService.select(menuRoleRelationRequest));
    }

    @RequestMapping(value = "/treeByRoleCode", method = RequestMethod.POST)
    public Result<List> treeByRoleCode (@RequestBody MenuRoleRelationRequest menuRoleRelationRequest) {
        return Result.success(menuRoleRelationService.treeByRoleCode(menuRoleRelationRequest));
    }
}
