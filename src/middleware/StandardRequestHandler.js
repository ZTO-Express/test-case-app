/* eslint-disable no-console */

import sessionUtil from '../utils/sessionUtil';
import appConfig from '../appConfig';
import appData from '../appData';
import urlConst from '../utils/urlConst';

function injectAppId(config) {
  if (config.data) {
    config.data.appId = appConfig.appId;
  }
  if (config.params) {
    config.params.appId = appConfig.appId;
  }
}
function injectMerchantId(config) {
  if (config.data && appData.userInfo.org) {
    config.data.merchantId = appData.userInfo.org;
    config.data.merchantNo = appData.userInfo.org;
  }
  if (config.params && appData.userInfo.org) {
    config.params.merchantId = appData.userInfo.org;
    config.params.merchantNo = appData.userInfo.org;
  }
}

function injectSmsExt(config) {
  if(config.url !== urlConst.SMS_CAPTCHA
    && config.url !== appConfig.API.prefix+urlConst.MERCHANT_REGISTER
    && config.url != appConfig.API.prefix+urlConst.MERCHANT_SEND_EMAIL){
    return;
  }
  if (config.data) {
    config.data.smsExt = appConfig.smsExt;
    config.data.loginUrl = appConfig.domain;
  }
  if (config.params) {
    config.params.smsExt = appConfig.smsExt;
    config.params.loginUrl = appConfig.domain;
  }
}


function succRequestHandle(config) {
  const accessToken = sessionUtil.get(sessionUtil.ACCESS_TOKEN);
  if (accessToken) {
    config.headers[sessionUtil.ACCESS_TOKEN] = accessToken;
  }

  injectAppId(config);
  injectMerchantId(config);
  injectSmsExt(config);

  if (!appConfig.isProduction) {
    console.log('url: ' + config.url);
    console.log('type: ' + config.method);
    console.log('data: ' + JSON.stringify(config.data, null, '\t'));
    console.log('params: ' + JSON.stringify(config.params, null, '\t'));
  }

  return config;
}

function errRequestHandle(error) {
  return error;
}

export default {
  succRequestHandle,
  errRequestHandle,
};
