<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    @select="handleSelect">
    <i class="icon-view-list pc-view-list " :class="{'is-collapse':sidebar.isCollapse}"
       @click="collapseSwitch"></i>
    <breadcrumb class="breadcrumb"></breadcrumb>

    <div class="right-menu">
      <el-dropdown trigger="click">
        <div>
          <i class="icon-user"></i>
          {{$appData.userInfo.nickName || $appData.userInfo.email ||$appData.userInfo.mobile ||
          $appData.userInfo.userName}}
          <i class="el-icon-caret-bottom"></i>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown-menu">
          <el-dropdown-item v-if="$appData.userInfo.org">
            <div >{{$appData.userInfo.org }}</div>
          </el-dropdown-item>
          <el-dropdown-item>
            <div @click="securityCenter">安全中心</div>
          </el-dropdown-item>
          <el-dropdown-item>
            <div @click="logout">退出登录</div>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <div class="right-menu">
      <el-tooltip effect="dark" :content="'点击全屏'" placement="bottom">
        <screenfull class="screenfull right-menu-item"></screenfull>
      </el-tooltip>
    </div>
    <div class="right-menu" style="cursor: pointer">
      <el-tooltip effect="dark" :content="'加入喜爱'" placement="bottom">
        <i class="icon-favorites" @click="favorites"></i>
      </el-tooltip>
    </div>
    <div class="right-menu mobile-collapse-switch">
      <i class="icon-view-list" @click="collapseSwitchMobile"></i>
    </div>
  </el-menu>
</template>

<script>
  import breadcrumb from '../breadcrumb/index.vue';
  import screenfull from '../screenfull/index.vue';

  export default {
  components: { breadcrumb, screenfull },
  data() {
    return {
      timeOut: '',
      sidebar: this.$appData.sidebar,
      activeIndex: '1',
    };
  },

  created() {
    //    if (appConfig.isProduction && this.$permUtil.hasPerm('operation-log-page/notification')) {
    //      this.loadNewAction(30000);
    //    }
  },
  beforeDestroy() {
    clearTimeout(this.timeOut);
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
    sortByTime(a, b) {
      return a.actionDate - b.actionDate;
    },
    hasPerm(permission) {
      if (!permission) {
        return true;
      }
      return this.$permUtil.hasPerm(permission);
    },

    // 定时任务更新推送
    // loadNewAction(period) {
    //   clearTimeout(this.timeOut);
    //
    //   let temp = [];
    //   const beginDate = this.$sessionUtil.userGet('action-notify-cache') && Number(this.$sessionUtil.userGet('action-notify-cache')) > Number(this.$sessionUtil.userGet('loginTime')) ?
    //     this.$formatters.formatTime(Number(this.$sessionUtil.userGet('action-notify-cache')) + 1000)
    //     : this.$formatters.formatTime(Number(this.$sessionUtil.userGet('loginTime')) + 1000);
    //
    //   Promise.all([this.getList('3', beginDate)]).then((x) => {
    //     if (x[0].length > 0) {
    //       temp = x[0];
    //       temp.sort(this.sortByTime);
    //       this.notify(temp);
    //       // 存储最大值
    //       this.$sessionUtil.userSet('action-notify-cache', temp[temp.length - 1].actionDate);
    //     }
    //   });
    //
    //   this.timeOut = setTimeout(() => {
    //     this.loadNewAction(period);
    //   }, period);
    // },

    getList(actionLevel, beginDate) {
      const param = {
        pageNo: 1,
        pageSize: 10,
        actionLevel,
        beginDate,
      };
      const result = [];
      return this.$action.operationLogAction.operationLogList(param)
        .then((resp) => {
          resp.data.list.forEach((item) => {
            result.push(item);
          });
          return Promise.resolve(result);
        }).catch((error) => {
          // this.$message.error(error.respMessage);
        });
    },

    notify(arr) {
      arr.forEach((item) => {
        const warn = setInterval(() => {
          this.$notify({
            title: '敏感操作提示',
            position: 'bottom-right',
            message: item.operatorName + ' 操作 ' + item.actionName + ' ' + item.opResultMsg,
            type: 'warning',
          });
          clearInterval(warn);
        }, 100);
      });
    },

    favorites() {
      const url = this.$route.path;
      const menu = this.sidebar.routerMenuMap[url];
      if (!(menu && menu.menuName)) {
        this.$message({ type: 'warning', showClose: true, message: '本页面无法加入喜爱' });
        return;
      }

      if (menu) {
        let localFavorites = this.$localUtil.userGet('user-favorites');
        if (localFavorites) {
          try {
            localFavorites = JSON.parse(localFavorites);
          } catch (e) {
            localFavorites = [];
          }

          for (let i = localFavorites.length - 1; i >= 0; i--) {
            const item = localFavorites[i];
            if ((!(item.url && item.menuName)) || item.menuName === menu.menuName) {
              localFavorites.splice(i, 1);
            }
          }
        } else {
          localFavorites = [];
        }
        const temp = {};
        Object.assign(temp, menu);
        temp.child = null;
        temp.children = null;
        temp.father = null;
        localFavorites.push(temp);
        this.$localUtil.userSet('user-favorites', JSON.stringify(localFavorites));
        this.$message({ type: 'success', showClose: true, message: '成功加入喜爱' });
      }
    },
    securityCenter() {
      this.$router.push({ name: 'securityCenter' });
    },
    logout() {
      this.$emit('logout');
    },
    handleSelect(key, keyPath) {
    },
    collapseSwitch() {
      this.sidebar.isCollapse = !this.sidebar.isCollapse;
      this.$sessionUtil.set(this.$sessionUtil.SIDEBAR_ISCOLLAPSE, this.sidebar.isCollapse);
    },
  },
};
</script>

<style scoped="">
  .el-menu {
    height: 45px;
  }

  .pc-view-list {
    float: left;
    font-size: 30px;
    position: relative;
    margin-top: 5px;
    outline: none;
  }

  .pc-view-list.is-collapse {
    display: inline-block;
    cursor: pointer;
    transform: rotate(90deg);
    transition: .38s;
    transform-origin: 50% 50%;
  }

  .breadcrumb {
    margin-left: 10px;
    float: left;
    position: relative;
    transform: translateY(85%);
    font-size: 16px;
    outline: none;
  }

  .right-menu {
    float: right;
  }

  .icon-user {
    font-size: 30px;
  }

  .user-dropdown-menu {
    min-width: 100px;
  }
</style>
