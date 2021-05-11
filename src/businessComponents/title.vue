<template>
  <div class="left-title flex">
    <div class="left-border"></div>
    <div :style="`font-size: ${fsize}px`" class="right-content">{{ title }}</div>
    <div>
      <slot></slot>
    </div>
  </div>
</template>
<script>

export default {
  name: 'left-title',
  props: {
    title: {
      default: '', type: String
    },
    fsize: {
      default: 16, type: Number
    }
  }
}
</script>
<style lang="scss" scoped>
.left-title {
  height: 22px;
  line-height: 22px;
  .left-border {
    height: 16px;
    margin-top: 3px;
    border-left: 6px solid #409eff;
    border-radius: 3px;
    width: 0;
    margin-right: 8px;
  }
  .right-content {
    font-family: PingFangSC;
    font-weight: 550 !important;
    color: #4a4a4a;
    white-space: nowrap;
  }
}
</style>



