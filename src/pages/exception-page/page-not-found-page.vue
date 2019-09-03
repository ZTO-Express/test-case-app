<template>
  <div class="tips">
    <el-card class="tips-card">
      <h3>未找到页面 ：(</h3>
      <p>您访问的页面不存在，请检查链接是否正确，然后再试。</p>
      <br/>
      <br/>
      <el-button size="small" @click='$router.push({path:"/home"})'>返回首页</el-button>
    </el-card>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  created() {

  },
  components: {},
  computed: {},
  methods: {},
};
</script>
<style scoped>
  .tips {
    height: 100%;
    width: 100%;
    overflow: auto;
  }

  .tips-card {
    width: 100%;
    max-width: 800px;
    height: 200px;
    margin: 8% auto;
    text-align: center
  }

</style>
