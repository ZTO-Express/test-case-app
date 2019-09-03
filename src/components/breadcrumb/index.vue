<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index)  in levelList"
                          :key="index" v-if='item.name'>
        <span v-if='index==levelList.length-1 || !item.path'
              class="no-redirect">{{item.name}}</span>
        <router-link v-else :to="item.path">{{item.name}}</router-link>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>

export default {
  created() {
    this.getBreadcrumb();
  },
  data() {
    return {
      levelList: [],
    };
  },
  watch: {
    $route() {
      this.getBreadcrumb();
    },
  },
  methods: {
    getBreadcrumb() {
      if (!this.$route.path.startsWith('/home/')) {
        return;
      }

      const temp = [];
      const unValid = {
        path: '',
        name: '无效路径',
      };

      temp.push({
        path: this.$appConfig.logoRoute,
        name: this.$appConfig.logoName,
      });

      if (this.$route.path !== this.$appConfig.logoRoute) {
        const menu = this.$appData.sidebar.routerMenuMap[this.$route.path];
        if (!menu) {
          temp.push(unValid);
        } else {
          this.enrichLevel(menu, temp, this.enrichLevel);
          if (!temp[temp.length - 1].path) {
            temp.splice(1, temp.length - 1);
            temp.push(unValid);
          }
        }
      }

      this.levelList.splice(0, this.levelList.length);
      this.levelList.push(...temp);
    },
    enrichLevel(menu, levelList, self) {
      if (menu.father) {
        this.enrichLevel(menu.father, levelList, self);
      } else if (menu.uiMetaId) {
        this.enrichLevel(this.getMenu(menu.menuCode), levelList, self);
      }
      levelList.push({
        path: menu.url ? '/home/' + menu.url : '',
        name: menu.menuName ? menu.menuName : '#',
      });
    },
    getMenu(menuCode) {
      let result;
      Object.values(this.$appData.sidebar.routerMenuMap).forEach((menu) => {
        if ((!menu.uiMetaId) && menu.menuCode === menuCode) {
          result = menu;
        }
      });
      return result;
    },
  },
};
</script>

<style scoped>
  .app-breadcrumb {
    display: inline-block;
  }
  .no-redirect {
    display: inline-block;
    color: #97a8be;
    cursor: text;
  }
</style>
