<template>
  <div class="tags-view-container">
    <el-tag size="small" :class="view.class"
            v-for="view in getVisitedViews" :key="view.code" :slot="view">
      <span class="view-name" @click="toView(view.path)">{{view.name}}</span>
      <span class='el-icon-close' @click='deleteTag(view.code)'></span>
    </el-tag>
  </div>
</template>

<script>
import sessionUtil from '../../utils/sessionUtil';

export default {

  data() {
    return {
      visitedViews: [],
    };
  },
  computed: {
    getVisitedViews() {
      const temp = [];
      this.visitedViews.forEach((item) => {
        if (item.userId === this.$appData.userInfo.userId) {
          temp.push(item);
        }
      });
      return temp.reverse();
    },
  },
  components: {

  },
  created() {
    this.visitedViews.splice(0, this.visitedViews.length);
    const visitedViewsTemp = JSON.parse(sessionUtil.get(sessionUtil.VISITED_VIEWS));
    if (visitedViewsTemp) {
      this.visitedViews.push(...visitedViewsTemp);
    } else {
      this.enrichVisitedViews();
    }
  },
  watch: {
    $route() {
      this.enrichVisitedViews();
    },
  },
  mounted() {

  },
  methods: {
    toView(path) {
      this.$router.push(path);
    },
    deleteTag(code) {
      let delIndex;
      this.visitedViews.forEach((view, index) => {
        if (view.code === code) {
          delIndex = index;
        }
      });
      if (delIndex !== undefined) {
        this.visitedViews.splice(delIndex, 1);
        sessionUtil.set(sessionUtil.VISITED_VIEWS, JSON.stringify(this.visitedViews));
      }
    },
    enrichVisitedViews() {
      if (!this.$route.path.startsWith('/home/')) {
        return;
      }

      let menu;
      if (this.$route.path === this.$appConfig.logoRoute) {
        menu = {
          url: this.$appConfig.logoRoute.substr(6),
          menuName: this.$appConfig.logoName,
          menuCode: 'logoCode',
        };
      } else {
        const routerMenuMap = this.$appData.sidebar.routerMenuMap;
        menu = routerMenuMap[this.$route.path];
      }

      if (!menu) {
        return;
      }

      for (let i = this.visitedViews.length - 1; i >= 0; i--) {
        const view = this.visitedViews[i];
        if (view.code === menu.menuCode && view.userId === this.$appData.userInfo.userId) {
          this.visitedViews.splice(i, 1);
        } else {
          view.class = 'tags-view-item';
        }
      }

      if (menu.url && menu.menuName) {
        this.visitedViews.push({
          userId: this.$appData.userInfo.userId,
          name: menu.menuName,
          path: '/home/' + menu.url,
          class: 'tags-view-item-active',
          code: menu.menuCode,
        });
      }
      sessionUtil.set(sessionUtil.VISITED_VIEWS, JSON.stringify(this.visitedViews));
    },
  },
};
</script>

<style  scoped>
  .tags-view-container{
    text-align: left;
    border-bottom: #67C23A;
  }

  .tags-view-item {
     margin: 5px 5px;
    background: #C0C4CC;
    border-radius: 0;
  }

  .tags-view-item-active {
    margin: 5px 5px;
    background: #67C23A;
    border-radius: 0;
  }

  .view-name {
    cursor: pointer
  }

  .tags-view-item .view-name{
   color: #606266;
  }

  .tags-view-item-active .view-name{
    color: #F2F6FC;
  }
</style>

