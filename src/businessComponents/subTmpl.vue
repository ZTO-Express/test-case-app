<template>
  <div class="trans-route-view">
    <transition name="fadeIn">
      <router-view v-if="!$route.meta.keepAlive"></router-view>
    </transition>
    <transition name="fadeIn">
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive"></router-view>
      </keep-alive>
    </transition>
  </div>
</template>
<script>
export default {
  name: 'subTmpl'
}
</script>
<style lang="scss">
.trans-route-view {
  .fadeIn-enter {
    opacity: 0;
  }
  .fadeIn-enter-active {
    transition: all 0.4s linear;
  }
  .fadeIn-leave-enter {
    transition: all 0.4s linear;
    opacity: 0;
  }
}
</style>

