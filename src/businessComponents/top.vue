<template>
  <div class="main">
    <div class="main-top">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item
          :to="{ path: item.path }"
          v-for="item in breads"
          :key="item.title"
        >{{ item.title }}</el-breadcrumb-item>
      </el-breadcrumb>
      <slot></slot>
    </div>
  </div>
</template>
<script>
import breadcrumb from './breadcrumb'
import comTitle from './comTitle'
export default {
  name: 'top',
  props: {
    breads: {
      default: [],
      type: Array
    },
    title: {
      default: '',
      type: String
    },
    subTitle: {
      default: '',
      type: String
    }
  },
  components: {
    breadcrumb, comTitle
  }
}
</script>
<style scoped lang="scss">
.main {
  padding: 20px 30px 20px;
  background: var(--whiteBgColor);
  .main-top {
    height: 20px;
    display: flex;
    justify-content: space-between;
    .el-breadcrumb__item {
      cursor: pointer;
    }
  }
}
</style>

