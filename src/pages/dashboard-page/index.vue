<template>
  <div class="statistics-center-tendency">
    <el-row class="search-box">
      <el-card>
        <el-form ref="searchForm"
                 :model="searchForm"
                 size="small"
                 :inline="true">
          <el-form-item prop="searchDate">
            <el-date-picker v-model="searchDate"
                            :clearable="true"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期(起)"
                            end-placeholder="结束日期(止)"
                            :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'">
            <el-select v-model="searchForm.userName"
                       @change="selectChanged"
                       filterable
                       clearable
                       placeholder="测试负责人">
              <el-option v-for="user in userList"
                         :key="user.userId"
                         :label="user.nickName"
                         :value="user.nickName">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="search"
                       type="primary"
                       :loading="loading">查询</el-button>
            <el-button @click="clear">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-row>
    <el-row :gutter="20"
            class="card-box">
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'">
        <el-card>
          <i class="icon icon-warehouse-o"
             style="color:#23b7e5"></i>
          <div class="count-box">
            <p>{{list.incomeAmount}}</p>
            <span>用例总数</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'">
        <el-card>
          <i class="icon icon-add-o"
             style="color:#23b7e5"></i>
          <div class="count-box">
            <p>{{list.chargeAmount}}</p>
            <span>新增用例数</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'">
        <el-card>
          <i class="icon icon-view-list-o"
             style="color:#4CAF50"></i>
          <div class="count-box">
            <p>{{list.playerCount}}</p>
            <span>计划总数</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'">
        <el-card>
          <i class="icon icon-add-o"
             style="color:#4CAF50"></i>
          <div class="count-box">
            <p>{{list.bookingCount}}</p>
            <span>新增计划数</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-card>
        <el-radio-group size="small"
                        v-model="countType"
                        @change="selectFilterChange">
          <el-radio-button label="周期"></el-radio-button>
          <el-radio-button v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'"
                           label="负责人"></el-radio-button>
        </el-radio-group>
        <div id="echartMap"></div>
      </el-card>
    </el-row>
  </div>
</template>
<script>
import moment from 'moment'
import echarts from 'echarts/lib/echarts' // 引入图表主体
import 'echarts/lib/chart/line' // 引入折线图。
import 'echarts/lib/component/tooltip' // 引入提示框组件、标题组件、工具箱组件。
import 'echarts/lib/component/title'
import 'echarts/lib/component/toolbox'

export default {
  components: {
  },
  data() {
    return {
      dialogVisiblePlayerCharge: false,
      dialogTypePlayerCharge: {},
      pickerOptions: {
        // disabledDate(time) {
        //   return time.getTime() > Date.now();
        // },
      },
      loading: false,
      searchDate: [
        moment(new Date().setDate(1)).format('YYYY-MM-DD'),
        moment(new Date()).format('YYYY-MM-DD')
      ],
      searchForm: {
        createUser: ''
      },
      userList: [],
      userX: [],
      userJsonList: [],
      accountNoList: [],
      list: {
        incomeAmount: '',
        unspentAmount: '',
        chargeAmount: '',
        remainTime: '',
        classTotal: '',
        signInCount: '',
        playerCount: '',
        bookingCount: ''
      },
      accountNo: '',
      countType: '周期',
      groupType: 0, // 图标类型，0是周期，1负责人
      echartMap: null, // 图表本身
      echartOption: { // 图表的配置项和数据
        grid: { x: '65' },
        tooltip: {
          trigger: 'axis'
        },
        noDataLoadingOption: {
          text: '暂无数据',
          effect: 'bubble',
          effectOption: {
            effect: {
              n: 0
            }
          }
        },
        legend: {},
        xAxis: {},
        yAxis: {},
        series: []
      },
      echartData: { // 图表数据
        xData: {
          userName: [],
          signInDate: []
        },
        yData: []
      }
    }
  },
  created() {
  },
  computed: {},
  mounted() {
    this.initUserList()
    this.search()
  },
  methods: {
    initUserList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_ALL_TESTER).then((res) => {
        if (res.code === '000000') {
          this.userList = res.data
        } else {
          this.showMsg(res.msg, 'error')
        }
      })
    },
    initEchart() { // 初始化图表
      const echartMap = document.getElementById('echartMap')
      this.echartMap = echarts.init(echartMap)
      this.echartMap.setOption(this.echartOption)
    },
    async search() {
      this.userJsonList = []
      this.userX = []
      var startDate = this.searchDate && this.searchDate[0] ? moment(this.searchDate[0]).format('YYYY-MM-DD') : ''
      var endDate = this.searchDate && this.searchDate[1] ? moment(this.searchDate[1]).format('YYYY-MM-DD') : ''
      const param1 = {
        'startDate': startDate,
        'endDate': endDate,
        'createUser': this.searchForm.createUser
      }

      await this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.PLANANDCASE_COUNT, param1).then((res) => {
        this.list.incomeAmount = res.data.totalCase
        this.list.chargeAmount = res.data.newCase
        this.list.playerCount = res.data.totalPlan
        this.list.bookingCount = res.data.newPlan
      }).catch((error) => {
        this.$message.error(error.respMessage)
      })
      // 2. 调用用例接口
      var caseXlist = []
      var caseYlist = []
      var planYlist = []

      const param2 = {
        'groupType': this.groupType,
        'startDate': startDate,
        'endDate': endDate,
        'createUser': this.$appData.userInfo.roles[0].roleName === '超级管理员' ? this.searchForm.createUser : this.$appData.userInfo.nickName
      }
      await this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.CASE_COUNT, param2).then((res) => {
        this.echartOption.legend = {
          data: ['计划数', '用例数']
        }

        var arr = res.data
        // var arr = [{ value: 1, key: '2021-06-01' }, { value: 0, key: '2021-06-02' }, { value: 0, key: '2021-06-03' }, { value: 0, key: '2021-06-04' }, { value: 0, key: '2021-06-05' }, { value: 0, key: '2021-06-06' }, { value: 12, key: '2021-06-07' }, { value: 60, key: '2021-06-08' }, { value: 0, key: '2021-06-09' }, { value: 2, key: '2021-06-10' }, { value: 0, key: '2021-06-11' }, { value: 0, key: '2021-06-12' }, { value: 0, key: '2021-06-13' }, { value: 0, key: '2021-06-14' }, { value: 0, key: '2021-06-15' }, { value: 0, key: '2021-06-16' }, { value: 0, key: '2021-06-17' }, { value: 0, key: '2021-06-18' }, { value: 0, key: '2021-06-19' }, { value: 0, key: '2021-06-20' }, { value: 0, key: '2021-06-21' }, { value: 0, key: '2021-06-22' }, { value: 0, key: '2021-06-23' }, { value: 0, key: '2021-06-24' }, { value: 0, key: '2021-06-25' }, { value: 0, key: '2021-06-26' }, { value: 0, key: '2021-06-27' }, { value: 0, key: '2021-06-28' }, { value: 1, key: '2021-06-29' }]
        for (var i = 0; i < arr.length; i++) {
          caseXlist.push(arr[i].key)
          caseYlist.push(arr[i].value)
          if (this.groupType === 1) {
            this.userX.push(arr[i].key)
            var jsonObj = {}
            jsonObj.name = arr[i].key
            jsonObj.case = arr[i].value
            jsonObj.plan = 0
            this.userJsonList.push(jsonObj)
          }
        }
      }).catch((error) => {
        this.$message.error(error.respMessage)
      })
      const param3 = {
        'groupType': this.groupType,
        'startDate': startDate,
        'endDate': endDate,
        'createUser': this.$appData.userInfo.roles[0].roleName === '超级管理员' ? this.searchForm.createUser : this.$appData.userInfo.nickName
      }
      await this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.PLAN_COUNT, param3).then((res) => {
        var arr = res.data

        for (var i = 0; i < arr.length; i++) {
          planYlist.push(arr[i].value)
          if (this.groupType === 1) {
            if (this.userX.indexOf(arr[i].key) === -1) {
              this.userX.push(arr[i].key)
              var jsonObj = {}
              jsonObj.name = arr[i].key
              jsonObj.case = 0
              jsonObj.plan = arr[i].value
              this.userJsonList.push(jsonObj)
            }
            for (var y = 0; y < this.userJsonList.length; y++) {
              if (this.userJsonList[y].name === arr[i].key) { // 存在设置，不存在重新定义对象，case为0，plan取值
                this.userJsonList[y].plan = arr[i].value
              }
            }
          }
        }
        console.log('userJsonList==', JSON.stringify(this.userJsonList))
      }).catch((error) => {
        this.$message.error(error.respMessage)
      })
      if (this.countType === '负责人') {
        this.echartOption.xAxis = {
          type: 'category', // 还有其他的type，可以去官网喵两眼哦
          data: this.userX, // x轴数据
          nameTextStyle: {
            fontWeight: 600,
            fontSize: 18
          }
        }
        // 当负责人，单独处理xlist,ylist,从json数组里面去取
        var caselist = []
        var planlist = []
        for (var i = 0; i < this.userJsonList.length; i++) {
          planlist.push(this.userJsonList[i].plan)
          caselist.push(this.userJsonList[i].case)
        }
        console.log('planlist==', planlist)
        this.echartOption.series = [
          {
            name: '计划数',
            data: planlist,
            type: 'line'
          },
          {
            name: '用例数',
            data: caselist,
            type: 'line'
          }
        ]
        // 拼装负责人图的X轴
      } else {
        this.echartOption.xAxis = {
          type: 'category', // 还有其他的type，可以去官网喵两眼哦
          data: caseXlist, // x轴数据
          // x轴名称样式
          nameTextStyle: {
            fontWeight: 600,
            fontSize: 18
          }
        }
        this.echartOption.series = [
          {
            name: '计划数',
            data: planYlist,
            type: 'line'
          },
          {
            name: '用例数',
            data: caseYlist,
            type: 'line'
          }
        ]
      }
      this.echartOption.yAxis = {
        type: 'value',
        nameTextStyle: {
          fontWeight: 600,
          fontSize: 18
        }
      }

      // 数据初始化之后才初始化echat
      this.initEchart()
    },
    clear() {
      this.searchDate = [
        moment(new Date().setDate(1)).format('YYYY-MM-DD'),
        moment(new Date()).format('YYYY-MM-DD')
      ]
      this.searchForm = {}
    },
    selectFilterChange() {
      if (this.countType === '周期') {
        this.groupType = 0
      } else {
        this.groupType = 1
      }
      this.search()
    },
    selectChanged(value) {
      this.searchForm.createUser = value
    }
  }
}
</script>

<style>
/* 搜索栏 */
.statistics-center-tendency .search-box .el-form-item--small.el-form-item {
  margin-bottom: 20px;
}

.statistics-center-tendency .search-box .el-card__body {
  padding: 20px 20px 0 20px !important;
}

/*统计卡片*/
.statistics-center-tendency .card-box {
  margin: 10px 0;
  text-align: right;
}

.statistics-center-tendency .card-box .el-card {
  padding: 25px;
}

.statistics-center-tendency .card-box .icon {
  display: block;
  float: left;
  height: 60px !important;
  width: 60px !important;
  font-size: 45px;
  line-height: 140%;
}

.statistics-center-tendency .card-box .el-card__body {
  padding: 0 !important;
}

.statistics-center-tendency .card-box .count-box {
  float: right;
}

.statistics-center-tendency .card-box .count-box p {
  margin: 6px 0;
  font-size: 18px;
  font-weight: bold;
}

.statistics-center-tendency .card-box .count-box span {
  font-size: 14px;
}

/*统计图表*/
.statistics-center-tendency #echartMap {
  width: 100%;
  height: 500px;
  margin: 20px 0;
}
</style>
