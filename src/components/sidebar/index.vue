<template>
  <el-scrollbar :class="sidebar.isCollapse ? 'sidebar-menu-container' : 'sidebar-menu-container sidebar-menu-container-collapse'">
    <el-menu
      ref="sideMenu"
      @open="handleOpen"
      @close="handleClose"
      @select="handleSelect"
      :background-color="sidebar.isCollapse ? '#304156':''"
      text-color="#fff"
      active-text-color="#409EFF"
      :collapse="sidebar.isCollapse"
      class="el-menu-vertical"
      :default-active="$route.path"
      :unique-opened="true"
      :collapse-transition="true">
      <el-menu-item class="logo-item level_1"  :index="$appConfig.logoRoute" >
        <div class="mobile-collapse-switch logo-switch">
          <i class="icon-view-list" @click="collapseSwitchMobile"></i>
        </div>

        <img v-if="$appConfig.logoUrl" class="logoImg" v-bind:src="$appConfig.logoUrl"/>
        <i class="icon-home logo-icon logo-name"></i>
        <span slot="title" class="menuLabel logo-name">
          {{$appConfig.logoName}}
        </span>
      </el-menu-item>
      <sidebar-item level="0" :menus="menus" :base="base" :isFirstLevel="true"></sidebar-item>
    </el-menu>
  </el-scrollbar>

</template>

<script>
import sidebarItem from './sidebar-item.vue';

export default {
  props: {
    menus: {
      type: Array,
    },
    base: {
      type: String,
    },
  },
  data() {
    return {
      sidebar: this.$appData.sidebar,
    };
  },
  mounted() {

  },
  components: {
    sidebarItem,
  },
  computed: {

  },
  watch: {

  },
  methods: {
    collapseSwitchMobile(){
      let item = document.getElementsByClassName("sidebar-menu-container-collapse")[0];
      let left = item.offsetLeft;
      if (left === 0) {
        item.style.left ="-240px";
      }
      if (left === -240) {
        item.style.left = 0;
      }
    },
    handleSelect(index, indexPath) {
      if (window.screen.width < 768) {
        let item = document.getElementsByClassName("sidebar-menu-container-collapse")[0];
        let left = item.offsetLeft;
        if (left === 0) {
          item.style.left ="-240px";
        }
      }

      this.$router.push(index);
    },
    handleOpen(index, indexPath) {

    },
    handleClose(index, indexPath) {

    },
  },
};
</script>

<style lang="stylus" scoped>
  .logo-icon {
    font-size 32px
    color white
  }
  .sidebar-menu-container {
    overflow hidden
  }
  .sidebar-menu-container-collapse{
    min-width 200px;
  }
  .sidebar-menu-container:not(.sidebar-menu-container-collapse){
    min-width 64px;
  }
  .el-menu-vertical{
    background-color #304156
    text-align left
    height: 100%
  }
  .el-menu-vertical:not(.el-menu--collapse) {
    min-width: 200px;
  }
  .logoImg {
    position: absolute;
    left: 10px;
    max-width: 160px;
    top: 8px;
  }
  .logo-item {
    overflow: hidden;
  }
  img.logoImg + i.logo-icon + span.menuLabel {
    display none
  }
  img.logoImg + i.logo-icon {
    display none
  }

</style>

<style lang="stylus">
  .sidebar-menu-container {
    .logo-item {
      .el-tooltip {
        display none
      }
    }
    .el-scrollbar__wrap {
      overflow-x: hidden;
      overflow-y: auto;
    }
    .el-scrollbar__view {
      height 100%
    }
    .el-scrollbar__bar.is-horizontal {
      display none;
    }
    .level_1,.level_1 div {
      background-color #304156
    }
    .level_2,.level_2 div {
      background-color #2d3b4f
    }
    .level_3,.level_3 div {
      background-color #283648
    }
  }
</style>
