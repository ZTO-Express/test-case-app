package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.SysUserInfo;
import com.zto.testcase.response.UserListItem;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface SysUserInfoMapper {

    List<SysUserInfo> getAllTester();

    int deleteByPrimaryKey(Integer id);

    int insert(SysUserInfo record);

    int insertSelective(SysUserInfo record);

    SysUserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserInfo record);

    int updateByUserId(SysUserInfo record);

    int updateByPrimaryKey(SysUserInfo record);

    SysUserInfo findByCondition(Map<String, String> params);

    List<SysUserInfo> findListByCondition(Map<String, String> params);

    void updateLoginErrorTimes(@Param("loginErrorTimes") Integer loginErrorTimes, @Param("id") Integer id);

    void updateUserStatusByUserId(SysUserInfo sysUserInfo);

    List<UserListItem> findByRoleCode(@Param("appId") String appId, @Param("roleCode") String roleCode);

    List<UserListItem> findByAppId(String appId);
}