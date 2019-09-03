<template>
  <div class="player-charge-dialog">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm"
               label-width="110px" size="small">
        <el-form-item prop="userName" label="充值姓名">
          <el-input v-model="dialogForm.userName" v-bind:disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item prop="userId" label="用户编号">
          <el-input v-model="dialogForm.userId" v-bind:disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item prop="accountNo" label="充值账户">
          <el-input v-model="dialogForm.accountNo" v-bind:disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item prop="payWay" label="支付方式">
          <el-select v-model.trim="dialogForm.payWay" clearable placeholder="支付方式">
            <el-option v-for="item in payWayArr" :key="item.value" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="seqId" label="支付凭证">
          <el-input v-model="dialogForm.seqId" maxlength="32" placeholder="支付凭证">
          </el-input>
        </el-form-item>
        <el-form-item prop="amount" label="充值金额">
          <el-input v-model="dialogForm.amount" clearable maxlength=8
                    placeholder="充值金额">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="classTotal" label="课时总数">
          <el-input v-model="dialogForm.classTotal" clearable maxlength=8
                    placeholder="课时总数">
            <template slot="append">节</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="perPrice" label="课时单价">
          <el-input v-model="price" clearable maxlength=8
                    placeholder="充值金额/课时总数" v-bind:disabled="true">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="remark" label="备注">
          <el-input type="textarea" v-model.trim="dialogForm.remark" clearable maxlength="100"></el-input>
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
    mounted() {
    },
    data() {
      const defaultDialogForm = {
        payWay: '',
        seqId: '',
        accountNo: '',
        amount: '',
        classTotal: '',
        perPrice: '',
        remark: '',
      };
      return {
        loading: false,
        title: '球员充值',
        dialogForm1Rules,
        rules,
        defaultDialogForm,
        dialogForm: JSON.parse(JSON.stringify(defaultDialogForm)),
        payWayArr: [
          {
            value: 'WeChat',
            label: "微信",
          },
          {
            value: 'ALIPAY',
            label: "支付宝",
          },
          {
            value: 'other',
            label: "其他",
          }
        ],
      };
    },
    watch: {
      dialogType() {
        this.showMod(this.dialogType);
      }
    },
    computed: {
      price() {
        if (this.dialogForm.amount == 'NaN' || this.dialogForm.amount == '' || this.dialogForm.classTotal == 'NaN' || this.dialogForm.classTotal == '') {
          return '';
        }
        return this.formatPrice(this.dialogForm.amount, this.dialogForm.classTotal)
      }
    },
    created() {
    },
    methods: {
      initForm(dialogType) {
        this.dialogForm = {};
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate();
        this.dialogForm.userName = dialogType.data.chineseName;
        this.dialogForm.userId = dialogType.data.userId;
        this.dialogForm.accountNo = dialogType.data.accountNo;
      },
      async submit() {
        this.$refs.dialogForm.validate((pass) => {
          if (!pass) {
            return;
          }
          this.loading = !this.loading;
          const params = Object.assign({}, this.dialogForm);
          params.createUser = this.$appData.userInfo.nickName;
          params.toAccountNo = this.dialogForm.accountNo;
          params.transType = "charge";
          this.$axiosUtil.post(this.$appConfig.MIX, this.$urlConst.CREATE_AUDIT_ORDER, params).then((res) => {
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
      },
      showMod(dialogType) {
        this.initForm(dialogType);
      },
      formatPrice(amount, classTotal) {
        this.dialogForm.perPrice = (amount / classTotal).toFixed(2);
        if (this.dialogForm.perPrice == 'NaN') {
          return '';
        }
        return this.dialogForm.perPrice;
      },
    }
  }
  ;
</script>

<style>
  /*间距*/
  .player-charge-dialog .el-dialog {
    width: 600px !important;
  }

  .player-charge-dialog h4 {
    color: #888;
    font-weight: normal;
  }

  .player-charge-dialog .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .player-charge-dialog .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .player-charge-dialog
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .player-charge-dialog .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .player-charge-dialog
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .player-charge-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .player-charge-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .player-charge-dialog
  .el-checkbox__input.is-disabled
  + span.el-checkbox__label {
    cursor: default; /*多选框标签禁用鼠标样式为默认*/
    color: #000;
  }
</style>
