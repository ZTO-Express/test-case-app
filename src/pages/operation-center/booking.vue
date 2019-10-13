<template>
  <div class="booking-center">
    <el-row class="header-box">
      <div style="text-align:right;margin-bottom: 10px">
        <el-button @click="openBookingList('new')" v-permission="'booking-center/booking/add'"
                   type="success" size="small" icon="el-icon-plus">新增预约
        </el-button>
      </div>
    </el-row>
    <el-row>
      <booking-list-dialog :visible.sync="dialogVisible" :dialogType="dialogType"
                        @submit="search(data.pagination.currentPage)"/>
    </el-row>
    <el-row class="search-box gap">
      <el-card align="left" id="el-bookingQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item prop="searchDate">
            <el-date-picker
              v-model="searchDate"
              :clearable="true"
              type="daterange"
              range-separator="至"
              start-placeholder="预约日期(起)"
              end-placeholder="预约日期(止)"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-select v-model.trim="searchForm.isReturnVisit" clearable placeholder="是否回访">
              <el-option v-for="item in isReturnVisitArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.isExperience" clearable placeholder="是否体验">
              <el-option v-for="item in isExperienceArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.isJoin" clearable placeholder="是否加入">
              <el-option v-for="item in isJoinArr" :key="item.value" :label="item.label"
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
          <el-table-column prop="bookingDate" label="预约日期" :formatter="formatDate"/>
          <el-table-column prop="bookingPlace" label="预约训练营"/>
          <el-table-column prop="source" label="来源">
            <template slot-scope="scope">
              <span v-if="scope.row.source === 'dianping'">大众点评</span>
              <span v-if="scope.row.source === 'friend'">朋友介绍</span>
              <span v-if="scope.row.source === 'other'">其他</span>
            </template>
          </el-table-column>
          <el-table-column prop="chineseName" label="中文名"/>
          <el-table-column prop="englishName" label="英文名"/>
          <el-table-column prop="birthday" label="生日" :formatter="formatDate"/>
          <el-table-column prop="age" label="年龄">
            <template slot-scope="scope">
              <span>{{scope.row.age}}岁</span>
            </template>
          </el-table-column>
          <el-table-column prop="height" label="身高">
            <template slot-scope="scope">
              <span>{{scope.row.height}}CM</span>
            </template>
          </el-table-column>
          <el-table-column prop="parentsName" label="家长姓名"/>
          <el-table-column prop="relation" label="关系">
            <template slot-scope="scope">
              <span v-if="scope.row.relation === 'father'">爸爸</span>
              <span v-if="scope.row.relation === 'mother'">妈妈</span>
              <span v-if="scope.row.relation === 'grandpa'">爷爷/外公</span>
              <span v-if="scope.row.relation === 'grandma'">奶奶/外婆</span>
            </template>
          </el-table-column>
          <el-table-column prop="concatWay" label="手机号码"/>
          <el-table-column prop="address" label="家庭住址"/>
          <el-table-column prop="isReturnVisit" label="是否回访">
            <template slot-scope="scope">
              <span v-if="scope.row.isReturnVisit === 0">未回访</span>
              <span v-if="scope.row.isReturnVisit === 1">已回访</span>
            </template>
          </el-table-column>
          <el-table-column prop="returnVisitDate" label="回访日期" :formatter="formatDate"/>
          <el-table-column prop="isExperience" label="是否体验">
            <template slot-scope="scope">
              <span v-if="scope.row.isExperience === 0">未体验</span>
              <span v-if="scope.row.isExperience === 1">已体验</span>
            </template>
          </el-table-column>
          <el-table-column prop="experienceDate" label="体验日期" :formatter="formatDate"/>
          <el-table-column prop="isJoin" label="是否加入">
            <template slot-scope="scope">
              <span v-if="scope.row.isJoin === 0">未加入</span>
              <span v-if="scope.row.isJoin === 1">已加入</span>
              <span v-if="scope.row.isJoin === 2">已退出</span>
            </template>
          </el-table-column>
          <el-table-column prop="joinDate" label="加入日期" :formatter="formatDate"/>
          <el-table-column prop="remark" label="备注"/>
          <el-table-column prop="remark1" label="备注1" v-if="false"/>
          <el-table-column prop="createUser" label="创建者"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="updateUser" label="更新者"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatTime"/>
          <el-table-column align="center" width='100' fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openBooking('edit',scope.row, scope.$index)"
                         v-permission="'booking-center/booking/edit'">编辑
              </el-button>
              <!--<el-button type="primary" size="mini" @click="delTestCase(scope.row)" v-if="scope.row.status === 1"-->
                         <!--v-permission="'test-case-center/test-case/del'">删除-->
              <!--</el-button>-->
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
  import BookingListDialog from "./bookingListDialog";

  export default {
    components: {
      BookingListDialog,
      SearchForm,
    },
    data() {
      const defaultSearchForm = {
        searchDate: '',
        isReturnVisit: '',
        isExperience: '',
        isJoin: '',
      };
      const defaultSearchDate = [
        moment(new Date().setDate(1)).format('YYYY-MM-DD'),
        moment(new Date()).format('YYYY-MM-DD'),
      ];

      return {

        loading: false,
        dialogVisible: false,
        dialogType: {},
        // 搜索字段
        defaultSearchForm,
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        // 日期控件
        defaultSearchDate,
        searchDate: [
          moment(new Date().setDate(1)).format('YYYY-MM-DD'),
          moment(new Date()).format('YYYY-MM-DD'),
        ],
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
        },
        isReturnVisitArr: [
          {
            value: 0,
            label: "未回访",
          },
          {
            value: 1,
            label: "已回访",
          }
        ],
        isExperienceArr: [
          {
            value: 0,
            label: "未体验",
          },
          {
            value: 1,
            label: "已体验",
          }
        ],
        isJoinArr: [
          {
            value: 0,
            label: "未加入",
          },
          {
            value: 1,
            label: "已加入",
          },
          {
            value: 2,
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
        _data["startTime"] = this.searchDate && this.searchDate[0] ? moment(this.searchDate[0]).format('YYYY-MM-DD 00:00:00') : '';
        _data["endTime"] = this.searchDate && this.searchDate[1] ? moment(this.searchDate[1]).format('YYYY-MM-DD 23:59:59') : '';
        _data["isReturnVisit"] = this.searchForm.isReturnVisit;
        _data["isExperience"] = this.searchForm.isExperience;
        _data["isJoin"] = this.searchForm.isJoin;
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
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_BOOKING_LIST, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.bookingDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      openBookingList(type = '', row = {}, index = '',) {
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
      tableRowClassName({row}) {
        if (row.isReturnVisit === 0) {
          return 'warning-row';
        }
        return '';
      },
      openBooking(type = '', row = {}, index = '',) {
        const data = JSON.parse(JSON.stringify(row));
        this.dialogVisible = !this.dialogVisible;
        this.dialogType = {type, data, index};
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

