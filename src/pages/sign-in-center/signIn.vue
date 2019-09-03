<template>
  <div>
    <el-row class="header-box">
      <div style="text-align:right;margin-bottom: 10px">
        <el-button @click="openSignInDialog('confirm')" v-permission="'sign-center/signIn/confirm'"
                   type="success" size="small" icon="el-icon-check">确认签到
        </el-button>
        <el-button @click="openSignInDialog('cancel')" v-permission="'sign-center/signIn/cancel'"
                   type="danger" size="small" icon="el-icon-close">取消签到
        </el-button>
      </div>
    </el-row>
    <el-row>
      <sign-in-dialog :visible.sync="dialogVisible" :dialogType="dialogType"
                      @submit="search(data.pagination.currentPage)"/>
    </el-row>
    <el-row class="search-box gap">
      <el-card align="left" id="el-sessionQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item>
            <el-select v-model.trim="searchForm.classesName" clearable placeholder="训练班">
              <el-option v-for="item in classesList" :key="item.classesName" :label="item.classesName"
                         :value="item.classesName"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="trainingDate">
            <el-date-picker style="width: 236.67px" v-model="searchForm.trainingDate"
                            type="date"
                            placeholder="训练日期"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd"
                            size="small"
            >
            </el-date-picker>
          </el-form-item>
          <!--<el-form-item>-->
          <!--<el-select v-model="searchForm.classesDay" clearable placeholder="训练日">-->
          <!--<el-option v-for="item in classesDayArr" :key="item.label" :label="item.label"-->
          <!--:value="item.label"></el-option>-->
          <!--</el-select>-->
          <!--</el-form-item>-->
          <el-form-item>
            <el-select v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'
                       || this.$appData.userInfo.roles[0].roleName == 'Boss'" v-model="searchForm.roleName" clearable
                       placeholder="签到角色">
              <el-option v-for="item in roleNameArr" :key="item.label" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'
                       || this.$appData.userInfo.roles[0].roleName == 'Boss'" v-model="searchForm.userName" clearable
                       placeholder="签到名单">
              <el-option v-for="user in userList"
                         :key="user.userId"
                         :label="user.englishName + '(' + user.chineseName + ')'"
                         :value="user.chineseName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.signInType" clearable placeholder="签到类型">
              <el-option v-for="item in signInTypeArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.signInState" clearable placeholder="签到状态">
              <el-option v-for="item in signInStateArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.status" clearable placeholder="训练课状态">
              <el-option v-for="item in statusArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'
                       || this.$appData.userInfo.roles[0].roleName == 'Boss'" v-model.trim="searchForm.echelonId"
                       clearable placeholder="梯队名称">
              <el-option v-for="item in echelonList" :key="item.id" :label="item.echelonName"
                         :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.placeId" clearable placeholder="场地名称">
              <el-option v-for="item in placeList" :key="item.id" :label="item.placeName"
                         :value="item.id"></el-option>
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
                  :row-class-name="tableRowStatus">
          <el-table-column
            label="序号"
            type="index"
            width="60">
          </el-table-column>
          <el-table-column prop="id" label="编号" v-if="false"/>
          <el-table-column prop="trainingSessionId" label="课程编号" v-if="false"/>
          <el-table-column prop="userName" label="签到名单"/>
          <el-table-column prop="roleName" label="签到角色">
            <template slot-scope="scope">
              <span v-if="scope.row.roleName === 'Player'">球员</span>
              <span v-if="scope.row.roleName === 'Coach'">教练</span>
            </template>
          </el-table-column>
          <el-table-column width="180px" prop="classesName" label="训练班"/>
          <el-table-column width="170px" prop="trainingDate" label="训练日期">
            <template slot-scope="scope">
              <span>{{formatDate(scope.row.trainingDate)}}({{scope.row.classesDay}}) </span>
            </template>
          </el-table-column>
          <el-table-column width="100px" prop="trainingTimeStart" label="开始时间"/>
          <el-table-column width="100px" prop="trainingTimeEnd" label="结束时间"/>
          <el-table-column width="80px" prop="echelonId" label="梯队" :formatter="formatEchelonName"/>
          <el-table-column prop="placeId" label="场地" :formatter="formatPlaceName"/>
          <el-table-column width="100px" prop="status" label="课程状态">
            <template slot-scope="scope">
              <span v-if="scope.row.status === 0">等待开课</span>
              <span v-if="scope.row.status === 1" style="color: forestgreen">已开课</span>
              <span v-if="scope.row.status === 2" style="color: red">已停课</span>
            </template>
          </el-table-column>
          <el-table-column prop="signInType" label="签到类型">
            <template slot-scope="scope">
              <span v-if="scope.row.signInType === 1" style="color: forestgreen">确认签到</span>
              <span v-if="scope.row.signInType === 2" style="color: red">取消签到</span>
            </template>
          </el-table-column>
          <el-table-column prop="signInState" label="签到状态">
            <template slot-scope="scope">
              <span v-if="scope.row.signInState === 0">待审核</span>
              <span v-if="scope.row.signInState === 1" style="color: forestgreen">成功</span>
              <span v-if="scope.row.signInState === 2" style="color: red">失败</span>
            </template>
          </el-table-column>
          <el-table-column width="200px" prop="remark" label="签到备注">
            <template slot-scope="scope">
              <el-popover v-if="needPop(scope.row.remark)" placement="top-start" width="200" trigger="hover"
                          :content="scope.row.remark">
                <span slot="reference">{...}</span>
              </el-popover>
              <span v-else>{{scope.row.remark}}</span>
            </template>
          </el-table-column>
          <el-table-column width="100px" prop="auditStatus" label="审核状态">
            <template slot-scope="scope">
              <span v-if="scope.row.auditStatus === 0">待审核</span>
              <span v-if="scope.row.auditStatus === 1">审核通过</span>
              <span v-if="scope.row.auditStatus === 2">审核拒绝</span>
            </template>
          </el-table-column>
          <el-table-column width="200px" prop="remark1" label="审核意见">
            <template slot-scope="scope">
              <el-popover v-if="needPop(scope.row.remark1)" placement="top-start" width="200" trigger="hover"
                          :content="scope.row.remark1">
                <span slot="reference">{...}</span>
              </el-popover>
              <span v-else>{{scope.row.remark1}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="auditUser" label="审核人"/>
          <el-table-column prop="auditTime" label="审核时间" :formatter="formatTime"/>
          <el-table-column prop="createUser" label="创建人"/>
          <el-table-column width="170px" prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="updateUser" label="更新人"/>
          <el-table-column width="170px" prop="updateTime" label="更新时间" :formatter="formatTime"/>
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
  import SignInDialog from "./signInDialog";

  export default {
    components: {
      SignInDialog,
      SearchForm,
    },
    data() {
      const defaultSearchForm = {
        trainingDay: '',
        classesName: '',
        echelonId: '',
        placeId: '',
        classesDay: '',
        status: '',
        signInType: '',
        signInStatus: '',
        roleName: '',
        userName: '',
      };
      return {
        loading: false,
        dialogVisible: false,
        dialogType: {},
        defaultSearchForm,
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        userList: [],
        classesList: [],
        echelonList: [],
        placeList: [],
        statusArr: [
          {
            value: 0,
            label: "等待开课",
          },
          {
            value: 1,
            label: "已开课",
          },
          {
            value: 2,
            label: "已停课",
          },
        ],
        signInTypeArr: [
          {
            value: 1,
            label: "确认签到",
          },
          {
            value: 2,
            label: "取消签到",
          },
        ],
        roleNameArr: [
          {
            value: "Coach",
            label: "教练",
          },
          {
            value: "Player",
            label: "球员",
          },
        ],
        signInStateArr: [
          {
            value: 0,
            label: "待审核",
          },
          {
            value: 1,
            label: "成功",
          },
          {
            value: 2,
            label: "失败",
          },
        ],
        classesDayArr: [
          {
            value: 0,
            label: "星期一",
          },
          {
            value: 1,
            label: "星期二",
          },
          {
            value: 2,
            label: "星期三",
          },
          {
            value: 3,
            label: "星期四",
          },
          {
            value: 4,
            label: "星期五",
          },
          {
            value: 5,
            label: "星期六",
          },
          {
            value: 6,
            label: "星期日",
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
      this.initUserList();
      this.initClassesList();
      this.initEchelonList();
      this.initPlaceList();
    },
    mounted() {
    },
    methods: {
      initUserList() {
        let param = {};
        param.pageSize = 10000;
        param.pageNo = 1;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_MEMBER_LIST, param).then((res) => {
          this.userList = res.data.memberDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initClassesList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_CLASSES_LIST, param).then((res) => {
          this.classesList = res.data.classesDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initEchelonList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_ECHELON_LIST, param).then((res) => {
          this.echelonList = res.data.echelonDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initPlaceList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_PLACE_LIST, param).then((res) => {
          this.placeList = res.data.placeDtoList;
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
        _data["roleName"] = this.searchForm.roleName;
        _data["classesName"] = this.searchForm.classesName;
        _data["echelonId"] = this.searchForm.echelonId;
        _data["placeId"] = this.searchForm.placeId;
        _data["classesDay"] = this.searchForm.classesDay;
        _data["trainingDate"] = this.searchForm.trainingDate;
        _data["status"] = this.searchForm.status;
        _data["signInType"] = this.searchForm.signInType;
        _data["signInState"] = this.searchForm.signInState;
        _data["userName"] = this.searchForm.userName;
        _data["userId"] = this.$appData.userInfo.roles[0].roleName != 'Player' ? '' : this.$appData.userInfo.userId;
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
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_SIGN_IN_LIST, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.signInDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      signInSession(type, row) {
        let text = "";
        let msg = "";
        let status;
        if (type == 'confirm') {
          text = '确认签到日期为：' + this.formatDate(row.trainingDate);
          msg = "签到确认成功";
          status = 1;
        } else {
          text = '确认取消签到日期为：' + this.formatDate(row.trainingDate);
          msg = "签到取消成功";
          status = 2;
        }
        this.$confirm(text, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.loading = !this.loading;
          const params = {
            trainingSessionId: row.id,
            userIdArr: this.$appData.userInfo.userId,
            userNameArr: this.$appData.userInfo.nickName,
            signInState: status,
            createUser: this.$appData.userInfo.nickName,
            updateUser: this.$appData.userInfo.nickName,
          };
          this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.SIGN_IN_SESSION, params).then((res) => {
            if (res.respCode === 'S0001') {
              this.loading = !this.loading;
              this.$message({type: 'success', message: res.respMessage});
              this.search();
            }
          }).catch((err) => {
            this.loading = !this.loading;
            this.$message({type: 'error', message: err.respMessage});
          });
        });
      },
      openSignInDialog(type = '', row = {}, index = '',) {
        const data = JSON.parse(JSON.stringify(row));
        this.dialogVisible = !this.dialogVisible;
        this.dialogType = {type, data, index};
      },
      tableRowStatus({row}) {
        if (row.signInState === 1) {
          return 'success-row';
        }
        if (row.signInState === 2) {
          return 'warning-row';
        }
        return '';
      },
      formatDate(val) { //格式化日期
        return this.$formatters.formatDate(val);
      },
      formatTime(row, col, val, index) { //格式化日期
        return this.$formatters.formatTime(val);
      },
      formatEchelonName(val) {
        let echelonName;
        for (let i = 0; i < this.echelonList.length; i++) {
          if (val.echelonId == this.echelonList[i].id) {
            echelonName = this.echelonList[i].echelonName;
          }
        }
        return echelonName
      },
      formatPlaceName(val) {
        let placeName;
        for (let i = 0; i < this.placeList.length; i++) {
          if (val.placeId == this.placeList[i].id) {
            placeName = this.placeList[i].placeName;
          }
        }
        return placeName
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

