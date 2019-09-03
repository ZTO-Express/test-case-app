<template>
  <div class="signIn-audit-dialog">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm" label-width="120px" size="small">
        <el-form-item prop="sessionName" label="训练课程">
          <el-input style="width: 435px" v-model="dialogForm.sessionName" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="trainingSessionId" label="训练课编号">
          <el-input v-model="dialogForm.trainingSessionId" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="classesId" label="训练班级编号">
          <el-input v-model="dialogForm.classesId" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="classesDay" label="训练时间">
          <el-input style="width: 220px" v-model="dialogForm.classesDay" v-bind:disabled="true"></el-input>
          -
          <el-input style="width: 200px" v-model="dialogForm.trainingTimeStart" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="userId" label="签到ID">
          <el-input style="width: 435px" v-model="dialogForm.userId" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="userName" label="签到人员">
          <el-input style="width: 435px" v-model="dialogForm.userName" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="remark" label="取消原因">
          <el-input type="textarea" v-model.trim="dialogForm.remark" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="auditStatus" label="审核结果">
          <el-select v-model.trim="dialogForm.auditStatus" clearable placeholder="审核结果">
            <el-option v-for="item in auditStatusArr" :key="item.value" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="remark1" label="审核意见">
          <el-input type="textarea" v-model.trim="dialogForm.remark1" clearable maxlength="100"></el-input>
        </el-form-item>
        <el-col align="right">
          <el-button @click="beforeClose" size="medium">取 消</el-button>
          <el-button @click="submit" size="medium" type="primary" :loading="loading">确 定
          </el-button>
        </el-col>
      </el-form>
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
    mounted() {
    },
    data() {
      const defaultDialogForm = {
        classesId: '',
        trainingSessionId: '',
        sessionName: '',
        trainingDate: '',
        classesDay: '',
        signInType: '',
        remark: '',
        trainingTimeStart: '',
        trainingTimeEnd: '',
        auditStatus: '',
        remark1: '',
        userId: '',
        userName: '',
        userIdArr: [],
        userNameArr: [],
      };
      return {
        loading: false,
        title: '审核',
        dialogForm1Rules,
        rules,
        defaultDialogForm,
        dialogForm: JSON.parse(JSON.stringify(defaultDialogForm)),
        auditStatusArr: [
          {
            value: 1,
            label: "审核通过",
          },
          {
            value: 2,
            label: "审核拒绝",
          }
        ],
      };
    },
    watch: {
      dialogType() {
        this.showMod(this.dialogType);
      }
    },
    computed: {},
    created() {
    },
    methods: {
      initDialog() {
        this.dialogForm.userIdArr = [];
        this.dialogForm.userNameArr = [];
        this.dialogForm.sessionName = this.dialogForm.classesName + "(" + this.formatDate(this.dialogForm.trainingDate) + ")";
        this.dialogForm.trainingTimeStart = this.dialogForm.trainingTimeStart + " - " + this.dialogForm.trainingTimeEnd;
        this.dialogForm.trainingTimeEnd = this.dialogForm.trainingTimeEnd;
        this.dialogForm.classesId = this.dialogForm.classesId;
        this.dialogForm.auditStatus = '';
      },
      async submit() {
        this.$refs.dialogForm.validate((pass) => {
          if (!pass) {
            return;
          }
          this.loading = !this.loading;
          const params = Object.assign({}, this.dialogForm);
          params.createUser = this.$appData.userInfo.nickName;
          params.auditUser = this.$appData.userInfo.nickName;
          params.auditTime = new Date();
          params.userIdArr.push(this.dialogForm.userId);
          params.userNameArr.push(this.dialogForm.userName);
          this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.SIGN_IN, params).then((res) => {
            if (res.code === 'S0001') {
              this.loading = !this.loading;
              if (res.msg.indexOf("失败") != -1) {
                this.$message({
                  dangerouslyUseHTMLString: true,
                  type: 'warning',
                  message: res.msg
                });
              } else {
                this.$message({
                  dangerouslyUseHTMLString: true,
                  type: 'success',
                  message: res.msg
                });
              }
              this.close();
              this.$emit('submit');
              // this.dialogForm = {};
            }
          }).catch((err) => {
            this.loading = !this.loading;
            let message = err.respMessage;
            this.$message({type: 'error', message: message});
            this.close();
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
      },
      showMod(dialogType) {
        if (dialogType) {
          this.dialogForm = dialogType.data; // 还原当前修改项
        }
        this.initDialog(dialogType);
      },
      formatPrice(amount, classTotal) {
        this.dialogForm.perPrice = (amount / classTotal).toFixed(2);
        if (this.dialogForm.perPrice == 'NaN') {
          return '';
        }
        return this.dialogForm.perPrice;
      },
      formatDate(val) { //格式化日期
        return this.$formatters.formatDate(val);
      },
    }
  }
  ;
</script>

<style>
  /*间距*/
  .signIn-audit-dialog .el-dialog {
    width: 700px !important;
  }

  .signIn-audit-dialog h4 {
    color: #888;
    font-weight: normal;
  }

  .signIn-audit-dialog .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .signIn-audit-dialog .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .signIn-audit-dialog
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .signIn-audit-dialog .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .signIn-audit-dialog
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .signIn-audit-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .signIn-audit-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .signIn-audit-dialog
  .el-checkbox__input.is-disabled
  + span.el-checkbox__label {
    cursor: default; /*多选框标签禁用鼠标样式为默认*/
    color: #000;
  }
</style>
