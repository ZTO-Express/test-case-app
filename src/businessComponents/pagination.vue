<template>
  <div class="pagination-body">
    <!-- <div class="pagination-total">
      <span style="padding-right: 10px">共{{ totalCount }}条数据</span>
      <span>第{{ currentPage }}/{{ pages }}页</span>
    </div> -->
    <div>
      <el-pagination
        @size-change="sizeChange"
        @current-change="updatePage"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30]"
        :page-size="pageSize"
        background
        layout="total, sizes, prev, pager, next"
        :total="totalCount"
      ></el-pagination>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    currentPage: {
      default: 1,
      type: Number
    },
    totalCount: {
      default: 0,
      type: Number
    },
    handleSizeChange: {
      type: Boolean,
      default: false
    }
    // pageSize: {
    //   default: 10,
    //   type: Number
    // }
  },
  data() {
    return {
      pageSize: 10
    }
  },
  methods: {
    updatePage(n) {
      this.$emit('updatePage', n)
    },
    sizeChange(n) {
      if (this.handleSizeChange) {
        this.$emit('sizeChange', n)
        return
      }
      this.pageSize = n
      this.$parent.page.pageSize = n
      this.updatePage(1)
    }
  },
  computed: {
    pages() {
      return Math.ceil(this.totalCount / this.pageSize)
    }
  }
}
</script>
<style lang="scss">
.pagination-body {
  display: flex;
  justify-content: flex-end;
  background-color: #ffffff;
  padding-top: 30px;
  font-size: 14px;
  .pagination-total {
    color: rgba(0, 0, 0, 0.65);
    line-height: 30px;
    .pr10 {
      padding-right: 10px;
    }
  }
  .el-pagination .el-select .el-input .el-input__inner {
    border-radius: 2px;
  }
  .el-pagination {
    .el-pager {
      .number {
        // margin: 0px 4px;
      }
    }
    .btn-prev,
    .btn-next {
      border: 0;
    }
  }
}
</style>

