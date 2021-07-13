package com.zto.testcase.service.impl;

import com.alibaba.fastjson.JSON;
import com.zto.testcase.enums.AppIpEnum;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.enums.FieldChinaeseEnum;
import com.zto.testcase.enums.UserStatusEnum;
import com.zto.testcase.mapper.testcase.SysUserInfoMapper;
import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.response.SysUserInfoData;
import com.zto.testcase.response.UserResponse;
import com.zto.testcase.service.SysUserInfoService;
import com.zto.testcase.util.MapBuilder;
import com.zto.testcase.util.SHA1;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SysUserInfoServiceImpl implements SysUserInfoService {

    private static final String LOGIN = "login";

    private static final String REGISTER = "register";

    @Resource
    private SysUserInfoMapper sysUserInfoMapper;

    //    @Resource
//    private RedisService redisService;
    @Value("${login.loginErrorTimes}")
    private Integer loginErrorTimes;

    @Value("${login.loginErrorRemind}")
    private Integer loginErrorRemind;

    private boolean validateRegex(String regex, String value) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public UserResponse register(SysUserInfo sysUserInfo) {
        UserResponse userResponse = new UserResponse();
        log.info("用户注册开始，用户名为:[{}],登陆用户名为：[{}]", sysUserInfo.getUserName(), sysUserInfo.getUserName());
        try {
            SysUserInfo queryResultSysUserInfo = null;
            String userStatus = null;
            if (!Optional.ofNullable(sysUserInfo.getUserStatus()).isPresent()) {
                userStatus = AppIpEnum.getStartStatus(sysUserInfo.getAppId());
                if (!Optional.ofNullable(userStatus).isPresent()) {
                    userResponse.setRespCode(FieldChinaeseEnum.USER_STATUS.getErrorCode());
                    userResponse.setRespMessage(FieldChinaeseEnum.USER_STATUS.getChinaese());
                    return userResponse;
                }
            } else {
                userStatus = sysUserInfo.getUserStatus();
            }

            if (Optional.ofNullable(sysUserInfo.getMobile()).isPresent()) {
                if (!validateRegex("^1[3|4|5|7|8][0-9]\\d{8}$", sysUserInfo.getMobile())) {
                    userResponse.setRespCode(FieldChinaeseEnum.MOBILE.getErrorCode());
                    userResponse.setRespMessage(FieldChinaeseEnum.MOBILE.getChinaese());
                    return userResponse;
                }
            }
            if (Optional.ofNullable(sysUserInfo.getEmail()).isPresent()) {
                if (!validateRegex(
                        "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
                        sysUserInfo.getEmail())) {
                    userResponse.setRespCode(FieldChinaeseEnum.EMAIL.getErrorCode());
                    userResponse.setRespMessage(FieldChinaeseEnum.EMAIL.getChinaese());
                    return userResponse;
                }
            }
            if (Optional.ofNullable(sysUserInfo.getUserName()).isPresent()) {
                if (!validateRegex("^[A-Za-z][A-Za-z1-9_-]+$", sysUserInfo.getUserName())) {
                    userResponse.setRespCode(FieldChinaeseEnum.USER_NAME.getErrorCode());
                    userResponse.setRespMessage(FieldChinaeseEnum.USER_NAME.getChinaese());
                    return userResponse;
                }
            }
            queryResultSysUserInfo = validateSysUserInfoExits(sysUserInfo, userResponse, REGISTER);
            if (Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional.ofNullable(userResponse)
                    .isPresent()) {
                return userResponse;
            }
            sysUserInfo.setUserStatus(userStatus);
            initSysUserInfo(sysUserInfo);
            sysUserInfoMapper.insertSelective(sysUserInfo);

            // 更新用户ID和密码
            sysUserInfo.setUserId(sysUserInfo.getAppId() + "1" + String.format("%09d", sysUserInfo.getId()));
            sysUserInfo.setUserPwd(SHA1.encode(sysUserInfo.getUserId() + sysUserInfo.getUserPwd()));
            sysUserInfoMapper.updateByPrimaryKey(sysUserInfo);

            sysUserInfo.setLoginErrorTimes(0);
//            redisService.setUserInfoToRedis(sysUserInfo);
            sysUserInfo.setUserPwd("");
            userResponse = new UserResponse(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode(),
                    ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
            SysUserInfoData sysUserInfoData = new SysUserInfoData();
            BeanUtils.copyProperties(sysUserInfo, sysUserInfoData);
            userResponse.setData(JSON.toJSONString(sysUserInfoData));
        } catch (Exception e) {
            log.error("用户注册异常[{}],[{}]", e.getMessage(), e);
            userResponse = new UserResponse(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(),
                    ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }

        return userResponse;
    }

    private void initSysUserInfo(SysUserInfo sysUserInfo) {
        if (!Optional.ofNullable(sysUserInfo.getDateCreated()).isPresent()) {
            sysUserInfo.setDateCreated(LocalDateTime.now());
        }
        if (!Optional.ofNullable(sysUserInfo.getStartDate()).isPresent()) {
            sysUserInfo.setDateUpdated(LocalDateTime.now());
        }
        if (!Optional.ofNullable(sysUserInfo.getStartDate()).isPresent()) {
            sysUserInfo.setStartDate(LocalDateTime.now());
        }
        if (!Optional.ofNullable(sysUserInfo.getEndDate()).isPresent()) {
            LocalDateTime date = LocalDateTime.now().plus(150L, ChronoUnit.YEARS);
            log.info(date.toString());
            sysUserInfo.setEndDate(date);
        }
    }

    @Override
    public UserResponse login(SysUserInfo sysUserInfo) {
        UserResponse userResponse;
        try {

            SysUserInfo queryResultSysUserInfo;

            queryResultSysUserInfo = validateSysUserInfoExits(sysUserInfo, null, LOGIN);
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                userResponse = new UserResponse(ErrorCodeEnum.ERROR_LOGIN_NAME_NOT_EXITS.getErrorCode(),
                        ErrorCodeEnum.ERROR_LOGIN_NAME_NOT_EXITS.getErrorMsg());
                return userResponse;
            } else {
                if (!Predicate.isEqual(queryResultSysUserInfo.getUserPwd())
                        .test(SHA1.encode(queryResultSysUserInfo.getUserId() + sysUserInfo.getUserPwd()))) {
                    userResponse = new UserResponse(ErrorCodeEnum.ERROR_NAMEORPWD.getErrorCode(),
                            ErrorCodeEnum.ERROR_NAMEORPWD.getErrorMsg());
                    Integer loginTimes = queryResultSysUserInfo.getLoginErrorTimes() == null ? 0
                            : (queryResultSysUserInfo.getLoginErrorTimes() + 1);
                    queryResultSysUserInfo.setLoginErrorTimes(loginTimes);
                    if (queryResultSysUserInfo.getLoginErrorTimes() >= loginErrorRemind
                            && queryResultSysUserInfo.getLoginErrorTimes() <= loginErrorTimes) {
                        Integer surplusLoginErrorTimes = loginErrorTimes - queryResultSysUserInfo.getLoginErrorTimes();
                        String responseMessage = "";
                        responseMessage = MessageFormat
                                .format(ErrorCodeEnum.ERROR_LEFTCHANGE.getErrorCode(), loginErrorTimes,
                                        surplusLoginErrorTimes);
                        userResponse = new UserResponse(ErrorCodeEnum.ERROR_LEFTCHANGE.getErrorMsg(), responseMessage);
                    }
                    if (queryResultSysUserInfo.getLoginErrorTimes() >= loginErrorTimes) {
                        queryResultSysUserInfo.setUserStatus(UserStatusEnum.LOCKING.getKey());
                        queryResultSysUserInfo.setDateUpdated(LocalDateTime.now());
                        userResponse = new UserResponse(ErrorCodeEnum.ERROR_LOCKEDWHENLOGIN.getErrorCode(),
                                ErrorCodeEnum.ERROR_LOCKEDWHENLOGIN.getErrorMsg());
                    }
                    sysUserInfoMapper.updateByPrimaryKeySelective(queryResultSysUserInfo);
//                    redisService.setUserInfoToRedis(queryResultSysUserInfo);
                    return userResponse;
                }
                if (!Predicate.isEqual(UserStatusEnum.NOMARL.getKey()).test(queryResultSysUserInfo.getUserStatus())) {
                    userResponse = new UserResponse(ErrorCodeEnum.ERROR_LOCKEDWHENLOGIN.getErrorCode(),
                            ErrorCodeEnum.ERROR_LOCKEDWHENLOGIN.getErrorMsg());
                    return userResponse;
                }
                userResponse = new UserResponse(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode(),
                        ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
                queryResultSysUserInfo.setLoginErrorTimes(0);
                sysUserInfoMapper.updateByPrimaryKeySelective(queryResultSysUserInfo);
//                redisService.setUserInfoToRedis(queryResultSysUserInfo);
                queryResultSysUserInfo.setUserPwd("");
                SysUserInfoData sysUserInfoData = new SysUserInfoData();
                BeanUtils.copyProperties(queryResultSysUserInfo, sysUserInfoData);
                userResponse.setData(JSON.toJSONString(sysUserInfoData));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户登录异常：[{}],[{}]", e, e.getMessage());
            userResponse = new UserResponse(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode(),
                    ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return userResponse;
    }

    private SysUserInfo validateSysUserInfoExits(SysUserInfo sysUserInfo, UserResponse userResponse, String flag) {

        Map<String, String> params = new HashMap<>();
        SysUserInfo queryResultSysUserInfo = null;
        params.put("appId", sysUserInfo.getAppId());
        Predicate<String> predicate = Predicate.isEqual(LOGIN);
        if (Optional.ofNullable(sysUserInfo.getUserName()).isPresent() && predicate.test(flag)) {
//            String userInfo = redisService.getDataFromRedis(sysUserInfo);
//            if (Optional.ofNullable(userInfo).isPresent()) {
//                SysUserInfoJsonData sysUserInfoData = JSONObject.parseObject(userInfo, SysUserInfoJsonData.class);
////                SysUserInfoJsonData sysUserInfoData = JsonUtil.toObject(userInfo, SysUserInfoJsonData.class);
//                queryResultSysUserInfo = new SysUserInfo();
//                BeanUtils.copyProperties(sysUserInfoData, queryResultSysUserInfo);
//
//            }
        }

        if (Predicate.isEqual(flag).test(LOGIN)) {
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional
                    .ofNullable(sysUserInfo.getUserName()).isPresent()) {
                params.put("loginName", sysUserInfo.getUserName());
                queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
            }
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional
                    .ofNullable(sysUserInfo.getUserName()).isPresent()) {
                params.put("mobile", sysUserInfo.getUserName());
                params.remove("loginName");
                queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
            }
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional
                    .ofNullable(sysUserInfo.getUserName()).isPresent()) {
                params.put("email", sysUserInfo.getUserName());
                params.remove("mobile");
                queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
            }
        } else {
            if (Optional.ofNullable(sysUserInfo.getUserName()).isPresent()) {
                params.put("loginName", sysUserInfo.getUserName());
                queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
                if (Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                    userResponse.setRespCode(ErrorCodeEnum.ERROR_EXITS_LOGIN_NAME.getErrorCode());
                    userResponse.setRespMessage(ErrorCodeEnum.ERROR_EXITS_LOGIN_NAME.getErrorMsg());
                }
            }
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional.ofNullable(sysUserInfo.getMobile())
                    .isPresent()) {
                params.put("mobile", sysUserInfo.getMobile());
                params.remove("loginName");
                queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
                if (Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                    userResponse.setRespCode(ErrorCodeEnum.ERROR_EXITS_MOBILE.getErrorCode());
                    userResponse.setRespMessage(ErrorCodeEnum.ERROR_EXITS_MOBILE.getErrorMsg());
                }
            }
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional.ofNullable(sysUserInfo.getEmail())
                    .isPresent()) {
                params.put("email", sysUserInfo.getEmail());
                params.remove("mobile");
                params.remove("loginName");
                queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
                if (Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                    userResponse.setRespCode(ErrorCodeEnum.ERROR_EXITS_EMAIL.getErrorCode());
                    userResponse.setRespMessage(ErrorCodeEnum.ERROR_EXITS_EMAIL.getErrorMsg());
                }
            }
        }
        return queryResultSysUserInfo;
    }

    @Override
    public UserResponse unLocking(String userId, String appId) {
        UserResponse userResponse = new UserResponse();
        try {
            SysUserInfo sysUserInfo = new SysUserInfo();
            sysUserInfo.setUserId(userId);
            sysUserInfo.setAppId(appId);
            sysUserInfo.setDateUpdated(LocalDateTime.now());
            sysUserInfo.setUserStatus(UserStatusEnum.NOMARL.getKey());
            sysUserInfo.setLoginErrorTimes(0);
            sysUserInfoMapper.updateUserStatusByUserId(sysUserInfo);
            Map<String, String> params = MapBuilder.make("userId", userId).put("appId", appId).build();
            SysUserInfo querySysUserInfo = sysUserInfoMapper.findByCondition(params);
//            redisService.setUserInfoToRedis(querySysUserInfo);
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
        } catch (Exception e) {
            log.error("解锁用户异常：[{}],[{}]", e, e.getMessage());
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode());
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
        }
        return userResponse;
    }

    @Override
    public UserResponse updateUserPwd(SysUserInfo sysUserInfo) {
        UserResponse userResponse = new UserResponse();
        try {
            SysUserInfo queryResultSysUserInfo = validateSysUserInfoExits(sysUserInfo, null, LOGIN);
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                userResponse.setRespCode(ErrorCodeEnum.ERROR_LOGIN_NAME_NOT_EXITS.getErrorCode());
                userResponse.setRespMessage(ErrorCodeEnum.ERROR_LOGIN_NAME_NOT_EXITS.getErrorMsg());
                return userResponse;
            }
            if (!Optional.ofNullable(sysUserInfo.getOldUserPwd()).isPresent()) {
                updateUserPassWord(sysUserInfo, queryResultSysUserInfo);

            } else {
                if (!Predicate.isEqual(UserStatusEnum.NOMARL.getKey()).test(queryResultSysUserInfo.getUserStatus())) {
                    userResponse = new UserResponse(ErrorCodeEnum.ERROR_LOCKEDWHENLOGIN.getErrorCode(),
                            ErrorCodeEnum.ERROR_LOCKEDWHENLOGIN.getErrorMsg());
                    return userResponse;
                }
                if (Predicate.isEqual(SHA1.encode(queryResultSysUserInfo.getUserId() + sysUserInfo.getOldUserPwd()))
                        .test(SHA1.encode(queryResultSysUserInfo.getUserId() + sysUserInfo.getUserPwd()))) {
                    userResponse = new UserResponse(ErrorCodeEnum.ERROR_OLD_PASSWORD_SAME_NEW_PWD.getErrorCode(),
                            ErrorCodeEnum.ERROR_OLD_PASSWORD_SAME_NEW_PWD.getErrorMsg());
                    return userResponse;
                }

                if (!Predicate.isEqual(SHA1.encode(queryResultSysUserInfo.getUserId() + sysUserInfo.getOldUserPwd()))
                        .test(queryResultSysUserInfo.getUserPwd())) {
                    userResponse = new UserResponse(ErrorCodeEnum.ERROR_OLD_PASSWORD.getErrorCode(),
                            ErrorCodeEnum.ERROR_OLD_PASSWORD.getErrorMsg());
                    return userResponse;
                } else {
                    updateUserPassWord(sysUserInfo, queryResultSysUserInfo);
                }
            }
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
        } catch (Exception e) {
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode());
            log.error("重置密码异常：[{}],[{}]", e, e.getMessage());
        }
        return userResponse;
    }

    @Override
    public UserResponse getUserInfoByUserName(SysUserInfo sysUserInfo) {
        UserResponse userResponse = new UserResponse();
        if (!Optional.ofNullable(sysUserInfo.getUserName()).isPresent() && !Optional.ofNullable(sysUserInfo.getUserId())
                .isPresent()) {
            userResponse.setRespCode(FieldChinaeseEnum.USER_NAME.getErrorCode());
            userResponse.setRespMessage(FieldChinaeseEnum.USER_NAME.getChinaese());
            return userResponse;
        }
        try {
            SysUserInfo queryResultSysUserInfo = null;
            if (Optional.ofNullable(sysUserInfo.getUserName()).isPresent()) {
                queryResultSysUserInfo = validateSysUserInfoExits(sysUserInfo, null, LOGIN);
            }
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                if (Optional.ofNullable(sysUserInfo.getUserId()).isPresent()) {
                    Map<String, String> sysUserInfoCondition = MapBuilder.make("userId", sysUserInfo.getUserId())
                            .build();
                    queryResultSysUserInfo = sysUserInfoMapper.findByCondition(sysUserInfoCondition);

                } else if (!Optional.ofNullable(sysUserInfo.getUserId()).isPresent()) {
                    if (!Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                        userResponse.setRespMessage(ErrorCodeEnum.ERROR_NOT_DATA.getErrorMsg());
                        userResponse.setRespCode(ErrorCodeEnum.ERROR_NOT_DATA.getErrorCode());
                        return userResponse;
                    }
                }
            }
            if (!Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                userResponse.setRespMessage(ErrorCodeEnum.ERROR_NOT_DATA.getErrorMsg());
                userResponse.setRespCode(ErrorCodeEnum.ERROR_NOT_DATA.getErrorCode());
                return userResponse;
            }
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
            queryResultSysUserInfo.setUserPwd("");
            SysUserInfoData sysUserInfoData = new SysUserInfoData();
            BeanUtils.copyProperties(queryResultSysUserInfo, sysUserInfoData);
            userResponse.setData(JSON.toJSONString(sysUserInfoData));
        } catch (Exception e) {
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode());
            log.error("查询 用户信息异常：[{}], [{}]", e, e.getMessage());
        }
        return userResponse;
    }

    @Override
    public UserResponse getUserDynamic(SysUserInfo sysUserInfo) {
        UserResponse userResponse = new UserResponse();
        if (!Optional.ofNullable(sysUserInfo.getUserName()).isPresent() && !Optional.ofNullable(sysUserInfo.getUserId())
                .isPresent()
                && !Optional.ofNullable(sysUserInfo.getUserStatus()).isPresent() && !Optional
                .ofNullable(sysUserInfo.getMobile()).isPresent()) {
            userResponse.setRespMessage(ErrorCodeEnum.PARAMER_ERROR.getErrorMsg());
            userResponse.setRespCode(ErrorCodeEnum.PARAMER_ERROR.getErrorCode());
            return userResponse;
        }
        try {
            Map<String, String> params = new HashMap<>();
            List<SysUserInfo> queryResultSysUserInfoList = new ArrayList<>();
            if (Optional.ofNullable(sysUserInfo.getMobile()).isPresent()) {
                if (!validateRegex("^1[3|4|5|7|8][0-9]\\d{8}$", sysUserInfo.getMobile())) {
                    userResponse.setRespCode(FieldChinaeseEnum.MOBILE.getErrorCode());
                    userResponse.setRespMessage(FieldChinaeseEnum.MOBILE.getChinaese());
                    return userResponse;
                }
                if (Optional.ofNullable(sysUserInfo.getUserStatus()).isPresent()) {
                    params.put("userStatus", sysUserInfo.getUserStatus());
                }
                params.put("appId", sysUserInfo.getAppId());
                params.put("mobile", sysUserInfo.getMobile());
                SysUserInfo queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
                if (Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                    queryResultSysUserInfo.setUserPwd("");
                    queryResultSysUserInfoList.add(queryResultSysUserInfo);
                }
            } else if (Optional.ofNullable(sysUserInfo.getUserStatus()).isPresent()) {
                params.clear();
                params.put("userStatus", sysUserInfo.getUserStatus());
                params.put("appId", sysUserInfo.getAppId());
                queryResultSysUserInfoList = sysUserInfoMapper.findListByCondition(params);
                for (SysUserInfo sysUserInfo1 : queryResultSysUserInfoList) {
                    sysUserInfo1.setUserPwd("");
                }
            }
            if (queryResultSysUserInfoList.size() == 0) {
                userResponse.setRespMessage(ErrorCodeEnum.ERROR_NOT_DATA.getErrorMsg());
                userResponse.setRespCode(ErrorCodeEnum.ERROR_NOT_DATA.getErrorCode());
                return userResponse;
            }
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
            userResponse.setData(JSON.toJSONString(queryResultSysUserInfoList));
        } catch (Exception e) {
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode());
            log.error("查询 用户信息异常：[{}], [{}]", e, e.getMessage());
        }
        return userResponse;
    }

    @Override
    public UserResponse update(SysUserInfo sysUserInfo) {
        UserResponse userResponse = new UserResponse();
        try {
            if (validateUpdateUserInfo(sysUserInfo, userResponse)) {
                return userResponse;
            }
            sysUserInfo.setDateUpdated(LocalDateTime.now());
            SysUserInfo oldSysUserInfo = sysUserInfoMapper
                    .findByCondition(MapBuilder.make("userId", sysUserInfo.getUserId()).build());
            sysUserInfoMapper.updateByUserId(sysUserInfo);
            SysUserInfo querySysUserInfo = sysUserInfoMapper
                    .findByCondition(MapBuilder.make("userId", sysUserInfo.getUserId()).build());
            if (Optional.ofNullable(querySysUserInfo).isPresent()) {
//                redisService.setUserInfoToRedis(querySysUserInfo);
            }
            if ((Optional.ofNullable(oldSysUserInfo.getMobile()).isPresent() || Optional
                    .ofNullable(oldSysUserInfo.getEmail()).isPresent()) &&
                    (Optional.ofNullable(sysUserInfo.getMobile()).isPresent() || Optional
                            .ofNullable(sysUserInfo.getEmail()).isPresent())) {
//                redisService.deleteByKey(oldSysUserInfo);
            }
            SysUserInfoData sysUserInfoData = new SysUserInfoData();
            BeanUtils.copyProperties(querySysUserInfo, sysUserInfoData);
            userResponse.setData(JSON.toJSONString(querySysUserInfo));

            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
        } catch (Exception e) {
            log.error("更新用户信息异常 ：[{}],[{}]", e, e.getMessage());
            userResponse.setRespMessage(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorMsg());
            userResponse.setRespCode(ErrorCodeEnum.SYSTEM_EXCEPTION.getErrorCode());
        }
        return userResponse;
    }

    private boolean validateUpdateUserInfo(SysUserInfo sysUserInfo, UserResponse userResponse) {
        SysUserInfo sysUserInfoExits = null;
        if (!Optional.ofNullable(sysUserInfo.getNickName()).isPresent() && !Optional.ofNullable(sysUserInfo.getMobile())
                .isPresent()
                && !Optional.ofNullable(sysUserInfo.getUserStatus()).isPresent() && !Optional
                .ofNullable(sysUserInfo.getMobile()).isPresent()) {
            userResponse.setRespMessage(ErrorCodeEnum.PARAMER_ERROR.getErrorMsg());
            userResponse.setRespCode(ErrorCodeEnum.PARAMER_ERROR.getErrorCode());
            return true;
        }
        String userStatus[] = {"2", "3", "4"};
        List userStatusList = Arrays.asList(userStatus);
        if (Optional.ofNullable(sysUserInfo.getUserStatus()).isPresent()) {
            if (!userStatusList.contains(sysUserInfo.getUserStatus())) {
                userResponse.setRespMessage(ErrorCodeEnum.PARAMER_ERROR.getErrorMsg());
                userResponse.setRespCode(ErrorCodeEnum.PARAMER_ERROR.getErrorCode());
                return true;
            }
        }
        if (Optional.ofNullable(sysUserInfo.getMobile()).isPresent()) {
            if (!validateRegex("^1[3|4|5|7|8][0-9]\\d{8}$", sysUserInfo.getMobile())) {
                userResponse.setRespCode(FieldChinaeseEnum.MOBILE.getErrorCode());
                userResponse.setRespMessage(FieldChinaeseEnum.MOBILE.getChinaese());
                return true;
            }
            List<SysUserInfo> sysUserInfoList = sysUserInfoMapper
                    .findListByCondition(MapBuilder.make("mobile", sysUserInfo.getMobile()).put("appId",
                            sysUserInfo.getAppId()).build());
            if (Optional.ofNullable(sysUserInfoList).isPresent() && sysUserInfoList.size() > 1) {
                userResponse.setRespCode(ErrorCodeEnum.MOBILE_EXITS.getErrorCode());
                userResponse.setRespMessage(ErrorCodeEnum.MOBILE_EXITS.getErrorMsg());
                return true;
            }
            if (sysUserInfoList.size() >= 1 && !Predicate.isEqual(sysUserInfo.getUserId())
                    .test(sysUserInfoList.get(0).getUserId())) {
                userResponse.setRespCode(ErrorCodeEnum.MOBILE_EXITS.getErrorCode());
                userResponse.setRespMessage(ErrorCodeEnum.MOBILE_EXITS.getErrorMsg());
                return true;
            }
        }
        if (Optional.ofNullable(sysUserInfo.getEmail()).isPresent()) {
            if (!validateRegex("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
                    sysUserInfo.getEmail())) {
                userResponse.setRespCode(FieldChinaeseEnum.EMAIL.getErrorCode());
                userResponse.setRespMessage(FieldChinaeseEnum.EMAIL.getChinaese());
                return true;
            }
            sysUserInfoExits = sysUserInfoMapper
                    .findByCondition(MapBuilder.make("email", sysUserInfo.getMobile()).put("appId",
                            sysUserInfo.getAppId()).build());
            if (Optional.ofNullable(sysUserInfoExits).isPresent()) {
                userResponse.setRespCode(ErrorCodeEnum.EMAIL_EXITS.getErrorCode());
                userResponse.setRespMessage(ErrorCodeEnum.EMAIL_EXITS.getErrorMsg());
                return true;
            }
        }
        if (Optional.ofNullable(sysUserInfo.getUserId()).isPresent()) {
            sysUserInfoExits = sysUserInfoMapper
                    .findByCondition(MapBuilder.make("userId", sysUserInfo.getUserId()).put("appId",
                            sysUserInfo.getAppId()).build());
            if (!Optional.ofNullable(sysUserInfoExits).isPresent()) {
                userResponse.setRespMessage(ErrorCodeEnum.ERROR_NOT_DATA.getErrorMsg());
                userResponse.setRespCode(ErrorCodeEnum.ERROR_NOT_DATA.getErrorCode());
                return true;
            }
        }
        return false;
    }

    private void updateUserPassWord(SysUserInfo sysUserInfo, SysUserInfo queryResultSysUserInfo) {
        String userPwd = sysUserInfo.getUserPwd();
        BeanUtils.copyProperties(queryResultSysUserInfo, sysUserInfo);
        sysUserInfo.setUserPwd(SHA1.encode(queryResultSysUserInfo.getUserId() + userPwd));
        sysUserInfo.setDateUpdated(LocalDateTime.now());
        if (Predicate.isEqual(UserStatusEnum.LOCKING.getKey()).test(queryResultSysUserInfo.getUserStatus())) {
            sysUserInfo.setUserStatus(UserStatusEnum.NOMARL.getKey());
        }
        sysUserInfo.setLoginErrorTimes(0);
        sysUserInfo.setId(queryResultSysUserInfo.getId());
        sysUserInfoMapper.updateByPrimaryKeySelective(sysUserInfo);
//        redisService.setUserInfoToRedis(sysUserInfo);
    }

    public static void main(String[] args) {
        System.out.println(SHA1.encode("boss007" + "admin1!"));

    }

}
