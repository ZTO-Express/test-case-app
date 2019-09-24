<template>
  <div class="place-center-dialog">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm" label-width="120px" size="small">
        <el-form-item prop="placeName" label="训练营名称">
          <el-input style="width: 360px" v-model.trim="dialogForm.placeName" clearable maxlength=100
                    placeholder="训练营名称"/>
        </el-form-item>
        <el-form-item prop="placeAddress" label="训练营地址">
          <el-input style="width: 360px" v-model.trim="dialogForm.placeAddress" clearable maxlength=100
                    placeholder="训练营地址"/>
        </el-form-item>
        <!--<el-form-item prop="placeRent" label="训练营租金">-->
          <!--<el-input style="width: 360px" v-model.trim="dialogForm.placeRent" clearable maxlength=6-->
                    <!--placeholder="训练营租金"/>-->
        <!--</el-form-item>-->
        <el-form-item prop="placeRemark" label="备注">
          <el-input type="textarea" style="width: 360px" v-model.trim="dialogForm.remark" clearable maxlength=100>
          </el-input>
        </el-form-item>
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
        placeName: '',
        placeAddress: '',
        status: '',
        placeRent: '',
        remark: '',
      };
      return {
        loading: false,
        allDisabled: false,
        title: '',
        dialogForm1Rules,
        rules,
        defaultDialogForm,
        dialogForm: JSON.parse(JSON.stringify(defaultDialogForm)),
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
    },
    methods: {
      initForm() {
        this.dialogForm = {};
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate();
      },
      initDialog() {
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
            reqUrl = this.$urlConst.ADD_PLACE;
          }
          if (this.curType === 'edit') {
            reqUrl = this.$urlConst.EDIT_PLACE;
          }
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
          this.title = "新增训练营";
          this.allDisabled = false;
          this.initForm();
        }
        if (this.curType === 'edit') {
          this.title = "编辑训练营";
          this.allDisabled = false;
          // this.initDialog();
        }
      },
    },
  };
</script>

<style>
  /*间距*/
  .place-center-dialog .el-dialog {
    width: 550px !important;
  }

  .place-center-dialog h4 {
    color: #888;
    font-weight: normal;
  }

  .place-center-dialog .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .place-center-dialog .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .place-center-dialog
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .place-center-dialog .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .place-center-dialog
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .place-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .place-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .place-center-dialog
  .el-checkbox__input.is-disabled
  + span.el-checkbox__label {
    cursor: default; /*多选框标签禁用鼠标样式为默认*/
    color: #000;
  }
</style>
