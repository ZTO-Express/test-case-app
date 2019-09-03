import appConfig from '../appConfig';
import appData from '../appData';
import router from '../router';

const routeMap = {
  homeExistPms: null,
  home: appConfig.pageNotFoundRoute,
  homePms: appConfig.pageNotFoundRoute,
  homeExist: appConfig.forbiddenRoute,
  Exist: null,

  homeExistPmsPageNotFound: null,
  homeExistPageNotFound: null,

  homeExistPmsForbidden: null,
  homeExistForbidden: null,

  homeExistDashboard: null,
  homeExistPmsDashboard: null,

  homeExistSecurity: null,
  homeExistPmsSecurity: null,
};

export default (to, from) => {
  if (!appConfig.isProduction) {
    console.info('route after enter', to);
  }
  let flag = '';
  if (to.path.startsWith('/home/')) {
    flag = flag.concat('home');
  }
  if (to.matched.length > 0) {
    flag = flag.concat('Exist');
  }
  if (appData.sidebar.routerMenuMap[to.path]) {
    flag = flag.concat('Pms');
  }
  if (to.path === appConfig.pageNotFoundRoute) {
    flag = flag.concat('PageNotFound');
  } else if (to.path === appConfig.forbiddenRoute) {
    flag = flag.concat('Forbidden');
  } else if (to.path === appConfig.logoRoute) {
    flag = flag.concat('Dashboard');
  } else if (to.path === appConfig.securityCenterRoute) {
    flag = flag.concat('Security');
  }

  if (routeMap[flag]) {
    router.replace(routeMap[flag]);
  } else if (routeMap[flag] === undefined) {
    router.replace('/login');
  }
};
