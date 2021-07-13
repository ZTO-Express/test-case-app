package com.zto.testcase.service.impl;

import com.zto.testcase.dto.UserDto;
import com.zto.testcase.enums.CommonStatusEnum;
import com.zto.testcase.enums.ErrorCodeEnum;
import com.zto.testcase.enums.FieldChinaeseEnum;
import com.zto.testcase.enums.UserStatusEnum;
import com.zto.testcase.exception.BaseException;
import com.zto.testcase.exception.CheckException;
import com.zto.testcase.mapper.testcase.SysUserInfoMapper;
import com.zto.testcase.mapper.testcase.UserRoleRelationMapper;
import com.zto.testcase.model.MenuInfo;
import com.zto.testcase.model.RoleInfo;
import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.model.UserRoleRelation;
import com.zto.testcase.request.MenuRequest;
import com.zto.testcase.request.MenuRoleRelationRequest;
import com.zto.testcase.request.PmsRequest;
import com.zto.testcase.request.RoleRequest;
import com.zto.testcase.response.SysUserInfoJsonData;
import com.zto.testcase.response.UserListItem;
import com.zto.testcase.service.MenuRoleRelationService;
import com.zto.testcase.service.MenuService;
import com.zto.testcase.service.PmsService;
import com.zto.testcase.service.RoleService;
import com.zto.testcase.service.UserService;
import com.zto.testcase.util.BeanUtil;
import com.zto.testcase.util.SHA1;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserRoleRelationMapper userRoleRelationMapper;

    @Resource
    private SysUserInfoMapper sysUserInfoMapper;

    @Resource
    private RoleService roleService;

    //    @Resource
//    private RedisService redisService;
    @Resource
    private MenuRoleRelationService menuRoleRelationService;

    @Resource
    private PmsService pmsService;

    @Resource
    private MenuService menuService;

    @Override
    public List<SysUserInfo> getAllTester() {
        return sysUserInfoMapper.getAllTester();
    }

    @Transactional
    @Override
    public String create(UserDto request) {
        if (StringUtils.isNotBlank(request.getUserPwd())) {
            request.setUserPwd(new String(Base64.getDecoder().decode(request.getUserPwd())));
        }

        // 验证角色编号
        String roleCode = request.getRoleCode();
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setAppId(request.getAppId());
        roleRequest.setRoleCode(roleCode);
        RoleInfo byRoleCode = roleService.findByRoleCode(roleRequest);
        if (null == byRoleCode) {
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_NOT_EXITS);
        }
        if (CommonStatusEnum.INACTIVE.getKey().equals(byRoleCode.getStatus())) {
            throw new BaseException(ErrorCodeEnum.ERROR_ROLE_INACTIVE);
        }

        SysUserInfo sysUserInfo = new SysUserInfo();
        BeanUtil.copyAttributeValue(request, sysUserInfo);
        initSysUserInfo(sysUserInfo);

        // 校验重复
        validateSysUserInfoExits(sysUserInfo);
        sysUserInfoMapper.insertSelective(sysUserInfo);

        // 更新
        sysUserInfo.setUserId(request.getAppId() + "1" + String.format("%09d", sysUserInfo.getId()));
        sysUserInfo.setUserPwd(SHA1.encode(sysUserInfo.getUserId() + request.getUserPwd()));
        sysUserInfoMapper.updateByPrimaryKey(sysUserInfo);

        // 用户绑定角色
        UserRoleRelation userRoleRelation = new UserRoleRelation();
        userRoleRelation.setAppId(request.getAppId());
        userRoleRelation.setRoleId(byRoleCode.getRoleCode());
        userRoleRelation.setUserId(sysUserInfo.getUserId());
        userRoleRelationMapper.insertSelective(userRoleRelation);

        return sysUserInfo.getUserId();
    }

    @Transactional
    @Override
    public String delete(String userId) {
        SysUserInfo userInfoByUserId = getUserInfoByUserId(userId);

        if (userInfoByUserId == null) {
            throw new BaseException(ErrorCodeEnum.ERROR_USER_ID_NOT_EXITS);
        }

        // 删除用户表记录
        sysUserInfoMapper.deleteByPrimaryKey(userInfoByUserId.getId());

        // 删除用户角色关联表记录
        userRoleRelationMapper.deleteByUserId(userId);

        // 清空redis 缓存
//        redisService.deleteByKey(userInfoByUserId);

        return userInfoByUserId.getUserId();
    }

    @Override
    public SysUserInfo getUserInfoByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new CheckException(FieldChinaeseEnum.USER_ID);
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        SysUserInfo byCondition = sysUserInfoMapper.findByCondition(paramMap);
        if (null == byCondition) {
            throw new BaseException(ErrorCodeEnum.ERROR_USER_ID_NOT_EXITS);
        }
        return byCondition;
    }

    @Transactional
    @Override
    public String updateByUserId(UserDto userRequest) {
        if (StringUtils.isNotBlank(userRequest.getUserPwd())) {
            userRequest.setUserPwd(new String(Base64.getDecoder().decode(userRequest.getUserPwd())));
        }

        // 校验userId
        SysUserInfo oldUserBean = getUserInfoByUserId(userRequest.getUserId());
        if (null == oldUserBean) {
            throw new BaseException(ErrorCodeEnum.ERROR_USER_ID_NOT_EXITS);
        }

        SysUserInfo preUpdateBean = new SysUserInfo();
        BeanUtil.copyAttributeValue(userRequest, preUpdateBean);

        // 更新前处理
        doBeforeUpdate(oldUserBean, preUpdateBean);

        // 无更新字段直接返回
        if (isNothingUpdate(preUpdateBean)) {
            if (StringUtils.isBlank(userRequest.getRoleCode())) {
                return "无任何更新";
            }
        } else {
            preUpdateBean.setDateUpdated(LocalDateTime.now());
            // 验重
            validateSysUserInfoExits(preUpdateBean);

            int i = sysUserInfoMapper.updateByUserId(preUpdateBean);
            if (i == 0) {
                throw new BaseException(ErrorCodeEnum.ERROR_USER_ID_NOT_EXITS);
            } else {
                // 刷新缓存
                SysUserInfo newUserBean = getUserInfoByUserId(userRequest.getUserId());
//                redisService.setUserInfoToRedis(newUserBean);
            }
        }

        if (StringUtils.isNotBlank(userRequest.getRoleCode())) {
            // 更新角色
            RoleRequest roleRequest = new RoleRequest();
            roleRequest.setRoleCode(userRequest.getRoleCode());
            roleRequest.setAppId(userRequest.getAppId());
            RoleInfo byRoleCode = roleService.findByRoleCode(roleRequest);
            if (null == byRoleCode) {
                throw new BaseException(ErrorCodeEnum.ERROR_ROLE_NOT_EXITS);
            }
            if (CommonStatusEnum.INACTIVE.getKey().equals(byRoleCode.getStatus())) {
                throw new BaseException(ErrorCodeEnum.ERROR_ROLE_INACTIVE);
            }
            userRoleRelationMapper.updateByUserId(preUpdateBean.getUserId(), userRequest.getRoleCode());
        }
        return preUpdateBean.getUserId();
    }

    @Override
    public List<UserListItem> all(String appId) {
        return sysUserInfoMapper.findByAppId(appId);
    }

    @Override
    public Map<String, Object> loadAppData(UserDto userRequest) {

        // 校验用户状态
        SysUserInfo userInfoByUserId = getUserInfoByUserId(userRequest.getUserId());
        if (null == userInfoByUserId) {
            log.warn("用户[{}]被删除.", userRequest.getUserId());
            throw new BaseException(ErrorCodeEnum.ERROR_USER_ID_NOT_EXITS);
        }

        if (userInfoByUserId.getUserStatus().equals(UserStatusEnum.LOCKING.getKey())) {
            log.warn("用户[{}]被禁用.", userInfoByUserId.getUserName());
            throw new BaseException(ErrorCodeEnum.ERROR_LOCKEDWHENLOGIN);
        }

        // 获取用户信息
        SysUserInfoJsonData userData = new SysUserInfoJsonData();
        BeanUtil.copyAttributeValue(userInfoByUserId, userData);

        // 获取角色
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setAppId(userRequest.getAppId());
        roleRequest.setUserId(userRequest.getUserId());
        List<RoleInfo> rolesByUserId = roleService.findByUserId(roleRequest);

        if (null == rolesByUserId || rolesByUserId.size() == 0) {
            log.warn("用户[{}]无可用角色.", userInfoByUserId.getUserName());
            throw new BaseException(ErrorCodeEnum.ERROR_USER_HAS_NO_ROLE); // 没有分配角色
        }

        boolean isSystemRole = false;
        for (RoleInfo roleInfo : rolesByUserId) {
            if ("1".equals(roleInfo.getSystemRole())) {
                isSystemRole = true;
            }
        }

        // 获取菜单树和空间信息
        List<MenuInfo> menuTrees = new ArrayList<>();
        Map<String, Object> pmsTree = new HashMap<>();
        List<MenuInfo> tempMenus;
        if (isSystemRole) {
            menuTrees = menuService.tree(MenuRequest.builder().appId(userRequest.getAppId()).build());
            pmsTree = pmsService.topTree(PmsRequest.builder().appId(userRequest.getAppId()).build());
        } else {
            for (RoleInfo ri : rolesByUserId) {
                tempMenus = menuRoleRelationService.treeByRoleCode(MenuRoleRelationRequest.builder()
                        .appId(userRequest.getAppId())
                        .roleCode(ri.getRoleCode())
                        .build());
                menuTrees.addAll(tempMenus);
                Map tempPms = pmsService.treeByRoleCode(PmsRequest.builder()
                        .appId(userRequest.getAppId())
                        .roleCode(ri.getRoleCode())
                        .build());
                pmsTree.putAll(tempPms);
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("menu", menuTrees); // 设置菜单信息
        resultMap.put("pms", pmsTree); // 设置权限信息
        userData.setRoles(rolesByUserId);
        userData.setUserPwd("****");
        resultMap.put("userInfo", userData); // 用户信息
        return resultMap;
    }

    @Override
    public List<UserListItem> findByRoleCode(UserDto userRequest) {
        return sysUserInfoMapper.findByRoleCode(userRequest.getAppId(), userRequest.getRoleCode());
    }

    @Override
    public UserListItem findByLoginName(UserDto userRequest) {
        Map<String, String> param = new HashMap<>();
        param.put("loginName", userRequest.getLoginName());
        param.put("appId", userRequest.getAppId());
        SysUserInfo byCondition = sysUserInfoMapper.findByCondition(param);
        UserListItem item = new UserListItem();
        if (byCondition == null) {
            log.info(">>>>用户信息查询结果为空!");
            item = null;
        } else {
            BeanUtil.copyAttributeValue(byCondition, item);
        }
        return item;
    }

    @Override
    public String logout(UserDto userRequest) {
        SysUserInfo userInfoByUserId = getUserInfoByUserId(userRequest.getUserId());
//        redisService.deleteByKey(userInfoByUserId);
        return "";
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
            sysUserInfo.setEndDate(date);
        }
    }

    private SysUserInfo validateSysUserInfoExits(SysUserInfo sysUserInfo) {

        Map<String, String> params = new HashMap<>();
        SysUserInfo queryResultSysUserInfo = null;
        params.put("appId", sysUserInfo.getAppId());

        if (Optional.ofNullable(sysUserInfo.getUserName()).isPresent()) {
            params.put("loginName", sysUserInfo.getUserName());
            queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
            if (Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                throw new BaseException(ErrorCodeEnum.ERROR_EXITS_LOGIN_NAME);
            }
        }
        if (!Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional.ofNullable(sysUserInfo.getMobile())
                .isPresent()) {
            params.put("mobile", sysUserInfo.getMobile());
            params.remove("loginName");
            queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
            if (Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                throw new BaseException(ErrorCodeEnum.ERROR_EXITS_MOBILE);
            }
        }
        if (!Optional.ofNullable(queryResultSysUserInfo).isPresent() && Optional.ofNullable(sysUserInfo.getEmail())
                .isPresent()) {
            params.put("email", sysUserInfo.getEmail());
            params.remove("mobile");
            queryResultSysUserInfo = sysUserInfoMapper.findByCondition(params);
            if (Optional.ofNullable(queryResultSysUserInfo).isPresent()) {
                throw new BaseException(ErrorCodeEnum.ERROR_EXITS_EMAIL);
            }
        }

        return queryResultSysUserInfo;
    }

    private boolean isNothingUpdate(SysUserInfo userRequest) {
        if (StringUtils.isBlank(userRequest.getUserName())
                && StringUtils.isBlank(userRequest.getUserPwd())
                && StringUtils.isBlank(userRequest.getUserStatus())
                && StringUtils.isBlank(userRequest.getNickName())
                && StringUtils.isBlank(userRequest.getMobile())
                && StringUtils.isBlank(userRequest.getEmail())) {
            return true;
        }
        return false;
    }

    /**
     * 属性为null或者属性没变化的时候只null
     *
     * @param oldUserBean
     * @param preUpdateBean
     */
    private void doBeforeUpdate(SysUserInfo oldUserBean, SysUserInfo preUpdateBean) {
        if (StringUtils.isNotBlank(preUpdateBean.getUserName()) && preUpdateBean.getUserName()
                .equals(oldUserBean.getUserName())) {
            preUpdateBean.setUserName(null);
        }
        if (StringUtils.isNotBlank(preUpdateBean.getUserPwd()) && preUpdateBean.getUserPwd()
                .equals(oldUserBean.getUserPwd())) {
            preUpdateBean.setUserPwd(null);
        }
        if (StringUtils.isNotBlank(preUpdateBean.getUserStatus()) && preUpdateBean.getUserStatus()
                .equals(oldUserBean.getUserStatus())) {
            preUpdateBean.setUserStatus(null);
        }
        if (StringUtils.isNotBlank(preUpdateBean.getNickName()) && preUpdateBean.getNickName()
                .equals(oldUserBean.getNickName())) {
            preUpdateBean.setNickName(null);
        }
        if (StringUtils.isNotBlank(preUpdateBean.getMobile()) && preUpdateBean.getMobile()
                .equals(oldUserBean.getMobile())) {
            preUpdateBean.setMobile(null);
        }
        if (StringUtils.isNotBlank(preUpdateBean.getEmail()) && preUpdateBean.getEmail()
                .equals(oldUserBean.getEmail())) {
            preUpdateBean.setEmail(null);
        }

        // 密码处理
        if (StringUtils.isNotBlank(preUpdateBean.getUserPwd())) {
            preUpdateBean.setUserPwd(SHA1.encode(preUpdateBean.getUserId() + preUpdateBean.getUserPwd()));
        }

        // 不允许更新APPID
        if (StringUtils.isNotBlank(preUpdateBean.getAppId())) {
            preUpdateBean.setAppId(null);
        }

    }
}
