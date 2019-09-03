<template>
  <div class="dialog-class">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm" label-width="120px" size="small">
        <el-form-item prop="sessionName" label="训练课程">
          <el-select style="width: 435px" v-model.trim="dialogForm.sessionName" clearable placeholder="请选择"
                     @change="formatDialogForm">
            <el-option v-for="item in sessionList"
                       :key="item.id"
                       :label="item.classesInfo"
                       :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="trainingSessionId" label="训练课编号" v-if="false">
          <el-input v-model="dialogForm.trainingSessionId" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="classesId" label="训练班级编号" v-if="false">
          <el-input v-model="dialogForm.classesId" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="classesDay" label="训练时间">
          <el-input style="width: 220px" v-model="dialogForm.classesDay" v-bind:disabled="true"></el-input>
          -
          <el-input style="width: 200px" v-model="dialogForm.trainingTimeStart" v-bind:disabled="true"></el-input>
        </el-form-item>
        <el-form-item prop="userIdArr" label="签到人员"
                      v-if="this.$appData.userInfo.roles[0].roleName != 'Player'">
          <el-select style="width: 435px" v-model="dialogForm.userIdArr" filterable multiple clearable placeholder="请选择"
                     @change="changeUserArr">
            <el-option v-for="user in userList"
                       :key="user.userId"
                       :label="user.englishName + '(' + user.chineseName + ')'"
                       :value="user.userId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="remark" label="取消原因" v-if="this.curType == 'cancel'">
          <el-input type="textarea" v-model.trim="dialogForm.remark" clearable maxlength="100"></el-input>
        </el-form-item>
        <el-form-item prop="perPrice" label="课时费用"
                      v-if="this.curType == 'confirm' && this.$appData.userInfo.roles[0].roleName == '超级管理员' || this.$appData.userInfo.roles[0].roleName == 'Boss'">
          <el-tooltip class="item" effect="light" content="临时课时费用，请在此输入" placement="top">
            <el-input style="width: 435px" v-model="dialogForm.tempTrainingExpense" clearable placeholder="课时费用">
              <template slot="append">元</template>
            </el-input>
          </el-tooltip>
        </el-form-item>
      </el-form>
      <el-col align="right">
        <el-button @click="beforeClose" size="medium">取 消</el-button>
        <el-button @click="submit" size="medium" type="primary" :loading="loading">确 认
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
    mounted() {
    },
    data() {
      const defaultDialogForm = {
        classesId: '',
        trainingSessionId: '',
        sessionName: '',
        classesDay: '',
        signInType: '',
        remark: '',
        trainingTimeStart: '',
        trainingTimeEnd: '',
        tempTrainingExpense: '',
        userIdArr: [],
        userNameArr: [],
      };
      return {
        sessionList: [],
        userList: [],
        isDisabled: false,
        loading: false,
        title: '',
        dialogForm1Rules,
        rules,
        defaultDialogForm,
        dialogForm: JSON.parse(JSON.stringify(defaultDialogForm)),
        curIndex: '',
        curType: '',
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
      initUserList() {
        let param = {};
        param.pageSize = 10000;
        param.pageNo = 1;
        param.classesIdStr = this.dialogForm.classesId;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_MEMBER_LIST, param).then((res) => {
          this.userList = res.data.memberDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initClassesList() {
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_SESSION_LIST_BY_MONTH, {}).then((res) => {
          this.sessionList = res.data.sessionDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initForm() {
        this.dialogForm.trainingSessionId = '';
        this.dialogForm.sessionName = '';
        this.dialogForm.classesDay = '';
        this.dialogForm.trainingTimeStart = '';
        this.dialogForm.trainingTimeEnd = '';
        this.dialogForm.userIdArr = [];
        this.dialogForm.userNameArr = [];
        this.dialogForm.tempTrainingExpense = '';
        this.dialogForm.remark = '';
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate();
      },
      formatDialogForm(e) {
        for (let i = 0; i < this.sessionList.length; i++) {
          if (e == this.sessionList[i].id) {
            this.dialogForm.trainingSessionId = e;
            this.dialogForm.classesDay = this.$formatters.formatDate(this.sessionList[i].trainingDate) + "(" + this.sessionList[i].classesDay + ")";
            this.dialogForm.trainingDate = this.$formatters.formatDate(this.sessionList[i].trainingDate);
            this.dialogForm.trainingTimeStart = this.sessionList[i].trainingTimeStart + " - " + this.sessionList[i].trainingTimeEnd;
            this.dialogForm.trainingTimeEnd = this.sessionList[i].trainingTimeEnd;
            this.dialogForm.classesId = this.sessionList[i].classesId;
            break;
          }
        }
        if (this.$appData.userInfo.roles[0].roleName != 'Player') {
          this.initUserList();
        }
      },
      changeUserArr(e) {
        const userIdArr = [];
        const userNameArr = [];
        for (let i = 0; i < this.userList.length; i++) {
          for (let j = 0; j < e.length; j++) {
            if (e[j] == this.userList[i].userId) {
              userIdArr.push(this.userList[i].userId);
              userNameArr.push(this.userList[i].chineseName);
            }
          }
        }
        this.dialogForm.userIdArr = userIdArr;
        this.dialogForm.userNameArr = userNameArr;
      },
      async submit() {
        this.$refs.dialogForm.validate((pass) => {
            if (!pass) {
              return;
            }
            this.loading = !this.loading;
            let type;
            if (this.curType == 'confirm') {
              type = 1;
            } else {
              type = 2;
            }
            const params = Object.assign({}, this.dialogForm);
            if (this.$appData.userInfo.roles[0].roleName == 'Player') {
              params.userIdArr.push(this.$appData.userInfo.userId);
              params.userNameArr.push(this.$appData.userInfo.nickName);
            }
            if (this.curType === 'cancel') {
              params.auditStatus = 0;
            }
            params.signInType = type;
            params.createUser = this.$appData.userInfo.nickName;
            params.updateUser = this.$appData.userInfo.nickName;
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
          }
        );
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
          // this.dialogForm = dialogType.data;
          // this.curIndex = dialogType.index;
          this.curType = dialogType.type;
        }
        if (this.curType === 'confirm') {
          this.title = "确认签到";
        }
        if (this.curType === 'cancel') {
          this.title = "取消签到";
        }
        this.initForm();
      },
    },
  };
</script>

<style>
  /*间距*/
  .dialog-class .el-dialog {
    width: 700px !important;
  }

  .dialog-class h4 {
    color: #888;
    font-weight: normal;
  }

  .dialog-class .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .dialog-class .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .dialog-class
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .dialog-class .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .dialog-class
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .dialog-class
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .dialog-class
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .dialog-class
  .el-checkbox__input.is-disabled
  + span.el-checkbox__label {
    cursor: default; /*多选框标签禁用鼠标样式为默认*/
    color: #000;
  }

</style>
