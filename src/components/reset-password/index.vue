<template>
  <el-dialog top="150px"  :modal-append-to-body="false"  :modal="modal" :close-on-click-modal="false" width="400px" :visible.sync="dialogVisible">
    <div class="reset-password-panel">
      <span class="font-a">修改密码</span><br/>
      <span class="font-b">6位以上长度，由字母、数字、符号组合而成</span><br/>
      <el-form class="reset-password-form" ref="form" :model="form" :rules="rules">
        <el-form-item v-if="needOldPwd" prop="originPassword">
          <el-input clearable type="password" v-model="form.originPassword" placeholder="原密码..."></el-input>
        </el-form-item>
        <el-form-item prop="newPassword">
          <el-input clearable type="password" v-model="form.newPassword" placeholder="输入新密码..."></el-input>
        </el-form-item>
        <el-form-item prop="again">
          <el-input clearable type="password" v-model="form.again" placeholder="再次输入..."></el-input>
        </el-form-item>
        <el-button type="primary" class="reset-password-btn" @click="nextStep">
          <span class="font-c">确定修改</span>
        </el-button>
      </el-form>
    </div>
  </el-dialog>

</template>

<script>

export default {
  components: {

  },
  data() {
    return {
      dialogVisible: false,
      form: {
        originPassword: '',
        newPassword: '',
        again: '',
      },
      rules: {
        originPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { trigger: 'blur', validator: this.validatePassword },
        ],
        again: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { trigger: 'blur', validator: this.validateAgain },
        ],
      },
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
    modal: {
      type: Boolean,
      default: true,
    },
    needOldPwd: {
      type: Boolean,
      default: true,
    },
  },
  computed: {

  },
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
  },
};
</script>

<style scoped="">
  .reset-password-btn {
    margin-top: 20px;
    width: 100%;
  }
  .reset-password-panel {
    width: 90%;
    margin: 0 auto;
    text-align: left;
  }
  .reset-password-form {
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
