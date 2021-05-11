import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/index.js' // 引入element ui js文件
import 'element-ui/lib/theme-chalk/index.css';
import VueAxios from 'vue-axios';
import axios from 'axios';
import permission from 'vue-permission';
import Vue2Filters from 'vue2-filters';
import VueClipboard from 'vue-clipboard2';
import echarts from 'echarts';
import 'github-markdown-css/github-markdown.css';

import mixins from '@/utils/businessCommon'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import '@/styles/global.scss'
import '@/styles/comscss/index.scss'
import '@/styles/comscss/icons/iconfont' // icon
import '@/styles/comscss/icons' // icon
import '@/styles/index.scss' // global css

import './app.css';
import '@/assets/css/iconfont.css';
import 'vue2-animate/dist/vue2-animate.min.css';

import App from './App.vue';
import router from './router';
import appData from './appData';
import appConfig from './appConfig';

import standardRequestHandler from './middleware/StandardRequestHandler';
import standardRespHandler from './middleware/StandardRespHandler';
import './utils/index';
import './action/index';


// 全局统一请求拦截器
axios.interceptors.request.use(standardRequestHandler.succRequestHandle, standardRequestHandler.errRequestHandle);

// 全局统一返回拦截器
axios.interceptors.response.use(standardRespHandler.succResponseHandle, standardRespHandler.errResponseHandle);

Vue.config.productionTip = false;

Vue.use(Vue2Filters);
Vue.use(ElementUI);
Vue.use(VueAxios, axios);
Vue.use(permission);
Vue.use(VueClipboard);
Vue.mixin(mixins)

Vue.prototype.$appData = appData;
Vue.prototype.$appConfig = appConfig;
Vue.prototype.$echarts = echarts;
Vue.prototype.$permission = Vue.permission;

// 设置baseUrl
const appDomain = document.location.host;
if (appDomain.indexOf(':') < 0) {
  appConfig.baseUrl = window.location.protocol + '//' + appDomain.replace(/^[^\.]+/, appConfig.apiDomainKey) + '/api';
}
appConfig.baseUrl= appConfig.baseUrl || appConfig.API.baseUrl || '';
axios.defaults.baseURL = appConfig.baseUrl;

Vue.config.errorHandler = function (err, vm, info) {
  console.info('App global Error!');
  console.error(err);
};

window.appVM = new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
