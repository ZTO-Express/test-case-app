<template>
  <div class="tips">
    <el-card class="tips-card">
      <h3>暂无访问权限</h3>
      <p>如需帮助，请联系您的管理员</p>
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
<style>
  .tips {
    background-color: #eee;
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
