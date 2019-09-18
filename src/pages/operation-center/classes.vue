<template>
  <div class="classes-center">
    <el-row class="header-box">
      <div style="text-align:right;margin-bottom: 10px">
        <el-button @click="openClasses('new')" v-permission="'classes-center/classes/add'"
                   type="success" size="small" icon="el-icon-plus">新增训练班
        </el-button>
      </div>
    </el-row>
    <el-row>
      <classes-list-dialog :visible.sync="dialogVisible" :dialogType="dialogType"
                           @submit="search(data.pagination.currentPage)"/>
    </el-row>
    <el-row class="search-box gap">
      <el-card align="left" id="el-classesQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item>
            <el-select v-model.trim="searchForm.classesName" clearable placeholder="训练班名称">
              <el-option v-for="item in classesList" :key="item.id" :label="item.classesName"
                         :value="item.classesName"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="searchForm.classesDay" clearable placeholder="训练日期">
              <el-option v-for="item in classesDayArr" :key="item.label" :label="item.label"
                         :value="item.label"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model.trim="searchForm.echelonId" clearable placeholder="梯队名称">
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
                  element-loading-background="#fff">
          <el-table-column
            label="序号"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column prop="id" label="编号" v-if="false"/>
          <el-table-column width="140px" prop="classesName" label="训练班名称"/>
          <el-table-column prop="echelonId" label="梯队名称" :formatter="formatEchelonName"/>
          <el-table-column prop="placeId" label="场地名称" :formatter="formatPlaceName"/>
          <el-table-column prop="classesPrice" label="训练班单价(元/节)" :formatter="formatAmount"/>
          <el-table-column prop="classesDay" label="训练日期"/>
          <el-table-column prop="remark" label="备注"/>
          <el-table-column prop="createUser" label="创建者"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="updateUser" label="更新者"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatTime"/>
          <el-table-column align="center" width='200' fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openClasses('edit',scope.row, scope.$index)"
                         v-permission="'classes-center/classes/edit'">编辑
              </el-button>
              <el-button type="primary" size="mini" @click="delClasses(scope.row)" v-if="scope.row.status === 1"
                         v-permission="'classes-center/classes/del'">删除
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
  import ClassesListDialog from "./classesDialog";

  export default {
    components: {
      ClassesListDialog,
      SearchForm,
    },
    data() {
      const defaultSearchForm = {
        classesName: '',
        echelonId: '',
        placeId: '',
        classesDay: '',
      };
      return {
        loading: false,
        dialogVisible: false,
        dialogType: {},
        // 搜索字段
        defaultSearchForm,
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        classesList: [],
        echelonList: [],
        placeList: [],
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
      this.initClassesList();
      this.initEchelonList();
      this.initPlaceList();
    },
    mounted() {
    },
    methods: {
      initClassesList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_CLASSES_NAME, param).then((res) => {
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
        _data["classesName"] = this.searchForm.classesName;
        _data["echelonId"] = this.searchForm.echelonId;
        _data["placeId"] = this.searchForm.placeId;
        _data["classesDay"] = this.searchForm.classesDay;
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
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_CLASSES_LIST, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.classesDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      delClasses(row) {
        this.$confirm('确定删除该训练班?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.loading = !this.loading;
          const params = {
            id: row.id,
            updateUser: this.$appData.userInfo.nickName,
          };
          this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.DEL_CLASSES, params).then((res) => {
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
      openClasses(type = '', row = {}, index = '',) {
        const data = JSON.parse(JSON.stringify(row));
        this.dialogVisible = !this.dialogVisible;
        this.dialogType = {type, data, index};
      },
      // tableRowClassName({row}) {
      //   if (row.isReturnVisit === 0) {
      //     return 'warning-row';
      //   }
      //   return '';
      // },
      formatTime(row, col, val, index) { //格式化日期
        return this.$formatters.formatTime(val);
      },
      formatAmount(row, col, val, index) { // 格式化金额 千位符
        if (!val || val === '0') return '--';
        return '￥' + val;
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
          if (val.echelonId == this.placeList[i].id) {
            placeName = this.placeList[i].placeName;
          }
        }
        return placeName
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

