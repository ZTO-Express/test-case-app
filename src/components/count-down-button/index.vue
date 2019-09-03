<template>
  <el-button id="sendCaptcha" :type="buttonType" :disabled="buttonDisabled" size="large" ref="sendCaptchaBtn">{{ time | change }}</el-button>
</template>

<script>

export default {
  data() {
    return {
      time: '获取验证码',
      intervalId: '',
      buttonDisabled: false,
    };
  },
  props: {
    triggerStart: {
      type: Boolean,
    },
    triggerInit: {
      type: Boolean,
    },
    countDownDuration: {
      type: Number,
      default: 60,
    },
    buttonType: {
      type: String,
    },
  },
  watch: {
    triggerStart() {
      this.countDown();
      this.buttonDisabled = true;
    },
    triggerInit() {
      clearInterval(this.intervalId);
      this.time = '获取验证码';
      this.buttonDisabled = false;
    },
  },
  methods: {
    countDown() {
      this.time = this.countDownDuration;
      this.intervalId = setInterval(() => {
        this.time--;
        if (this.time === 0) {
          this.buttonDisabled = false;
          this.time = '重新获取';
          clearInterval(this.intervalId);
        }
      }, 1000);
    },
  },
  filters: {
    change(value) {
      if (!value) return '';
      if (!isNaN(value)) {
        return value + 'S';
      }
      return value;
    },
  },
  beforeDestroy() {
    clearInterval(this.intervalId);
  },
};
</script>
<style scoped>

</style>
