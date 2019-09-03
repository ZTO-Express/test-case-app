import Vue from 'vue';

import $appConst from './appConst';
import $axiosUtil from './axiosUtil';
import $urlConst from './urlConst';
import $formatters from './formatters';
import $sessionUtil from './sessionUtil';
import $logUtil from './logUtil';
import $permUtil from './permUtil';
import $regexUtil from './regexUtil';
import $appConfig from '../appConfig';
import $appData from '../appData';
import $localUtil from './localUtil';

Vue.prototype.$appConst = $appConst;
Vue.prototype.$axiosUtil = $axiosUtil;
Vue.prototype.$urlConst = $urlConst;
Vue.prototype.$sessionUtil = $sessionUtil;
Vue.prototype.$formatters = $formatters;
Vue.prototype.$logUtil = $logUtil;
Vue.prototype.$permUtil = $permUtil;
Vue.prototype.$regexUtil = $regexUtil;
Vue.prototype.$localUtil = $localUtil;

const utils = {
  $appConst,
  $axiosUtil,
  $urlConst,
  $sessionUtil,
  $formatters,
  $logUtil,
  $permUtil,
  $regexUtil,
  $appConfig,
  $appData,
  $localUtil,
};

export default utils;
