<template>
  <div class="dialog-class">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm" label-width="100px" size="small">
        <el-form-item prop="trainingDate" label="训练日期:">
          <el-input style="width: 360px" v-model.trim="dialogForm.trainingDate"
                    v-bind:disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item prop="classesId" label="训练班名称:">
          <el-select style="width: 360px" v-model.trim="dialogForm.classesId" clearable placeholder="训练班名称"
                     v-bind:disabled="isDisabled">
            <el-option v-for="item in classesList" :key="item.classesName" :label="item.classesName"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="trainingTimeStart" label="训练时间:">
          <el-time-select style="width: 160px"
                          placeholder="训练开始时间"
                          v-model="dialogForm.trainingTimeStart"
                          :picker-options="{
                start: '09:00',
                step: '00:30',
                end: '21:00'
              }"
                          v-bind:disabled="isDisabled">
          </el-time-select>
          --
          <el-time-select style="width: 160px"
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
        <el-form-item prop="remark" label="备注:">
          <el-input style="width: 360px" v-model.trim="dialogForm.remark" clearable maxlength=255
                    v-bind:disabled="isDisabled">
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
    mounted() {
    },
    data() {
      const defaultDialogForm = {
        classesName: '',
        classesDay: '',
        status: '',
        remark: '',
      };
      return {
        classesList: [],
        isDisabled: false,
        loading: false,
        title: '',
        dialogForm1Rules,
        rules,
        defaultDialogForm,
        dialogForm: JSON.parse(JSON.stringify(defaultDialogForm)),
        statusArr: [
          {
            value: 0,
            label: "已退出",
          },
          {
            value: 1,
            label: "正常",
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
        this.dialogForm = {};
        this.dialogForm.status = 1;
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate();
      },
      initDialog() {
        this.dialogForm.birthday = this.$formatters.formatDate(this.dialogForm.birthday);
        this.imageUrl = this.dialogForm.img;
        if (this.curType === 'check') {
          this.isDisabled = true;
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
          if (this.curType === 'confirm') {
            reqUrl = this.$urlConst.ADD_COACH;
          }
          if (this.curType === 'cancel') {
            reqUrl = this.$urlConst.EDIT_COACH;
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
          this.dialogForm = dialogType.data;

          this.curIndex = dialogType.index;
          this.curType = dialogType.type;
        }
        if (this.curType === 'confirm') {
          this.title = "确认签到";
          this.isDisabled = false;
          this.initForm();
        }
        if (this.curType === 'cancel') {
          this.title = "取消签到";
          this.isDisabled = false;
          this.initDialog();
        }
      },
      uploadUrl() {
        const url = this.$appConfig.MIX.baseUrl + this.$urlConst.UPLOAD_IMG;
        return url;
      },
      handleAvatarSuccess(res, file) {
        this.dialogForm.img = res.data.path;
        this.imageUrl = URL.createObjectURL(file.raw);
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isPNG = file.type === 'image/png';
        const isBMP = file.type === 'image/bmp';
        const isLt4M = file.size / 1024 / 1024 < 4;

        if (!isJPG && !isPNG && !isBMP) {
          this.$message.error('上传图片必须是JPG/PNG/BMP 格式!');
        }
        if (!isLt4M) {
          this.$message.error('上传头像图片大小不能超过 4MB!');
        }

        //去获取拍照时的信息，解决拍出来的照片旋转问题
        Exif.getData(file, () => {
          debugger
          this.rotate = 'avatar-uploader';
          let Orientation;
          Orientation = Exif.getTag(file, 'Orientation');
          console.log("old : " + this.rotate)
          if (Orientation != "" && Orientation != 1) {
            switch (Orientation) {
              case 6://需要顺时针（向左）90度旋转
                this.rotate = 'avatar-uploader-90';
                console.log("new : " + this.rotate)
                break;
              case 8://需要逆时针（向右）90度旋转
                this.rotate = 'avatar-uploader-N90';
                console.log("new : " + this.rotate)
                break;
              case 3://需要180度旋转
                this.rotate = 'avatar-uploader-180';
                console.log("new : " + this.rotate)
                break;
            }
          }
        });
        return (isJPG || isBMP || isPNG) && isLt4M;
      },


      dataURLtoFile: (dataurl, filename) => {
        const arr = dataurl.split(',')
        const mime = arr[0].match(/:(.*?);/)[1]
        const bstr = atob(arr[1])
        let n = bstr.length
        let u8arr = new Uint8Array(n);
        while (n--) {
          u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, {type: mime});
      },

      rotateImage: (image, width, height) => {
        let canvas = document.createElement('canvas')
        let ctx = canvas.getContext('2d')
        ctx.save()
        canvas.width = height
        canvas.height = width
        ctx.rotate(90 * Math.PI / 180)
        ctx.drawImage(image, 0, -height)
        ctx.restore()
        return canvas.toDataURL("image/jpeg")
      },
      beforeUpLoad(file) {
        return new Promise((resolve) => {
          this.getOrientation(file).then((orient) => {
            if (orient === 6) {
              let reader = new FileReader();
              let img = new Image();
              reader.onload = (e) => {
                img.src = e.target.result;
                img.onload = function () {
                  debugger
                  const data = this.rotateImage(img, img.width, img.height);
                  const newFile = this.dataURLtoFile(data, file.name);
                  resolve(newFile)
                }
              }
              reader.readAsDataURL(file)
            } else {
              resolve(file)
            }
          })
        })
      },
      getOrientation: (file) => {
        return new Promise((resolve) => {
          Exif.getData(file, function () {
            const orient = Exif.getTag(this, 'Orientation')
            resolve(orient)
          })
        })
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

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader-90 .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transform: rotate(90deg);
  }

  .avatar-uploader-N90 .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transform: rotate(-90deg);
  }

  .avatar-uploader-180 .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transform: rotate(180deg);
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
    /*transform: rotate(90deg);*/
  }
</style>
