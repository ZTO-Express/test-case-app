package com.zto.testcase.controller.sysmanage;

import com.alibaba.fastjson.JSONObject;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.model.TcCaseType;
import com.zto.testcase.model.TcTestStage;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.TestConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: wxc
 * @Description:测试相关配置controller
 * @Date: Create in 下午2:56 2021/3/10
 */
@RequestMapping("/config")
@Api(value = "测试相关配置 接口文档")
@RestController
public class TestConfigController {
    private static final Logger logger = LoggerFactory.getLogger(TestConfigController.class);

    @Resource
    public TestConfigService testConfigService;

    /***
     * 获取所有用例类型
     * @return
     */
    @RequestMapping(value = "/type/listAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有用例类型")
    public Result listAllType(@RequestParam(required = false) Integer type) {
        Result result=new Result();
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>listAllType request : {}",type);
            //查询用例类型
            if(type==null||type==1){
                result = Result.success(testConfigService.listAllType());
            }else if(type==2){ //查询用例标签
                result = Result.success(testConfigService.listAllTag());
            }else {
                result=Result.error(ErrorCodeEnum.PARAM_IS_EMPTY.getErrorCode(),"type类型不存在，1：用例类型，2：用例标签");
            }

            logger.info(">>>>>listAllType request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>listAllType request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    /***
     * 获取所有执行阶段类型
     * @return
     */
    @RequestMapping(value = "/stage/listAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有执行阶段类型")
    public Result listAllStage() {
        Result result;
        long time = System.currentTimeMillis();
        try {
            logger.info(">>>>>>listAllStage request : {}");
            result = Result.success(testConfigService.listAllStage());
            logger.info(">>>>>listAllStage request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>listAllStage request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    /***
     * 新增或者更新用例标签
     * @param caseType
     * @return
     */
    @RequestMapping(value = "/type/addOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增或者更新用例标签", notes = "新增或者更新用例标签")
    public Result addTypeOrUpdate(@RequestBody @Valid TcCaseType caseType) {
        logger.info(">>>>>>addTypeOrUpdate request : {}", JSONObject.toJSONString(caseType));

        Result result;
        long time = System.currentTimeMillis();
        //id为0提示错误
        if (caseType.getId() != null && caseType.getId() == 0) {
            return Result.error(ErrorCodeEnum.PARAM_IS_EMPTY.getErrorCode(), "ID不能为0");
        }
        //判断名称是否存在
        if (caseType.getName() == null || StringUtils.isEmpty(caseType.getName())) {
            return Result.error(ErrorCodeEnum.PARAM_IS_EMPTY.getErrorCode(), ErrorCodeEnum.PARAM_IS_EMPTY.getErrorMsg());
        }
        //新增,判断重名
        if (testConfigService.findTypeByName(caseType.getName()) != null) {
            return Result.error(ErrorCodeEnum.DATA_IS_EXIST.getErrorCode(), ErrorCodeEnum.DATA_IS_EXIST.getErrorMsg());
        }
        try {
            result = Result.success(testConfigService.addTypeOrUpdate(caseType));
            logger.info(">>>>>addTypeOrUpdate request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>addTypeOrUpdate request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    /***
     * 新增或者更新执行阶段
     * @param testStage
     * @return
     */
    @RequestMapping(value = "/stage/addOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增或者更新执行阶段", notes = "新增或者更新执行阶段")
    public Result addStageOrUpdate(@RequestBody @Valid TcTestStage testStage) {
        logger.info(">>>>>>addStageOrUpdate request : {}", JSONObject.toJSONString(testStage));

        Result result;
        long time = System.currentTimeMillis();
        //id不为0
        if (testStage.getId() != null && testStage.getId() == 0) {
            return Result.error(ErrorCodeEnum.PARAM_IS_EMPTY.getErrorCode(), "ID不能为0");
        }
        //判断名称是否存在
        if (testStage.getName() == null || StringUtils.isEmpty(testStage.getName())) {
            return Result.error(ErrorCodeEnum.PARAM_IS_EMPTY.getErrorCode(), ErrorCodeEnum.PARAM_IS_EMPTY.getErrorMsg());
        }
        //判断重名
        if (testConfigService.findStageByName(testStage.getName()) != null) {
            return Result.error(ErrorCodeEnum.DATA_IS_EXIST.getErrorCode(), ErrorCodeEnum.DATA_IS_EXIST.getErrorMsg());
        }
        try {
            result = Result.success(testConfigService.addStageOrUpdate(testStage));
            logger.info(">>>>>addStageOrUpdate request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>addStageOrUpdate request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }

    @RequestMapping(value = "/type/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "根据ID删除用例类型")
    public Result delType(@RequestParam("id") Integer id) {
        logger.info(">>>>>>delType request : {}", id);
        Result result;
        long time = System.currentTimeMillis();
        try {
            TcCaseType caseType = new TcCaseType();
            //状态设置成0-删除
            caseType.setId(id);
            caseType.setStatus((byte) 0);
            result = Result.success(testConfigService.changeTypeStatus(caseType));
            logger.info(">>>>>delType request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>delType request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;


    }

    @RequestMapping(value = "/stage/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "根据ID删除阶段")
    public Result delStage(@RequestParam("id") Integer id) {
        logger.info(">>>>>>delStage request : {}", id);
        Result result;
        long time = System.currentTimeMillis();
        try {
            TcTestStage testStage = new TcTestStage();
            //状态设置成0-删除
            testStage.setId(id);
            testStage.setStatus((byte) 0);
            result = Result.success(testConfigService.changeStageStatus(testStage));
            logger.info(">>>>>delType request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(">>>>>delType request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;


    }
}
