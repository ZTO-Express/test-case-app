<template>
  <el-container>
    <sidebar :menus="menus" base="/home"></sidebar>
    <el-container>
      <el-header style="height: auto">
        <navbar @logout="logout"></navbar>
        <tags-view></tags-view>
      </el-header>
      <el-main id="body-container-id"  class="body-container">
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import sidebar from '../components/sidebar/index.vue';
import navbar from '../components/navbar/index.vue';
import tagsView from '../components/tags-view/index.vue';
import sessionUtil from '../utils/sessionUtil';

export default {
  data() {
    return {
      menus: this.$appData.menus,
    };
  },
  components: {
    sidebar, navbar, tagsView,
  },
  computed: {

  },
  mounted() {

  },
  methods: {
    logout() {
      // 请求SSO清除缓存
      this.$action.authAction.logout(this.$appData.userInfo.userId)
        .then((response) => {
          sessionUtil.rm(sessionUtil.ACCESS_TOKEN);
          this.$router.replace('/');
        }).catch((error) => {
          this.$message({ type: 'error', showClose: true, message: error.respMessage });
        });
    },
  },
};
</script>

<style lang="stylus" scoped="">
  .el-header{
    background-color: white;
    padding-left 0px;
    min-height 80px
    overflow auto
  }

  .body-container {
    background: #e3e3e3;
    overflow auto
    min-width 800px
  }
</style>
