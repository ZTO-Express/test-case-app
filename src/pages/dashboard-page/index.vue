<template>
  <div class="statistics-center-tendency">
    <el-row>
      <!-- <player-charge-dialog :visible.sync="dialogVisiblePlayerCharge"
                            :dialogType="dialogTypePlayerCharge"
                            @submit="search()" /> -->
    </el-row>
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
                       icon="el-icon-search"
                       type="primary"
                       :loading="loading">搜索</el-button>
            <el-button @click="clear"
                       icon="el-icon-close">清空</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-row>
    <el-row :gutter="20"
            class="card-box">
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'">
        <el-card>
          <i class="icon icon-haikezhangguizhushou_dingdan"
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
          <i class="icon icon-haikezhangguizhushou_dingdan"
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
          <i class="icon icon-jiaoyijilu-copy-copy"
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
          <i class="icon icon-jiaoyijilu-copy-copy"
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
      // 负责人X轴
      userX: [],
      // 负责人json数组
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
    if (this.$appData.userInfo.roles[0].roleName === '超级管理员') {
      this.initUserList()
    } else {
      this.getAccountInfo()
    }
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
    getAccountInfo() {
      // const param = {}
      // param.pageSize = 1
      // param.pageNo = 1
      // param.memberId = this.$appData.userInfo.userId
      // this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.QUERY_ACCOUNT_LIST, param).then((res) => {
      //   this.accountNoList = res.data.accountDtoList
      // }).catch((error) => {
      //   this.$message.error(error.msg)
      // })
    },
    initEchart() { // 初始化图表
      const echartMap = document.getElementById('echartMap')
      this.echartMap = echarts.init(echartMap)
      this.echartMap.setOption(this.echartOption)
    },
    getData() {
      const _data = {}
      _data['startDate'] = this.searchDate && this.searchDate[0] ? moment(this.searchDate[0]).format('YYYY-MM-DD 00:00:00') : ''
      _data['endDate'] = this.searchDate && this.searchDate[1] ? moment(this.searchDate[1]).format('YYYY-MM-DD 23:59:59') : ''
      // _data['createrUser'] = this.$appData.userInfo.roles[0].roleName === '超级管理员' ? '' : this.$appData.userInfo.nickName
      return _data
    },
    async search() {
      // this.loading = !this.loading
      // 1.先调用获取用例数据接口
      // const param = {
      //   'startDate': '2021-01-01',
      //   'endDate': '2021-05-13',
      //   'createrUser': '超级管理员'
      // }
      // 先清空一下
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
        // this.loading = !this.loading
        this.list.incomeAmount = res.data.totalCase
        this.list.chargeAmount = res.data.newCase
        this.list.playerCount = res.data.totalPlan
        this.list.bookingCount = res.data.newPlan
      }).catch((error) => {
        // this.loading = false
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
        'createUser': this.searchForm.createUser
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
          // 如果是负责人的话，拼装userX
          if (this.groupType === 1) {
            this.userX.push(arr[i].key)
            // 负责人json数组
            var jsonObj = {}
            jsonObj.name = arr[i].key
            jsonObj.case = arr[i].value
            jsonObj.plan = 0
            this.userJsonList.push(jsonObj)
          }
        }
      }).catch((error) => {
        // this.loading = false
        this.$message.error(error.respMessage)
      })
      // 3.调用计划书接口
      const param3 = {
        'groupType': this.groupType,
        'startDate': startDate,
        'endDate': endDate,
        'createUser': this.searchForm.createUser

      }
      await this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.PLAN_COUNT, param3).then((res) => {
        var arr = res.data

        for (var i = 0; i < arr.length; i++) {
          planYlist.push(arr[i].value)
          // 如果是负责人的话，拼装userX
          if (this.groupType === 1) {
            // 去重 X轴
            if (this.userX.indexOf(arr[i].key) === -1) {
              this.userX.push(arr[i].key)
              // json数组添加只有plan没有case的json对象
              var jsonObj = {}
              jsonObj.name = arr[i].key
              jsonObj.case = 0
              jsonObj.plan = arr[i].value
              this.userJsonList.push(jsonObj)
            }
            // json数据处理,先遍历
            for (var y = 0; y < this.userJsonList.length; y++) {
              if (this.userJsonList[y].name === arr[i].key) { // 存在设置，不存在重新定义对象，case为0，plan取值
                this.userJsonList[y].plan = arr[i].value
              }
            }
          }
        }
        console.log('userJsonList==', JSON.stringify(this.userJsonList))
      }).catch((error) => {
        // this.loading = false
        this.$message.error(error.respMessage)
      })
      if (this.countType === '负责人') {
        this.echartOption.xAxis = {
          type: 'category', // 还有其他的type，可以去官网喵两眼哦
          data: this.userX, // x轴数据
          // data: res.result.lineDetail.xdata,
          name: '负责人', // x轴名称
          // x轴名称样式
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
            // data: [6, 1],
            data: planlist,
            type: 'line'
          },
          {
            name: '用例数',
            // data: [820, 932, 901, 934, 1290, 1330, 1320],
            data: caselist,
            type: 'line'
          }
        ]
        // 拼装负责人图的X轴
      } else {
        this.echartOption.xAxis = {
          type: 'category', // 还有其他的type，可以去官网喵两眼哦
          data: caseXlist, // x轴数据
          // data: res.result.lineDetail.xdata,
          name: '日期', // x轴名称
          // x轴名称样式
          nameTextStyle: {
            fontWeight: 600,
            fontSize: 18
          }
        }
        this.echartOption.series = [
          {
            name: '计划数',
            // data: [6, 1],
            data: planYlist,
            type: 'line'
          },
          {
            name: '用例数',
            // data: [820, 932, 901, 934, 1290, 1330, 1320],
            data: caseYlist,
            type: 'line'
          }
        ]
      }
      this.echartOption.yAxis = {
        type: 'value',
        name: '个数', // y轴名称
        // y轴名称样式
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
    formatEchartData(data) { // 格式化请求数据
      if (!data || !data.userNameContent || !data.signInDateContent) return
      const dateArr = []
      const userNameArr = []
      // const countArr = []
      const dateCountArr = []
      const userNameCountArr = []
      data.signInDateContent.forEach(item => {
        dateArr.push(item.signInDate)
        dateCountArr.push(item.dateSignInCount)
      })
      data.userNameContent.forEach(item => {
        userNameArr.push(item.userName)
        userNameCountArr.push(item.userNameSignInCount)
      })
      // if (this.countType === '签到日期') {
      //   countArr = dateCountArr;
      // } else {
      //   countArr = userNameCountArr;
      // }
      this.list = data.count // 卡片统计信息赋值
      this.echartData = { // 统计图表赋值
        xData: {
          signInDate: dateArr,
          userName: userNameArr
        },
        yData: {
          signInDate: dateCountArr,
          userName: userNameCountArr
        }
      }
      this.inJectEchartData()
    },
    inJectEchartData() { // 注入数据更新图表
      const xAxis = []
      const series = []
      if (this.countType === '签到名单') {
        xAxis.push({
          type: 'category', // 类型为类目
          boundaryGap: false, // 分割线跟类目对齐
          data: this.echartData.xData.userName && this.echartData.xData.userName.length > 0 ? this.echartData.xData.userName
            : [],
          axisLine: {
            lineStyle: {
              color: '#aeafb0'
            }
          }
        })
        series.push(
          { // Y轴系列数据
            name: '签到次数',
            type: 'line',
            smooth: true,
            itemStyle: {
              normal: {
                color: '#1f8cf2',
                lineStyle: {
                  color: '#1f8cf2'
                }
              }
            },
            data: this.echartData.yData.userName && this.echartData.yData.userName.length > 0 ? this.echartData.yData.userName
              : [0]
          }
        )
      }
      if (this.countType === '签到日期') {
        xAxis.push({
          type: 'category', // 类型为类目
          boundaryGap: false, // 分割线跟类目对齐
          data: this.echartData.xData.signInDate && this.echartData.xData.signInDate.length > 0 ? this.echartData.xData.signInDate
            : [this.$formatters.formatTime(new Date())],
          axisLine: {
            lineStyle: {
              color: '#aeafb0'
            }
          }
        })
        series.push(
          { // Y轴系列数据
            name: '签到人数',
            type: 'line',
            smooth: true,
            itemStyle: {
              normal: {
                color: '#1f8cf2',
                lineStyle: {
                  color: '#1f8cf2'
                }
              }
            },
            data: this.echartData.yData.signInDate && this.echartData.yData.signInDate.length > 0 ? this.echartData.yData.signInDate
              : [0]
          }
        )
      }

      // if (false) { // 是否开启默认筛选
      //   this.echartOption.legend = { // 默认筛选按钮配置
      //     data: ['名单', '日期']
      //   }
      // }

      this.echartOption.xAxis = xAxis
      this.echartOption.series = series
      this.echartOption.yAxis = {
        axisLine: {
          lineStyle: {
            color: '#aeafb0'
          }
        },
        splitLine: {
          lineStyle: {
            color: '#f3f4f9'
          }
        }
      }
      this.echartMap.setOption(this.echartOption)
    },

    selectFilterChange() {
      console.log('countType==', this.countType)
      if (this.countType === '周期') {
        this.groupType = 0
      } else {
        this.groupType = 1
      }
      this.search()
      // this.inJectEchartData()
    },

    openChargeDialog() {
      const data = {
        'accountNo': this.accountNoList[0].accountNo,
        'chineseName': this.$appData.userInfo.nickName,
        'userId': this.$appData.userInfo.userId
      }
      this.dialogVisiblePlayerCharge = !this.dialogVisiblePlayerCharge
      this.dialogTypePlayerCharge = { data }
    },
    selectChanged(value) {
      // console.log(value)
      // 接口调入入参的createUser变更
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
