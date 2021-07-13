package com.zto.testcase.mapper.testcase;

import com.zto.testcase.model.MenuInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuInfoMapper {

    int insert(MenuInfo menuInfo);

    int updateById(MenuInfo menuInfo);

    List<MenuInfo> select(MenuInfo menuInfo);
}