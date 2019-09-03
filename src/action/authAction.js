import Vue from 'vue';
import urlConst from '../utils/urlConst';
import axiosUtil from '../utils/axiosUtil';
import appData from '../appData';
import appConfig from '../appConfig';
import sessionUtil from '../utils/sessionUtil';

function init() {
  return axiosUtil.post(appConfig.SSO, urlConst.APPINFO_LIST, {
    domain: document.location.host,
  }).then((resp) => {
    resp.data.forEach((appInfo) => {
      if (appInfo.appId) {
        appConfig.appId = appInfo.appId;
      }
      if(appInfo.smsExt){
        appConfig.smsExt = appInfo.smsExt;
        appConfig.domain = document.location.host;
      }
    });
  }).catch((error) => {
    Vue.prototype.$message({ type: 'error', showClose: true, message: error.respMessage });
  });
}

// 登录
function login(userName, password, authToken) {
  /* eslint-disable */
  const data = {
    userName,
    userPwd: new Buffer(password).toString('base64'),
    auth_token: authToken,
  };
  return axiosUtil.post(appConfig.API, urlConst.LOGIN_PATH, data);
}

// 登出
function logout(userId) {
  const data = {
    userId,
  };
  return axiosUtil.post(appConfig.API, urlConst.LOGOUT, data);
}

// 用户初始信息
function loadAppData(userId) {
  const accessToken = sessionUtil.get(sessionUtil.ACCESS_TOKEN);
  const data = {
    // userId: userId,
    userId: accessToken ? accessToken.split(":")[1] : null,
  };
  return axiosUtil.get(appConfig.SSO, urlConst.LOAD_APP_DATA_PATH, data)
    .then((response) => {
      appData.menus.splice(0, appData.menus.length);
      appData.menus.push(...response.data.menu);
      appData.pms = response.data.pms;
      appData.userInfo = response.data.userInfo;
      appData.sidebar.routerMenuMap = {};

      Vue.permission.reset();
      Vue.permission.authorize(response.data.pms);
      if (appData.userInfo.roles[0].systemRole === '1') {
        Vue.permission.god = true;
      }

      let doToMenu = (menu) => {
        if(menu.child && menu.child.length > 0){
          menu.child.forEach(doToMenu)
        } else {
          appData.sidebar.routerMenuMap['/home/' + menu.url] = menu;
        }
      };
      appData.menus.forEach(doToMenu);
      const keys = Object.getOwnPropertyNames(appData.pms);

      keys.forEach((controlId)=>{
        if(controlId.indexOf('url:')===0){
          let url = controlId.substring(controlId.indexOf('url:')+4);
          appData.pms[controlId].url = url;
          if (! appData.sidebar.routerMenuMap['/home/' + url]){
            appData.sidebar.routerMenuMap['/home/' + url] = appData.pms[controlId];
          }
        }
      });
      return Promise.resolve(response);
    }).catch((error) => {
      Vue.prototype.$message({
        type: 'error',
        message: error.respMessage,
      });
      return Promise.reject(error);
    });
}

// 修改密码
function updateUserPwd(oldUserPwd, userPwd){
  return axiosUtil.get(appConfig.SSO, urlConst.UPDATE_USER_PWD, {
    userName: appData.userInfo.userName,
    userPwd,
    oldUserPwd,
  });
}

export default {
  loadAppData,
  login,
  logout,
  updateUserPwd,
  init
};
