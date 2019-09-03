<template>
  <el-dialog top="150px" :modal-append-to-body="false"  :close-on-click-modal="false" width="400px" :visible.sync="dialogVisible">
    <div class="qrcode-app-panel">
      <span class="font-a">安装口令码APP</span><br/>
      <span class="font-b">请扫描二维码下载并安装阿里云【身份宝】手机应用</span><br/>
      <span class="font-b">若无法扫码，可通过 http://otp.aliyun.com/m/ 下载</span><br/>
      <img class="qrcode-img" v-bind:src="$appConst.shenFenBaoQrcodeUrl"/>
      <span class="font-c" style="display: block;text-align: center">扫一扫，下载身份宝APP</span>
      <el-button type="primary" class="qrcode-app-btn" @click="nextStep">
        <span class="font-c">我安装好了，下一步</span>
      </el-button>
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
  .qrcode-app-btn {
    margin-top: 20px;
    width: 100%;
  }
  .qrcode-app-panel {
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
