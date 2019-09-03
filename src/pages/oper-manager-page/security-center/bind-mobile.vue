<template>
  <el-dialog top="150px"  :modal-append-to-body="false" :close-on-click-modal="false" width="400px" :visible.sync="dialogVisible">
    <div class="bind-mobile-panel">
      <span class="font-a">绑定手机</span><br/>
      <span class="font-b">请输入要绑定的新号码，点击获取验证码</span><br/>
      <el-form class="bind-mobile-form" ref="form" :model="form"  :rules="rules">
        <el-form-item prop="newMobile">
          <el-input clearable v-model="form.newMobile" placeholder="新手机号..."></el-input>
        </el-form-item>
        <el-form-item prop="verifyCode">
          <el-input clearable style="width: 200px" v-model="form.verifyCode" placeholder="验证码..."></el-input>
          <count-down-button :triggerStart="triggerStart" :triggerInit="triggerInit"
                             style="float: right; width: 100px" @click.native="sendSms"></count-down-button>
        </el-form-item>
        <el-button type="primary" class="bind-mobile-btn" @click="nextStep">
          <span class="font-c">绑定</span>
        </el-button>
      </el-form>
    </div>
  </el-dialog>

</template>

<script>
import countDownButton from '../../../components/count-down-button/index.vue';

export default {
  components: {
    countDownButton,
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        newMobile: '',
        verifyCode: '',
      },
      triggerStart: false,
      triggerInit: false,

      rules: {
        newMobile: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { trigger: 'blur', validator: this.validateMobile },
        ],
        verifyCode: [
          { trigger: 'blur', validator: this.validateVerifyCode },
        ],
      },

      onSendSms: false,
    };
  },
  watch: {
    trigger() {
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }

      this.dialogVisible = true;
    },
  },
  props: {
    trigger: {
      type: Boolean,
    },
  },
  computed: {

  },
  methods: {
    validateMobile(rule, value, callback) {
      const mobile = /^1[0-9]{10}$/;
      if (!mobile.test(value)) {
        callback(new Error('请输入正确的手机号码'));
      } else {
        callback();
      }
    },
    validateVerifyCode(rule, value, callback) {
      if (this.onSendSms) {
        callback();
      }

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
    },
    sendSms() {
      this.onSendSms = true;
      this.$refs.form.validate((valid) => {
        this.onSendSms = false;
        if (!valid) {
          return;
        }

        this.$emit('sendSms', this.form.newMobile, this.smsSuccess);
      });
    },
    smsSuccess() {
      this.triggerStart = !this.triggerStart;
    },
  },
};
</script>

<style scoped="">
  .bind-mobile-btn {
    margin-top: 20px;
    width: 100%;
  }
  .bind-mobile-panel {
    width: 90%;
    margin: 0 auto;
    text-align: left;
  }
  .bind-mobile-form {
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
