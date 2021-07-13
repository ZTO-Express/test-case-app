package com.zto.testcase.controller.basic;

import com.zto.testcase.dto.UserDto;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.response.Result;
import com.zto.testcase.response.UserListItem;
import com.zto.testcase.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("用户管理")
@RestController
@RequestMapping("/v1/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
//    @Resource
//    private RedisService redisService;

    @ApiOperation("新增用户")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Result<String> create(@RequestBody UserDto userDto) {
        return Result.success(userService.create(userDto));
    }

    @ApiOperation("删除用户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result<String> delete(@RequestBody UserDto userDto) {
        return Result.success(userService.delete(userDto.getUserId()));
    }

    @ApiOperation("更新用户")
    @RequestMapping(value = "/updateByUserId", method = RequestMethod.POST)
    public Result<String> updateByUserId(@RequestBody UserDto userDto) {
        return Result.success(userService.updateByUserId(userDto));
    }

    @ApiOperation("用户列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    public Result<List<UserListItem>> findAll(@RequestBody UserDto UserDto) {
        return Result.success(userService.all(UserDto.getAppId()));
    }

    @RequestMapping(value = "/findByRoleCode", method = RequestMethod.POST)
    public Result<List<UserListItem>> findByRoleCode(@RequestBody UserDto UserDto) {
        return Result.success(userService.findByRoleCode(UserDto));
    }

    @ApiOperation("刷新获取数据")
    @RequestMapping(value = "/loadAppData", method = RequestMethod.GET)
    public Result<Map<String, Object>> loadAppData(UserDto UserDto) {
//        SysUserInfo sysUserInfo = new SysUserInfo();
//        sysUserInfo.setUserName(UserDto.getUserName());
//        sysUserInfo.setUserPwd(UserDto.getUserPwd());
//        sysUserInfo.setAppId(UserDto.getAppId());
//        String redisJson = redisService.getDataFromRedis(sysUserInfo);
//        if (StringUtils.isBlank(redisJson)) {
//            return new ResultBean<>((ResultCode.ERROR_INVALID));
//        }
        return Result.success(userService.loadAppData(UserDto));
    }

    @ApiOperation("通过登录用户名获取用户信息")
    @RequestMapping(value = "/findByLoginName", method = RequestMethod.POST)
    public Result<UserListItem> findByLoginName(@RequestBody UserDto UserDto) {
        UserListItem item;
        try {
            item = userService.findByLoginName(UserDto);
        } catch (Exception e) {
            return Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        if (item == null) {
            return Result.error(ErrorCodeEnum.ERROR_LOGIN_NAME_NOT_EXIST);
        }
        return Result.success(item);
    }

    @ApiOperation("登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result<String> logout(@RequestBody UserDto UserDto) {
        return Result.success(userService.logout(UserDto));
    }

    @ApiOperation(value = "获取所有测试人员")
    @RequestMapping(value = "/getAllTester", method = RequestMethod.GET)
    public Result getAllTester() {
        Result result;
        long time = System.currentTimeMillis();
        try {
            log.info(">>>>>>getAllTester request 请求开始。");
            result = Result.success(userService.getAllTester());
            log.info(">>>>>listAllType request  请求结束，耗时：[{}]ms", System.currentTimeMillis() - time);
        } catch (BaseException e) {
            result = Result.error(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            log.error(">>>>>getAllTester request  异常，{}", e);
            result = Result
                    .error(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(), ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return result;
    }
}
