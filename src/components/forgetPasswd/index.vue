<template>
  <el-dialog top="30vh" :modal-append-to-body="false" :modal="true" :close-on-click-modal="false" width="400px"
             :visible.sync="dialogVisible">
    <div class="forget-passwd-panel">
      <span class="font-a">忘记密码</span><br/>
      <!--<span class="font-b">验证用户名</span><br/>-->
      <!--<span class="font-a">修改密码</span><br/>-->
      <el-form class="forget-passwd-form" ref="form" :model="form" :rules="rules">
        <el-form-item prop="userName">
          <el-input clearable type="text" v-model="form.userName" placeholder="用户名..."></el-input>
        </el-form-item>
        <!--<span class="font-b">6位以上长度，由字母、数字、符号组合而成</span><br/>-->
        <!--<el-form class="reset-password-form" ref="form" :model="form" :rules="rules">-->
        <!--<el-form-item prop="newPassword">-->
          <!--<el-input clearable type="password" v-model="form.newPassword" placeholder="输入新密码..."></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item prop="again">-->
          <!--<el-input clearable type="password" v-model="form.again" placeholder="再次输入..."></el-input>-->
        <!--</el-form-item>-->
        <!--<span class="font-b">6位以上长度，由字母、数字、符号组合而成</span><br/>-->
        <!--<el-form-item prop="picCode">-->
        <!--<el-input clearable type="text" v-model="form.picCode" placeholder="图形验证码..."></el-input>-->
        <!--<a href="javascript:void(0)">-->
        <!--<img :src="imgCaptChaStr" class="captchaImg" alt="看不清换一种" @click="imgCaptchaClicked"/>-->
        <!--</a>-->
        <!--</el-form-item>-->
        <el-button type="primary" class="forget-passwd-btn" @click="nextStep">
          <span class="font-c">下一步</span>
        </el-button>
      </el-form>
    </div>
  </el-dialog>

</template>

<script>

  export default {
    components: {},
    data() {
      return {
        dialogVisible: false,
        form: {
          userName: '',
          // picCode: '',
          newPassword: '',
          again: '',
        },
        rules: {
          userName: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {
              required: true, min: 4, max: 50, message: '长度必须4-50', trigger: 'blur',
            },
          ],
          // picCode: [
          //   { required: true, message: '请输入图形验证码', trigger: 'blur' },
          //   { trigger: 'blur', validator: this.validatePicCode },
          // ],
          newPassword: [
            {required: true, message: '请输入新密码', trigger: 'blur'},
            {trigger: 'blur', validator: this.validatePassword},
          ],
          again: [
            {required: true, message: '请再次输入新密码', trigger: 'blur'},
            {trigger: 'blur', validator: this.validateAgain},
          ],
        },
        imgCaptChaStr: '',
        captcha: '',
      };
    },
    created() {

    },
    watch: {
      trigger() {
        if (this.$refs.form) {
          this.$refs.form.resetFields();
        }

        this.dialogVisible = true;
        // this.imgCaptchaClicked();
      },
    },
    props: {
      trigger: {
        type: Boolean,
      },
    },
    computed: {},
    methods: {
      validatePassword(rule, value, callback) {
        const haveNum = /[0-9]+/;
        const haveLetter = /[a-zA-Z]+/;
        const haveChar = /[^a-zA-Z0-9]+/;
        if (value.length < 6) {
          callback(new Error('密码必须大于等于6位'));
        } else if (!(haveNum.test(value) && haveLetter.test(value) && haveChar.test(value))) {
          callback('密码必须包含字母、数字、符号');
        } else {
          callback();
        }
      },
      validateAgain(rule, value, callback) {
        if (value !== this.form.newPassword) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      // validatePicCode(rule, value, callback) {
      //   const captcha = this.captcha || '';
      //   const inputCaptcha = value || '';
      //   if (!captcha) {
      //     callback(new Error('请重新获取验证码'));
      //   } else if (inputCaptcha.toUpperCase() !== captcha.toUpperCase()) {
      //     callback(new Error('图形验证码错误'));
      //   } else {
      //     callback();
      //   }
      // },
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
      imgCaptchaClicked() {
        this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.CREATE_CAPTCHA)
          .then((response) => {
            this.imgCaptChaStr = this.$appConfig.baseUrl + response.data.img_url;
            this.captcha = response.data.captcha;
          }).catch((error) => {
          this.$message({type: 'error', showClose: true, message: error.respMessage});
        });
      },
    },
  };
</script>

<style scoped="">
  .forget-passwd-btn {
    margin-top: 20px;
    width: 100%;
  }

  .forget-passwd-panel {
    width: 90%;
    margin: 0 auto;
    text-align: left;
  }

  .forget-passwd-form {
    margin-top: 20px;
  }

  .font-a {
    font-size: 16px;
    font-weight: bold;
  }

  .captchaImg {
    width: 90px;
    height: 34px;
    position: absolute;
    right: 2px;
    bottom: 2px;
    margin-right: 4px;
    font-size: 12px;
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
