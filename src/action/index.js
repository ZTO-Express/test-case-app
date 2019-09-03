import Vue from 'vue';
import authAction from './authAction';
import permAction from './permAction';
import operationLogAction from './operationLogAction';

Vue.prototype.$action = {};
Vue.prototype.$action.authAction = authAction;
Vue.prototype.$action.permAction = permAction;
Vue.prototype.$action.operationLogAction = operationLogAction;

