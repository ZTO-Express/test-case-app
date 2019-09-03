<template>
  <div class="booking-center-dialog">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm" label-width="80px" size="small">
        <el-row>
          <el-col :span="12">
            <el-form-item prop="bookingDate" label="预约日期">
              <el-date-picker
                v-model="dialogForm.bookingDate"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions"
                size="small">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="source" label="预约来源">
              <el-select style="width: 220px" v-model.trim="dialogForm.source" clearable placeholder="请选择">
                <el-option
                  v-for="item in sourceArr"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="chineseName" label="中文名">
              <el-input style="width: 220px" v-model.trim="dialogForm.chineseName" clearable maxlength=100 placeholder="中文名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="englishName" label="英文名">
              <el-input style="width: 220px" v-model.trim="dialogForm.englishName" clearable maxlength=100 placeholder="英文名"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="birthday" label="生日">
              <el-date-picker
                v-model="dialogForm.birthday"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="age" label="年龄">
              <el-input style="width: 220px" v-model.trim="dialogForm.age" clearable maxlength=2>
                <template slot="append">岁</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="height" label="身高">
              <el-input style="width: 220px" v-model.trim="dialogForm.height" clearable maxlength=3>
                <template slot="append">CM</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="weight" label="体重">
              <el-input style="width: 220px" v-model.trim="dialogForm.weight" clearable maxlength=8>
                <template slot="append">KG</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="parentsName" label="家长姓名">
              <el-input style="width: 220px" v-model.trim="dialogForm.parentsName" clearable maxlength=5>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="relation" label="关系">
              <el-select style="width: 220px" v-model.trim="dialogForm.relation" clearable placeholder="请选择">
                <el-option
                  v-for="item in relationArr"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="concatWay" label="联系方式">
              <el-input style="width: 220px" v-model.trim="dialogForm.concatWay" clearable maxlength=50>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="address" label="家庭住址">
              <el-input style="width: 220px" v-model.trim="dialogForm.address" clearable maxlength=255>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="isReturnVisit" label="是否回访">
              <el-select v-model.trim="dialogForm.isReturnVisit" clearable placeholder="请选择">
                <el-option v-for="item in isReturnVisitArr" :key="item.value" :label="item.label"
                           :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="returnVisitDate" label="回访日期">
              <el-date-picker
                v-model="dialogForm.returnVisitDate"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="isExperience" label="是否体验">
              <el-select v-model="dialogForm.isExperience" clearable placeholder="请选择">
              <el-option v-for="item in isExperienceArr" :key="item.value" :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="experienceDate" label="体验日期">
              <el-date-picker
                v-model="dialogForm.experienceDate"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="isJoin" label="是否加入">
              <el-select v-model="dialogForm.isJoin" clearable placeholder="请选择">
                <el-option v-for="item in isJoinArr" :key="item.value" :label="item.label"
                           :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="joinDate" label="加入日期">
              <el-date-picker
                v-model="dialogForm.joinDate"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item prop="bookingRemark" label="备注">
              <el-input style="width: 540px" v-model.trim="dialogForm.remark" clearable maxlength=255>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-col align="right">
        <el-button @click="beforeClose" size="medium">取 消</el-button>
        <el-button @click="submit" size="medium" type="primary" :loading="loading">确 定
        </el-button>
      </el-col>
    </el-dialog>
  </div>
</template>

<script>
  import {dialogForm1Rules, rules} from './validRules';

  export default {
    props: {
      visible: Boolean,
      dialogType: Object,
    },
    data() {
      const defaultDialogForm = {
        bookingDate: '',
        source: '',
        chineseName: '',
        englishName: '',
        birthday: '',
        age: '',
        weight: '',
        parentsName: '',
        relation: '',
        concatWay: '',
        address: '',
        isReturnVisit: '',
        returnVisitDate: '',
        isExperience: '',
        experienceDate: '',
        isJoin: '',
        joinDate: '',
      };
      return {
        loading: false,
        allDisabled: false,
        title: '',
        dialogForm1Rules,
        rules,
        defaultDialogForm,
        dialogForm: JSON.parse(JSON.stringify(defaultDialogForm)),
        relationArr: [
          {
            value: 'father',
            label: '爸爸',
          },
          {
            value: 'mother',
            label: '妈妈',
          },
          {
            value: 'grandpa',
            label: '爷爷/外公',
          },
          {
            value: 'grandma',
            label: '奶奶/外婆',
          },
        ],
        sourceArr: [
          {
            value: 'dianping',
            label: '大众点评',
          },
          {
            value: 'friend',
            label: '朋友介绍',
          },
          {
            value: 'other',
            label: '其他',
          },
        ],
        isReturnVisitArr: [
          {
            value: 0,
            label: "未回访",
          },
          {
            value: 1,
            label: "已回访",
          },
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
        curIndex: '',
        curType: '',
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
        },
      };
    },
    watch: {
      dialogType() {
        this.showMod(this.dialogType);
      },
    },
    created() {
      // this.initTestServiceList();
      // this.initTestEnvList();
    },
    methods: {
      initForm() {
        this.dialogForm = {};
        this.dialogForm.isReturnVisit = 0;
        this.dialogForm.isExperience = 0;
        this.dialogForm.isJoin = 0;
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate();
      },
      // getData() {
      //   let _data = {};
      //   if (this.curType === 'new') {
      //     _data["serviceId"] = this.dialogForm.serviceId;
      //   }
      //   if (this.curType === 'edit') {
      //     _data["serviceId"] = this.dialogForm.serviceIdDisplay;
      //   }
      //   return _data;
      // },
      initDialog() {
        this.dialogForm.bookingDate = this.$formatters.formatDate(this.dialogForm.bookingDate);
        this.dialogForm.birthday = this.$formatters.formatDate(this.dialogForm.birthday);
        this.dialogForm.returnVisitDate = this.$formatters.formatDate(this.dialogForm.returnVisitDate);
        this.dialogForm.experienceDate = this.$formatters.formatDate(this.dialogForm.experienceDate);
        this.dialogForm.joinDate = this.$formatters.formatDate(this.dialogForm.joinDate);
      },
      async submit() {
        this.$refs.dialogForm.validate((pass) => {
          if (!pass) {
            return;
          }
          this.loading = !this.loading;
          const params = Object.assign({}, this.dialogForm);
          params.createUser = this.$appData.userInfo.nickName;
          params.updateUser = this.$appData.userInfo.nickName;
          let reqUrl = '';
          if (this.curType === 'new') {
            reqUrl = this.$urlConst.ADD_BOOKING_LIST;
          }
          if (this.curType === 'edit') {
            reqUrl = this.$urlConst.EDIT_BOOKING_LIST;
          }
          console.log(params);
          this.$axiosUtil.post(this.$appConfig.MIX, reqUrl, params).then((res) => {
            if (res.code === 'S0001') {
              this.loading = !this.loading;
              this.close();
              this.$message({type: 'success', message: res.msg});
              this.$emit('submit');
              this.dialogForm = {};
            }
          }).catch((err) => {
            this.loading = !this.loading;
            let message = err.respMessage;
            this.$message({type: 'error', message: message});
          });
        });
      },
      beforeClose() {
        this.$confirm('变动信息将丢失, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.close();
        }).catch(() => {
        });
      },
      close() {
        this.$emit('update:visible', false);
        this.initForm();
      },
      showMod(dialogType) {
        if (dialogType) {
          this.dialogForm = dialogType.data; // 还原当前修改项

          this.curIndex = dialogType.index; // 记录当前修改位置
          this.curType = dialogType.type;
        }
        if (this.curType === 'new') {
          this.title = "新增预约";
          this.allDisabled = false;
          this.initForm();
        }
        if (this.curType === 'edit') {
          this.title = "编辑预约";
          this.allDisabled = false;
          this.initDialog();
        }
      },
    },
  };
</script>

<style>
  /*间距*/

  .booking-center-dialog .el-dialog {
    width: 700px !important;
  }

  .booking-center-dialog h4 {
    color: #888;
    font-weight: normal;
  }

  .booking-center-dialog .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .booking-center-dialog .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .booking-center-dialog
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .booking-center-dialog .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .booking-center-dialog
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .booking-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .booking-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .booking-center-dialog
  .el-checkbox__input.is-disabled
  + span.el-checkbox__label {
    cursor: default; /*多选框标签禁用鼠标样式为默认*/
    color: #000;
  }
</style>
