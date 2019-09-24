<template>
  <div>
    <el-row class="search-box gap">
      <el-card align="left" id="el-accountQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item>
            <el-select v-model="searchForm.userName" filterable clearable placeholder="姓名"
                       v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'
                       || this.$appData.userInfo.roles[0].roleName == 'Boss'">
              <el-option v-for="user in userList"
                         :key="user.userId"
                         :label="user.nickName + '(' + user.userName + ')'"
                         :value="user.userId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model="searchForm.accountNo" clearable placeholder="账户号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="searchForm.accountName" clearable placeholder="账户名称"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'
                       || this.$appData.userInfo.roles[0].roleName == 'Boss'" v-model="searchForm.accountType" clearable
                       placeholder="账户类型">
              <el-option v-for="item in accountTypeArr"
                         :key="item.label"
                         :label="item.label"
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
                  element-loading-background="#fff">
          <el-table-column
            label="序号"
            type="index"
            width="60">
          </el-table-column>
          <el-table-column prop="id" label="编号" v-if="false"/>
          <el-table-column width="110px" prop="memberId" label="成员编号"/>
          <el-table-column width="100px" prop="accountNo" label="账户号"/>
          <el-table-column width="110px" prop="accountName" label="账户名称"/>
          <el-table-column prop="accountType" label="账户类型">
            <template slot-scope="scope">
              <span v-if="scope.row.accountType === 'club'">俱乐部</span>
              <span v-if="scope.row.accountType === 'player'">球员</span>
              <span v-if="scope.row.accountType === 'coach'">教练</span>
            </template>
          </el-table-column>
          <el-table-column prop="balance" label="账户余额(元)" :formatter="formatAmount"/>
          <el-table-column prop="perPrice" label="当前课时单价(元)" :formatter="formatAmount"/>
          <el-table-column prop="classTotal" label="累计课时数(节)"/>
          <el-table-column prop="remainTime" label="剩余课时数(节)"/>
          <el-table-column prop="nextPerPrice" label="预充值课时单价(元)" :formatter="formatAmount"/>
          <el-table-column prop="nextRemainTime" label="预充值课时数(节)"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatTime"/>
          <!--<el-table-column align="center" width='100px' fixed="right" label="操作">-->
          <!--<template slot-scope="scope">-->
          <!--<el-button type="primary" size="mini" @click="openAccountInfo(scope.row)"-->
          <!--v-permission="'account-center/account-info/info'">账户流水-->
          <!--</el-button>-->
          <!--</template>-->
          <!--</el-table-column>-->
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

  export default {
    components: {
      SearchForm
    },
    data() {
      const defaultSearchForm = {
        accountNo: '',
        accountName: '',
        accountType: '',
        userName: '',
      };

      return {
        loading: false,
        // 搜索字段
        defaultSearchForm,
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        userList: [],
        accountTypeArr: [
          {
            value: "club",
            label: "俱乐部",
          },
          {
            value: "coach",
            label: "教练",
          },
          {
            value: "player",
            label: "球员",
          },
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
      if (this.$appData.userInfo.roles[0].roleName == '超级管理员'
        || this.$appData.userInfo.roles[0].roleName == 'Boss') {
        this.initUserList();
      }
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
        _data["accountNo"] = this.searchForm.accountNo;
        _data["accountName"] = this.searchForm.accountName;
        _data["accountType"] = this.searchForm.accountType;
        _data["memberId"] = this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'
          ? this.searchForm.userName : this.$appData.userInfo.userId;
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
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_ACCOUNT_LIST, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.accountDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
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
      openAccountInfo(val) {

      }
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

