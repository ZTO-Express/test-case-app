<template>
  <el-dialog top="150px"  :modal-append-to-body="false" :close-on-click-modal="false" width="400px" :visible.sync="dialogVisible">
    <div class="qrcode-verify-code-panel">
      <span class="font-a">绑定口令码APP</span><br/>
      <span class="font-b">请在 身份宝APP 中，点击页面底部的扫描二维码按钮</span><br/>
      <span class="font-b">若已绑定过，请点击 +加号，选择 扫描二维码 菜单</span><br/>
      <div style="text-align: center; margin-top: 20px; margin-bottom: 10px">
        <qrcode-vue :foreground="qrcodeForeground" :size="qrcodeSize" :value="qrcodeText"></qrcode-vue >
      </div>

      <span class="font-c" style="display: block;text-align: center">请使用 身份宝APP 进行扫码</span>
      <el-button type="primary" class="qrcode-verify-code-btn"  @click="nextStep">
        <span class="font-c">我已看到验证码，下一步</span>
      </el-button>
      <a href="javascript:void(0)" v-show="false">返回上一步</a>
    </div>
  </el-dialog>

</template>

<script>
import QrcodeVue from 'qrcode.vue';

export default {
  components: {
    QrcodeVue,
  },
  data() {
    return {
      dialogVisible: false,
      qrcodeSize: 180,
      qrcodeForeground: '#3c46ff',
    };
  },
  watch: {
    trigger() {
      this.dialogVisible = true;
    },
  },
  props: {
    trigger: {
      type: Boolean,
    },
    qrcodeText: {
      type: String,
    },
  },
  mounted() {

  },
  computed: {

  },
  methods: {
    nextStep() {
      this.dialogVisible = false;
      this.$emit('next');
    },
  },
};
</script>

<style scoped="">
  .qrcode-img {
    display: block;
    margin:auto;
  }
  .qrcode-verify-code-btn {
    margin-top: 20px;
    margin-bottom: 10px;
    width: 100%;
  }
  .qrcode-verify-code-panel {
    width: 90%;
    margin: 0 auto;
    text-align: left;
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
