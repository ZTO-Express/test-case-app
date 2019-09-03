<template>
  <div class="sidebar-item">
    <template v-for="(item) in menus">
      <el-submenu  :class="'level_'+ (Number(level) + 1)" v-if="hasChild(item) && (Number(level) + 1) === 1"
                  :index="item.menuCode" :key="item.menuCode">
        <template slot="title">
          <i v-if="item.icon" :class="'icon-'+item.icon + (isFirstLevel ? ' first-level-menu-icon':'')"></i>
          <span slot="title" v-if="item.menuName && !(sidebar.isCollapse && isFirstLevel)">
              {{item.menuName}}
          </span>
        </template>
        <sidebar-item :level="String(Number(level) + 1)" :menus="item.child" :base="base" :key="item.menuCode">
        </sidebar-item>
      </el-submenu>
      <el-menu-item-group  :class="'level_'+ (Number(level) + 1)" v-if="hasChild(item) && (Number(level) + 1) === 2"
                   :index="item.menuCode" :key="item.menuCode">
        <template slot="title">
          <i v-if="item.icon" :class="'icon-'+item.icon + (isFirstLevel ? ' first-level-menu-icon':'')"></i>
          <span slot="title" v-if="item.menuName && !(sidebar.isCollapse && isFirstLevel)">
              {{item.menuName}}
          </span>
        </template>
        <sidebar-item :level="String(Number(level) + 1)" :menus="item.child" :base="base" :key="item.menuCode">
        </sidebar-item>
      </el-menu-item-group>
      <el-menu-item :class="'level_'+ (Number(level) + 1)" v-else-if="!!item.url" :key="item.menuCode" :index="base+'/'+item.url" >
        <i v-if="item.icon" :class="'icon-'+item.icon + (isFirstLevel ? ' first-level-menu-icon':'')"></i>
        <span slot="title" v-if="item.menuName">
              {{item.menuName}}
        </span>
      </el-menu-item>
    </template>
  </div>

</template>

<script>
import sessionUtil from '../../utils/sessionUtil';

export default {
  name: 'sidebar-item',
  props: {
    level: {
      type: String,
    },
    menus: {
      type: Array,
    },
    base: {
      type: String,
    },
    isFirstLevel: {
      type: Boolean,
    },
  },
  data() {
    return {
      sidebar: this.$appData.sidebar,
    };
  },
  computed: {

  },
  methods: {
    hasChild(item) {
      this.sidebar.menuMap[item.menuCode] = item;
      const has = item.child && item.child.length > 0;
      if (has) {
        item.child.forEach((child) => {
          child.father = item;
        });
      }

      return has;
    },
  },
};
</script>

<style lang="stylus" scoped>
  i {
    font-size: 30px;
  }

</style>

<style lang="stylus">
  i.first-level-menu-icon + i.el-icon-arrow-right {
    display: none;
  }

</style>
