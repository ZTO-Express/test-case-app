<template>
  <el-breadcrumb class="breadcuumb">
    <el-breadcrumb-item v-for="(item,index) in titles" v-if="!item.ignoreNav" :key="index">
      <a @click="goPath(item.path, item)" class="title">{{item.title}}</a>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>
<script>
export default {
  props: {
    titles: {
      type: Array,
      required: true
    }
  },
  methods: {
    goPath(path, item) {
      this.$emit('goPath', path)
      this.$emit('goToPath', item)
    }
  }
}
</script>
<style lang="scss" scoped>
.breadcuumb {
  line-height: 20px;
  .title {
    color: #a6a6a6;
    font-weight: normal;
    display: inline-block;
    padding: 1px 0px;
    &:hover {
      color: var(--primaryBlueColor);
    }
  }
}
</style>


