<template>
  <el-dialog
    class="auth-verify-dialog"
    :modal-append-to-body="true"
    :append-to-body="true"
    @close="handleDialogClose" :modal="modal" :close-on-click-modal="false" width="400px" :visible.sync="dialogVisible" v-if="dialogVisible">
    <div class="auth-verify-panel">
      <span class="font-a">{{title || '身份验证'}}</span><br/>
      <span class="font-b">为保护您的账号安全，请输入验证码</span><br/>
      <span class="font-b">若未收到，可点击重新获取，或更换验证方式</span><br/>
      <el-form class="auth-verify-form" ref="form" :model="form" :rules="rules" @submit.native.prevent>
        <el-form-item prop="verifyType">
          <el-radio-group v-model="form.verifyType">
            <el-radio v-if="enableAuthType === 'all' || enableAuthType === 'dynamic' " label="dynamic">动态口令验证</el-radio>
            <el-radio v-if="enableAuthType === 'all' || enableAuthType === 'mobile' " label="mobile">手机验证</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="verifyCode">
          <el-input clearable v-if="form.verifyType === 'mobile'" style="width: 200px" v-model="form.verifyCode" placeholder="输入手机验证码..." @keyup.enter.native="nextStep"></el-input>
          <el-input clearable v-if="form.verifyType === 'dynamic'" v-model="form.verifyCode" placeholder="输入6位动态口令码..." @keyup.enter.native="nextStep"></el-input>
          <count-down-button v-show="form.verifyType === 'mobile'" :triggerStart="triggerStart" :triggerInit="triggerInit"
                             style="float: right; width: 100px" @click.native="sendSms"></count-down-button>
        </el-form-item>

        <el-button type="primary" class="auth-verify-btn" @click="nextStep">
          <span class="font-c">{{btnValue || '继续'}}</span>
        </el-button>
      </el-form>
    </div>
  </el-dialog>

</template>

<script>
import countDownButton from '../count-down-button/index.vue';

export default {
  components: {
    countDownButton,
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        verifyType: '',
        verifyCode: '',
      },
      triggerStart: false,
      triggerInit: false,

      enableAuthType: '',

      rules: {
        verifyCode: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { trigger: 'blur', validator: this.validateVerifyCode },
        ],
      },
    };
  },
  watch: {
    trigger() {
      this.enableAuthType = this.authType || 'all';

      if (this.authType === 'all') {
        this.isDynamicCodeConfigured();
      } else {
        this.dialogVisible = true;
      }
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
    },
    enableAuthType(val) {
      this.form.verifyType = val === 'all' ? 'dynamic' : val;
    },
    sessionId() {
      this.triggerInit = !this.triggerInit;
    },
  },
  props: {
    trigger: {
      type: Boolean,
    },

    btnValue: {
      type: String,
    },
    title: {
      type: String,
    },
    authType: {
      type: String,
    },
    authName: {
      type: String,
    },
    modal: {
      type: Boolean,
      default: true,
    },
    sessionId: {
      type: String,
    },
  },
  computed: {

  },
  methods: {
    handleDialogClose() {
      this.form.verifyCode = '';
    },
    isDynamicCodeConfigured() {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.IS_DYNAMIC_CODE_CONFIGURED, {
        user_name: this.authName || this.$appData.userInfo.userName,
      }).then((response) => {
        this.dialogVisible = true;
        this.$appData.dynamicCode = response.data;
        if (response.data === '0') {
          this.enableAuthType = 'mobile';
        }
      }).catch((error) => {
        this.$message({ type: 'error', showClose: true, message: error.respMessage });
      });
    },
    validateVerifyCode(rule, value, callback) {
      const sixNum = /[0-9]{6}/;
      if (!sixNum.test(value)) {
        callback(new Error('请输入6位纯数字验证码'));
      } else {
        callback();
      }
    },
    nextStep() {
      this.$refs.form.validate((valid) => {
        if (!valid) {
          return;
        }
        this.$emit('next', this.form, this.success);
      });
    },
    success() {
      this.dialogVisible = false;
      this.form.verifyCode = '';
    },
    sendSms() {
      this.$emit('sendSms', this.$appData.userInfo.mobile, this.smsSuccess);
    },
    smsSuccess() {
      this.triggerStart = !this.triggerStart;
    },
  },
};
</script>

<style scoped="">
  .auth-verify-btn {
    margin-top: 20px;
    width: 100%;
  }
  .auth-verify-panel {
    width: 90%;
    margin: 0 auto;
    text-align: left;
  }
  .auth-verify-form {
    margin-top: 20px;
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
</style>
