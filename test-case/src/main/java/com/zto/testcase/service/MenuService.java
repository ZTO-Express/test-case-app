package com.zto.testcase.service;

import com.zto.testcase.model.MenuInfo;
import com.zto.testcase.request.MenuRequest;
import java.util.List;

public interface MenuService {

    long insert(MenuRequest request);

    int updateById(MenuRequest request);

    int deleteById(MenuRequest request);

    List<MenuInfo> select(MenuRequest request);

    List tree(MenuRequest request);

    int updateSrl(MenuRequest request);
}
