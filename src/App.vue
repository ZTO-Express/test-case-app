<template>
  <div id="app">
    <router-view/>
  </div>
</template>
<script>
export default {
  data() {
    return {

    };
  },
  mounted() {
    document.getElementById('app-title').innerHTML = this.$appConfig.appTitle;
  },
};
</script>
<style lang="stylus">
#app {
  font-family 'Avenir', Helvetica, Arial, sans-serif
  -webkit-font-smoothing antialiased
  -moz-osx-font-smoothing grayscale
  /*text-align center*/
  color #2c3e50
}

html, body, #app, #app .el-container {
  margin 0;
  height: 100%;
}

#nav {
  padding 30px
  a {
    font-weight bold
    color #2c3e50
    &.router-link-exact-active {
      color #42b983
    }
  }
}

</style>
