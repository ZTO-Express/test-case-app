<template>
  <div class="place-center">
    <el-row class="header-box">
      <div style="text-align:right;margin-bottom: 10px">
        <el-button @click="openPlaceDialog('new')" v-permission="'place-center/place/add'"
                   type="success" size="small" icon="el-icon-plus">新增训练营
        </el-button>
      </div>
    </el-row>
    <el-row>
      <place-dialog :visible.sync="dialogVisible" :dialogType="dialogType"
                         @submit="search(data.pagination.currentPage)"/>
    </el-row>
    <el-row class="search-box gap">
      <el-card align="left" id="el-placeQuery">
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item>
            <el-select v-model="searchForm.placeId" clearable placeholder="训练营名称">
              <el-option v-for="item in placeList"
                         :key="item.id"
                         :label="item.placeName"
                         :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item>-->
          <!--<el-input v-model.trim="searchForm.placeAddress" clearable placeholder="训练营地址"/>-->
          <!--</el-form-item>-->
          <el-form-item>
            <el-select v-model="searchForm.status" clearable placeholder="训练营状态">
              <el-option v-for="item in statusArr"
                         :key="item.value"
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
                  element-loading-background="#fff"
                  :row-class-name="tableRowStatus">
          <el-table-column
            label="序号"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column prop="id" label="编号" v-if="false"/>
          <el-table-column prop="placeName" label="训练营名称"/>
          <el-table-column width="180px" prop="placeAddress" label="训练营地址"/>
          <el-table-column prop="status" label="训练营状态">
            <template slot-scope="scope">
              <span v-if="scope.row.status === 0">已弃用</span>
              <span v-if="scope.row.status === 1">使用中</span>
            </template>
          </el-table-column>
          <el-table-column prop="placeRent" label="训练营租金" :formatter="formatAmount"/>
          <el-table-column width="200px" prop="remark" label="备注">
            <template slot-scope="scope">
              <el-popover v-if="needPop(scope.row.remark)" placement="top-start" width="200" trigger="hover"
                          :content="scope.row.remark">
                <span slot="reference">{...}</span>
              </el-popover>
              <span v-else>{{scope.row.remark}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createUser" label="创建者"/>
          <el-table-column prop="createTime" label="创建时间" :formatter="formatTime"/>
          <el-table-column prop="updateUser" label="更新者"/>
          <el-table-column prop="updateTime" label="更新时间" :formatter="formatTime"/>
          <el-table-column align="center" width='140' fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openPlaceDialog('edit',scope.row, scope.$index)"
                         v-if="scope.row.status === 1"
                         v-permission="'place-center/place/edit'">编辑
              </el-button>
              <el-button type="primary" size="mini" @click="delClasses(scope.row)" v-if="scope.row.status === 1"
                         v-permission="'place-center/place/del'">删除
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
  import PlaceDialog from "./placeDialog";

  export default {
    components: {
      PlaceDialog,
      SearchForm,
    },
    data() {
      const defaultSearchForm = {
        placeName: '',
        placeAddress: '',
        status: '',
      };
      return {
        loading: false,
        dialogVisible: false,
        dialogType: {},
        placeList: [],
        // 搜索字段
        defaultSearchForm,
        searchForm: JSON.parse(JSON.stringify(defaultSearchForm)),
        statusArr: [
          {
            value: 0,
            label: "已停用",
          },
          {
            value: 1,
            label: "使用中",
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
      this.initPlaceList();
      this.search();
    },
    mounted() {
    },
    methods: {
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
        _data["id"] = this.searchForm.placeId;
        _data["placeAddress"] = this.searchForm.placeAddress;
        _data["status"] = this.searchForm.status;
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
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_PLACE_LIST, _data).then((res) => {
          this.loading = !this.loading;
          this.data.pagination.total = res.data.count;
          this.data.viewData.list = res.data.placeDtoList;
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      delClasses(row) {
        this.$confirm('确定删除该训练营?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.loading = !this.loading;
          const params = {
            id: row.id,
            updateUser: this.$appData.userInfo.nickName,
          };
          this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.DEL_PLACE, params).then((res) => {
            if (res.respCode === 'S0001') {
              this.loading = !this.loading;
              this.$message({type: 'success', message: '删除成功'});
              this.search();
            }
          }).catch((err) => {
            this.loading = !this.loading;
            this.$message({type: 'error', message: err.respMessage});
          });
        }).catch(() => {

        });
      },
      openPlaceDialog(type = '', row = {}, index = '',) {
        const data = JSON.parse(JSON.stringify(row));
        this.dialogVisible = !this.dialogVisible;
        this.dialogType = {type, data, index};
      },
      tableRowStatus({row}) {
        if (row.status === 0) {
          return 'warning-row';
        }
        return '';
      },
      formatTime(row, col, val, index) { //格式化日期
        return this.$formatters.formatTime(val);
      },
      formatAmount(row, col, val, index) { // 格式化金额 千位符
        if (!val || val === '0') return '--';
        return '￥' + val;
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

