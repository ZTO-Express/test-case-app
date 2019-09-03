<template>
  <el-card class="securty-center-card">
    <reset-password :trigger="resetPasswordTrigger" @next="resetPasswordNext"></reset-password>
    <auth-verify :sessionId="authVerifySessionId" @sendSms="sendSms" :title="authTitle" :btnValue="authVerifyBtnValue"
                 :authType="authType" @next="authVerifyNext" :trigger="authVerifyTrigger" ></auth-verify>
    <bind-mobile @sendSms="sendSms"  :trigger="bindMobileTrigger" @next="bindMobileNext"></bind-mobile>
    <qrcode-app :trigger="qrcodeAppTrigger" @next="qrcodeAppNext"></qrcode-app>
    <qrcode-verify-code :qrcodeText="qrcodeText" :trigger="qrcodeVerifyCodeTrigger" @next="qrcodeVerifyCodeNext"></qrcode-verify-code>

    <span class="font-a">{{
        '安全状态良好，建议定期修改密码，保护账号及资金安全。'
      }}</span>
    <div class="securty-center-item">
      <div class="securty-center-item-status">
        <i class="icon-complete-r"></i>
      </div>
      <div class="securty-center-item-content">
        <div>
          <span class="font-a">登录密码</span><br/>
          <span class="font-b">用于账号登录的密码，6位以上，由字母、数字、符号组合而成。</span><br/>
        </div>
      </div>
      <el-button type="primary" class="securty-center-item-btn" @click="triggerResetPassword">
        <span class="font-c">修改</span>
      </el-button>
    </div>
    <!--<div class="securty-center-item">-->
      <!--<div class="securty-center-item-status">-->
        <!--<i class="icon-complete-r"></i>-->
      <!--</div>-->
      <!--<div class="securty-center-item-content">-->
        <!--<div>-->
          <!--<span class="font-a">手机验证码：已绑定手机 {{$formatters.stringfyPhoneNum($appData.userInfo.mobile)}}</span><br/>-->
          <!--<span class="font-b">短信验证码是最常见的身份验证方式，</span><br/>-->
          <!--<span class="font-b">同时，绑定的手机号也用于接收账户及资金变动时的信息通知</span><br/>-->
        <!--</div>-->
      <!--</div>-->
      <!--<el-button type="primary" class="securty-center-item-btn" @click="bindMobileSession">-->
        <!--<span class="font-c">更换手机</span>-->
      <!--</el-button>-->
    <!--</div>-->
    <!--<div :class="{mask:unDynamicCode}" id="dynamicPos">-->
      <!--<div class="mask-layer"></div>-->
      <!--<div class="mask-close" @click="maskClose">×</div>-->
      <!--<div class="securty-center-item" :class="{light:unDynamicCode}">-->
        <!--<div class="mask-guide"><p>亲爱的用户，为避免短信验证码接收延迟或超限，我们建议您绑定更加高效安全的“口令码”，并且将其作为默认的身份验证方式。</p></div>-->
        <!--<div class="securty-center-item-status">-->
          <!--<i :class="bindDynamicStatus ? 'icon-complete-r' : 'icon-exclamatory-mark-r'"></i>-->
        <!--</div>-->
        <!--<div class="securty-center-item-content">-->
          <!--<div>-->
            <!--<span class="font-a">动态口令码：{{bindDynamicStatus ? '已绑定' : '未绑定'}}</span><br/>-->
            <!--<span class="font-b">基于TOTP的动态密码验证方式，兼容 Google Authenticator应用，安全且无需联网。</span><br/>-->
            <!--<span class="font-b">尤其适用于“无法绑定手机”、“无法接收验证码”的用户。</span><br/>-->
            <!--<a style="font-size: 14px;" target="_blank" href="auth/auth.html">了解使用方法?</a>-->
          <!--</div>-->
        <!--</div>-->
        <!--<el-button :type="bindDynamicStatus ? 'primary':'danger'" class="securty-center-item-btn" @click="bindDynamicSession">-->
          <!--<span class="font-c">{{bindDynamicStatus ? '重新绑定' : '立即绑定'}}</span>-->
        <!--</el-button>-->
      <!--</div>-->
    <!--</div>-->
  </el-card>

</template>

<script>
import resetPassword from '../../../components/reset-password/index.vue';
import authVerify from '../../../components/auth-verify/index.vue';
import bindMobile from './bind-mobile.vue';
import qrcodeApp from './qrcode-app.vue';
import qrcodeVerifyCode from './qrcode-verify-code.vue';

export default {
  components: {
    resetPassword,
    authVerify,
    bindMobile,
    qrcodeApp,
    qrcodeVerifyCode,
  },
  data() {
    return {
      resetPasswordTrigger: false,
      authVerifyTrigger: false,
      bindMobileTrigger: false,
      qrcodeAppTrigger: false,
      qrcodeVerifyCodeTrigger: false,
      unDynamicCode: false,

      authType: '',
      authVerifyBtnValue: '',
      authTitle: '',
      authVerifySessionId: '',

      resetPasswordNext() {},
      authVerifyNext() {},
      qrcodeAppNext() {},
      qrcodeVerifyCodeNext() {},
      bindMobileNext() {},

      bindMobileSessionData: {
        steps: {
          auth_verify: this.triggerAuthVerifyFormBindMobile,
          '': this.triggerBindMobile,
        },
      },
      bindDynamicSessionData: {
        steps: {
          auth_verify: this.triggerAuthVerifyFormBindGoogle,
          google_qr_gen: this.triggerGoogleQrcode,
          '': this.triggerAuthVerifyUseDynamic,
        },
      },
      useInfo: this.$appData.userInfo,
      bindDynamicStatus: false,
      qrcodeText: '',
    };
  },
  computed: {

  },
  mounted() {
    if (window.screen.width < 768) {
      if (!this.$sessionUtil.get("security-center-refreash")) {
        this.$sessionUtil.set("security-center-refreash", "true");
        window.location.reload();
      }
    }

    // this.isDynamicCodeConfigured();
    this.dynamicCodeGuide(); // 登录页进来判断是否绑定过
  },
  methods: {
    bindMobileSession() {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.BIND_MOBILE_AUTHORIZATION).then((response) => {
        this.nextStep(this.bindMobileSessionData, response.data);
      }).catch((error) => {
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    triggerAuthVerifyFormBindMobile() {
      this.triggerAuthVerify(this.bindMobileSessionData);
    },
    triggerAuthVerifyFormBindGoogle() {
      this.triggerAuthVerify(this.bindDynamicSessionData);
    },
    triggerAuthVerify(sessionData) {
      this.authVerifyNext = (authForm, success) => {
        if (authForm.verifyType === 'mobile') {
          this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.SMS_CAPTCHA_VERIFY, {
            captcha: authForm.verifyCode,
            mobile: this.$appData.userInfo.mobile,
            auth_token: sessionData.auth_token,
          }).then((response) => {
            success();
            this.nextStep(sessionData, response.data);
          }).catch((error) => {
            this.$message({ type: 'error', showClose: true, message: error.respMessage });
          });
        } else if (authForm.verifyType === 'dynamic') {
          this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GOOGLE_CAPTCHA, {
            captcha: authForm.verifyCode,
            auth_token: sessionData.auth_token,
          }).then((response) => {
            success();
            this.nextStep(sessionData, response.data);
          }).catch((error) => {
            this.$message({ type: 'error', showClose: true, message: error.respMessage });
          });
        }
      };
      this.authType = 'all';
      this.authVerifySessionId = sessionData.session_name + sessionData.auth_next_step;
      this.authVerifyTrigger = !this.authVerifyTrigger;
    },
    triggerBindMobile() {
      this.bindMobileNext = this.bindMobile;
      this.bindMobileTrigger = !this.bindMobileTrigger;
    },
    bindMobile(form, success) {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.BIND_MOBILE, {
        auth_token: this.bindMobileSessionData.auth_token,
        mobile: form.newMobile,
        captcha: form.verifyCode,
      }).then((response) => {
        this.$set(this.$appData.userInfo, 'mobile', form.newMobile);
        success();
        this.$message({ type: 'success', showClose: true, message: '新号码更换成功' });
      }).catch((error) => {
        if (error.respCode === 'F40109') {
          this.$message({ type: 'error', showClose: true, message: '该号码已绑定其他账号，请解绑后再试' });
        } else {
          this.$message({ type: 'error', showClose: true, message: error.respMessage });
        }
      });
    },
    bindDynamicSession() {
      this.unDynamicCode = false;
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GOOGLE_AUTH_BIND_AUTHORIZATION).then((response) => {
        this.nextStep(this.bindDynamicSessionData, response.data);
      }).catch((error) => {
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    triggerGoogleQrcode() {
      this.qrcodeAppNext = () => {
        this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GOOGLE_AUTH_QRCODE, {
          user_name: this.$appData.userInfo.email,
          user_id: this.$appData.userInfo.userId,
          auth_token: this.bindDynamicSessionData.auth_token,
        }).then((response) => {
          this.qrcodeText = response.data.optauthUrl;
          this.qrcodeVerifyCodeNext = () => {
            this.nextStep(this.bindDynamicSessionData, response.data);
          };
          this.qrcodeVerifyCodeTrigger = !this.qrcodeVerifyCodeTrigger;
        }).catch((error) => {
          this.$message({ type: 'error', showClose: true, message: error.respMessage });
        });
      };
      this.qrcodeAppTrigger = !this.qrcodeAppTrigger;
    },
    triggerAuthVerifyUseDynamic() {
      this.authType = 'dynamic';
      this.authVerifyBtnValue = '立即验证';
      this.authTitle = '验证动态口令码';
      this.authVerifyNext = (authForm, success) => {
        this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GOOGLE_AUTH_BIND, {
          captcha: authForm.verifyCode,
          auth_token: this.bindDynamicSessionData.auth_token,
          word_token: this.bindDynamicSessionData.wordToken,
        }).then((response) => {
          this.isDynamicCodeConfigured();
          success();
          
          this.$appData.dynamicCode = '1';
          this.$message({ type: 'success', showClose: true, message: '动态口令码绑定成功' });
        }).catch((error) => {
          this.$message({ type: 'error', showClose: true, message: error.respMessage });
        });
      };
      this.authVerifySessionId = this.bindDynamicSessionData.session_name + this.bindDynamicSessionData.auth_next_step;
      this.authVerifyTrigger = !this.authVerifyTrigger;
    },

    resetPassword(form, success) {
      this.$action.authAction.updateUserPwd(form.originPassword, form.newPassword).then((response) => {
        success();
        this.$message({ type: 'success', showClose: true, message: '登录密码修改成功' });
      }).catch((error) => {
        if (error.respCode === 'F40307') {
          this.$message({ type: 'error', showClose: true, message: '原密码错误' });
        } else {
          this.$message({ type: 'error', showClose: true, message: error.respMessage });
        }
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
    isDynamicCodeConfigured() {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.IS_DYNAMIC_CODE_CONFIGURED, {
        user_name: this.$appData.userInfo.userName,
      }).then((response) => {
        this.dialogVisible = true;

        if (response.data === '1') {
          this.bindDynamicStatus = true;
        }
      }).catch((error) => {
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    sendSms(mobile, smsSuccess) {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.SMS_CAPTCHA, {
        mobile,
      }).then((response) => {
        smsSuccess();
      }).catch((error) => {
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    triggerResetPassword() {
      this.resetPasswordNext = this.resetPassword;

      this.resetPasswordTrigger = !this.resetPasswordTrigger;
    },
    goAnchor(selector) {
      this.$el.querySelector(selector).scrollIntoView()
    },
    dynamicCodeGuide() {
      if(this.$appData.dynamicCode === '0'){
        this.unDynamicCode = true;
        this.goAnchor('#dynamicPos');
      }
    },
    maskClose() {
      this.unDynamicCode = false;
    }
  },
};
</script>

<style scoped="">
  .securty-center-item-btn {
    float: right;
    margin-top: 40px;
    margin-right: 40px;
    width: 100px;
  }
  .securty-center-item-status {
    float: left;
    width: 100px;
    text-align: center;
    margin-top: 40px;
  }
  .securty-center-item-content {
    float: left;
    display: flex;
    height: 100%;
    align-items: center;
  }
  .securty-center-item-status .icon-complete-r {
    font-size: 30px;
    color: #1afa29;
  }
  .securty-center-item-status  .icon-exclamatory-mark-r {
    font-size: 30px;
    color: #d81e06;
  }
  .font-a {
    font-size: 16px;
    font-weight:bold;
  }
  .font-b {
    margin-top: 3px;
    font-size: 14px;
    color: #858384;
  }
  .font-c {
    font-size: 14px;
  }
  .securty-center-card {
    text-align: center;
    padding-top: 30px;
    padding-bottom: 100px;
    overflow: auto;
  }
  .securty-center-item {
    text-align: left;
    width: 900px;
    height: 120px;
    background-color: rgba(248, 249, 252, 1);
    margin:50px auto;
  }
  /* 遮罩样式 */
  .securty-center-card .mask-layer{
    display: none;
    position: fixed;
    height: 100%;
    width: 100%;
    background: rgba(0,0,0,.7);
    top: 0;
    left: 0;
  }
  .securty-center-card .light{
    position: relative; /*这里不事先隐藏加载，影响页面响应*/
    background-color: #fff !important;
  }
  .securty-center-card .mask-close{
    display: none;
    position: absolute;
    color: #fff;
    top: 0;
    right: 20px;
    font-size: 40px;
    cursor: pointer;
  }
  .securty-center-card .mask-guide{
    display: none;
    position: absolute;
    width: 140px;
    height: 140px;
    top: -150px;
    right: 100px;
    background-image: url('../../../../public/img/guide_arrow.png');
    background-repeat: no-repeat;
    background-size: 100%;
  }
  .securty-center-card .mask-guide p {
    font-size: 14px;
    font-weight: bold;
    color: #fff;
    width: 320px;
    position: relative; 
    left: -340px;
    bottom: 30px;
  }
  @media screen and (max-width: 768px) {
    .securty-center-card .mask button{
      float: none !important;
      margin: 0 0 10px 45px;
    }
  }
  .securty-center-card .mask .mask-layer,
  .securty-center-card .mask .mask-close,
  .securty-center-card .mask-guide{
    display: block;
  }
</style>
