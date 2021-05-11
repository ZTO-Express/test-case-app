<!--
     按钮-可自定义
 -->
<template>
  <div class="flex flex-col flex-center nodata">
    <div>
      <img :src="empty" alt />
    </div>
    <div class="desc">{{desc}}</div>
  </div>
</template>
<script>
import empty from '@/assets/noData.png'
export default {
  name: 'customBtn',
  props: {
    desc: {
      default: '暂无数据',
      type: String
    }
  },
  data() {
    return {
      empty: empty
    }
  },
  methods: {

  }
}
</script>
<style lang="scss">
.nodata {
  .desc {
    font-size: 16px;
    color: #999;
    transform: translateX(-8px);
  }
}
</style>
