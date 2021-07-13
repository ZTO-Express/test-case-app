package com.zto.testcase.mapper.testcase;

import java.util.List;

import com.zto.testcase.model.TcCaseType;

public interface TcCaseTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TcCaseType record);

    int insertSelective(TcCaseType record);

    TcCaseType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TcCaseType record);

    int updateByPrimaryKey(TcCaseType record);

	List<TcCaseType> listAll();

    List<TcCaseType> listAllTag();

	int addType(TcCaseType caseType);

	int updateType(TcCaseType caseType);

	TcCaseType findTypeByName(String name);

	TcCaseType findTypeById(Integer id);

	int changeStatus(TcCaseType caseType);
}