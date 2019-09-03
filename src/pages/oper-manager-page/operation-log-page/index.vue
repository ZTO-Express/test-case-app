<template>
  <div>

    <el-card class="box-card search-panel">
      <el-form ref="searchForm" :model="searchForm" :inline="true" align="left">
        <el-form-item prop="timeRange">
          <el-date-picker
            size="mini"
            v-model="searchForm.timeRange"
            @change="dateChange"
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            :clearable="true"
            :editable="false"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            :picker-options="datePickerOptions">
          </el-date-picker>
        </el-form-item>

        <el-form-item prop="sessionToken">
          <el-input clearable v-model="searchForm.sessionToken" placeholder="精确查询" size="mini">
            <template slot="prepend">登录码：</template>
          </el-input>
        </el-form-item>

        <el-form-item prop="actionLevel">
          <el-select v-model="searchForm.actionLevel" placeholder="操作危险级别" size="mini" clearable>
            <el-option
              v-for="item in dangerLevelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item prop="operatorName">
          <el-input clearable v-model="searchForm.operatorName" placeholder="姓名" size="mini"/>
        </el-form-item>
        <el-form-item prop="actionName">
          <el-input clearable v-model="searchForm.actionName" placeholder="操作名称" size="mini"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="search" size="mini" icon="el-icon-search">查询</el-button>
          <el-button @click="$refs.searchForm.resetFields()" size="mini" icon="el-icon-close">清空</el-button>
        </el-form-item>
      </el-form>
      <div class="tx14 tx-hint" align="left">
        开始和结束时间不填，默认获取当天数据
      </div>
    </el-card>

    <el-card class="box-card">
      <el-table
        :data="list"
        border>
        <el-table-column
          prop="sessionToken"
          align="center"
          label="登录码">
        </el-table-column>

        <el-table-column
          prop="actionDate"
          align="center"
          label="操作时间">
          <template slot-scope="scope">
            {{formatTime(scope.row.actionDate)}}
          </template>
        </el-table-column>

        <el-table-column
          prop="operatorName"
          align="center"
          label="姓名">
        </el-table-column>

        <el-table-column
          prop="ipAddress"
          align="center"
          label="IP地址">
        </el-table-column>

        <el-table-column
          prop="actionGroupName"
          align="center"
          width="150"
          label="操作分类名称">
        </el-table-column>

        <el-table-column
          prop="actionName"
          align="center"
          label="操作名称"
          width="150">
        </el-table-column>

        <el-table-column
          prop="requestParams"
          align="center"
          label="请求详情"
          width="150">
          <template slot-scope="scope">
            <a href="javascript:void(0);" @click="showLongText(scope.row.requestParams, scope.row.action)"
               :disabled="isBlank(scope.row.requestParams)">
              {{isBlank(scope.row.requestParams) ? '空' : '{...}'}}
            </a>
          </template>
        </el-table-column>

        <el-table-column
          prop="opResultMsg"
          align="center"
          label="返回消息"
          width="150">
        </el-table-column>

        <el-table-column
          prop="opResultDetails"
          align="center"
          label="返回详细内容"
          width="150">
          <template slot-scope="scope">
            <a href="javascript:void(0);" @click="showLongText(scope.row.opResultDetails, '详细内容')"
               :disabled="isBlank(scope.row.opResultDetails)">
              {{isBlank(scope.row.opResultDetails) ? '空' : '{...}'}}
            </a>

          </template>
        </el-table-column>

        <el-table-column
          prop="actionLevel"
          align="center"
          label="操作危险级别"
          width="150">
          <template slot-scope="scope">
            {{getDangerLevelMsg(scope.row.actionLevel)}}
          </template>
        </el-table-column>
      </el-table>
      <div align="center">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="listQuery.pageNo"
          layout="total,sizes, prev, pager, next, jumper"
          :page-size="listQuery.pageSize"
          :total="listQuery.total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>
<script>
import moment from 'moment';

export default {
  data() {
    return {
      searchForm: {
        operatorName: '',
        timeRange: '',
        sessionToken: '',
        actionLevel: '',
      },
      list: [],
      listQuery: {
        pageNo: 1,
        pageSize: 10,
        total: 0,
      },
      datePickerOptions: {
        disabledDate(time) {
          const d = 7776000000;
          return time.getTime() >= Date.now();
        },
      },

      dangerLevelOptions: [{
        value: '0',
        label: '【0】低风险',
      }, {
        value: '1',
        label: '【1】正常',
      }, {
        value: '2',
        label: '【2】受信任',
      }, {
        value: '3',
        label: '【3】重要或危险',
      }],
      dialogVisible: false,
      dialogTitle: '',
      dialogText: '',
    };
  },
  created() {
    this.getList();
  },
  components: {},
  computed: {},
  watch: {},
  methods: {
    search() {
      this.getList();
    },

    getList() {
      const param = {
      };

      Object.assign(param, this.searchForm, this.listQuery);
      param.beginDate = this.searchForm.timeRange[0];
      param.endDate = this.searchForm.timeRange[1];
      param.timeRange = '';
      this.$action.operationLogAction.operationLogList(param)
        .then((resp) => {
          this.list = [];
          this.listQuery.total = resp.data.total;
          resp.data.list.forEach((item) => {
            this.list.push(item);
          });
        }).catch((error) => {
          this.$message.error(error.respMessage);
        });
    },

    handleCurrentChange(val) {
      this.listQuery.pageNo = val;
      this.getList();
    },

    handleSizeChange(size) {
      this.listQuery.pageSize = size;
      this.getList();
    },

    formatTime(timeStr, format) {
      if (!format) {
        format = 'YYYY-MM-DD HH:mm:ss';
      }
      return moment(timeStr).format(format);
    },

    getDangerLevelMsg(key) {
      let result = '未知';
      this.dangerLevelOptions.forEach((item) => {
        if (item.value === key) {
          result = item.label;
        }
      });
      return result;
    },

    isBlank(str) {
      if (str === 'null' || str === null || str === undefined || str === '') {
        return true;
      }
      return false;
    },

    showLongText(text, title) {
      const h = this.$createElement;
      this.$msgbox({
        title,
        message: h('p', { style: 'color: teal; word-wrap:break-word;' }, text),
        showCancelButton: false,
        confirmButtonText: '确定',
        dangerouslyUseHTMLString: true,
      });
    },

    dateChange(currentDate) {
      if (currentDate && currentDate.length && currentDate.length > 0) {
        const endDate = currentDate[1];
        if (endDate.indexOf('00:00:00') > -1) {
          currentDate[1] = currentDate[1].replace('00:00:00', '23:59:59');
          return currentDate;
        }
        return currentDate;
      }
      return currentDate;
    },
  },
};
</script>

<style scoped>

</style>
