package com.zto.testcase.controller.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.enums.FieldChinaeseEnum;
import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.request.LoginUserRequest;
import com.zto.testcase.request.QueryUserRequest;
import com.zto.testcase.request.RegisterUserRequest;
import com.zto.testcase.request.UpdateUserInfoRequest;
import com.zto.testcase.request.UpdateUserRequest;
import com.zto.testcase.response.BaseResponse;
import com.zto.testcase.response.UserResponse;
import com.zto.testcase.service.SysUserInfoService;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/info")
@Slf4j
public class UserInfoController {

    @Resource
    private SysUserInfoService sysUserInfoService;

    @Resource
    private Validator validator;

    protected boolean validate(Object target, BaseResponse response) {
        boolean validate = true;
        BindingResult bindingResult = new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
        validator.validate(target, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Optional<FieldError> result =
                    fieldErrors.stream().filter(obj -> FieldChinaeseEnum.getChinaese(obj.getField()) != null)
                            .findFirst();
            response.setRespCode(FieldChinaeseEnum.getChinaese(result.get().getField()).getErrorCode());
            response.setRespMessage(FieldChinaeseEnum.getChinaese(result.get().getField()).getChinaese());
            validate = false;
        }
        return validate;
    }

    @RequestMapping("/register")
    public UserResponse register(RegisterUserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            SysUserInfo sysUserInfo = JSONObject.parseObject(JSON.toJSONString(userRequest), SysUserInfo.class);
//            SysUserInfo sysUserInfo = JsonUtil.toObject(JsonUtil.toJson(userRequest), SysUserInfo.class);
            Optional<String> optionalUserName = Optional.ofNullable(sysUserInfo.getUserName());
            Optional<String> optionalMobile = Optional.ofNullable(sysUserInfo.getMobile());
            Optional<String> optionalEmail = Optional.ofNullable(sysUserInfo.getEmail());

            if (!optionalUserName.isPresent() && !optionalMobile.isPresent() && !optionalEmail.isPresent()) {
                userResponse = new UserResponse(ErrorCodeEnum.ERROR_REQUIRED.getErrorCode(), ErrorCodeEnum.ERROR_REQUIRED.getErrorMsg());
                return userResponse;
            }
            if (!validate(userRequest, userResponse)) {
                return userResponse;
            }
            log.info("传入的用户名是：[{}],登陆用户名：[{}],手机号：[{}],邮箱：[{}]", sysUserInfo.getUserName(), sysUserInfo.getUserName(),
                    sysUserInfo.getMobile(), sysUserInfo.getEmail());
            userResponse = sysUserInfoService.register(sysUserInfo);

        } catch (Exception e) {
            log.error("用户注册异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }

    @RequestMapping("/login")
    public UserResponse login(LoginUserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            if (!validate(userRequest, userResponse)) {
                return userResponse;
            }
            SysUserInfo sysUserInfo = JSONObject.parseObject(JSON.toJSONString(userRequest), SysUserInfo.class);
//            SysUserInfo sysUserInfo = JsonUtil.toObject(JsonUtil.toJson(userRequest), SysUserInfo.class);
            log.info("传入登陆用户名是：[{}]，对应系统是：[{}]", sysUserInfo.getUserName(),
                    sysUserInfo.getAppId());
            userResponse = sysUserInfoService.login(sysUserInfo);
        } catch (Exception e) {
            log.error("用户注册异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }

    @RequestMapping("/unLocking/{userId}/{appId}")
    public UserResponse unLocking(@PathVariable String userId, @PathVariable String appId) {
        UserResponse userResponse = null;
        try {
            log.info("传入的登陆用户id是：[{}],系统是:[{}]", userId, appId);
            userResponse = sysUserInfoService.unLocking(userId, appId);
        } catch (Exception e) {
            log.error("用户注册异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }

    @RequestMapping("/query")
    public UserResponse getUserInfoByUserId(QueryUserRequest queryUserRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            if (!validate(queryUserRequest, userResponse)) {
                return userResponse;
            }
            log.info("传入的查询用户名是：[{}]，用户id是:[{}]", queryUserRequest.getUserName(), queryUserRequest.getUserId());
            SysUserInfo sysUserInfo = JSONObject.parseObject(JSON.toJSONString(queryUserRequest), SysUserInfo.class);
//            SysUserInfo sysUserInfo = JsonUtil.toObject(JsonUtil.toJson(queryUserRequest), SysUserInfo.class);
            userResponse = sysUserInfoService.getUserInfoByUserName(sysUserInfo);
        } catch (Exception e) {
            log.error("用户注册异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }

    @RequestMapping("/queryList")
    public UserResponse getUserDynamic(QueryUserRequest queryUserRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            if (!validate(queryUserRequest, userResponse)) {
                return userResponse;
            }
            log.info("传入的查询手机号是：[{}]，appId是:[{}]，用户状态是:[{}]", queryUserRequest.getMobile(), queryUserRequest.getAppId(),
                    queryUserRequest.getUserStatus());
            SysUserInfo sysUserInfo = JSONObject.parseObject(JSON.toJSONString(queryUserRequest), SysUserInfo.class);
//            SysUserInfo sysUserInfo = JsonUtil.toObject(JsonUtil.toJson(queryUserRequest), SysUserInfo.class);
            userResponse = sysUserInfoService.getUserDynamic(sysUserInfo);
        } catch (Exception e) {
            log.error("查询用户信息异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }


    @RequestMapping("/updateUserPwd")
    public UserResponse updateUserPwd(UpdateUserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            if (!validate(userRequest, userResponse)) {
                return userResponse;
            }
            SysUserInfo sysUserInfo = JSONObject.parseObject(JSON.toJSONString(userRequest), SysUserInfo.class);
//            SysUserInfo sysUserInfo = JsonUtil.toObject(JsonUtil.toJson(userRequest), SysUserInfo.class);
            log.info("传入的用户登陆是：[{}],对应系统是：[{}]", sysUserInfo.getUserName(),
                    sysUserInfo.getAppId());
            userResponse = sysUserInfoService.updateUserPwd(sysUserInfo);
        } catch (Exception e) {
            log.error("用户注册异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }


    @RequestMapping("/update")
    public UserResponse update(UpdateUserInfoRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        try {
            if (!validate(userRequest, userResponse)) {
                return userResponse;
            }
            SysUserInfo sysUserInfo = JSONObject.parseObject(JSON.toJSONString(userRequest), SysUserInfo.class);
//            SysUserInfo sysUserInfo = JsonUtil.toObject(JsonUtil.toJson(userRequest), SysUserInfo.class);
            log.info("传入的修改用户的ID是：[{}],对应系统是：[{}]", sysUserInfo.getUserId(),
                    sysUserInfo.getAppId());
            userResponse = sysUserInfoService.update(sysUserInfo);
        } catch (Exception e) {
            log.error("用户注册异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }


}
