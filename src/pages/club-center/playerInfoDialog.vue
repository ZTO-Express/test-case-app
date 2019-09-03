<template>
  <div class="player-center-dialog">
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :before-close="beforeClose"
      :modal-append-to-body="false">
      <el-form :model="dialogForm" :rules="dialogForm1Rules" ref="dialogForm"
               label-width="130px" size="small">
        <el-row>
          <!--<el-col :span="12">-->
          <!--<el-form-item class="avatar-uploader" prop="img" label="照片" v-if="this.curType == 'check'">-->
          <!--<img :src="dialogForm.img" class="avatar"/>-->
          <!--</el-form-item>-->
          <!--</el-col>-->
          <el-col :span="12">
            <el-form-item :class="rotate" prop="img" label="照片">
              <el-upload
                :action="uploadUrl()"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload" :class="rotate" v-bind:disabled="isDisabled">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <span style="color: #ff5555;font-size: xx-small"
                    v-if="this.curType !== 'check'">支持JPG/PNG/BMP格式的图片且不大于4M</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item prop="classesId" label="所属班级">
            <el-select style="width: 435px" v-model="dialogForm.classesId" multiple
                       clearable placeholder="请选择"
                       v-bind:disabled="isDisabled">
              <el-option style="width: 435px"
                         v-for="classes in classesList"
                         :key="classes.id"
                         :label="classes.classesName"
                         :value="classes.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="chineseName" label="中文名">
              <el-input style="width: 200px" v-model.trim="dialogForm.chineseName" clearable maxlength=20
                        placeholder="中文名" v-bind:disabled="isDisabled"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="englishName" label="英文名">
              <el-input style="width: 200px" v-model.trim="dialogForm.englishName" clearable maxlength=20
                        placeholder="英文名" v-bind:disabled="isDisabled"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="mobile" label="手机号">
              <el-input style="width: 200px" v-model.trim="dialogForm.mobile" clearable maxlength=11
                        placeholder="手机号" v-bind:disabled="isDisabled"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="birthday" label="生日">
              <el-date-picker
                style="width: 200px"
                v-model="dialogForm.birthday"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions" v-bind:disabled="isDisabled">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!--<el-col :span="12">-->
          <!--<el-form-item prop="nationality" label="国籍">-->
          <!--<el-input style="width: 200px" v-model.trim="dialogForm.nationality" clearable maxlength=20-->
          <!--v-bind:disabled="isDisabled">-->
          <!--</el-input>-->
          <!--</el-form-item>-->
          <!--</el-col>-->
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="height" label="身高">
              <el-input style="width: 200px" v-model.trim="dialogForm.height" clearable maxlength=4
                        v-bind:disabled="isDisabled">
                <template slot="append">CM</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="weight" label="体重">
              <el-input style="width: 200px" v-model.trim="dialogForm.weight" clearable maxlength=4
                        v-bind:disabled="isDisabled">
                <template slot="append">KG</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="chineseZodiac" label="生肖">
              <el-input style="width: 200px" v-model.trim="dialogForm.chineseZodiac" clearable maxlength=4
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="constellation" label="星座">
              <el-input style="width: 200px" v-model.trim="dialogForm.constellation" clearable maxlength=10
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="yearsExp" label="球龄">
              <el-input style="width: 200px" v-model.trim="dialogForm.yearsExp" clearable maxlength=4
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="strongerfoot" label="主力脚">
              <el-input style="width: 200px" v-model.trim="dialogForm.strongerfoot" clearable maxlength=10
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="position" label="场上位置">
              <el-input style="width: 200px" v-model.trim="dialogForm.yearsExp" clearable maxlength=4
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="no" label="球衣号码">
              <el-input style="width: 200px" v-model.trim="dialogForm.no" clearable maxlength=10
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="favoritePlayer" label="最喜爱的球员">
              <el-input style="width: 200px" v-model.trim="dialogForm.favoritePlayer" clearable maxlength=50
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="favoriteClub" label="最喜爱的俱乐部">
              <el-input style="width: 200px" v-model.trim="dialogForm.favoriteClub" clearable maxlength=50
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="hobby" label="爱好">
              <el-input type="textarea" style="width: 200px" v-model.trim="dialogForm.hobby" clearable maxlength=100
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="dream" label="梦想">
              <el-input type="textarea" style="width: 200px" v-model.trim="dialogForm.dream" clearable maxlength=100
                        v-bind:disabled="isDisabled">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="status" label="状态">
              <el-select style="width: 200px" v-model="dialogForm.status" clearable placeholder="请选择"
                         v-bind:disabled="isDisabled">
                <el-option v-for="item in statusArr" :key="item.value" :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="joinDate" label="加入日期">
              <el-date-picker
                style="width: 200px"
                v-model="dialogForm.joinDate"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions"
                size="small"
                v-bind:disabled="isDisabled">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
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
  import Exif from 'exif-js'

  export default {
    props: {
      visible: Boolean,
      dialogType: Object,
    },

    mounted() {
    },
    data() {
      const defaultDialogForm = {
        chineseName: '',
        englishName: '',
        mobile: '',
        no: '',
        birthday: '',
        nationality: '',
        height: '',
        weight: '',
        chineseZodiac: '',
        constellation: '',
        yearsExp: '',
        strongerFoot: '',
        position: '',
        favoritePlayer: '',
        favoriteClub: '',
        hobby: '',
        dream: '',
        img: '',
        status: '',
        joinDate: '',
        remark: '',
        classesId: [],
      };
      return {
        file: '',
        rotate: 'avatar-uploader',
        imageUrl: '',
        isDisabled: false,
        loading: false,
        allDisabled: false,
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
        classesList: [],
        curIndex: '',
        curType: '',
        pickerOptions:
          {
            disabledDate(time) {
              return time.getTime() > Date.now();
            }
            ,
          }
        ,
      }
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
        this.dialogForm.chineseName = '';
        this.dialogForm.englishName = '';
        this.dialogForm.mobile = '';
        this.dialogForm.no = '';
        this.dialogForm.birthday = '';
        this.dialogForm.nationality = '';
        this.dialogForm.height = '';
        this.dialogForm.weight = '';
        this.dialogForm.chineseZodiac = '';
        this.dialogForm.constellation = '';
        this.dialogForm.yearsExp = '';
        this.dialogForm.strongerFoot = '';
        this.dialogForm.favoritePlayer = '';
        this.dialogForm.favoritePlayer = '';
        this.dialogForm.position = '';
        this.dialogForm.favoriteClub = '';
        this.dialogForm.hobby = '';
        this.dialogForm.dream = '';
        this.dialogForm.img = '';
        this.dialogForm.joinDate = '';
        this.dialogForm.remark = '';
        this.dialogForm.classesId = [];
        this.imageUrl = '';
        this.dialogForm.status = 1;
        this.$refs.dialogForm && this.$refs.dialogForm.clearValidate();
      },
      initDialog() {
        this.dialogForm.joinDate = this.$formatters.formatDate(this.dialogForm.joinDate);
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
          if (this.curType === 'new') {
            reqUrl = this.$urlConst.ADD_PLAYER;
          }
          if (this.curType === 'edit') {
            reqUrl = this.$urlConst.EDIT_PLAYER;
          }
          this.$axiosUtil.post(this.$appConfig.MIX, reqUrl, params).then((res) => {
            if (res.code === 'S0001') {
              this.loading = !this.loading;
              this.close();
              this.$message({type: 'success', message: res.msg});
              this.$emit('submit');
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
          this.title = "新增球员";
          this.isDisabled = false;
          this.initForm();
        }
        if (this.curType === 'edit') {
          this.dialogForm = dialogType.data; // 还原当前修改项
          this.curIndex = dialogType.index; // 记录当前修改位置
          this.title = "编辑球员";
          this.isDisabled = false;
          this.initDialog();
        }
        if (this.curType === 'check') {
          this.dialogForm = dialogType.data; // 还原当前修改项
          this.curIndex = dialogType.index; // 记录当前修改位置
          this.title = "查看球员";
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
  .player-center-dialog .el-dialog {
    width: 1000px !important;
  }

  .player-center-dialog h4 {
    color: #888;
    font-weight: normal;
  }

  .player-center-dialog .el-dialog__body {
    overflow: hidden;
    padding: 20px 30px 30px !important;
  }

  .player-center-dialog .el-input.is-disabled .el-input__inner {
    cursor: default; /*输入框禁用鼠标样式为默认*/
    color: #000;
  }

  .player-center-dialog
  .el-select
  .el-input.is-disabled
  .el-input__inner {
    cursor: default; /*选择框禁用鼠标样式为默认*/
  }

  .player-center-dialog .el-input.is-disabled .el-input__icon {
    cursor: default; /*选择框下拉图标禁用鼠标样式为默认*/
  }

  .player-center-dialog
  .el-checkbox__input.is-disabled
  .el-checkbox__inner {
    cursor: default; /*多选框禁用鼠标样式为默认*/
  }

  .player-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner {
    background-color: #f58628;
    border-color: #f58628;
  }

  .player-center-dialog
  .el-checkbox__input.is-disabled.is-checked
  .el-checkbox__inner::after {
    border-color: #fff;
  }

  .player-center-dialog
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
