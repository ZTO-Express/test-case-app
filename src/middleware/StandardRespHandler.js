/* eslint-disable no-console */

import appConfig from '../appConfig';
import router from '../router';
import sessionUtil from '../utils/sessionUtil';

function succResponseHandle(response) {
  if (!appConfig.isProduction) {
    console.log('response: ' + JSON.stringify(response.data, null, '\t'));
  }

  if (response.data.code && !response.data.respCode) {
    response.data.respCode = response.data.code;
  }
  if (response.data.msg && !response.data.respMessage) {
    response.data.respMessage = response.data.msg;
  }
  if (response.data.respCode !== 'S0001') {
    if (response.data.respCode === 'F40001' || response.data.respCode === 'F0004') {
      const msgs = JSON.parse(response.data.respMessage);
      response.data.respMessage = Object.values(msgs)[0];
    }

    if (!response.data.respMessage) {
      response.data.respMessage = '未知错误';
    }

    if (response.data.respMessage === '系统繁忙，请稍后再试') {
      if (response.data.respCode === '01' || response.data.respCode === '02' || response.data.respCode === '03' || response.data.respCode === '04') {
        if (response.data.respMessage === '已超过当日短信发送最大次数') {
          response.data.respMessage = '已超过当日短信发送最大次数';
        } else {
          response.data.respMessage = '短信服务异常';
        }
      }
    }
    return Promise.reject(response.data);
  }
  return Promise.resolve(response.data);
}

function errResponseHandle(error) {
  if (!appConfig.isProduction) {
    console.dir(error);
  }

  if (!error.response) {
    const errorResp = {
      respCode: error,
      respMessage: '网络异常，请检查网络后再试',
    };
    return Promise.reject(errorResp);
  }
  const response = {};
  if (error.response.data.code && !error.response.data.respCode) {
    error.response.data.respCode = error.response.data.code;
  }
  if (error.response.data.msg && !error.response.data.respMessage) {
    error.response.data.respMessage = error.response.data.msg;
  }
  switch (error.response.data.respCode || error.response.status) {
    case 400:
      response.respMessage = '请求格式错误';
      break;
    case 404:
      response.respMessage = '系统繁忙，请稍后再试';
      break;
    case 415:
      response.respMessage = '服务器无法处理请求附带的媒体格式';
      break;
    case 500:
      response.respMessage = '服务器内部错误';
      break;
    case 'F404':
      Object.assign(response, error.response.data);
      break;
    case 'F424':
      Object.assign(response, error.response.data);
      break;
    case 'F401-1':
      Object.assign(response, error.response.data);
      response.respMessage = '操作停留时间过长，请关闭弹窗重试';
      break;
    case 'F403':
      Object.assign(response, error.response.data);
      sessionUtil.rm(sessionUtil.ACCESS_TOKEN);
      router.replace('/login');
      break;
    case 'F401':
      Object.assign(response, error.response.data);
      sessionUtil.rm(sessionUtil.ACCESS_TOKEN);
      router.replace('/login');
      break;
    default:
      response.respMessage = error.response.data.respMessage || error.message || '未知错误';
      break;
  }
  if (!appConfig.isProduction) {
    console.log('response: ' + JSON.stringify(response, null, '\t'));
  }
  return Promise.reject(response);
}

export default {
  succResponseHandle,
  errResponseHandle,
};
