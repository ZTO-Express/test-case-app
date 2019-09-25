<template>
  <div>
    <el-row class="search-box gap">
      <el-card align="left" id="el-sessionQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item label="教练:" v-show="this.$appData.userInfo.roles[0].roleCode == '00001'
                       || this.$appData.userInfo.roles[0].roleCode == '100037'">
            <el-select v-model.trim="searchForm.userId" filterable clearable placeholder="请选择">
              <el-option v-for="item in coachList"
                         :key="item.userId"
                         :label="item.chineseName + '(' + item.englishName + ')'"
                         :value="item.userId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="训练日期:" prop="searchDate">
            <el-date-picker
              v-model="searchDate"
              :clearable="true"
              type="daterange"
              range-separator="至"
              start-placeholder="训练日期(起)"
              end-placeholder="训练日期(止)"
              :picker-options="pickerOptions">
            </el-date-picker>
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
        <!--<p align="left" style="font-size: small;color: red">-->
        <!--<span>签到费用合计:{{formatAmountByVal(this.data.signInChargeTotal)}}|场地租金合计:{{formatAmountByVal(this.data.placeRentTotal)}}|收入合计:{{formatAmountByVal(this.data.incomeTotal)}}</span>-->
        <!--</p>-->
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
          <el-table-column prop="chineseName" label="中文名"/>
          <el-table-column prop="englishName" label="英文名"/>
          <el-table-column prop="userId" label="用户编号"/>
          <el-table-column prop="share" label="股份比例(%)"/>
          <el-table-column prop="sessionCount" label="签到课程数"/>
          <el-table-column prop="playerCount" label="签到球员数"/>
          <el-table-column prop="amount" label="签到费用" :formatter="formatAmount"/>
          <el-table-column prop="placeRent" label="场地租金" :formatter="formatAmount"/>
          <el-table-column prop="income" label="收入" :formatter="formatAmount"/>
          <el-table-column prop="pay" label="应付工资" :formatter="formatAmount"/>
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
  import moment from 'moment';
  import SearchForm from '../../components/search-form/searchForm.vue';

  export default {
    components: {
      SearchForm,
    },
    data() {
      const defaultSearchForm = {
        userId: '',
        trainingDay: '',
      };
      const defaultSearchDate = [
        moment(new Date().setDate(1)).format('YYYY-MM-DD'),
        moment(new Date()).format('YYYY-MM-DD'),
      ];
      return {
        loading: false,
        dialogVisible: false,
        dialogType: {},
        defaultSearchForm,
        defaultSearchDate,
        searchDate: [
          moment(new Date().setDate(1)).format('YYYY-MM-DD'),
          moment(new Date()).format('YYYY-MM-DD'),
        ],
        pickerOptions: {
          // disabledDate(time) {
          //   return time.getTime() > Date.now();
          // },
        },
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        coachList: [],
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
          // signInChargeTotal: "",
          // placeRentTotal: "",
          // incomeTotal: "",
        }
      }
    },
    created: function () {
      this.initCoachList();
      this.search();
    },
    mounted() {
    },
    methods: {
      initCoachList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_COACH_LIST, param).then((res) => {
          this.coachList = res.data.coachDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
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
        this.searchDate = this.defaultSearchDate;
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
        _data["userId"] = this.$appData.userInfo.roles[0].roleCode == '00001'
        || this.$appData.userInfo.roles[0].roleCode == '100037' ? this.searchForm.userId : this.$appData.userInfo.userId;
        _data["startTime"] = this.searchDate && this.searchDate[0] ? moment(this.searchDate[0]).format('YYYY-MM-DD 00:00:00') : '';
        _data["endTime"] = this.searchDate && this.searchDate[1] ? moment(this.searchDate[1]).format('YYYY-MM-DD 23:59:59') : '';
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
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_SALARY_STATISTICS, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.salaryDtoStatisticsList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      formatDate(val) { //格式化日期
        return this.$formatters.formatDate(val);
      },
      formatTime(row, col, val, index) { //格式化日期
        return this.$formatters.formatTime(val);
      },
      formatAmount(row, col, val, index) { // 格式化金额 千位符
        if (!val || val === '0') return '--';
        return '￥' + val.toFixed(2);
      },
      formatAmountByVal(val) { // 格式化金额 千位符
        if (!val || val === '0') return '￥0.00';
        return '￥' + val.toFixed(2);
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

