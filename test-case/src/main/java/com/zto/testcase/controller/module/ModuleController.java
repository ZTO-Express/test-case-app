package com.zto.testcase.controller.module;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.model.TcTestCaseModule;
import com.zto.testcase.model.TcTestcase;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.ModuleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/module")
@Api(value = "Module Api 接口文档")
@RestController
public class ModuleController {


    private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);

    @Resource
    public ModuleService moduleService;

    @RequestMapping(value = "/getModuleTree")
    @ResponseBody
    @ApiOperation(value = "getModuleTree Api", notes = "模块树")
    public Result getModuleTree(@RequestParam(value = "planId",required = false)Integer planId) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            result = Result.success(moduleService.getModuleTree(planId));
            logger.info(">>>>>getModuleTree 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>getModuleTree 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/getNextModulesById")
    @ResponseBody
    @ApiOperation(value = "getNextModulesById Api", notes = "下级树")
    public Result getNextModulesById(@RequestBody TcTestCaseModule tcTestCaseModule) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            result = Result.success(moduleService.getNextModulesById(tcTestCaseModule));
            logger.info(">>>>>getModuleSubTreeById 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>getModuleSubTreeById 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/addModule")
    @ResponseBody
    @ApiOperation(value = "addModule Api", notes = "添加模块")
    public Result addModule(@RequestBody TcTestCaseModule tcTestCaseModule) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>addModule 请求开始，request : {}", JSON.toJSONString(tcTestCaseModule));
            List<TcTestCaseModule> tcTestCaseModuleList = moduleService.repeatCheckLevelName(tcTestCaseModule);
            if (tcTestCaseModuleList.size() > 0) {
                result = Result
                        .error(ErrorCodeEnum.DATA_IS_EXIST.getErrorCode(), ErrorCodeEnum.DATA_IS_EXIST.getErrorMsg());
            } else {
                result = Result.success(moduleService.addModule(tcTestCaseModule));
            }
            logger.info(">>>>>addModule 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>addModule 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/repeatCheckLevelName")
    @ResponseBody
    @ApiOperation(value = "repeatCheckLevelName Api", notes = "添加重复检查模块")
    public Result repeatCheckLevelName(@RequestBody TcTestCaseModule tcTestCaseModule) {
        Result result = Result.success();
        long time = System.currentTimeMillis();
        try {
            List<TcTestCaseModule> tcTestCaseModuleList = moduleService.repeatCheckLevelName(tcTestCaseModule);
            if (tcTestCaseModuleList.size() > 0) {
                result = Result
                        .error(ErrorCodeEnum.DATA_IS_EXIST.getErrorCode(), ErrorCodeEnum.DATA_IS_EXIST.getErrorMsg());
            }
            logger.info(">>>>>repeatCheckLevelName request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>repeatCheckLevelName request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }


    @RequestMapping(value = "/deleteModule")
    @ResponseBody
    @ApiOperation(value = "deleteModule Api", notes = "删除模块")
    public Result deleteModule(@RequestBody TcTestCaseModule tcTestCaseModule) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>deleteModule 请求开始，request : {}", JSON.toJSONString(tcTestCaseModule));
            List<TcTestCaseModule> tcTestCaseModuleList = moduleService.querySubModule(tcTestCaseModule);
            List<TcTestcase> tcTestCaseList = moduleService.queryTcTestcaseWithModuleId(tcTestCaseModule);
            
            if (tcTestCaseModuleList.size() > 0 || tcTestCaseList.size() > 0) {
                result = Result.error(ErrorCodeEnum.MODULE_IS_NOT_EMPTY.getErrorCode(),
                        ErrorCodeEnum.MODULE_IS_NOT_EMPTY.getErrorMsg());
            } else {
                result = Result.success(moduleService.deleteModule(tcTestCaseModule));
            }
            logger.info(">>>>>deleteModule 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>deleteModule 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/editModule")
    @ResponseBody
    @ApiOperation(value = "editModule Api", notes = "编辑模块")
    public Result editModule(@RequestBody TcTestCaseModule tcTestCaseModule) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>editModule 请求开始，request : {}", JSON.toJSONString(tcTestCaseModule));
            result = moduleService.editModule(tcTestCaseModule);
            logger.info(">>>>>editModule 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>editModule 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

}
