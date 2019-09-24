<template>
  <div class="session-center-dialog">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm" label-width="100px" size="small">
        <el-form-item prop="trainingDate" label="训练日期:">
          <el-date-picker style="width: 360px"
                          v-model="dialogForm.trainingDate"
                          type="date"
                          placeholder="选择日期"
                          format="yyyy 年 MM 月 dd 日"
                          value-format="yyyy-MM-dd"
                          size="small"
                          v-bind:disabled="isDisabled">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="classesId" label="训练班名称:">
          <el-select style="width: 360px" v-model.trim="dialogForm.classesId" filterable clearable placeholder="训练班名称"
                     v-bind:disabled="isDisabled" @change="formatTrainingTime">
            <el-option v-for="item in classesList" :key="item.classesName" :label="item.classesName"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="trainingTimeStart" label="训练时间:">
          <el-time-select style="width: 170px"
                          placeholder="训练开始时间"
                          v-model="dialogForm.trainingTimeStart"
                          :picker-options="{
                start: '09:00',
                step: '00:30',
                end: '21:00'
              }"
                          v-bind:disabled="isDisabled">
          </el-time-select>
          ---
          <el-time-select style="width: 170px"
                          placeholder="训练结束时间"
                          v-model="dialogForm.trainingTimeEnd"
                          :picker-options="{
                start: '09:00',
                step: '00:30',
                end: '21:00',
                minTime: dialogForm.trainingTimeStart
            }"
                          v-bind:disabled="isDisabled">
          </el-time-select>
        </el-form-item>
        <el-form-item prop="remark" label="备注:" v-if="this.curType == 'cancel' || this.curType == 'check'">
          <el-input type="textarea" style="width: 360px" v-model.trim="dialogForm.remark" clearable maxlength=255
                    v-bind:disabled="remarkIsDisabled">
          </el-input>
        </el-form-item>
      </el-form>
      <el-col align="right">
        <el-button @click="close" size="medium" v-if="this.curType == 'check'">关 闭</el-button>
        <el-button @click="beforeClose" size="medium" v-if="this.curType != 'check'">取 消</el-button>
        <el-button @click="submit" size="medium" type="primary" :loading="loading" v-if="this.curType != 'check'">确 定
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
        trainingDate: '',
        classesId: '',
        trainingTimeStart: '',
        trainingTimeEnd: '',
        remark: '',
      };
      return {
        loading: false,
        isDisabled: false,
        remarkIsDisabled: false,
        title: '',
        dialogForm1Rules,
        rules,
        defaultDialogForm,
        dialogForm: JSON.parse(JSON.stringify(defaultDialogForm)),
        classesList: [],
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
      this.initClassesList();
    },
    methods: {
      initClassesList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_CLASSES_LIST, param).then((res) => {
          this.classesList = res.data.classesDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initForm() {
        // this.dialogForm = {};
        this.dialogForm.trainingDate = '';
        this.dialogForm.classesId = '';
        this.dialogForm.trainingTimeStart = '';
        this.dialogForm.trainingTimeEnd = '';

        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate();
      },
      initDialog() {
        this.dialogForm.trainingDate = this.$formatters.formatDate(this.dialogForm.trainingDate);
        if (this.curType === 'check') {
          this.isDisabled = true;
          this.remarkIsDisabled = true;
        }
        if (this.curType === 'cancel') {
          this.isDisabled = true;
          this.remarkIsDisabled = false;
        }
        if (this.curType === 'edit') {
          this.isDisabled = false;
        }
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
            reqUrl = this.$urlConst.ADD_SESSION;
          }
          if (this.curType === 'edit') {
            reqUrl = this.$urlConst.EDIT_SESSION;
          }
          if (this.curType === 'cancel') {
            reqUrl = this.$urlConst.CANCEL_SESSION;
          }
          this.$axiosUtil.post(this.$appConfig.MIX, reqUrl, params).then((res) => {
            if (res.code === 'S0001') {
              this.loading = !this.loading;
              this.close();
              this.$message({type: 'success', message: res.msg});
              this.$emit('submit');
              // this.dialogForm = {};
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
          this.curType = dialogType.type;
        }
        if (this.curType === 'new') {
          this.title = "新增训练课程";
          this.isDisabled = false;
          this.initForm();
        }
        if (this.curType === 'edit') {
          this.dialogForm = dialogType.data; // 还原当前修改项

          this.curIndex = dialogType.index; // 记录当前修改位置
          this.title = "编辑训练课程";
          // this.isDisabled = false;
          this.initDialog();
        }
        if (this.curType === 'cancel') {
          this.title = "取消训练课程";
          // this.isDisabled = true;
          // this.remarkIsDisabled = false;
          this.initDialog();
        }
        if (this.curType === 'check') {
          this.title = "查看训练课程";
          // this.isDisabled = true;
          // this.remarkIsDisabled = false;
          this.initDialog();
        }
      },
      formatTrainingTime(e) {
        for (let i = 0; i < this.classesList.length; i++) {
          if (e == this.classesList[i].id) {
            this.dialogForm.trainingTimeStart = this.classesList[i].trainingTimeStart;
            this.dialogForm.trainingTimeEnd = this.classesList[i].trainingTimeEnd;
            break;
          }
        }
      },
    },
  };
</script>

<style>
  /*间距*/
  .session-center-dialog .el-dialog {
    width: 600px !important;
  }

  .session-center-dialog h4 {
    color: #888;
    font-weight: normal;
  }

  .session-center-dialog .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .session-center-dialog .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .session-center-dialog
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .session-center-dialog .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .session-center-dialog
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .session-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .session-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .session-center-dialog
  .el-checkbox__input.is-disabled
  + span.el-checkbox__label {
    cursor: default; /*多选框标签禁用鼠标样式为默认*/
    color: #000;
  }
</style>
