<template>
  <div class="statistics-center-tendency">
    <el-row>
      <player-charge-dialog :visible.sync="dialogVisiblePlayerCharge" :dialogType="dialogTypePlayerCharge"
                            @submit="search()"/>
    </el-row>
    <el-row class="search-box">
      <el-card>
        <el-form ref="searchForm" :model="searchForm" size="small" :inline="true">
          <el-form-item prop="searchDate">
            <el-date-picker
              v-model="searchDate"
              :clearable="true"
              type="daterange"
              range-separator="至"
              start-placeholder="签到日期(起)"
              end-placeholder="签到日期(止)"
              :picker-options="pickerOptions">
            </el-date-picker>
          </el-form-item>
          <el-form-item
            v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'">
            <el-select v-model="searchForm.userName" filterable clearable placeholder="球员姓名">
              <el-option v-for="user in userList"
                         :key="user.userId"
                         :label="user.chineseName + '(' + user.englishName + ')'"
                         :value="user.userId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="search" icon="el-icon-search" type="primary" :loading="loading">搜索</el-button>
            <el-button @click="clear" icon="el-icon-close">清空</el-button>
            <el-button @click="openChargeDialog()" icon="el-icon-star-off" type="danger"
                       v-permission="'dashboard-page/player/charge'"
                       v-show="this.$appData.userInfo.roles[0].roleName == 'Player'" align="right">充值
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-row>
    <el-row :gutter="20" class="card-box">
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'">
        <el-card>
          <i class="icon icon-haikezhangguizhushou_dingdan" style="color:#23b7e5"></i>
          <div class="count-box">
            <p>{{list.incomeAmount}}</p>
            <span>课时总收入(元)</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'">
        <el-card>
          <i class="icon icon-haikezhangguizhushou_dingdan" style="color:#23b7e5"></i>
          <div class="count-box">
            <p>{{list.chargeAmount}}</p>
            <span>新增充值金额(元)</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'">
        <el-card>
          <i class="icon icon-jiaoyijilu-copy-copy" style="color:#4CAF50"></i>
          <div class="count-box">
            <p>{{list.playerCount}}</p>
            <span>新增球员数</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'">
        <el-card>
          <i class="icon icon-jiaoyijilu-copy-copy" style="color:#4CAF50"></i>
          <div class="count-box">
            <p>{{list.bookingCount}}</p>
            <span>新增预约数</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="card-box">
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName != 'Coach'">
        <el-card>
          <i class="icon icon-haikezhangguizhushou_dingdan" style="color:#23b7e5"></i>
          <div class="count-box">
            <p>{{list.unspentAmount}}</p>
            <span>未消费金额(元)</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName != 'Coach'">
        <el-card>
          <i class="icon icon-jiaoyijilu-copy-copy" style="color:#4CAF50"></i>
          <div class="count-box">
            <p>{{list.remainTime}}</p>
            <span>剩余课时数(节)</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6"
              v-show="this.$appData.userInfo.roles[0].roleName != 'Coach'">
        <el-card>
          <i class="icon icon-jiaoyijilu-copy-copy" style="color:#4CAF50"></i>
          <div class="count-box">
            <p>{{list.classTotal}}</p>
            <span>累计课时数(节)</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <i class="icon icon-touxiang" style="color:#FF7043"></i>
          <div class="count-box">
            <p>{{list.signInCount}}</p>
            <span>签到次数</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-card>
        <el-radio-group size="small" v-model="countType" @change="selectFilterChange">
          <el-radio-button label="签到日期"></el-radio-button>
          <el-radio-button
            v-show="this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'"
            label="签到名单"></el-radio-button>
        </el-radio-group>
        <div id="echartMap"></div>
      </el-card>
    </el-row>
  </div>
</template>
<script>
  import moment from 'moment';
  import echarts from 'echarts/lib/echarts'; // 引入图表主体
  import 'echarts/lib/chart/line'; // 引入折线图。
  import 'echarts/lib/component/tooltip';  // 引入提示框组件、标题组件、工具箱组件。
  import 'echarts/lib/component/title';
  import 'echarts/lib/component/toolbox';

  import playerChargeDialog from "./playerChargeDialog";

  export default {
    components: {
      playerChargeDialog,
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
          moment(new Date()).format('YYYY-MM-DD'),
        ],
        searchForm: {
          userId: ''
        },
        userList: [],
        accountNoList: [],
        list: {
          incomeAmount: '',
          unspentAmount: '',
          chargeAmount: '',
          remainTime: '',
          classTotal: '',
          signInCount: '',
          playerCount: '',
          bookingCount: '',
        },
        accountNo: '',
        countType: '签到日期',
        echartMap: null, //图表本身
        echartOption: { //图表的配置项和数据
          grid: {x: '65'},
          tooltip: {
            trigger: 'axis',
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
          series: [],
        },
        echartData: { //图表数据
          xData: {
            userName: [],
            signInDate: []
          },
          yData: [],
        },
      };
    },
    created() {
    },
    computed: {},
    mounted() {
      if (this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss') {
        this.initUserList();
      } else {
        this.getAccountInfo();
      }
      this.initEchart();
      this.search();
    },
    methods: {
      initUserList() {
        let param = {};
        param.pageSize = 10000;
        param.pageNo = 1;
        param.status = 1;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_PLAYER_LIST, param).then((res) => {
          this.userList = res.data.playerDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      getAccountInfo() {
        let param = {};
        param.pageSize = 1;
        param.pageNo = 1;
        param.memberId = this.$appData.userInfo.userId;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_ACCOUNT_LIST, param).then((res) => {
          this.accountNoList = res.data.accountDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initEchart() { //初始化图表
        const echartMap = document.getElementById('echartMap');
        this.echartMap = echarts.init(echartMap);
        this.echartMap.setOption(this.echartOption);
      },
      getData() {
        let _data = {};
        _data["startTime"] = this.searchDate && this.searchDate[0] ? moment(this.searchDate[0]).format('YYYY-MM-DD 00:00:00') : '';
        _data["endTime"] = this.searchDate && this.searchDate[1] ? moment(this.searchDate[1]).format('YYYY-MM-DD 23:59:59') : '';
        _data["memberId"] = this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss' ? this.searchForm.userName : this.$appData.userInfo.userId;
        return _data;
      },
      search() {
        this.loading = !this.loading;
        const _data = this.getData();
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_DASHBOARD_DATA, _data).then((res) => {
          this.loading = !this.loading;
          this.formatEchartData(res.data);
        }).catch((error) => {
          this.loading = false;
          this.$message.error(error.respMessage);
        })
      },
      clear() {
        this.searchDate = [
          moment(new Date().setDate(1)).format('YYYY-MM-DD'),
          moment(new Date()).format('YYYY-MM-DD'),
        ];
        this.searchForm = {};
      },
      formatEchartData(data) { // 格式化请求数据
        if (!data || !data.userNameContent || !data.signInDateContent) return;
        let dateArr = [];
        let userNameArr = [];
        let countArr = [];
        let dateCountArr = [];
        let userNameCountArr = [];
        data.signInDateContent.forEach(item => {
          dateArr.push(item.signInDate);
          dateCountArr.push(item.dateSignInCount);
        });
        data.userNameContent.forEach(item => {
          userNameArr.push(item.userName);
          userNameCountArr.push(item.userNameSignInCount);
        });
        // if (this.countType === '签到日期') {
        //   countArr = dateCountArr;
        // } else {
        //   countArr = userNameCountArr;
        // }
        this.list = data.count; // 卡片统计信息赋值
        this.echartData = { // 统计图表赋值
          xData: {
            signInDate: dateArr,
            userName: userNameArr
          },
          yData: {
            signInDate: dateCountArr,
            userName: userNameCountArr
          }
        };
        this.inJectEchartData();
      },
      inJectEchartData() { // 注入数据更新图表
        const xAxis = [];
        const series = [];
        if (this.countType === '签到名单') {
          xAxis.push({
            type: 'category', // 类型为类目
            boundaryGap: false, // 分割线跟类目对齐
            data: this.echartData.xData.userName && this.echartData.xData.userName.length > 0 ? this.echartData.xData.userName
              : [],
            axisLine: {
              lineStyle: {
                color: '#aeafb0',
              }
            },
          });
          series.push(
            { //Y轴系列数据
              name: '签到次数',
              type: 'line',
              smooth: true,
              itemStyle: {
                normal: {
                  color: "#1f8cf2",
                  lineStyle: {
                    color: '#1f8cf2',
                  },
                },
              },
              data: this.echartData.yData.userName && this.echartData.yData.userName.length > 0 ? this.echartData.yData.userName
                : [0],
            }
          );
        }
        if (this.countType === '签到日期') {
          xAxis.push({
            type: 'category', // 类型为类目
            boundaryGap: false, // 分割线跟类目对齐
            data: this.echartData.xData.signInDate && this.echartData.xData.signInDate.length > 0 ? this.echartData.xData.signInDate
              : [this.$formatters.formatTime(new Date())],
            axisLine: {
              lineStyle: {
                color: '#aeafb0',
              }
            },
          });
          series.push(
            { //Y轴系列数据
              name: '签到人数',
              type: 'line',
              smooth: true,
              itemStyle: {
                normal: {
                  color: "#1f8cf2",
                  lineStyle: {
                    color: '#1f8cf2',
                  },
                },
              },
              data: this.echartData.yData.signInDate && this.echartData.yData.signInDate.length > 0 ? this.echartData.yData.signInDate
                : [0],
            }
          );
        }

        if (false) { //是否开启默认筛选
          this.echartOption.legend = { //默认筛选按钮配置
            data: ['名单', '日期']
          };
        }

        this.echartOption.xAxis = xAxis;
        this.echartOption.series = series;
        this.echartOption.yAxis = {
          axisLine: {
            lineStyle: {
              color: '#aeafb0',
            }
          },
          splitLine: {
            lineStyle: {
              color: '#f3f4f9',
            }
          },
        };
        this.echartMap.setOption(this.echartOption);
      },

      selectFilterChange() {
        this.inJectEchartData();
      },

      openChargeDialog() {
        const data = {
          "accountNo": this.accountNoList[0].accountNo,
          "chineseName": this.$appData.userInfo.nickName,
          "userId": this.$appData.userInfo.userId
        };
        this.dialogVisiblePlayerCharge = !this.dialogVisiblePlayerCharge;
        this.dialogTypePlayerCharge = {data};
      },

    },
  };
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
