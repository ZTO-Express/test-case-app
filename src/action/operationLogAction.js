import urlConst from '../utils/urlConst';
import axiosUtil from '../utils/axiosUtil';
import appConfig from '../appConfig';

// 操作日志列表
function operationLogList(param) {
  return axiosUtil.get(appConfig.SSO, urlConst.OPERATION_Log_LIST, param);
}

export default {
  operationLogList,
};
