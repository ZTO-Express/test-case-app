<template>
  <div class="content">
    <el-row>
      <!--<sign-in-dialog :visible.sync="dialogVisible" :dialogType="dialogType"-->
      <!--@submit="search()"/>-->
    </el-row>
    <Calendar
      ref="Calendar"
      :markDateMore="sessionList"
      agoDayHide="1530115221"
      v-on:choseDay="clickDay"
      v-on:changeMonth="changeMonth"
    ></Calendar>
    <br>
    <div class="bottom">
      <el-tooltip class="item" effect="light" content="等待开课" placement="bottom-start">
        <el-button type="info" icon="el-icon-view" circle></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="light" content="正常开课" placement="bottom">
        <el-button type="success" icon="el-icon-check" circle></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="light" content="已停课" placement="bottom">
        <el-button type="danger" icon="el-icon-close" circle></el-button>
      </el-tooltip>
      <el-tooltip class="item" effect="light" content="当天有多节课程" placement="bottom-end">
        <el-button type="warning" icon="el-icon-warning" circle></el-button>
      </el-tooltip>
    </div>
    <div>
      <p id="demo">点击这个按钮，获得您的坐标：</p>
      <el-button @click="getLocation()" v-permission="'sign-center/signIn/confirm'"
                 type="success" size="medium" icon="el-icon-finished">获取坐标
      </el-button>
    </div>
    <el-row class="div">
      <div style="text-align:center;margin-bottom: 10px">
        <el-button @click="signIn('confirm')" v-permission="'sign-center/signIn/confirm'"
                   type="success" size="medium" icon="el-icon-finished">确认签到
        </el-button>
        <el-button @click="signIn('cancel')" v-permission="'sign-center/signIn/cancel'"
                   size="medium">取消签到
        </el-button>
      </div>
    </el-row>
  </div>
</template>

<script>
  import Calendar from "vue-calendar-component";
  import moment from 'moment';
  // import signInDialog from "./signInDialog";

  export default {
    data() {
      return {
        dialogVisible: false,
        dialogType: {},
        sessionList: [],
        sessionArr: [],
        date: '',
        startTime: '',
        endTime: '',
        x: ''
      };
    },
    components: {
      Calendar,
      // signInDialog
    },
    methods: {
      getData() {
        let _data = {};
        _data["startTime"] = this.startTime ? this.startTime : this.formatDate(moment().startOf('month').valueOf());
        _data["endTime"] = this.endTime ? this.endTime : this.formatDate(moment().endOf('month').valueOf());
        return _data;
      },
      search() {
        const params = this.getData();
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_SESSION_LIST_BY_MONTH, params).then((res) => {
          this.loading = !this.loading;
          this.sessionArr = res.data.sessionDtoList;
          const array = res.data.sessionDtoList;
          for (let i = 0; i < array.length; i++) {
            const temp = {};
            temp.date = this.formatDate(array[i].trainingDate);
            if (array[i].status == 0) {
              temp.className = 'wait';
            } else if (array[i].status == 1) {
              temp.className = 'normal';
            } else {
              temp.className = 'cancel';
            }
            for (let j = 0; j < this.sessionList.length; j++) {
              if (temp.date == this.sessionList[j].date && temp.className != this.sessionList[j].className) {
                temp.className = 'warn';
              }
            }
            this.sessionList.push(temp);
          }
        }).catch((error) => {
          this.loading = !this.loading;
          this.$message.error(error.respMessage);
        })
      },
      clickDay(data) {
        this.date = data;

        console.log("选中了", data); //选中某天
      },
      clickToday(data) {
        console.log("跳到了本月今天", data); //跳到了本月
      },
      changeMonth(data) {
        console.log("左右点击切换月份", data); //左右点击切换月份
        data = moment(data);
        this.startTime = this.formatDate(data.startOf('month').valueOf());
        this.endTime = this.formatDate(data.endOf('month').valueOf());
        console.log(this.startTime + "-" + this.endTime);
        this.search();
      },
      signIn(type) {
        console.log(this.$appData.userInfo.nickName);
        console.log(this.$appData.userInfo.id);
        console.log(this.$appData.userInfo.userId);
        if (this.$appData.userInfo.nickName == '超级管理员') {
          alert("I'm admin")
          // this.openSignInDialog();
        } else {
          console.log(this.date)
          if (this.date == '' || this.date == null || this.date == undefined) {
            this.$message({type: 'warning', message: '请选择签到日期'});
            return;
          }
          let text = "";
          let msg = "";
          let url = "";
          if (type == 'confirm') {
            text = '确认签到日期为：' + this.date;
            url = this.$urlConst.CONFIRM_SIGN_IN;
            msg = "确认签到成功";
          } else {
            text = '确认取消签到日期为：' + this.date;
            url = this.$urlConst.CANCEL_SIGN_IN;
            msg = "取消签到成功";
          }
          this.$confirm(text, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }).then(() => {
            this.loading = !this.loading;
            const params = {
              userId: this.$appData.userInfo.userId,
              updateUser: this.$appData.userInfo.nickName,
            };
            this.$axiosUtil.post(this.$appConfig.MIX, url, params).then((res) => {
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

        }
      },
      openSignInDialog(type = '', row = {}, index = '',) {
        console.log(this.date)
        if (this.date == '' || this.date == null || this.date == undefined) {
          this.$message({type: 'warning', message: '请选择签到日期!'});
          return;
        }
        const data = JSON.parse(JSON.stringify(this.sessionArr));
        console.log(data)
        this.dialogVisible = !this.dialogVisible;
        this.dialogType = {type, data, index};
      },
      formatDate(val) {
        return this.$formatters.formatDate(val);
      },

      getLocation() {
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(this.showMap, this.handleError, {
            enableHighAccuracy: true,
            maximumAge: 1000
          });
        } else {
          alert('您的浏览器不支持使用HTML 5来获取地理位置服务');
        }
        // let x = document.getElementById("demo");
        // alert(1)
        // if (navigator.geolocation) {
        //   navigator.geolocation.getCurrentPosition(this.showPosition);
        // } else {
        //   alert(2)
        //   x.innerHTML = "Geolocation is not supported by this browser.";
        // }
      },
      showPosition(position) {
        alert(position);
        let x = document.getElementById("demo");
        x.innerHTML = "Latitude: " + position.coords.latitude +
          "<br />Longitude: " + position.coords.longitude;

        console.log(position);
        let lat = position.coords.latitude; //纬度
        let lag = position.coords.longitude; //经度
        alert("纬度:" + lat + ",经度:" + lag);
        console.log("纬度:" + lat + ",经度:" + lag);
      },


      showMap(value) {
        let longitude = value.coords.longitude;
        let latitude = value.coords.latitude;
        let map = new BMap.Map('map');
        let point = new BMap.Point(longitude, latitude);    // 创建点坐标
        map.centerAndZoom(point, 15);
        let marker = new BMap.Marker(new BMap.Point(longitude, latitude));  // 创建标注
        map.addOverlay(marker);              // 将标注添加到地图中
      },

      handleError(value) {
        switch (value.code) {
          case 1:
            alert('位置服务被拒绝');
            break;
          case 2:
            alert('暂时获取不到位置信息');
            break;
          case 3:
            alert('获取信息超时');
            break;
          case 4:
            alert('未知错误');
            break;
        }
      }


    },
    created() {
      this.search();
    }
  }
  ;
</script>

<style scoped>
  .div {
    margin: auto;
    width: 220px;
    height: 44px;
    line-height: 44px;
    /*background: #0fc37c;*/
    color: #fff;
    font-size: 17px;
    text-align: center;
    margin-top: 20px;
  }

  .wh_container >>> .wait {
    background-color: #909399;
  }

  .wh_container >>> .normal {
    background-color: #85ce61;
  }

  .wh_container >>> .cancel {
    background-color: #f78989;
  }

  .wh_container >>> .warn {
    background-color: #e6a23c;
  }

  .wh_content_item > .wh_isMark {
    background: orange;
  }

  .wh_container >>> .wh_content_all {
    /* background-color: red; */
  }

  .bottom {
    clear: both;
    text-align: center;
  }

  .item {
    margin: 4px;
  }
</style>