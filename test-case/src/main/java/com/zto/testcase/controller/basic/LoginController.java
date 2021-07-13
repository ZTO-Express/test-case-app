package com.zto.testcase.controller.basic;

import com.zto.testcase.request.LoginRequest;
import com.zto.testcase.response.Result;
import com.zto.testcase.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "统一登录")
@RestController
@RequestMapping("/v1/sso")
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * Swagger注解
     * swagger通过注解表明该接口会生成文档，包括接口名、请求方法、参数、返回信息的等等。
     *
     * @Api：修饰整个类，描述Controller的作用
     * @ApiOperation：描述一个类的一个方法，或者说一个接口
     * @ApiParam：单个参数描述
     * @ApiModel：用对象来接收参数
     * @ApiProperty：用对象接收参数时，描述对象的一个字段
     * @ApiResponse：HTTP响应其中1个描述
     * @ApiResponses：HTTP响应整体描述
     * @ApiIgnore：使用该注解忽略这个API
     * @ApiError ：发生错误返回的信息
     * @ApiImplicitParam：一个请求参数
     * @ApiImplicitParams：多个请求参数
     */

//    @ApiOperation(value = "操作员登陆")
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ResultBean<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
//        return Result.success(ResultCode.SUCCESS, loginService.login(loginRequest));
//    }
    @ApiOperation(value = "操作员登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @ApiOperation(value = "登陆授权")
    @RequestMapping(value = "/loginAuthorization", method = RequestMethod.POST)
    public Result<Map<String, Object>> loginAuthorization(@RequestBody LoginRequest loginRequest) {
        return Result.success(loginService.loginAuthorization(loginRequest));
    }

    @ApiOperation(value = "忘记密码授权")
    @RequestMapping(value = "/forgetPswAuthorization", method = RequestMethod.POST)
    public Result<Map<String, Object>> forgetPswAuthorization() {
        return Result.success(loginService.forgetPswAuthorization());
    }
}
