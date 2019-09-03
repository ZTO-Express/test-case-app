
export default {
  appId: 'mix',
  appTitle: 'MIX F.C.运营管理系统',
  logoUrl: 'img/MIX.png',
  logoRoute: '/home/dashboard-page/index',
  logoName: 'MARS',
  apiDomainKey: 'ccpay-api',
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
    prefix: '',
  },

  SSO: {
    baseUrl: process.env.VUE_APP_BASE_SSO_URL,
    prefix: '/sso',
  },
  CCPAY: {
    baseUrl: process.env.VUE_APP_BASE_CCPAY_URL,
    prefix: '/ccpay',
  },
  // ACCOUNT: {
  //   baseUrl: process.env.VUE_APP_BASE_ACCOUNT_URL,
  //   prefix: '/account',
  // },
  // QAMP: {
  //   baseUrl: process.env.VUE_APP_BASE_QAMP_URL,
  //   prefix: '/qamp',
  // },
  MIX: {
    baseUrl: process.env.VUE_APP_BASE_MIX_URL,
    prefix: '/mix',
  },
};
