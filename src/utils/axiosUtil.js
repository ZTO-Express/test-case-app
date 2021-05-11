import Vue from 'vue';
import appConfig from '../appConfig';

function getReqUrl(baseUrl, prefix, url) {
  let reqUrl = url;
  if (appConfig.mode === 'dev' || appConfig.mode === 'test' || appConfig.mode === 'production') {
    reqUrl = baseUrl + reqUrl;
  } else if (appConfig.mode !== 'mock') {
    reqUrl = prefix + reqUrl;
  }
  return reqUrl;
}

function fullReqUrl(service, url) {
  let reqUrl = getReqUrl(service.baseUrl, service.prefix, url);
  if (!(reqUrl.startsWith('https:') || reqUrl.startsWith('http:'))) {

    reqUrl = appConfig.baseUrl + reqUrl;
  }
  return reqUrl;
}

function post(service, url, data, headers, opts) {
  const config = Object.assign({
    url: getReqUrl(service.baseUrl, service.prefix, url),
    method: 'post',
    data,
  }, opts);

  if (headers) {
    config.headers = headers;
  }

  return Vue.axios(config);
}

function get(service, url, data, headers) {
  const config = {
    url: getReqUrl(service.baseUrl, service.prefix, url),
    method: 'get',
    params: data,
  };
  if (headers) {
    config.headers = headers;
  }
  return Vue.axios(config);
}

function del(service, url, data, headers) {
  const config = {
    url: getReqUrl(service.baseUrl, service.prefix, url),
    method: 'DELETE',
    params: data,
  };
  if (headers) {
    config.headers = headers;
  }
  return Vue.axios(config);
}

function download(service, url, data) {
  const config = {
    url: getReqUrl(service.baseUrl, service.prefix, url),
    method: 'post',
    data,
    responseType: 'arraybuffer',
  };
  return Vue.axios(config);
}

export default {
  getReqUrl,
  fullReqUrl,
  post,
  get,
  del,
  download,
};
