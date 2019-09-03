/* eslint-disable no-console */

import appConfig from '../appConfig';

export default (to, from, next) => {
  if (!appConfig.isProduction) {
    console.info('route before enter', to);
  }

  next();
};
