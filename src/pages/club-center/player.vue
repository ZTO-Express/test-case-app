<template>
  <div class="player-center">
    <el-row class="header-box">
      <div style="text-align:right;margin-bottom: 10px">
        <el-button @click="openPlayerInfo('new')" v-permission="'player-center/player/add'"
                   type="success" size="small" icon="el-icon-plus">新增球员
        </el-button>
      </div>
    </el-row>
    <el-row>
      <player-info-dialog :visible.sync="dialogVisiblePlayerInfo" :dialogType="dialogTypPlayerInfo"
                          @submit="search(data.pagination.currentPage)"/>
      <player-charge-dialog :visible.sync="dialogVisiblePlayerCharge" :dialogType="dialogTypePlayerCharge"
                            @submit="search(data.pagination.currentPage)"/>
    </el-row>
    <el-row class="search-box gap">
      <el-card align="left" id="el-playerQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item prop="userIdArr"
                        v-if="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'">
            <el-select v-model="searchForm.userName" filterable clearable placeholder="球员姓名"
                       v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'
                       || this.$appData.userInfo.roles[0].roleName == 'Boss'">
              <el-option v-for="user in userList"
                         :key="user.userId"
                         :label="user.chineseName + '(' + user.englishName + ')'"
                         :value="user.userId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model.trim="searchForm.status" clearable placeholder="球员状态">
              <el-option v-for="item in statusArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item label="英文名字">-->
          <!--<el-input v-model="searchForm.englishName" clearable placeholder="英文名字"></el-input>-->
          <!--</el-form-item>-->
          <el-form-item prop="searchDate">
            <el-date-picker
              v-model="searchDate"
              :clearable="true"
              type="daterange"
              range-separator="至"
              start-placeholder="出生日期(起)"
              end-placeholder="出生日期(止)"
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
        <el-table :data="data.viewData.list" border v-loading="loading"
                  ref="table"
                  tooltip-effect="light"
                  show-overflow-tooltip
                  element-loading-text="拼命加载中"
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="#fff"
                  :default-sort="{prop:'totalRemainTime',order:'ascending'}"
                  @sort-change="searchBySort"
                  :row-class-name="tableRowClassName">
          <el-table-column label="序号"
                           type="index"
                           width="50"></el-table-column>
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="球衣号码:">
                  <span>{{ props.row.no }}</span>
                </el-form-item>
                <el-form-item label="所属班级:">
                  <span>{{ props.row.classesName }}</span>
                </el-form-item>
                <el-form-item label="生日:">
                  <span>{{ formatDate(props.row, '', props.row.birthday) }}</span>
                </el-form-item>
                <el-form-item label="加入日期:">
                  <span>{{ props.row.joinDate }}</span>
                </el-form-item>
                <el-form-item label="国籍:">
                  <span>{{ props.row.nationality }}</span>
                </el-form-item>
                <el-form-item label="身高(CM):" prop="height">
                  <span>{{ props.row.height }}</span>
                </el-form-item>
                <el-form-item label="体重(KG):">
                  <span>{{ props.row.weight }}</span>
                </el-form-item>
                <el-form-item label="生肖:">
                  <span>{{ props.row.chineseZodiac }}</span>
                </el-form-item>
                <el-form-item label="星座:">
                  <span>{{ props.row.constellation }}</span>
                </el-form-item>
                <el-form-item label="球龄:">
                  <span>{{ props.row.yearsExp }}</span>
                </el-form-item>
                <el-form-item label="主力脚:">
                  <span>{{ props.row.strongerFoot }}</span>
                </el-form-item>
                <el-form-item label="位置:">
                  <span>{{ props.row.position }}</span>
                </el-form-item>
                <el-form-item label="最喜欢的球星:">
                  <span>{{ props.row.favoritePlayer }}</span>
                </el-form-item>
                <el-form-item label="最喜欢的俱乐部:">
                  <span>{{ props.row.favoriteClub }}</span>
                </el-form-item>
                <el-form-item label="爱好:">
                  <span>{{ props.row.hobby }}</span>
                </el-form-item>
                <el-form-item label="梦想:">
                  <span>{{ props.row.dream }}</span>
                </el-form-item>
                <el-form-item label="备注:">
                  <span>{{ props.row.remark }}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column prop="id" label="编号" v-if="false"/>
          <el-table-column prop="userId" label="球员编号" v-if="false"/>
          <el-table-column prop="chineseName" label="中文名"/>
          <el-table-column prop="englishName" label="英文名"/>
          <el-table-column prop="mobile" label="手机号"/>
          <el-table-column prop="accountNo" label="账号"/>
          <el-table-column prop="balance" label="账户余额(元)" :formatter="formatAmount"/>
          <el-table-column prop="totalRemainTime" label="剩余课时(节)" sortable="custom">
            <!--<template slot-scope="scope">-->
              <!--<span>{{scope.row.remainTime + scope.row.nextRemainTime}}</span>-->
            <!--</template>-->
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template slot-scope="scope">
              <span v-if="scope.row.status === 0">已退出</span>
              <span v-if="scope.row.status === 1">正常</span>
            </template>
          </el-table-column>
          <el-table-column prop="createUser" label="创建者"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="updateUser" label="更新者"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatTime"/>
          <el-table-column align="center" width='250px' label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openPlayerInfo('check',scope.row, scope.$index)"
                         v-permission="'club-center/player/check'">查看
              </el-button>
              <el-button type="primary" size="mini" @click="openPlayerInfo('edit',scope.row, scope.$index)"
                         v-permission="'club-center/player/edit'">编辑
              </el-button>
              <el-button type="primary" size="mini" @click="delPlayer(scope.row)" v-if="scope.row.status === 1"
                         v-permission="'club-center/player/del'">删除
              </el-button>
              <el-button type="primary" size="mini" @click="openChargeDialog(scope.row)"
                         v-permission="'club-center/player/charge'">充值
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
  import moment from 'moment';
  import SearchForm from '../../components/search-form/searchForm.vue';
  import playerInfoDialog from "./playerInfoDialog";
  import playerChargeDialog from "../dashboard-page/playerChargeDialog";

  export default {
    components: {
      playerInfoDialog,
      playerChargeDialog,
      SearchForm,
    },
    data() {
      const defaultSearchForm = {
        status: '',
        userName: '',
        chineseName: '',
        englishName: '',
        searchDate: '',
        prop: '',
        order: '',
      };
      const defaultSearchDate = [
        // moment(new Date().setDate(1)).format('YYYY-MM-DD'),
        // moment(new Date()).format('YYYY-MM-DD'),
      ];
      return {
        loading: false,
        dialogVisiblePlayerInfo: false,
        dialogVisiblePlayerCharge: false,
        dialogTypPlayerInfo: {},
        dialogTypePlayerCharge: {},
        userList: [],
        classesList: [],
        searchDate: [
          // moment(new Date().setDate(1)).format('YYYY-MM-DD'),
          // moment(new Date()).format('YYYY-MM-DD'),
        ],
        defaultSearchDate,
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
          // 列表
          viewData: {
            list: [],
          },
        }
      }
    },
    created: function () {
      // this.search();
      this.initUserList();
    },
    mounted() {
    },
    methods: {
      initUserList() {
        let param = {};
        param.pageSize = 10000;
        param.pageNo = 1;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_PLAYER_LIST, param).then((res) => {
          this.userList = res.data.playerDtoList;
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
        _data["startTime"] = this.searchDate && this.searchDate[0] ? moment(this.searchDate[0]).format('YYYY-MM-DD') : '';
        _data["endTime"] = this.searchDate && this.searchDate[1] ? moment(this.searchDate[1]).format('YYYY-MM-DD') : '';
        _data["status"] = this.searchForm.status;
        _data["userId"] = this.searchForm.userName;
        _data["prop"] = this.searchForm.prop;
        _data["order"] = this.searchForm.order;
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
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_PLAYER_LIST, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.playerDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      delPlayer(row) {
        this.$confirm('确定删除该球员?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.loading = !this.loading;
          const params = {
            id: row.id,
            updateUser: this.$appData.userInfo.nickName,
          };
          this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.DEL_PLAYER, params).then((res) => {
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
      openPlayerInfo(type = '', row = {}, index = '',) {
        const data = JSON.parse(JSON.stringify(row));
        this.dialogVisiblePlayerInfo = !this.dialogVisiblePlayerInfo;
        this.dialogTypPlayerInfo = {type, data, index};
      },
      openChargeDialog(row) {
        const data = JSON.parse(JSON.stringify(row));
        this.dialogVisiblePlayerCharge = !this.dialogVisiblePlayerCharge;
        this.dialogTypePlayerCharge = {data};
      },
      formatDate(row, col, val, index) { //格式化日期
        return this.$formatters.formatDate(val);
      },
      formatTime(row, col, val, index) { //格式化时间
        return this.$formatters.formatTime(val);
      },
      formatAmount(row, col, val, index) { // 格式化金额 千位符
        // if (!val || val === '0') return '--';
        return '￥' + val;
      },
      tableRowClassName({row}) {
        if (row.status === 0) {
          return 'warning-row';
        }
        return '';
      },
      searchBySort(column, prop, order) {
        if (column.prop == null || column.order == null) {
          this.searchForm.prop = "";
          this.searchForm.order = "";
        } else {
          if (column.prop == "totalRemainTime") {
            this.searchForm.prop = "total_remain_time";
          }
          if (column.order == "ascending") {
            this.searchForm.order = "ASC";
          }
          if (column.order == "descending") {
            this.searchForm.order = "DESC";
          }
        }
        this.search(1);
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

  .demo-table-expand {
    font-size: 0;
  }

  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

</style>

