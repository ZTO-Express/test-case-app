package com.zto.testcase.mapper.testcase;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zto.testcase.model.TcFile;

public interface TcFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TcFile record);

    int insertAndGetId(TcFile record);

    int insertSelective(TcFile record);

    TcFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TcFile record);

    int updateByPrimaryKey(TcFile record);

	void updateFileRelationship(@Param(value="ids")List<Integer> ids, @Param(value="relationId")Integer relationId);

	List<TcFile> getFileListByCaseId(Integer caseId);

	int copyInsert(TcFile newTcFile);

    List<String> getFilePathListByCaseId(Integer caseId);

}