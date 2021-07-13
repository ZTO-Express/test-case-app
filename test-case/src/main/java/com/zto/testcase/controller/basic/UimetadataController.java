package com.zto.testcase.controller.basic;

import com.zto.testcase.request.UimetadataRequest;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.UimetadataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("控件管理")
@RestController
@RequestMapping("/v1/uimeta")
public class UimetadataController {

    @Resource
    private UimetadataService uimetadataService;

    @ApiOperation(value = "创建控件")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<Long> insert (@RequestBody UimetadataRequest uimetadataRequest) {
        return Result.success(uimetadataService.insert(uimetadataRequest));
    }

    @ApiOperation(value = "查找控件")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Result<List> select (@RequestBody UimetadataRequest uimetadataRequest) {
        return Result.success(uimetadataService.select(uimetadataRequest));
    }

    @ApiOperation(value = "删除控件")
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    public Result<Integer> deleteById (@RequestBody UimetadataRequest uimetadataRequest) {
        return Result.success(uimetadataService.deleteById(uimetadataRequest));
    }

    @ApiOperation(value = "更新控件ById")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Result<Integer> updateById (@RequestBody UimetadataRequest uimetadataRequest) {
        return Result.success(uimetadataService.updateById(uimetadataRequest));
    }

    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public Result<Map> tree (@RequestBody UimetadataRequest uimetadataRequest) {
        return Result.success(uimetadataService.tree(uimetadataRequest));
    }

}
