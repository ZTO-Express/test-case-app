<template>
  <div class="classes-center-dialog">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm" label-width="120px" size="small">
        <el-form-item prop="classesName" label="训练班名称">
          <el-input style="width: 360px" v-model.trim="dialogForm.classesName" clearable maxlength=100
                    placeholder="训练班名称"/>
        </el-form-item>
        <el-form-item prop="echelonId" label="梯队">
          <el-select style="width: 360px" v-model.trim="dialogForm.echelonId" clearable placeholder="梯队">
            <el-option v-for="item in echelonList" :key="item.id" :label="item.echelonName"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="placeId" label="场地名称">
          <el-select style="width: 360px" v-model="dialogForm.placeId" clearable placeholder="场地名称">
            <el-option v-for="item in placeList" :key="item.id" :label="item.placeName"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="classesPrice" label="训练班单价">
          <el-input style="width: 360px" v-model.trim="dialogForm.classesPrice" clearable maxlength=6
                    placeholder="训练班单价"/>
        </el-form-item>
        <el-form-item prop="classesDay" label="训练日期">
          <el-select style="width: 360px" v-model="dialogForm.classesDay" clearable placeholder="训练日期">
            <el-option v-for="item in classesDayArr" :key="item.label" :label="item.label"
                       :value="item.label"></el-option>
          </el-select>
        </el-form-item>
        <!--<el-form-item prop="remark" label="备注">-->
          <!--<el-input style="width: 360px" v-model.trim="dialogForm.remark" clearable maxlength=255>-->
          <!--</el-input>-->
        <!--</el-form-item>-->
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
        classesName: '',
        echelonId: '',
        placeId: '',
        classesPrice: '',
        classesDay: '',
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
        echelonList: [],
        placeList: [],
        classesDayArr: [
          {
            value: 0,
            label: "星期一",
          },
          {
            value: 1,
            label: "星期二",
          },
          {
            value: 2,
            label: "星期三",
          },
          {
            value: 3,
            label: "星期四",
          },
          {
            value: 4,
            label: "星期五",
          },
          {
            value: 5,
            label: "星期六",
          },
          {
            value: 6,
            label: "星期日",
          },
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
      this.initEchelonList();
      this.initPlaceList();
    },
    methods: {
      initForm() {
        this.dialogForm = {};
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
            reqUrl = this.$urlConst.ADD_CLASSES;
          }
          if (this.curType === 'edit') {
            reqUrl = this.$urlConst.EDIT_CLASSES;
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
          this.title = "新增训练班";
          this.allDisabled = false;
          this.initForm();
        }
        if (this.curType === 'edit') {
          this.title = "编辑训练班";
          this.allDisabled = false;
          this.initDialog();
        }
      },
      initEchelonList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_ECHELON_LIST, param).then((res) => {
          this.echelonList = res.data.echelonDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
      initPlaceList() {
        const param = {};
        param.pageNo = 1;
        param.pageSize = 1000;
        this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.QUERY_PLACE_LIST, param).then((res) => {
          this.placeList = res.data.placeDtoList;
        }).catch((error) => {
          this.$message.error(error.msg);
        })
      },
    },
  };
</script>

<style>
  /*间距*/
  .classes-center-dialog .el-dialog {
    width: 550px !important;
  }

  .classes-center-dialog h4 {
    color: #888;
    font-weight: normal;
  }

  .classes-center-dialog .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .classes-center-dialog .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .classes-center-dialog
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .classes-center-dialog .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .classes-center-dialog
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .classes-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .classes-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .classes-center-dialog
  .el-checkbox__input.is-disabled
  + span.el-checkbox__label {
    cursor: default; /*多选框标签禁用鼠标样式为默认*/
    color: #000;
  }
</style>
