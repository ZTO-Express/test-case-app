const login = require('./mock/auth/login.json');
const load_app_data = require('./mock/auth/load_app_data.json');
const login_authorization = require('./mock/auth/login_authorization.json');
const is_google_token_configured = require('./mock/auth/is_google_token_configured.json');
const google_captcha = require('./mock/auth/google_captcha.json');

const mock = (router) => {
  router.post('/v1/google-code/is-google-token-configured', (req, res) => {
    res.send(is_google_token_configured);
  });
  router.post('/v1/sso/login-authorization', (req, res) => {
    res.send(login_authorization);
  });
  router.post('/sso/v1/sso/login', (req, res) => {
    res.send(login);
  });
  router.post('/v1/session-auth/google-captcha', (req, res) => {
    res.send(google_captcha);
  });
  router.get('/v1/user/loadAppData', (req, res) => {
    res.send(load_app_data);
  });
};

module.exports = {
  mock,
};

