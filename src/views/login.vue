<template>
  <div>
    <auth-verify :sessionId="authVerifySessionId" :authName="authName" :modal="true" @sendSms="sendSms" :authType="authType" @next="authVerifyNext" :trigger="authVerifyTrigger" ></auth-verify>
    <forget-passwd @next="forgetPasswdNext" :trigger="forgetPasswdTyigger"></forget-passwd>
    <reset-password :needOldPwd="false" :modal="true" :trigger="resetPasswordTrigger" @next="resetPasswordNext"></reset-password>

    <div class="login-container" >
      <el-form autoComplete="on" :model="loginForm" :rules="loginRules"
               ref="loginForm" label-position="left" label-width="0px"
               class="card-box login-form">
        <div align="center" >
          <img v-if="$appConfig.logoUrl" class="logoImg"  v-bind:src="$appConfig.logoUrl"/>
          <span class="logo-name" v-else>{{$appConfig.logoName}}</span>
        </div>
        <br>
        <br>
        <el-form-item prop="username">
          <i class="icon-user"></i>
          <el-input clearable name="username" type="text" v-model="loginForm.username"
                    autoComplete="on" placeholder="请输入用户名" autofocus @keyup.enter.native="loginSession"/>
          <i class="icon-unseen-o" style="visibility:hidden"></i>
        </el-form-item>
        <el-form-item prop="password" style="margin-bottom: 0px">
          <i class="icon-key"></i>
          <el-input clearable name="password" :type="pwdType"  @keyup.enter.native="loginSession"
                    v-model="loginForm.password" autoComplete="on"
                    placeholder="请输入密码"></el-input>
          <i class="icon-unseen-o" @click="showPwd"></i>
        </el-form-item>

        <div style="text-align: right; margin-bottom: 30px; font-size: 13px" >
          <a href="javascript:void(0)" @click="forgetPasswordSession">忘记密码?</a>
        </div>

        <el-form-item>
          <el-button type="primary" class="sign-btn" :loading="loading"
                     @click.native.prevent="loginSession">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import sessionUtil from '../utils/sessionUtil';
import authAction from '../action/authAction';
import authVerify from '../components/auth-verify/index.vue';
import forgetPasswd from '../components/forgetPasswd/index.vue';
import resetPassword from '../components/reset-password/index.vue';

export default {
  components: {
    authVerify, forgetPasswd, resetPassword,
  },
  data() {
    const validateUsername = (rule, value, callback) => {
      callback();
    };
    const validatePass = (rule, value, callback) => {
      callback();
    };
    return {
      data: {
        userId: '',
      },
      loginForm: {
        username: this.$appConfig.defaultUsername,
        password: this.$appConfig.defaultPassword,
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { trigger: 'blur', validator: validateUsername },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { trigger: 'blur', validator: validatePass },
        ],
      },
      loading: false,
      pwdType: 'password',

      authVerifyTrigger: false,
      authVerifyNext() {},
      authType: 'all',
      authName: '',

      forgetPasswdTyigger: false,
      forgetPasswdNext() {},

      loginSessionData: {
        steps: {
          login: this.loginVerify,
          auth_verify: this.triggerAuthVerifyForLogin,
          '': this.loginSuccess,
        },
      },

      resetPasswordTrigger: false,
      resetPasswordNext() {},
      resetPasswordSessionData: {
        steps: {
          login_name_check: this.triggerForgetPasswd,
          // auth_verify_bf_login: this.triggerAuthVerifyForPassword,
          '': this.triggerResetPassword,
        },
      },

      authVerifySessionId: '',
    };
  },

  methods: {
    forgetPasswordSession() {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.FORGET_PASSWD_AUTHORIZATION).then((response) => {
        this.nextStep(this.resetPasswordSessionData, response.data);
      }).catch((error) => {
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    triggerForgetPasswd() {
      this.forgetPasswdNext = (form, success) => {
        this.resetPasswordSessionData.userName = form.userName;
        this.$axiosUtil.post(this.$appConfig.SSO, this.$urlConst.LOGIN_NAME_CHECK, {
          loginName: form.userName,
          auth_token: this.resetPasswordSessionData.auth_token,
          // userPwd: new Buffer(form.newPassword).toString('base64'),
        }).then((response) => {
          this.userId = response.data.userId;
          success();
          this.nextStep(this.resetPasswordSessionData, response.data);
        }).catch((error) => {
          if (error.respCode === 'F41008') {
            this.$message({ type: 'error', showClose: true, message: '用户名有误，请重试' });
          } else {
            this.$message({ type: 'error', showClose: true, message: error.respMessage });
          }
        });
      };

      this.forgetPasswdTyigger = !this.forgetPasswdTyigger;
    },
    // triggerAuthVerifyForPassword() {
    //   this.$appData.userInfo.mobile = null;
    //   this.triggerAuthVerify(this.resetPasswordSessionData, this.$urlConst.SMS_CAPTCHA_BF_LOGIN, this.$urlConst.GOOGLE_CAPTCHA_BF_LOGIN, this.resetPasswordSessionData.userName);
    // },
    triggerResetPassword() {
      this.resetPasswordNext = (form, success) => {
        this.$axiosUtil.post(this.$appConfig.SSO, this.$urlConst.UPDATE_USER, {
          userId: this.userId,
          userPwd: new Buffer(form.newPassword).toString('base64'),
          auth_token: this.resetPasswordSessionData.auth_token,
          userToken: this.resetPasswordSessionData.userToken,
        }).then((response) => {
          success();
          this.$message({ type: 'success', showClose: true, message: '登录密码修改成功' });
        }).catch((error) => {
          this.$message({ type: 'error', showClose: true, message: error.respMessage });
        });
      };
      this.resetPasswordTrigger = !this.resetPasswordTrigger;
    },
    showPwd() {
      if (this.pwdType === 'password') {
        this.pwdType = '';
      } else {
        this.pwdType = 'password';
      }
    },
    loginSession() {
      if (this.loading) {
        return;
      }
      this.loading = true;
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.LOGIN_AUTHORIZATION, {
        login_name: this.loginForm.username
      }).then((response) => {

        this.nextStep(this.loginSessionData, response.data);
      }).catch((error) => {
        this.loading = false;
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    loginVerify() {
      this.$refs.loginForm.validate((valid) => {
        if (!valid) {
          this.loading = false;
          return;
        }
        authAction.login(this.loginForm.username, this.loginForm.password, this.loginSessionData.auth_token).then((response) => {
          this.loading = false;

          if (!response.data.userInfo.mobile) {
            this.$message({ type: 'error', showClose: true, message: '暂未绑定手机号，请联系管理员' });
            return;
          }

          Object.assign(this.$appData.userInfo, response.data.userInfo);
          this.nextStep(this.loginSessionData, response.data);
        }).catch((error) => {
          this.loading = false;
          this.$message({
            type: 'error',
            message: error.respMessage,
          });
        });
      });
    },
    // triggerAuthVerifyForLogin() {
    //   this.triggerAuthVerify(this.loginSessionData, this.$urlConst.SMS_CAPTCHA_VERIFY, this.$urlConst.GOOGLE_CAPTCHA, this.loginSessionData.userName);
    // },

    triggerAuthVerify(sessionData, smsUrl, googleUrl, authName) {
      const header = {
        'access-token': sessionData.access_token,
      };
      this.authVerifyNext = (authForm, success) => {
        debugger
        if (authForm.verifyType === 'mobile') {
          this.$axiosUtil.post(this.$appConfig.API, smsUrl, {
            captcha: authForm.verifyCode,
            mobile: this.$appData.userInfo.mobile,
            auth_token: sessionData.auth_token,
            userToken: sessionData.userToken,
          }, header).then((response) => {
            success();
            this.nextStep(sessionData, response.data);
          }).catch((error) => {
            this.$message({ type: 'error', showClose: true, message: error.respMessage });
          });
        } else if (authForm.verifyType === 'dynamic') {
          this.$axiosUtil.post(this.$appConfig.API, googleUrl, {
            captcha: authForm.verifyCode,
            auth_token: sessionData.auth_token,
            userToken: sessionData.userToken,
          }, header).then((response) => {
            success();
            this.nextStep(sessionData, response.data);
          }).catch((error) => {
            this.$message({ type: 'error', showClose: true, message: error.respMessage });
          });
        }
      };
      this.authType = 'all';
      this.authName = authName;
      this.authVerifySessionId = sessionData.session_name + sessionData.auth_next_step;
      this.authVerifyTrigger = !this.authVerifyTrigger;
    },
    loginSuccess() {
      sessionUtil.clear();
      sessionUtil.set(sessionUtil.ACCESS_TOKEN, this.loginSessionData.access_token);
      sessionUtil.userSet('loginTime', new Date().getTime());
      if(this.$appData.dynamicCode === '0'){
        this.$router.replace('/home/operate/securityCenter');
      }else{
        this.$router.replace('/home');
      }
    },
    sendSms(mobile, smsSuccess) {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.SMS_CAPTCHA, {
        mobile,
        userToken: this.resetPasswordSessionData.userToken,
      }).then((response) => {
        smsSuccess();
      }).catch((error) => {
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    nextStep(sessionData, respData) {
      Object.assign(sessionData, {
        session_name: '',
        auth_token: '',
        auth_next_step: '',
      });
      Object.assign(sessionData, respData);
      let nextStep = sessionData.steps[sessionData.auth_next_step];
      if (!nextStep) {
        nextStep = sessionData.steps[''];
      }

      if (nextStep) {
        nextStep();
      }
    },
  },
};
</script>

<style lang="stylus">

  .login-container {
    position: fixed;
    height: 100%;
    width:100%;
    background-color: #2d3a4b;
    input:-webkit-autofill {
      -webkit-box-shadow: 0 0 0px 1000px #293444 inset !important;
      -webkit-text-fill-color: #fff !important;
    }
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: #eee;
      height: 47px;
    }
    .el-input {
      display: inline-block;
      height: 47px;
      width: 80%;
    }
    .sign-btn {
      width:100%;
    }
    .title {
      font-size: 26px;
      font-weight: 400;
      color: #eee;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
    .login-form {
      position: absolute;
      left: 0;
      right: 0;
      width: 350px;
      padding: 35px 35px 15px 35px;
      margin: 120px auto;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
  }
</style>
