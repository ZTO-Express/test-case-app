/** ******************* api公共模块  *********************** */
const UPLOAD = '/v1/file/static/diablo';
const UPLOAD_CUSTOMER_IMG = '/v1/file/agent-manager_files/img'; // /opt/files/agent-manager_files/img 兼容老boss图片上传路径
const UPLOAD_CUSTOMER_FILE = '/v1/file/agent-manager_files/file'; // /opt/files/agent-manager_files/file 兼容老boss附件上传路径
const UPLOAD_KEYS_STORE = '/v1/file/keys_store';
const UPLOAD_RECONCILE_FILE = '/v1/file/reconcile'; // 对账单文件目录
const UPLOAD_APP_FILE = '/v1/file/app'; // app文件目录
const SMS_CAPTCHA = '/v1/captcha/sms-captcha'; // 发送短信验证码
const SMS_CAPTCHA_VERIFY = '/v1/session-auth/sms-captcha'; // 短信验证码校验
const BIND_MOBILE = '/v1/user-mobile/bind-mobile'; // 绑定手机号
// const BIND_MOBILE_AUTHORIZATION = '/v1/user-mobile/authorization'; // 绑定手机号授权
// const IS_DYNAMIC_CODE_CONFIGURED = '/v1/google-code/is-google-token-configured'; // 查询用户是否绑定了动态验证码
// const GOOGLE_AUTH_BIND_AUTHORIZATION = '/v1/google-code/google-auth-bind-authorization'; // google 码绑定授权
// const GOOGLE_AUTH_QRCODE = '/v1/google-code/google-auth-qrcode'; // google 动态二维码
const GOOGLE_CAPTCHA = '/v1/session-auth/google-captcha'; // google验证码校验
// const FORGET_PASSWD_AUTHORIZATION = '/v1/forget-psw/authorization'; // 忘记密码授权
// const LOGIN_PATH = '/sso/v1/sso/login';
// const GOOGLE_AUTH_BIND = '/v1/google-code/google-auth-bind'; // google 绑定
// const LOGIN_NAME_CHECK = '/v1/forget-psw/login-name-check'; // 登录名验证
const SMS_CAPTCHA_BF_LOGIN = '/v1/session-auth/sms-captcha-bf-login'; // 登录前 短信验证
const GOOGLE_CAPTCHA_BF_LOGIN = '/v1/session-auth/google-captcha-bf-login'; // 登录前google 码验证
const UPDATE_PASSWD = '/v1/forget-psw/confirm-update'; // 忘记密码修改密码
const CREATE_CAPTCHA = '/v1/imageCaptcha/createCaptcha'; // 创建图片验证码
const OPERATOR_UPDATE_AUTHORIZATION = '/v1/operator-update/authorization'; // 编辑操作员授权
/** ******************* sso权限模块 *********************** */
const LOGIN_PATH = '/v1/sso/login';
const FORGET_PASSWD_AUTHORIZATION = '/v1/sso/forgetPswAuthorization'; // 忘记密码授权
const LOGIN_AUTHORIZATION = '/v1/sso/loginAuthorization'; // 登录授权
const LOGIN_NAME_CHECK = '/v1/user/findByLoginName'; // 登录名验证
const BIND_MOBILE_AUTHORIZATION = '/v1/user-mobile/authorization'; // 绑定手机号授权
const LOGOUT = '/v1/user/logout';
const LOAD_APP_DATA_PATH = '/v1/user/loadAppData';
const FIND_USERS = '/v1/user/findAll';
const UPDATE_USER = '/v1/user/updateByUserId';
const CREATE_USER = '/v1/user/create';
const DELETE_USER = '/v1/user/delete';
const FIND_ROLES = '/v1/role/findAll';
const MENU_TREE = '/v1/menu/tree';
const MENU_UPDATE = '/v1/menu/updateById';
const MENU_CREATE = '/v1/menu/create';
const MENU_DELETE = '/v1/menu/deleteById';
const UI_META_TREE = '/v1/uimeta/tree';
const UI_META_CREATE = '/v1/uimeta/create';
const UI_META_UPDATE = '/v1/uimeta/updateById';
const UI_META_DELETE = '/v1/uimeta/deleteById';
const UI_META_BY_ROLE = '/v1/uimeta/treeByRoleCode';
const UI_META_BATCH_CREATE = '/v1/pms/control/batchCreate';
const UI_META_DELETE_BY_ROLECODE = '/v1/pms/control/deleteByRoleCode';
const UI_META_BATCH_UPDATE = '/v1/pms/control/batchUpdateById';
const DELETE_ROLE = '/v1/role/delete';
const UPDATE_ROLE = '/v1/role/updateByRoleCode';
const CREATE_ROLE = '/v1/role/create';
const BAND_MENU = '/v1/role/bandMenu';
const MENU_TREE_BY_ROLE = '/v1/pms/menu/treeByRoleCode';
const PMS_BY_ROLE = '/v1/pms/control/treeByRoleCode';
const SLR_UPDATE = '/v1/menu/updateSrl'; // 跟新排序
const UPDATE_USER_PWD = '/v1/user/info/updateUserPwd'; // 修改密码
const OPERATION_Log_LIST = '/v1/operationLog/list'; // 操作日志
const APPINFO_LIST = '/v1/appInfo/list'; // appinfo


/** ******************* MIX 管理平台服务模块 *********************** */
const QUERY_BOOKING_LIST = '/v1/booking/getBookingList';
const ADD_BOOKING_LIST = '/v1/booking/addBookingList';
const EDIT_BOOKING_LIST = '/v1/booking/editBookingList';
const DEL_BOOKING_LIST = '/v1/booking/delBookingList';
const QUERY_COACH_LIST = '/v1/coach/getCoachList';

const ADD_COACH = '/v1/coach/addCoach';
const EDIT_COACH = '/v1/coach/editCoach';
const DEL_COACH = '/v1/coach/delCoach';
const QUERY_PLAYER_LIST = '/v1/player/getPlayerList';
const QUERY_MEMBER_LIST = '/v1/member/getMemberList';

const ADD_PLAYER = '/v1/player/addPlayer';
const EDIT_PLAYER = '/v1/player/editPlayer';
const DEL_PLAYER = '/v1/player/delPlayer';

const QUERY_CLASSES_LIST = '/v1/classes/getClassesList';
const QUERY_CLASSES_NAME = '/v1/classes/getClassesName';
const ADD_CLASSES = '/v1/classes/addClasses';
const EDIT_CLASSES = '/v1/classes/editClasses';
const DEL_CLASSES = '/v1/classes/delClasses';

const QUERY_SESSION_LIST = '/v1/session/getSessionList';
const QUERY_SESSION_LIST_BY_MONTH = '/v1/session/getSessionListByMonth';
const ADD_SESSION = '/v1/session/addSession';
const EDIT_SESSION = '/v1/session/editSession';
const OPEN_SESSION = '/v1/session/openSession';
const CANCEL_SESSION = '/v1/session/cancelSession';

const SIGN_IN = '/v1/signIn/signIn';
const QUERY_SIGN_IN_LIST = '/v1/signIn/getSignInList';

const QUERY_ECHELON_LIST = '/v1/echelon/getEchelonList';
const ADD_ECHELON = '/v1/echelon/addEchelon';
const EDIT_ECHELON = '/v1/echelon/editEchelon';
const DEL_ECHELON = '/v1/echelon/delEchelon';

const QUERY_PLACE_LIST = '/v1/place/getPlaceList';
const ADD_PLACE = '/v1/place/addPlace';
const EDIT_PLACE = '/v1/place/editPlace';
const DEL_PLACE = '/v1/place/delPlace';

const QUERY_ACCOUNT_LIST = '/v1/account/getAccountList';

const QUERY_DASHBOARD_DATA = '/v1/dataCenter/getDashboardData';


const PLAYER_CHARGE = '/v1/account/charge';

const CREATE_AUDIT_ORDER = '/v1/audit/createAuditOrder';
const UPDATE_AUDIT_ORDER = '/v1/audit/updateAuditOrder';
const QUERY_AUDIT_ORDER = '/v1/audit/getAuditList';

const UPLOAD_IMG = '/v1/upload/img';

export default {
  OPERATION_Log_LIST,
  CREATE_CAPTCHA,
  UPDATE_PASSWD,
  GOOGLE_CAPTCHA_BF_LOGIN,
  SMS_CAPTCHA_BF_LOGIN,
  LOGOUT,
  LOGIN_NAME_CHECK,
  FORGET_PASSWD_AUTHORIZATION,
  LOGIN_AUTHORIZATION,
  // GOOGLE_CAPTCHA,
  // GOOGLE_AUTH_BIND,
  // GOOGLE_AUTH_QRCODE,
  // GOOGLE_AUTH_BIND_AUTHORIZATION,
  // IS_DYNAMIC_CODE_CONFIGURED,
  BIND_MOBILE,
  SMS_CAPTCHA_VERIFY,
  SMS_CAPTCHA,
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
  UPLOAD,
  UPLOAD_KEYS_STORE,
  UPLOAD_CUSTOMER_IMG,
  UPLOAD_CUSTOMER_FILE,
  UPLOAD_RECONCILE_FILE,
  OPERATOR_UPDATE_AUTHORIZATION,
  APPINFO_LIST,
  UPLOAD_APP_FILE,

  QUERY_BOOKING_LIST,
  ADD_BOOKING_LIST,
  EDIT_BOOKING_LIST,
  DEL_BOOKING_LIST,
  QUERY_COACH_LIST,

  ADD_COACH,
  EDIT_COACH,
  DEL_COACH,
  QUERY_PLAYER_LIST,
  QUERY_MEMBER_LIST,

  ADD_PLAYER,
  EDIT_PLAYER,
  DEL_PLAYER,

  QUERY_CLASSES_LIST,
  QUERY_CLASSES_NAME,
  ADD_CLASSES,
  EDIT_CLASSES,
  DEL_CLASSES,

  QUERY_SESSION_LIST,
  QUERY_SESSION_LIST_BY_MONTH,
  ADD_SESSION,
  EDIT_SESSION,
  OPEN_SESSION,
  CANCEL_SESSION,

  QUERY_SIGN_IN_LIST,
  SIGN_IN,

  QUERY_ECHELON_LIST,
  ADD_ECHELON,
  EDIT_ECHELON,
  DEL_ECHELON,

  QUERY_PLACE_LIST,
  ADD_PLACE,
  EDIT_PLACE,
  DEL_PLACE,

  QUERY_ACCOUNT_LIST,

  QUERY_DASHBOARD_DATA,

  PLAYER_CHARGE,

  CREATE_AUDIT_ORDER,
  UPDATE_AUDIT_ORDER,
  QUERY_AUDIT_ORDER,

  UPLOAD_IMG,
};
