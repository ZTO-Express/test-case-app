<template>

  <div class="statistics-center-tendency">

    <el-row>

      <player-charge-dialog :visible.sync="dialogVisiblePlayerCharge"
                            :dialogType="dialogTypePlayerCharge"
                            @submit="search()" />

    </el-row>

    <el-row class="search-box">

      <el-card>

        <!-- TimePicker 时间选择器用于选择或输入日期 -->

        <el-form ref="searchForm"
                 :model="searchForm"
                 size="small"
                 :inline="true">

          <el-form-item prop="searchDate">

            <el-date-picker is-range
                            v-model="searchDate"
                            :clearable="true"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期(起)"
                            end-placeholder="结束日期(止)"
                            placeholder="选择时间范围"
                            :picker-options="pickerOptions">

            </el-date-picker>

          </el-form-item>

          <el-form-item v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员'">

            <el-select v-model="searchForm.userName"
                       filterable
                       clearable
                       placeholder="测试负责人">

              <el-option v-for="user in userList"
                         :key="user.userId"
                         :label="user.nickName"
                         :value="user.userId">

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
                           label="负责人">

          </el-radio-button>

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

      dialogVisiblePlayerCharge: true,

      dialogTypePlayerCharge: {},

      pickerOptions: {

        // disabledDate (time) {

        //   return time.getTime() > Date.now(10);

        // },

      },

      value1: '',

      value2: '',

      loading: true,

      searchDate: [

        moment(new Date().setDate(1)).format('YYYY-MM-DD'),

        moment(new Date()).format('YYYY-MM-DD')

      ],

      searchForm: {

        userId: ''

      },

      nickName: [],

      userList: [],

      accountNoList: [],

      usecase_X: [],

      usecase_Y: [],

      plan_X: [],

      plan_Y: [],

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

      countType: '',

      echartMap: null, // 图表本身

      // echartOption: { //图表的配置项和数据

      //   grid: { x: '65' },

      //   tooltip: {

      //     trigger: 'axis',

      //   },

      //   // noDataLoadingOption: {

      //   //   text: '暂无数据',

      //   //   effect: 'bubble',

      //   //   effectOption: {

      //   //     effect: {

      //   //       n: 0

      //   //     }

      //   //   }

      //   // },

      //   legend: {

      //     data: ['计划数', '用例数']

      //   },

      //   xAxis: {},

      //   yAxis: {},

      //   series: [],

      // },

      echartOption: {

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

        legend: {

          data: ['计划数', '用例数']

        },

        xAxis: {

          name: '天',

          type: 'category', // 类型为类目

          boundaryGap: false, // 分割线跟类目对齐

          axisLine: {

            lineStyle: {

              color: '#aeafb0'

            }

          },

          data: []

        },

        yAxis: {

          name: '个数',

          // type: 'category',强制显示所有文本标签

          axisLine: {

            lineStyle: {

              color: '#aeafb0'

            }

          }

        },

        series: [

          {

            name: '计划数',

            smooth: true,

            type: 'line', // 图形类型

            itemStyle: {

              normal: {

                color: 'red',

                lineStyle: {

                  color: 'red'

                }

              }

            },

            data: []

          },

          {

            name: '用例数',

            smooth: true,

            type: 'line', // 图形类型

            itemStyle: {

              normal: {

                color: '#1f8cf2',

                lineStyle: {

                  color: '#1f8cf2'

                }

              }

            },

            data: []

          }

        ]

      },

      echartData: {}

    }
  },

  created() { },

  computed: {},

  mounted() {
    if (this.$appData.userInfo.roles[0].roleName === '超级管理员') {
      this.initUserList()

      this.number()
    } else {

      // this.getAccountInfo();

    }

    this.initEchart()

    this.search()
  },

  methods: {

    number() {
      const _data = this.getData()

      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.POST_LIST, _data).then((res) => {
        if (res.code === '000000') {
          console.log(res)

          console.log('ehdgrcveydg')

          this.list = res.data

          this.list.incomeAmount = res.data.totalCase

          this.list.chargeAmount = res.data.newCase

          this.list.playerCount = res.data.totalPlan

          this.list.bookingCount = res.data.newPlan
        } else {
          this.showMsg(res.msg, 'error')
        }
      })
    },

    // 用例数折线

    Usercase() {
      const _data = this.getData()

      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.Super_Echarts, _data).then((res) => {
        if (res.code === '000000') {
          console.log('用例折线')

          var arr = res.data

          console.log(res.data)

          for (var i = 0; i < arr.length; i++) {
            this.usecase_X.push(arr[i].key)

            console.log(this.usecase_X)

            this.usecase_Y.push(arr[i].value)

            console.log(this.usecase_Y)
          }
        } else {
          this.showMsg(res.msg, 'error')
        }
      })
    },

    // 计划数折线

    Plancase() {
      // console.log("经开");

      const _data = this.getData()

      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.POST_Account_Name, _data).then((res) => {
        console.log('经开')

        if (res.code === '000000') {
          console.log('计划折线')

          var arr = res.data

          console.log(res.data)

          for (var i = 0; i < arr.length; i++) {
            this.plan_X.push(arr[i].key)

            console.log(this.plan_X)

            this.plan_Y.push(arr[i].value)

            console.log(this.plan_Y)
          }

          this.echartData = { // 图表数据

            legend: {

              data: ['计划数', '用例数']

            },

            xData: {

              // 负责人x坐标

              userName: this.plan_X,

              // 周期x坐标

              signInDate: this.plan_X

            },

            yData: {

              // 负责人y计划坐标

              signInDate: this.plan_Y,

              userName: this.usecase_Y

            }

          }
        } else {
          console.log('经开2')

          this.showMsg(res.msg, 'error')
        }
      })
    },

    // 负责人数据

    initUserList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_ALL_TESTER).then((res) => {
        if (res.code === '000000') {
          this.userList = res.data

          var arr = res.data

          for (var i = 0; i < arr.length; i++) {
            this.nickName.push(arr[i].nickName)
          }
        } else {
          this.showMsg(res.msg, 'error')
        }
      })
    },

    // getAccountInfo () {

    //   let param = {};

    //   param.pageSize = 1;

    //   param.pageNo = 1;

    //   param.memberId = this.$appData.userInfo.userId;

    //   this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_ACCOUNT_LIST, param).then((res) => {

    //     this.accountNoList = res.data.accountDtoList;

    //     console.log(res.data);

    //   }).catch((error) => {

    //     this.$message.error(error.msg);

    //   })

    // },

    initEchart() { // 初始化图表
      // 定义一个名为echartMap的变量，基于准备好的dom，初始化echarts实例

      const echartMap = document.getElementById('echartMap')

      this.echartMap = echarts.init(echartMap)

      // 展示数据， 使用刚指定的配置项和数据显示图表。

      this.echartMap.setOption(this.echartOption)
    },

    getData() {
      const _data = {}

      _data['startDate'] = this.searchDate && this.searchDate[0] ? moment(this.searchDate[0]).format('YYYY-MM-DD 00:00:00') : ''

      _data['endDate'] = this.searchDate && this.searchDate[1] ? moment(this.searchDate[1]).format('YYYY-MM-DD 23:59:59') : ''

      _data['createrUser'] = this.$appData.userInfo.roles[0].roleName === '超级管理员' ? this.searchForm.userName : this.$appData.userInfo.userId

      if (this.countType === '负责人') {
        console.log('k线图')

        _data['groupType'] = 1
      } else {
        console.log('折线图')

        _data['groupType'] = 0
      }

      return _data
    },

    search() {
      this.loading = !this.loading

      const _data = this.getData()

      this.Usercase()

      this.Plancase()

      this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_DASHBOARD_DATA, _data).then((res) => {
        this.loading = !this.loading

        this.formatEchartData(res.data)
      }).catch((error) => {
        this.loading = false

        this.$message.error(error.respMessage)
      })
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

      // let countArr = [];

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

        // 关联X坐标数据

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

      if (this.countType === '负责人') {
        xAxis.push({

          name: '测试负责人',

          type: 'category', // 类型为类目

          boundaryGap: false, // 分割线跟类目对齐

          // 负责人X坐标

          data: this.echartData.xData.userName && this.echartData.xData.userName.length > 0 ? this.echartData.xData.userName

            : [0],

          axisLine: {

            lineStyle: {

              color: '#aeafb0'

            }

          }

        })

        series.push(

          { // Y轴系列数据

            name: '用例数',

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

            // 负责人y坐标用例

            data: this.echartData.yData.userName && this.echartData.yData.userName.length > 0 ? this.echartData.yData.userName

              : [0]

          },

          { // Y轴系列数据

            name: '计划数',

            type: 'line',

            smooth: true,

            itemStyle: {

              normal: {

                color: 'red',

                lineStyle: {

                  color: 'red'

                }

              }

            },

            // 负责人y坐标计划

            data: this.echartData.yData.signInDate && this.echartData.yData.signInDate.length > 0 ? this.echartData.yData.signInDate

              : [0]

          },

        )
      }

      if (this.countType === '周期') {
        // console.log(this.countType);

        xAxis.push({

          name: '天',

          type: 'category', // 类型为类目

          boundaryGap: false, // 分割线跟类目对齐

          // 周期X坐标

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

            name: '用例数',

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

            // 周期y坐标用例

            data: this.echartData.yData.userName && this.echartData.yData.userName.length > 0 ? this.echartData.yData.userName

              : [0]

          },

          { // Y轴系列数据

            name: '计划数',

            type: 'line',

            smooth: true,

            itemStyle: {

              normal: {

                color: 'red',

                lineStyle: {

                  color: 'red'

                }

              }

            },

            // 周期y坐标计划

            data: this.echartData.yData.signInDate && this.echartData.yData.signInDate.length > 0 ? this.echartData.yData.signInDate

              : [0]

          },

        )
      }

      // if (false) { //是否开启默认筛选

      //   this.echartOption.legend = { //默认筛选按钮配置

      //     data: ['负责人', '周期']

      //   };

      // }

      this.echartOption.xAxis = xAxis

      this.echartOption.series = series

      this.echartOption.yAxis = {

        name: '个数',

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
      this.inJectEchartData()
    },

    openChargeDialog() {
      const data = {

        'accountNo': this.accountNoList[0].accountNo,

        'chineseName': this.$appData.userInfo.nickName,

        'userId': this.$appData.userInfo.userId

      }

      this.dialogVisiblePlayerCharge = !this.dialogVisiblePlayerCharge

      this.dialogTypePlayerCharge = { data }
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

