package com.zto.testcase.controller.testcase;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.zto.testcase.dto.TcTestCaseDto;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.model.TcTestcase;
import com.zto.testcase.request.TcPlanCaseStatisticReq;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.TestCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/testcase")
@Api(value = "TestCase Api 接口文档")
@RestController
public class TestCaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseController.class);

    @Resource
    public TestCaseService testCaseService;

    @RequestMapping(value = "/addTestCase")
    @ResponseBody
    @ApiOperation(value = "addTestCase Api", notes = "添加用例")
    public Result addTestCase(@RequestBody TcTestcase tcTestcase) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>addTestCase 请求开始，request : {}", JSON.toJSONString(tcTestcase));
            result = Result.success(testCaseService.addTestCase(tcTestcase));
            logger.info(">>>>>addTestCase 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>addTestCase 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/deleteTestCase")
    @ResponseBody
    @ApiOperation(value = "deleteTestCase Api", notes = "删除用例")
    public Result deleteTestCase(@RequestBody TcTestcase tcTestcase) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>deleteTestCase 请求开始，request : {}", JSON.toJSONString(tcTestcase));
            int ret = testCaseService.deleteTestCase(tcTestcase);
            if (ret != -1) {
                result = Result.success();
            } else {
                result = Result.error(ErrorCodeEnum.OPERATION_FAILURE.getErrorCode(),
                        ErrorCodeEnum.OPERATION_FAILURE.getErrorMsg());
            }
            logger.info(">>>>>deleteTestCase 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>deleteTestCase 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/getTestCaseList")
    @ResponseBody
    @ApiOperation(value = "getTestCaseList  Api", notes = "用例列表")
    public Result<PageInfo<TcTestcase>> getTestCaseList(@RequestBody TcTestCaseDto tcTestCaseDto) {
        return testCaseService.getTestCaseList(tcTestCaseDto);
    }

    @RequestMapping(value = "/editTestCase")
    @ResponseBody
    @ApiOperation(value = "editTestCase Api", notes = "编辑用例")
    public Result editTestCase(@RequestBody TcTestcase tcTestcase) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>editTestCase 请求开始，request : {}", JSON.toJSONString(tcTestcase));
            int ret = testCaseService.editTestCase(tcTestcase);
            if (ret > 0) {
                result = Result.success();
            } else {
                result = Result.error(ErrorCodeEnum.OPERATION_FAILURE.getErrorCode(),
                        ErrorCodeEnum.OPERATION_FAILURE.getErrorMsg());
            }
            logger.info(">>>>>editTestCase request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>editTestCase request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/copyEditTestCase")
    @ResponseBody
    @ApiOperation(value = "copyEditTestCase Api", notes = "复制编辑用例")
    public Result copyEditTestCase(@RequestBody TcTestcase tcTestcase) {
        Result result;
        Integer moduleId = tcTestcase.getModuleId();
        if (null != moduleId) {
            long time = System.currentTimeMillis();
            try {
                logger.info(">>>>>copyEditTestCase 请求开始，request : {}", JSON.toJSONString(tcTestcase));

                result = Result.success(testCaseService.addTestCaseToNewModule(tcTestcase));
                logger.info(">>>>>copyEditTestCase 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
            } catch (Exception e) {
                logger.error(">>>>>copyEditTestCase 异常，{}", e);
                result = Result
                        .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(),
                                ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
            }
        } else {
            result = Result.error(ErrorCodeEnum.MODULE_IS_NULL.getErrorCode(),
                    ErrorCodeEnum.MODULE_IS_NULL.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/getFileListByCaseId")
    @ResponseBody
    @ApiOperation(value = "getFileListByCaseId Api", notes = "用例文件列表")
    public Result getFileListByCaseId(@RequestBody TcTestcase tcTestcase) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>getFileListByCaseId 请求开始，request : {}", JSON.toJSONString(tcTestcase));
            result = Result.success(testCaseService.getFileListByCaseId(tcTestcase));
            logger.info(">>>>>getFileListByCaseId request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>getFileListByCaseId request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/getTestcaseStepListByCaseId")
    @ResponseBody
    @ApiOperation(value = "getTestcaseStepListByCaseId Api", notes = "用例步骤列表")
    public Result getTestcaseStepListByCaseId(@RequestBody TcTestcase tcTestcase) {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>getTestcaseStepListByCaseId 请求开始，request : {}", JSON.toJSONString(tcTestcase));
            result = Result.success(testCaseService.getTestcaseStepListByCaseId(tcTestcase));
            logger.info(">>>>>getTestcaseStepListByCaseId 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>getTestcaseStepListByCaseId 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/copyTestCase")
    @ResponseBody
    @ApiOperation(value = "copyTestCase Api", notes = "复制用例")
    public Result copyTestCase(@RequestBody TcTestcase tcTestcase) {

        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>copyTestCase 请求开始，request : {}", JSON.toJSONString(tcTestcase));
            int ret = testCaseService.copyTestCase(tcTestcase);
            if (ret != -1) {
                result = Result.success();
            } else {
                result = Result.error(ErrorCodeEnum.OPERATION_FAILURE.getErrorCode(),
                        ErrorCodeEnum.OPERATION_FAILURE.getErrorMsg());
            }
            logger.info(">>>>>copyTestCase 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>copyTestCase 异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/exportTestCase", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void exportTestCase(TcTestCaseDto tcTestCaseDto, HttpServletResponse response) {
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>导出用例 请求开始。请求参数：{} ", tcTestCaseDto);
            testCaseService.exportTestCase(tcTestCaseDto, response);
            logger.info(">>>>>>导出用例 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            logger.error(">>>>>导出用例 异常。", e);
            try {
                response.sendError(201, e.getErrorMsg());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            logger.error(">>>>>导出用例 异常。", e);
            try {
                response.sendError(202,
                        ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @PostMapping(value = "/moveTestCase")
    @ResponseBody
    @ApiOperation(value = "moveTestCase Api", notes = "移动用例")
    public Result moveTestCase(@RequestBody TcTestcase tcTestcase) {

        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>moveTestCase 请求开始，request : {}", JSON.toJSONString(tcTestcase));
            result = testCaseService.moveTestCase(tcTestcase);
            logger.info(">>>>>moveTestCase 请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (Exception e) {
            logger.error(">>>>>moveTestCase 请求异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    /**
     * 统计一不同时间内创建用例数或者不同人创建用例数
     *
     * @param tcPlanCaseStatisticReq
     * @return
     */
    @PostMapping("/caseCount")
    public Result<List<Map<String,Long>>> planCount(@RequestBody TcPlanCaseStatisticReq tcPlanCaseStatisticReq) {
        return Result.success(testCaseService.caseCount(tcPlanCaseStatisticReq));
    }
}
