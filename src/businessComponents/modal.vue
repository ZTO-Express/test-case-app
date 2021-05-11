<template>
  <el-dialog :title="title" :visible="visible" :custom-class="dialogHeader" :width="width" :show-close="showClose" :close-on-click-modal="stopClose" :top="top" @close="cancle" :append-to-body="appendTobody" :before-close="beforeClose">
    <slot></slot>
    <span slot="footer" v-if="showConfirm || showCancle || showReset">
      <el-button @click="confirm" :disabled="confirmDisabled" v-text="confirmTitle" v-if="showConfirm" type="primary" size="small" style="width:80px;font-size:12px;" />
      <el-button @click="cancle" v-text="cancleTitle" v-if="showCancle" size="small" style="width:80px;font-size:12px;" />
      <el-button @click="reset" v-text="resetTitle" v-if="showReset" size="small" style="width:80px;font-size:12px;" />
    </span>
  </el-dialog>
</template>
<script>
import dragDialog from '@/components/dragDialog'
export default {
  props: {
    // 模态框标题
    title: {
      default: '温馨提示',
      type: String
    },
    // 控制模态框是否展示
    visible: {
      default: false,
      type: Boolean
    },
    // 取消按钮标题
    cancleTitle: {
      default: '取消',
      type: String
    },
    // 重置按钮
    resetTitle: {
      default: '重置',
      type: String
    },
    // 是否展示取消按钮
    showCancle: {
      default: true,
      type: Boolean
    },
    // 是否展示确认按钮
    showConfirm: {
      default: true,
      type: Boolean
    },
    // 是否显示重置按钮
    showReset: {
      default: false,
      type: Boolean
    },
    // 重置表单的ref
    resetRef: {
      default: '',
      type: String
    },
    // 确认按钮标题
    confirmTitle: {
      default: '确定',
      type: String
    },
    // 确认按钮是否可点击
    confirmDisabled: {
      default: false,
      type: Boolean
    },
    // 模态框宽度
    width: {
      default: '650px',
      type: String
    },
    // 距离top距离
    top: {
      default: '86px',
      type: String
    },
    // 模态框自定义样式
    dialogHeader: {
      default: 'dialog-header',
      type: String
    },
    // 嵌套 Dialog
    appendTobody: {
      default: false,
      type: Boolean
    },
    // 异步点击事件
    confirmAsyncEvent: {
      type: Function,
      default: null
    },
    beforeClose: {
      type: Function,
      default: null
    },
    showClose: { // 是否显示关闭按钮
      default: true,
      type: Boolean
    },
    // 关闭前回调
    stopClose: {
      default: false,
      type: Boolean
    },
    // 激活取消事件
    isActiveCance: {
      default: false,
      type: Boolean
    }
  },
  directives: { dragDialog },
  methods: {
    confirm() {
      this.$emit('confirm')
    },
    reset() {
      this.$emit('reset')
    },
    cancle() {
      this.$emit('update:visible', false)
      if (this.isActiveCance) {
        this.$emit('isActiveCance')
      }
    }
  }
}

</script>
<style lang="scss">
.dialog-header {
  border-radius: 0px;

  .el-dialog__header {
    padding: 13px 20px 3px;
    border-bottom: 1px solid #e8e8e8;

    .el-dialog__title {
      display: inline-block;
      line-height: 28px;
      font-weight: bold;
      font-size: 18px;
      height: 28px;
      padding-left: 10px;
    }

    .el-dialog__headerbtn:hover .el-dialog__close {
      color: #1890ff;
    }

    .el-icon-close:before {
      /*content: "\E60F";*/
    }

    .el-dialog__headerbtn {
      top: 16px;
      right: 20px;
    }
  }

  .el-dialog__body {
    padding: 0 40px 30px;
  }

  .el-dialog__footer {
    padding: 0 40px 30px;
  }
}

</style>
