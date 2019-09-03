import appConfig from '../appConfig';
/* eslint-disable */
function log(...message) {
  if (!appConfig.isProduction) {
    console.log(...message);
  }
}

function info(...message) {
  if (!appConfig.isProduction) {
    console.info(...message);
  }
}

function error(...message) {
  if (!appConfig.isProduction) {
    console.error(...message);
  }
}

export default {
  log,
  info,
  error,
};
