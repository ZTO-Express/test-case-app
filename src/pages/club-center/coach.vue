<template>
  <div class="coach-center">
    <el-row class="header-box">
      <div style="text-align:right;margin-bottom: 10px">
        <el-button @click="openCoachInfo('new')" v-permission="'coach-center/coach/add'"
                   type="success" size="small" icon="el-icon-plus">新增教练员
        </el-button>
      </div>
    </el-row>
    <el-row>
      <coach-info-dialog :visible.sync="dialogVisible" :dialogType="dialogType"
                         @submit="search(data.pagination.currentPage)"/>
    </el-row>
    <el-row class="search-box gap">
      <el-card align="left" id="el-coachQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item>
            <el-select v-model.trim="searchForm.status" clearable placeholder="状态">
              <el-option v-for="item in statusArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model="searchForm.chineseName" clearable placeholder="中文名字"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="searchForm.englishName" clearable placeholder="英文名字"></el-input>
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
        <!-- <div class="table-title float-Right">
            <el-button type="default" size="mini" @click="exportData" icon="el-icon-upload2" v-permission="'merchant-center/account-statement/export'">导出</el-button>
        </div> -->
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
          <el-table-column prop="chineseName" label="中文名"/>
          <el-table-column prop="englishName" label="英文名"/>
          <el-table-column prop="mobile" label="手机号"/>
          <el-table-column prop="accountNo" label="账号"/>
          <el-table-column prop="balance" label="账户余额(元)" :formatter="formatAmount"/>
          <el-table-column prop="nationality" label="国籍"/>
          <el-table-column prop="birthday" label="生日" :formatter="formatDate"/>
          <el-table-column prop="height" label="身高(CM)">
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template slot-scope="scope">
              <span v-if="scope.row.status === 0">已退出</span>
              <span v-if="scope.row.status === 1">正常</span>
            </template>
          </el-table-column>
          <el-table-column prop="share" label="比例(%)"
                           v-if="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'"/>
          <el-table-column prop="remark" label="备注"/>
          <el-table-column prop="createUser" label="创建者"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="updateUser" label="更新者"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatTime"/>
          <el-table-column align="center" width='200px' fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openCoachInfo('check',scope.row, scope.$index)"
                         v-permission="'club-center/coach/check'">查看
              </el-button>
              <el-button type="primary" size="mini" @click="openCoachInfo('edit',scope.row, scope.$index)"
                         v-permission="'club-center/coach/edit'">编辑
              </el-button>
              <el-button type="primary" size="mini" @click="delCoach(scope.row)" v-if="scope.row.status === 1"
                         v-permission="'club-center/coach/del'">删除
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
  import coachInfoDialog from "./coachInfoDialog";

  export default {
    components: {
      coachInfoDialog,
      SearchForm,
    },
    data() {
      const defaultSearchForm = {
        status: '',
        chineseName: '',
        englishName: '',
      };

      return {

        loading: false,
        dialogVisible: false,
        dialogType: {},
        // 搜索字段
        defaultSearchForm,
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
        },
        statusArr: [
          {
            value: 1,
            label: "正常",
          },
          {
            value: 0,
            label: "已退出",
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


          // // 日期控件
          // defaultSearchDate,
          // transformSearchDate,
          // searchDateType,
          // searchDate: defaultSearchDate,
          // pickerOptions: {
          //   shortcuts: generateShortCuts(shortcutsConfig, typeof defaultSearchDate),
          // },
          //
          // // 下拉框控件
          // transferTypeArr: [],

          // 列表
          viewData: {
            list: [],
          },
        }
      }
    },
    created: function () {
      this.search();
    },
    mounted() {
    },
    methods: {
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
        _data["status"] = this.searchForm.status;
        _data["chineseName"] = this.searchForm.chineseName;
        _data["englishName"] = this.searchForm.englishName;
        return _data;
      },
      search(pageNo = 1) {
        if (!this.searchFormVerify()) return; // 搜索表单验证，若有
        this.loading = !this.loading;

        // 修复翻页号
        if (pageNo === 1) {
          this.data.pagination.currentPage = 1;
        }
        ;
        const _data = this.getData();
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_COACH_LIST, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.coachDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      delCoach(row) {
        this.$confirm('确定删除该教练员?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.loading = !this.loading;
          const params = {
            id: row.id,
            updateUser: this.$appData.userInfo.nickName,
          };
          this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.DEL_COACH, params).then((res) => {
            if (res.respCode === 'S0001') {
              this.loading = !this.loading;
              this.$message({type: 'success', message: '删除成功'});
              this.search();
            }
          }).catch((err) => {
            this.loading = !this.loading;
            this.$message({type: 'error', message: err.respMessage});
          });
        });
      },
      openCoachInfo(type = '', row = {}, index = '',) {
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
        if (row.status === 0) {
          return 'warning-row';
        }
        return '';
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

