
export default {
  appId: 'test-case',
  appTitle: '测试用例管理平台',
  logoUrl: 'img/zto-logo.jpg',
  logoRoute: '/home/dashboard-page/index',
  logoName: '首页',
  apiDomainKey: 'testcase',
  pageNotFoundRoute: '/home/pageNotFound',
  forbiddenRoute: '/home/forbidden',
  securityCenterRoute: '/home/operate/securityCenter',
  smsExt: '',
  domain:'',
  homeGif: 'img/avatar.gif',

  isProduction: process.env.VUE_APP_MODE === 'production',
  mode: process.env.VUE_APP_MODE,
  defaultUsername: process.env.VUE_APP_DEFAULT_USERNAME,
  defaultPassword: process.env.VUE_APP_DEFAULT_PASSWORD,
  API: {
    baseUrl: process.env.VUE_APP_BASE_API_URL,
    prefix: '/testcase',
  },

  SSO: {
    baseUrl: process.env.VUE_APP_BASE_SSO_URL,
    prefix: '/sso',
  },
};
