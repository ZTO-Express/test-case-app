const mock = require('./mock.js');
const path = require('path');

const resolve = dir => path.join(__dirname, dir);

module.exports = {
  productionSourceMap: false,
  lintOnSave: false,
  devServer: {
    before: (app) => {
      mock.mock(app);
    },
  },
  baseUrl: '',
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@', resolve('src'))
      .set('assets', resolve('src/assets'))
      .set('components', resolve('src/components'))
      .set('utils', resolve('src/utils'));
  },
};
