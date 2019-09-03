<template>
  <div class="audit-center">
    <el-row>
      <audit-dialog :visible.sync="dialogVisible" :dialogType="dialogType"
                    @submit="search(data.pagination.currentPage)"/>
    </el-row>
    <el-row class="search-box gap">
      <el-card align="left" id="el-auditQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item>
            <el-input v-model="searchForm.tradeNo" clearable placeholder="订单号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.userName" clearable placeholder="球员姓名"
                       v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'
                       || this.$appData.userInfo.roles[0].roleName == 'Boss'">
              <el-option v-for="user in userList"
                         :key="user.userId"
                         :label="user.nickName"
                         :value="user.userId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
          <el-select v-model.trim="searchForm.auditStatus" clearable placeholder="审核状态">
            <el-option v-for="item in statusArr" :key="item.value" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
          <el-form-item>
            <el-select v-model.trim="searchForm.transType" clearable placeholder="交易类型">
              <el-option v-for="item in transTypeArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="search(1)" icon="el-icon-search" type="primary" :loading="loading">搜索</el-button>
            <el-button @click="clear" icon="el-icon-close">清空</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-row>
    <el-row>
      <el-card class="table-box gap" align="center">
        <el-table :data="data.viewData.list" border v-loading="loading"
                  ref="table"
                  tooltip-effect="light"
                  show-overflow-tooltip
                  element-loading-text="拼命加载中"
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="#fff"
                  :row-class-name="tableRowClassName">
          <el-table-column
            label="序号"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column prop="id" label="编号" v-if="false"/>
          <el-table-column prop="userName" label="球员姓名"/>
          <el-table-column prop="tradeNo" label="交易号"/>
          <el-table-column prop="payWay" label="支付方式"/>
          <el-table-column prop="seqId" label="支付凭证"/>
          <el-table-column prop="amount" label="金额(元)" :formatter="formatAmount"/>
          <el-table-column prop="fromAccountNo" label="转出账号"/>
          <el-table-column prop="toAccountNo" label="转入账号"/>
          <el-table-column prop="transType" label="交易类型">
            <template slot-scope="scope">
              <span v-if="scope.row.transType === 'charge'">充值</span>
              <span v-if="scope.row.transType === 'pay'">支付</span>
              <span v-if="scope.row.transType === 'transfer'">转账</span>
            </template>
          </el-table-column>
          <el-table-column prop="classTotal" label="课时数(节)"/>
          <el-table-column prop="perPrice" label="课时单价(元)" :formatter="formatAmount"/>
          <el-table-column width="100px" prop="auditStatus" label="状态">
            <template slot-scope="scope">
              <span v-if="scope.row.auditStatus === 0">待审核</span>
              <span v-if="scope.row.auditStatus === 1">审核通过</span>
              <span v-if="scope.row.auditStatus === 2">审核拒绝</span>
            </template>
          </el-table-column>
          <el-table-column width="200px" prop="remark" label="审核备注">
            <template slot-scope="scope">
              <el-popover v-if="needPop(scope.row.remark)" placement="top-start" width="200" trigger="hover"
                          :content="scope.row.remark">
                <span slot="reference">{...}</span>
              </el-popover>
              <span v-else>{{scope.row.remark}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createUser" label="创建人"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="auditUser" label="审核人"/>
          <el-table-column prop="auditTime" label="审核时间" :formatter="formatTime"/>
          <el-table-column align="center" width='80px' fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openAuditInfo('edit',scope.row, scope.$index)"
                         v-permission="'audit-center/audit'"
                         v-show="scope.row.auditStatus === 0">审核
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          align="center"
          :current-page="this.data.pagination.currentPage"
          :page-size="this.data.pagination.currentSize"
          :page-sizes="this.data.pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :total="this.data.pagination.total">
        </el-pagination>
      </el-card>
    </el-row>
  </div>
</template>

<script>
  import SearchForm from '../../components/search-form/searchForm.vue';

  import auditDialog from './auditDialog';

  export default {
    components: {
      SearchForm,
      auditDialog
    },
    data() {
      const defaultSearchForm = {
        tradeNo: '',
        userId: '',
        auditStatus: '',
        transType: ''
      };

      return {
        loading: false,
        dialogVisible: false,
        dialogType: {},
        // 搜索字段
        defaultSearchForm,
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        userList: [],
        statusArr: [
          {
            value: 0,
            label: "待审核",
          },
          {
            value: 1,
            label: "审核通过",
          },
          {
            value: 2,
            label: "审核拒绝",
          }
        ],
        transTypeArr: [
          {
            value: 'charge',
            label: "充值",
          },
          {
            value: 'pay',
            label: "支付",
          },
          {
            value: 'transfer',
            label: "转账",
          }
        ],
        data: {
          pagination: {
            total: 0,
            pageSize: [10, 20, 40, 80, 100],
            currentSize: 10,
            currentPage: 1,
            keyCurrSize: "pageSize",
            keyCurrPage: "pageNo",
          },

          // 列表
          viewData: {
            list: [],
          },
        }
      }
    },
    created: function () {
      this.search();
      this.initUserList();
    },
    mounted() {
    },
    methods: {
      initUserList() {
        this.$axiosUtil.post(this.$appConfig.SSO, this.$urlConst.FIND_USERS, {}).then((res) => {
          this.userList = res.data;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      handleSizeChange(val) {
        this.data.pagination.currentSize = val;
        this.search(); // 点击页码数时传页码，不点就说明是搜索
      },
      handleCurrentChange(val) {
        this.data.pagination.currentPage = val;
        this.search(val);
      },
      clear() {
        this.searchForm = JSON.parse(JSON.stringify(this.defaultSearchForm));
      },
      searchFormVerify() {
        let verifyResult = true;
        this.$refs.searchForm && this.$refs.searchForm.validate(pass => {
          verifyResult = pass
        });
        return verifyResult;
      },
      getData() {
        let _data = {};
        const pagination = this.data.pagination;
        if (pagination
          && pagination.currentSize
          && pagination.keyCurrSize
          && pagination.currentPage
          && pagination.keyCurrPage
        ) {
          _data[pagination.keyCurrSize] = pagination.currentSize;
          _data[pagination.keyCurrPage] = pagination.currentPage;
        }
        _data["tradeNo"] = this.searchForm.tradeNo;
        _data["userId"] = this.searchForm.userName;
        _data["auditStatus"] = this.searchForm.auditStatus;
        _data["transType"] = this.searchForm.transType;
        return _data;
      },
      search(pageNo = 1) {
        if (!this.searchFormVerify()) return; // 搜索表单验证，若有
        this.loading = !this.loading;

        // 修复翻页号
        if (pageNo === 1) {
          this.data.pagination.currentPage = 1;
        }
        const _data = this.getData();
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_AUDIT_ORDER, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.auditDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      openAuditInfo(type = '', row = {}, index = '',) {
        const data = JSON.parse(JSON.stringify(row));
        this.dialogVisible = !this.dialogVisible;
        this.dialogType = {type, data, index};
      },
      formatDate(row, col, val, index) { //格式化日期
        return this.$formatters.formatDate(val);
      },
      formatTime(row, col, val, index) { //格式化日期
        return this.$formatters.formatTime(val);
      },
      formatAmount(row, col, val, index) { // 格式化金额 千位符
        if (!val || val === '0') return '--';
        return '￥' + val;
      },
      tableRowClassName({row}) {
        if (row.auditStatus === 1) {
          return 'success-row';
        }
        if (row.auditStatus === 2) {
          return 'warning-row';
        }
        return '';
      },
      needPop(val) {
        if (val && val.length > 20) {
          return true
        }
      },
    }
  }
</script>

<style scoped>
  /*搜索栏*/
  .el-form-item {
    margin: 5px 5px 5px 0;
  }

  /*间距*/
  .gap {
    margin-bottom: 20px;
  }

  /*表格头*/
  .table-title {
    padding-bottom: 18px;
  }

  .float-Right {
    float: right;
  }

</style>

