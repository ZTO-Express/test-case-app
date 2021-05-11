<template>
  <div class="title-common">
    <div class="title">
      <span :class="[titleSize]" v-text="title" />
      <span class="title-sub" v-if="subTitle" v-text="subTitle" />
      <slot name="hover-icon"></slot>
    </div>
    <slot></slot>
  </div>
</template>
<script>
export default {
  props: {
    size: {
      type: String,
      default: 'normal'
    },
    // 标题
    title: {
      type: String,
      required: true
    },
    // 小标题说明
    subTitle: {
      type: String,
      default: ''
    }
  },
  computed: {
    titleSize() {
      return `title-${this.size}`
    }
  }
}
</script>
<style lang="scss" scoped>
.title-common {
  display: flex;
  align-items: center;
  justify-content: space-between;
  .title {
    display: flex;
    align-items: center;
  }
  .title-sub {
    font-size: var(--h6);
    color: #363636;
    padding-left: 19px;
  }
  .title > span:first-child {
    color: #000;
    border-left: 4px solid var(--primaryBlueColor);
    padding-left: 9px;
  }
  .title-big {
    display: inline-block;
    font-size: 18px;
    line-height: 18px;
    font-weight: 700;
  }
  .title-normal {
    font-size: var(--h3);
    line-height: var(--h3);
  }
  .title-small {
    font-size: var(--h3);
    line-height: var(--h3);
    font-weight: 550;
  }
  .title-mini {
    font-size: var(--h4);
    line-height: var(--h4);
    font-weight: 550;
  }
  .title-exmini {
    font-size: var(--h6);
    line-height: var(--h6);
    font-weight: 550;
  }
}
</style>


