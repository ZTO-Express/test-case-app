import Vue from 'vue';
import Router from 'vue-router';

import routerBeforeHandler from './middleware/routerBeforeHandler';
import routerAfterHandler from './middleware/routerAfterHandler';
import authAction from './action/authAction';
import appData from './appData';
import appConfig from './appConfig';
import sessionUtil from './utils/sessionUtil';
import dashboard from './pages/dashboard-page/index.vue';

import menu from './pages/oper-manager-page/menu-page/index.vue';
import userList from './pages/oper-manager-page/user-page/list.vue';
import roleList from './pages/oper-manager-page/role-page/list.vue';
import forbiddenPage from './pages/exception-page/forbidden-page.vue';
import pageNotFound from './pages/exception-page/page-not-found-page.vue';
import login from './views/login.vue';
import home from './views/home.vue';
// 安全中心
import securityCenter from './pages/oper-manager-page/security-center/index.vue';
// 操作日志
import operationLog from './pages/oper-manager-page/operation-log-page/index.vue';

// MIX
import bookingCenter from './pages/operation-center/booking';
import classesCenter from './pages/operation-center/classes';
import placeCenter from './pages/operation-center/place';
import trainingSessionCenter from './pages/operation-center/trainingSession';
import coachCenter from './pages/club-center/coach';
import playerCenter from './pages/club-center/player';
import echelonCenter from './pages/club-center/echelon';
import signInCenter from './pages/sign-in-center/signIn';
import h5SignInCenter from './pages/sign-in-center/h5SignIn';
import audit from './pages/audit-center/audit';
import signInAudit from './pages/audit-center/signInAudit';
import order from './pages/order-center/order';
import account from './pages/finance-center/account';

Vue.use(Router);

const router = new Router({
  scrollBehavior: () => ({ y: 0 }),
  mode: 'hash',
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      name: 'login',
      component: login,
      beforeEnter: (to, from, next) => {
        sessionUtil.rm(sessionUtil.ACCESS_TOKEN);
        authAction.init().then(() => next());
      },
    },
    {
      path: '/home',
      name: 'home',
      redirect: appConfig.logoRoute,
      component: home,
      beforeEnter: (to, from, next) => {
        authAction.init().then(() => {
          authAction.loadAppData(appData.userInfo.userId).then(() => next());
        });
      },
      children: [
        {
          path: 'dashboard',
          component: dashboard,
        },
        {
          path: 'user/list',
          component: userList,
        },
        {
          path: 'role/list',
          component: roleList,
        },
        {
          path: 'menu/index',
          component: menu,
        },
        {
          path: 'forbidden',
          component: forbiddenPage,
        },
        {
          path: 'pageNotFound',
          component: pageNotFound,
        },
        {
          path: 'operate/securityCenter',
          name: 'securityCenter',
          component: securityCenter,
        },
        {
          path: 'operation-log',
          name: 'operationLog',
          component: operationLog,
        },
        {
          path: 'operation-center/booking',
          name: 'bookingCenter',
          component: bookingCenter,
        },
        {
          path: 'operation-center/classes',
          name: 'classesCenter',
          component: classesCenter,
        },
        {
          path: 'operation-center/place',
          name: 'placeCenter',
          component: placeCenter,
        },
        {
          path: 'operation-center/trainingSession',
          name: 'trainingSessionCenter',
          component: trainingSessionCenter,
        },
        {
          path: 'club-center/coach',
          name: 'coachCenter',
          component: coachCenter,
        },
        {
          path: 'club-center/player',
          name: 'playerCenter',
          component: playerCenter,
        },
        {
          path: 'club-center/echelon',
          name: 'echelonCenter',
          component: echelonCenter,
        },
        {
          path: 'sign-in-center/signIn',
          name: 'signInCenter',
          component: signInCenter,
        },
        {
          path: 'sign-in-center/h5SignIn',
          name: 'h5SignInCenter',
          component: h5SignInCenter,
        },
        {
          path: 'audit-center/audit',
          name: 'audit',
          component: audit,
        },
        {
          path: 'audit-center/signInAudit',
          name: 'signInAudit',
          component: signInAudit,
        },
        {
          path: 'order-center/order',
          name: 'order',
          component: order,
        },
        {
          path: 'finance-center/account',
          name: 'account',
          component: account,
        },
      ],
    },
  ],
});

const requireComponent = require.context(
  // 其组件目录的相对路径
  './pages',
  // 是否查询其子目录
  true,
  // 匹配基础组件文件名的正则表达式
  /index\.vue$/
);
requireComponent.keys().forEach(filePath => {
  // 获取组件配置
  const componentConfig = requireComponent(filePath);
  const routeList = router.options.routes;
  routeList[routeList.length - 1].children.push({
    path: filePath.substring(2).replace('.vue', ''),
    component: componentConfig.default || componentConfig
  });
});
router.addRoutes(router.options.routes);

router.beforeEach((to, from, next) => {
  // 全局route 中间件， 这里需要规范化
  routerBeforeHandler(to, from, next);
});

router.afterEach((route) => {
  // 全局route 中间件， 这里需要规范化
  routerAfterHandler(route);
});

export default router;
