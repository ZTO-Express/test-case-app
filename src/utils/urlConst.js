/*  ******************* sso权限模块 *********************** */
const LOGIN_PATH = '/testcase/v1/sso/login'
const FORGET_PASSWD_AUTHORIZATION = '/testcase/v1/sso/forgetPswAuthorization' // 忘记密码授权
const LOGIN_AUTHORIZATION = '/testcase/v1/sso/loginAuthorization' // 登录授权
const LOGIN_NAME_CHECK = '/testcase/v1/user/findByLoginName' // 登录名验证
const BIND_MOBILE_AUTHORIZATION = '/v1/user-mobile/authorization' // 绑定手机号授权
const LOGOUT = '/testcase/v1/user/logout'
const LOAD_APP_DATA_PATH = '/testcase/v1/user/loadAppData'
const FIND_USERS = '/testcase/v1/user/findAll'
const UPDATE_USER = '/testcase/v1/user/updateByUserId'
const CREATE_USER = '/testcase/v1/user/create'
const DELETE_USER = '/testcase/v1/user/delete'
const FIND_ROLES = '/testcase/v1/role/findAll'
const MENU_TREE = '/testcase/v1/menu/tree'
const MENU_UPDATE = '/testcase/v1/menu/updateById'
const MENU_CREATE = '/testcase/v1/menu/create'
const MENU_DELETE = '/testcase/v1/menu/deleteById'
const UI_META_TREE = '/testcase/v1/uimeta/tree'
const UI_META_CREATE = '/testcase/v1/uimeta/create'
const UI_META_UPDATE = '/testcase/v1/uimeta/updateById'
const UI_META_DELETE = '/testcase/v1/uimeta/deleteById'
const UI_META_BY_ROLE = '/testcase/v1/uimeta/treeByRoleCode'
const UI_META_BATCH_CREATE = '/testcase/v1/pms/control/batchCreate'
const UI_META_DELETE_BY_ROLECODE = '/testcase/v1/pms/control/deleteByRoleCode'
const UI_META_BATCH_UPDATE = '/testcase/v1/pms/control/batchUpdateById'
const DELETE_ROLE = '/testcase/v1/role/delete'
const UPDATE_ROLE = '/testcase/v1/role/updateByRoleCode'
const CREATE_ROLE = '/testcase/v1/role/create'
const BAND_MENU = '/testcase/v1/role/bandMenu'
const MENU_TREE_BY_ROLE = '/testcase/v1/pms/menu/treeByRoleCode'
const PMS_BY_ROLE = '/testcase/v1/pms/control/treeByRoleCode'
const SLR_UPDATE = '/testcase/v1/menu/updateSrl' // 跟新排序
const UPDATE_USER_PWD = '/testcase/v1/user/info/updateUserPwd' // 修改密码
const OPERATION_LOG_LIST = '/testcase/v1/operationLog/list' // 操作日志
const APPINFO_LIST = '/testcase/v1/appInfo/list' // appinfo

/** ******************* 测试用例管理平台服务模块 *********************** */
const GET_MODULE_TREE = '/testcase/module/getModuleTree'
const GET_TESTCASE_LIST = '/testcase/testcase/getTestCaseList'
const EXPORT_TEST_CASE = '/testcase/testcase/exportTestCase'
const GET_PLAN_COUNT = '/testcase/plan/getPlanCount'
const GET_PLAN_LIST = '/testcase/plan/planList'
const DELETE_PLAN = '/testcase/plan/deletePlan'
const SAVE_PLAN = '/testcase/plan/addOrUpdatePlan'
const GET_PLAN_DETAIL = '/testcase/plan/planDetail'
const GET_ALL_TESTER = '/testcase/v1/user/getAllTester'
const GET_ALL_STAGE = '/testcase/config/stage/listAll'
const GET_BUSINESS_LINE_TREE = '/testcase/sys/business/getBusinessLineTree'
const GET_PROJECT_LIST_BY_BID = '/testcase/sys/business/getProjectListByBid'
const GET_VERSION_BY_PID = '/testcase/sys/business/getVersionByPid'
const GET_TYPE_LIST = '/testcase/config/type/listAll'
const GET_FILE_LIST_BY_CASE_ID = '/testcase/testcase/getFileListByCaseId'

const ADD_MODULE = '/testcase/module/addModule'
const EDIT_MODULE = '/testcase/module/editModule'
const DELETE_MODULE = '/testcase/module/deleteModule'
const GET_NEXT_MODULES_BY_ID = '/testcase/module/getNextModulesById'
const ADD_TEST_CASE = '/testcase/testcase/addTestCase'
const EDIT_TEST_CASE = '/testcase/testcase/editTestCase'
const GET_CASE_DETAIL = '/testcase/testcase/getTestcaseStepListByCaseId'
const COPY_EDIT_TESTCASE = '/testcase/testcase/copyEditTestCase'
const DEL_TESTCASE = '/testcase/testcase/deleteTestCase'
const MOVE_TESTCASE = '/testcase/testcase/moveTestCase'
const ASSOCIATE_TESTCASE = '/testcase/plan/associateCase'
const UPDATE_CASEINFO = '/testcase/plan/batchEdit'
const UPDATE_STATUS = '/testcase/plan/planState'

const FILE_UPLOAD = '/testcase/file/upload'
const FILE_DOWNLOAD = '/testcase/file/download'
const FILE_DELETE = '/testcase/file/delete'

export default {
  OPERATION_LOG_LIST,
  LOGOUT,
  LOGIN_NAME_CHECK,
  FORGET_PASSWD_AUTHORIZATION,
  LOGIN_AUTHORIZATION,
  BIND_MOBILE_AUTHORIZATION,
  UPDATE_USER_PWD,
  LOGIN_PATH,
  LOAD_APP_DATA_PATH,
  FIND_USERS,
  FIND_ROLES,
  UPDATE_USER,
  CREATE_USER,
  DELETE_USER,
  MENU_TREE,
  UI_META_TREE,
  MENU_UPDATE,
  DELETE_ROLE,
  UPDATE_ROLE,
  CREATE_ROLE,
  BAND_MENU,
  MENU_CREATE,
  MENU_DELETE,
  UI_META_CREATE,
  UI_META_UPDATE,
  UI_META_DELETE,
  UI_META_DELETE_BY_ROLECODE,
  UI_META_BATCH_CREATE,
  UI_META_BATCH_UPDATE,
  MENU_TREE_BY_ROLE,
  UI_META_BY_ROLE,
  SLR_UPDATE,
  PMS_BY_ROLE,
  APPINFO_LIST,

  GET_MODULE_TREE,
  GET_TESTCASE_LIST,
  EXPORT_TEST_CASE,
  GET_PLAN_COUNT,
  GET_PLAN_LIST,
  SAVE_PLAN,
  DELETE_PLAN,
  GET_PLAN_DETAIL,
  GET_ALL_TESTER,
  GET_ALL_STAGE,
  GET_BUSINESS_LINE_TREE,
  GET_PROJECT_LIST_BY_BID,
  GET_VERSION_BY_PID,
  GET_TYPE_LIST,
  GET_FILE_LIST_BY_CASE_ID,

  ADD_MODULE,
  EDIT_MODULE,
  GET_NEXT_MODULES_BY_ID,
  ADD_TEST_CASE,
  EDIT_TEST_CASE,
  DELETE_MODULE,
  FILE_UPLOAD,
  FILE_DOWNLOAD,
  GET_CASE_DETAIL,
  FILE_DELETE,
  COPY_EDIT_TESTCASE,
  DEL_TESTCASE,
  MOVE_TESTCASE,
  ASSOCIATE_TESTCASE,
  UPDATE_CASEINFO,
  UPDATE_STATUS
}
