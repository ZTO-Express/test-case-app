<template>
  <div class="complete-pagination-body">
    <el-pagination background :layout="layout" :page-sizes="pageSizes" :page-size="pageSize" :current-page="currentPage" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
  </div>
</template>
<script>
export default {
  props: {
    currentPage: {
      default: 1,
      type: Number
    },
    total: {
      default: 0,
      type: Number
    },
    pageSize: {
      default: 10,
      type: Number
    },
    pageSizes: {
      default: () => [10, 20, 50, 100],
      type: Array
    },
    layout: {
      default: 'total, sizes, prev, pager, next, jumper',
      type: String
    }
  },
  data() {
    return {
      pageInfo: {
        total: this.total,
        pageSize: this.pageSize,
        currentPage: this.currentPage
      }
    }
  },
  watch: {},
  methods: {
    handleSizeChange(val) {
      const pageInfo = {
        total: this.total,
        pageSize: val,
        currentPage: 1
      }
      this.updatePage(pageInfo)
    },
    handleCurrentChange(val) {
      const pageInfo = {
        total: this.total,
        pageSize: this.pageSize,
        currentPage: val
      }
      this.updatePage(pageInfo)
    },
    updatePage(v) {
      this.$emit('updatePage', v)
    }
  }
}

</script>
<style lang="scss">
.complete-pagination-body {
  display: flex;
  justify-content: flex-end;

  .el-pagination {
    padding: 20px;

    .btn-prev,
    .btn-next {
      border: none;
    }
  }
}

</style>
