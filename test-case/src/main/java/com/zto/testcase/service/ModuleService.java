package com.zto.testcase.service;

import com.zto.testcase.response.Result;
import java.util.List;

import com.zto.testcase.model.TcTestCaseModule;
import com.zto.testcase.model.TcTestcase;

public interface ModuleService {
	
    List<TcTestCaseModule> getModuleTree(Integer planId);

	TcTestCaseModule addModule(TcTestCaseModule tcTestCaseModule);

	int deleteModule(TcTestCaseModule tcTestCaseModule);

	List<TcTestCaseModule> querySubModule(TcTestCaseModule tcTestCaseModule);

	Result editModule(TcTestCaseModule tcTestCaseModule);
	
	public List<TcTestCaseModule> getSubModuleTree(int currentModuleId);

	List<TcTestCaseModule> repeatCheckLevelName(TcTestCaseModule tcTestCaseModule);

	List<TcTestCaseModule> getNextModulesById(TcTestCaseModule tcTestCaseModule);

	List<TcTestcase> queryTcTestcaseWithModuleId(TcTestCaseModule tcTestCaseModule);
    
}
