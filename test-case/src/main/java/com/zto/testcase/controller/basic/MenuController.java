package com.zto.testcase.controller.basic;

import com.zto.testcase.request.MenuRequest;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.MenuService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Long> create (@RequestBody MenuRequest request) {
        return Result.success(menuService.insert(request));
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Result<Integer> updateById (@RequestBody MenuRequest request) {
        return Result.success(menuService.updateById(request));
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public Result<Integer> deleteById (@RequestBody MenuRequest request) {
        return Result.success(menuService.deleteById(request));
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Result<List> query (@RequestBody MenuRequest request) {
        return Result.success(menuService.select(request));
    }

    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public Result<List> tree (@RequestBody MenuRequest request) {
        return Result.success(menuService.tree(request));
    }

    @RequestMapping(value = "/updateSrl", method = RequestMethod.POST)
    public Result<Integer> updateSrl (@RequestBody MenuRequest request) {
        return Result.success(menuService.updateSrl(request));
    }
}
