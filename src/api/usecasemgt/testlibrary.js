
import httpService from './../httpService'
export default {
  // 获取模块树
  getModuleList: params => httpService.accessAPI({
    url: '/testcase/module/getModuleTree',
    method: 'GET',
    query: params
  }),
  getModuleLevel5List: params => httpService.accessAPI({
    url: '/testcase/module/getNextModulesById',
    method: 'POST',
    body: params
  }),
  // 模块树新增
  addModule: params => httpService.accessAPI({
    url: `/testcase/module/addModule`,
    method: 'POST',
    body: params
  }),
  // 模块树编辑
  editModule: params => httpService.accessAPI({
    url: `/testcase/module/editModule`,
    method: 'POST',
    body: params
  }),
  // 模块树删除
  delModule: params => httpService.accessAPI({
    url: `/testcase/module/deleteModule`,
    method: 'POST',
    body: params
  }),
  // 用例查询
  getCaseList: params => httpService.accessAPI({
    url: '/testcase/testcase/getTestCaseList',
    method: 'POST',
    body: params
  }),
  // 用例新增
  addCase: params => httpService.accessAPI({
    url: '/testcase/testcase/addTestCase',
    method: 'POST',
    body: params
  }),
  // 用例编辑
  editCase: params => httpService.accessAPI({
    url: '/testcase/testcase/editTestCase ',
    method: 'POST',
    body: params
  }),
  copyEdit: params => httpService.accessAPI({
    url: '/testcase/testcase/copyEditTestCase',
    method: 'POST',
    body: params
  }),
  // 用例删除
  delCase: params => httpService.accessAPI({
    url: '/testcase/testcase/deleteTestCase',
    method: 'POST',
    body: params
  }),
  // 用例移动
  moveCase: params => httpService.accessAPI({
    url: '/testcase/testcase/moveTestCase',
    method: 'POST',
    body: params
  }),
  // 执行计划-测试用例-步骤结果详情
  getCaseListDetail: params => httpService.accessAPI({
    url: '/testcase/plan/caseDetail',
    method: 'GET',
    query: params
  }),
  getCaseDetail: params => httpService.accessAPI({
    url: '/testcase/testcase/getTestcaseStepListByCaseId',
    method: 'POST',
    body: params
  }),
  getCaseFileList: params => httpService.accessAPI({
    url: '/testcase/testcase/getFileListByCaseId',
    method: 'POST',
    body: params
  }),
  // 关联用例
  associateCase: params => httpService.accessAPI({
    url: '/testcase/plan/associateCase',
    method: 'POST',
    body: params
  }),
  // 文件删除
  delFile: params => httpService.accessAPI({
    url: '/testcase/file/delete',
    method: 'DELETE',
    query: params
  }),
  // 文件下载
  downloadFile: params => httpService.accessAPI({
    url: '/testcase/file/download',
    method: 'GET',
    query: params
  }),
  // 用例类型列表
  getTypeList: params => httpService.accessAPI({
    url: ' /testcase/config/type/listAll',
    method: 'GET',
    query: params
  }),
  // 更改状态
  updateStatus: params => httpService.accessAPI({
    url: ' /testcase/plan/planState',
    method: 'POST',
    body: params
  }),
  // 更改执行人/执行结果
  updateCaseInfo: params => httpService.accessAPI({
    url: ' /testcase/plan/batchEdit',
    method: 'POST',
    body: params
  })
}
