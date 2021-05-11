<template>
  <div class="testplan-index">
    <template>
      <el-tabs v-model="listPara.state" @tab-click="handleClick">
        <el-tab-pane :label="`全部执行计划(${this.total})`" name="3"></el-tab-pane>
        <el-tab-pane :label="`未开始(${this.count0})`" name="0"></el-tab-pane>
        <el-tab-pane :label="`进行中(${this.count1})`" name="1"></el-tab-pane>
        <el-tab-pane :label="`已完成(${this.count2})`" name="2"></el-tab-pane>
      </el-tabs>
    </template>
    <div class="c-content-table">
      <div style="float: left" class="c-content-bottom">
        <el-button type="primary" size="small" @click="handleOperate('add')"><i class="el-icon-plus"></i> 新建执行计划</el-button>
      </div>
      <div style="float: right" class="c-content-bottom">
        <el-select v-model="listPara.listType" filterable @change="getPlanCountAndList" style="width: 140px;height: auto">
          <el-option :key="index" :label="item.name" :value="item.id" v-for="(item,index) in listTypeList" />
        </el-select>
        <el-input prefix-icon="el-icon-search" placeholder="搜索执行计划" v-model="listPara.planName" @input="getPlanCountAndList('planName')" clearable style="width: 250px"></el-input>
      </div>
    </div>
    <div class="c-content-table">
      <el-table :data="tableData" >
        <el-table-column
          fixed
          prop="planName"
          min-width="300"
          label="执行计划名称"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="executeUserName"
          label="负责人"
          width="60"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="state"
          label="当前状态"
          width="70"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            <el-tag
              :type="row.state === 0 ? 'primary' : row.state === 1 ? 'warning' : 'success'" size="mini"
              disable-transitions>{{row.state === 0 ? '未开始' : row.state === 1 ? '进行中' : '已完成'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="passRate"
          label="通过率"
          width="70"
          show-overflow-tooltip
        >
            <template slot-scope="{ row }">
              <span>{{ row.passRate }}%</span>
            </template>
        </el-table-column>
        <el-table-column
          prop="executeNum"
          label="已测用例"
          width="70"
          show-overflow-tooltip
        >
          <template slot-scope="{ row }">
            <span>{{ row.executeNum }}/{{row.totalNum}}</span>
          </template>
        </el-table-column>
        <!--<el-table-column-->
          <!--prop=""-->
          <!--label="结果分布"-->
          <!--show-overflow-tooltip-->
        <!--&gt;-->
          <!--<el-progress :percentage="50" status="exception"></el-progress>-->
        <!--</el-table-column>-->
        <el-table-column
          prop="stageName"
          label="测试阶段"
          width="90"
        >
          <template slot-scope="{ row }">
            <el-tag type="info" size="mini" disable-transitions>{{ row.stageName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="bname"
          label="关联部门"
          width="120"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="pname"
          label="关联项目"
          width="150"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="vname"
          label="关联版本"
          min-width="150"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="createUser"
          label="创建人"
          width="70"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          prop="updateUser"
          label="更新人"
          width="70"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="{ row }">
            <el-button @click="handleOperate('edit',row)" type="primary" size="mini"
              >编辑</el-button
            >
            <el-button @click="handleOperate('del',row)" type="danger" size="mini"
              >删除</el-button
            >
            <el-button @click="handleOperate('detail', row)"  size="mini">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="c-content-footer">
      <complete-pagination
        :total="page.total"
        :currentPage="page.currentPage"
        :pageSize="page.pageSize"
        @updatePage="updatePage"
      />
    </div>
    <modal
      :visible.sync="modal.visiable"
      :title="modal.title"
      width="40%"
      confirmTitle="保存"
      :showCancle="false"
      :showConfirm="false"
      :showReset="false"
      :before-close="handleClose"
    >
      <template>
        <operate ref="operate" @update="getPlanCountAndList" />
      </template>
    </modal>
  </div>
</template>
<script>
import operate from './operate'

export default {
  data() {
    return {
      tableData: [
        {
          title: ''
        }
      ],
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1
      },
      modal: {
        visiable: false,
        title: '新建'
      },
      listPara: {
        pageSize: 10,
        pageNo: 1,
        listType: '1',
        state: '1',
        planName: '',
        user: ''
      },
      listTypeList: [
        { name: '显示与我相关', id: '1' },
        { name: '显示所有', id: '3' }
      ],
      count0: 0,
      count1: 0,
      count2: 0,
      total: 0
    }
  },
  components: { operate },
  computed: {
    // ...mapGetters(['userInfo'])
  },
  methods: {
    initViewData() {
      this.getPlanCountAndList()
    },
    // tab切换
    handleClick() {
      this.getPlanList()
    },
    getPlanCountAndList(val) {
      if (val !== undefined && val === 'planName') {
        this.page.currentPage = 1
      }
      this.listPara.user = this.$appData.userInfo.nickName
      this.listPara.state = '3'
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_PLAN_COUNT, this.listPara).then((res) => {
        if (res.code === '000000') {
          this.count0 = res.data.initCount
          this.count1 = res.data.inHandCount
          this.count2 = res.data.doneCount
          this.total = this.count0 + this.count1 + this.count2
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      }).catch((error) => {
        this.$message.error(error.msg);
      })
      this.getPlanList()
    },
    handleClose() {
      this.modal.visiable = false
      this.getPlanCountAndList()
    },
    // 初始list
    getPlanList(val) {
      if (val !== undefined) {
        this.listPara.state = '3'
      }
      this.modal.visiable = false
      this.listPara.user = this.$appData.userInfo.nickName
      this.listPara.pageSize = this.page.pageSize
      this.listPara.pageNo = this.page.currentPage
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_PLAN_LIST, this.listPara).then((res) => {
        if (res.code === '000000') {
          this.page.currentPage = res.data.pageNum
          this.page.pageSize = res.data.pageSize
          this.page.total = res.data.total
          this.tableData = res.data.list
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      }).catch((error) => {
        this.$message.error(error.msg);
      })
    },
    // 点击操作 增加、编辑
    handleOperate(type, row) {
      const obj = {
        add: () => this.executeAddRow(),
        edit: () => this.executeEditRow(row),
        del: () => this.executeDelRow(row),
        detail: () => this.goTestLib(row)
      }
      obj[type].call(this)
    },
    // 执行：增加
    executeAddRow(type, row) {
      this.modal.visiable = true
      this.modal.title = '新建执行计划'
      this.$nextTick(() => {
        this.$refs.operate.initViewData(type)
      })
    },
    // 执行：编辑
    executeEditRow(row) {
      this.modal.visiable = true
      this.modal.title = '编辑执行计划'
      this.$nextTick(() => {
        this.$refs.operate.initViewData(row)
      })
    },
    // 执行：删除
    executeDelRow(row) {
      this.$confirm('确认删除该计划', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
            this.$axiosUtil.del(this.$appConfig.API, this.$urlConst.DELETE_PLAN + '/' + row.id).then((res) => {
            if (res.code === '000000') {
              this.showMsg(res.msg || res.message, 'success')
              this.getPlanCountAndList()
            } else {
              this.showMsg(res.msg || res.message, 'error')
            }
          })
        })
    },
    // 跳转到测试库
    goTestLib(row) {
      this.$router.push({ path: '/home/testlibrary', query: { id: row.id }})
    },
    // 分页
    updatePage(val) {
      this.page.pageSize = val.pageSize
      // this.listPara.pageNo = val.currentPage
      this.page.currentPage = val.currentPage
      this.initViewData()
    }
  },
  mounted() {
    this.initViewData()
  }
}
</script>
<style lang="scss">
.testplan-index {
  padding: 24px;
  .header {
    display: flex;
    justify-content: space-between;
  }
  .c-content-table {
    margin-top: 15px;
  }
  .c-content-bottom {
    margin-bottom: 15px;
  }
}
</style>
