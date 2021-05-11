<template>
  <el-button
    :style="{width:width}"
    :size="size"
    :type="type"
    @click="btnClick"
    v-if="visible"
    :disabled="disabled"
    :class="{disabled:disabled}"
    :icon="icon"
  >{{title}}</el-button>
</template>
<script>
export default {
  name: 'customBtn',
  props: {
    size: {
      default: 'small', type: String
    },
    type: {
      default: 'primary', type: String
    },
    title: {
      default: '确定', type: String
    },
    visible: {
      default: true, type: Boolean
    },
    disabled: {
      default: false, type: Boolean
    },
    width: {
      default: '80px', type: String
    },
    icon: {
      default: '', type: String
    }
  },
  methods: {
    btnClick() {
      this.$emit('btnClick')
    }
  }
}
</script>
<style lang="scss" scoped>
.el-button {
  height: 32px;
  padding: 8px 10px;
  border-radius: 2px;
  font-size: 12px;
  // background-color: #1AB394;
}
</style>



