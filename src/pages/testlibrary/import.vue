<template>
  <div class="env-msg-gloaba">
    <el-form ref="importFile"
             :model="importFile"
             label-width="80px"
             v-loading="loading"
             element-loading-text="拼命导入中"
             element-loading-spinner="el-icon-loading">
      <div class="header">
        <el-tabs @tab-click="handleClick"
                 v-model="activeTab">
          <el-tab-pane label="表格文件(.xlsx)"
                       name="first">
            <div>
              <div style="margin-top:10px">
                <span class="font1">1.下载表格模板</span>
                <p class="font2">下载表格模板后，将用例内容按照格式填入表格中。</p>
                <el-button type="primary"
                           size="small"
                           style="width:80px;font-size:12px;"
                           @click="download(1)">下载</el-button>
              </div>
              <div style="margin-top:10px">
                <span class="font1">2.选择用例文件</span>
                <p class="font2">文件格式支持.xlsx;系统会按照第一个工作表里的格式读取数据，请确保整个文件格式正确。</p>
                <div class="btn-background">
                  <el-upload class="upload-demo"
                             ref="uploadFiles"
                             :action="excelUpload()"
                             accept='.xlsx'
                             :limit=1
                             :data="uploadPara"
                             :before-upload="beforeUploadFiles"
                             :on-success="uploadFilesSuccess"
                             :on-error="uploadFilesError"
                             :file-list="uploadFileList">
                    <el-button type="default"
                               size="small"
                               style="width:100px;font-size:12px;"
                               @click="submit"><i class="el-icon-upload2"></i>选择文件</el-button>
                    <div slot="tip"
                         class="el-upload__tip">1次只能上传1个文件(.xlsx)，且不超过10M</div>
                  </el-upload>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="思维导图(.xmind)"
                       name="second">
            <div>
              <div style="margin-top:10px">
                <span class="font1">1.下载思维导图模板</span>
                <p class="font2">下载思维导图模板后，将用例内容按照格式填入
                    <el-link :href="linkUrl"
                         target="_blank"
                         type="primary"
                        >详细链接</el-link>
                </p>
                <el-button type="primary"
                           size="small"
                           style="width:80px;font-size:12px;"
                           @click="download(2)">下载</el-button>
              </div>
              <div style="margin-top:10px">
                <span class="font1">2.选择用例文件</span>
                <p class="font2">文件格式支持.xmind;系统会按照第一个画布里的格式读取数据，请确保整个文件格式正确。</p>
                <div class="btn-background">
                  <el-upload class="upload-demo"
                             ref="uploadFiles"
                             :action="xmindUpload()"
                             accept='.xmind'
                             :limit=1
                             :data="uploadPara"
                             :before-upload="beforeUploadFilesXmind"
                             :on-success="uploadFilesSuccess"
                             :on-error="uploadFilesError"
                             :file-list="uploadFileList">
                    <el-button type="default"
                               size="small"
                               style="width:100px;font-size:12px;"
                               @click="submit"><i class="el-icon-upload2"></i>选择文件</el-button>
                    <div slot="tip"
                         class="el-upload__tip">1次只能上传1个文件(.xmind)，且不超过10M</div>
                  </el-upload>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <!-- <div style="text-align:right">
        <el-button type="default"
                   @click="submit">取消</el-button>
        <el-button type="primary"
                   @click="submit">导入</el-button>
      </div> -->
    </el-form>

  </div>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'importFile',
  data() {
    return {
      importFile: {},
      activeTab: 'first',
      uploadPara: {
        user: ''
      },
      uploadFileList: [],
      loading: false,
      linkUrl: 'http://w.ztosys.com/125010930'
    }
  },
  mounted() {
    this.init()
  },
  components: {
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  methods: {
    init() {
      this.uploadPara.user = this.$appData.userInfo.nickName
    },
    initViewData(command) {
      this.activeTab = command
    },
    handleClick() {

    },
    reset() {

    },
    submit() {
      console.log('点击导入！！！', JSON.stringify(this.uploadFileList))
    },
    // 文件下载
    download(type) {
      window.open(this.$appConfig.API.baseUrl + `/testcase/file/downloadTemplate?type=` + type)
    },
    importExcel() {

    },
    // 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeUploadFiles(file) {
      const extension = file.name.substring(file.name.lastIndexOf('.') + 1)
      // 后缀名限制
      if (extension !== 'xlsx') {
        this.$message({
          message: '上传文件只能是xlsx格式!',
          type: 'warning'
        })
      }
      // 大小限制
      const fileSize = file.size
      const lisLt30M = fileSize / 1000 / 1000 < 10
      if (!lisLt30M) {
        this.$message({
          message: '上传文件大小不能超过 10MB!',
          type: 'warning'
        })
      }
      // 开启loading
      this.loading = true
    },
    // 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeUploadFilesXmind(file) {
      const extension = file.name.substring(file.name.lastIndexOf('.') + 1)
      // 后缀名限制
      if (extension !== 'xmind') {
        this.$message({
          message: '上传文件只能是xmind格式!',
          type: 'warning'
        })
      }
      // 大小限制
      const fileSize = file.size
      const lisLt30M = fileSize / 1000 / 1000 < 10
      if (!lisLt30M) {
        this.$message({
          message: '上传文件大小不能超过 10MB!',
          type: 'warning'
        })
      }
      // 开启loading
      this.loading = true
    },
    // 文件上传成功时的钩子
    uploadFilesSuccess(res) {
      if (res.code === '000000') {
        this.showMsg(res.msg, 'success')
        // this.uploadFileList.push(res.data)
        // 去掉文件列表
        this.$refs.uploadFiles.clearFiles()
        // 关闭loading
        this.loading = false
        this.$emit('update')
      } else {
        // 关闭loading
        this.loading = false
        this.showMsg(res.msg, 'warning')
        // 去掉文件列表
        this.$refs.uploadFiles.clearFiles()
      }
    },
    // 删除文件
    delFile(file, fileList) {
      this.uploadFileList = fileList
      const para = {
        id: file.id
      }
      this.$axiosUtil.del(this.$appConfig.API, this.$urlConst.FILE_DELETE, para).then((res) => {
        if (res.code === '000000') {
          console.log()
        } else {
          this.showMsg(res.msg || res.message)
        }
      })
    },
    // 文件上传失败时的钩子
    uploadFilesError(err, file, fileList) {
      // 关闭loading
      this.loading = false
      this.showMsg('上传失败', 'warning')
      console.log(err)
    },
    // 删除上传文件
    handleRemove(file, fileList) {
      this.uploadFileList = JSON.parse(JSON.stringify(fileList))
    },
    excelUpload() {
      return this.$appConfig.API.baseUrl + '/testcase/file/importExcel'
    },
    xmindUpload() {
      return this.$appConfig.API.baseUrl + '/testcase/file/importXmind'
    }
  }
}

</script>

<style lang="scss">
.env-msg-gloaba {
  margin-top: 15px;
  .header {
    display: flex;
    justify-content: space-between;
  }
  .search {
    margin-bottom: 10px;
    line-height: 32px;
    display: flex;
    justify-content: space-between;
  }

  .global-body {
    .el-form-item--mini.el-form-item {
      margin: 4px 0 12px 0;
    }

    .operation-header {
      display: flex;
      justify-content: space-between;

      .btnBG {
        height: 21px;
        margin-top: 10px;
      }
    }
  }

  .drive-info {
    margin: 15px 0 10px;
  }

  .global-footer {
    margin-top: 20px;
  }

  .create-footer {
    margin-top: 20px;
    text-align: right;
  }

  .usc-content-codemirror {
    height: 260px;
  }

  .json-view-list {
    padding: 15px 0;

    .view {
      margin-top: 15px;
    }
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .font1 {
    font-family: PingFangSC;
    font-weight: 550 !important;
    color: #4a4a4a;
    white-space: nowrap;
  }
  .font2 {
    font-family: PingFangSC;
    font-weight: 300 !important;
    color: #969393;
    white-space: nowrap;
  }
  .btn-background2 {
    z-index: 10;
    height: 50;
    color: #fff;
    font-weight: bold;
    border-radius: 4px;
    position: relative;
    padding: 32px 0 32px 160px;
    background: url(~@/assets/welcome/icon-workbench.png) right center no-repeat,
      linear-gradient(#4478f7, #86b5fd);
  }
  .btn-background {
    background: #f3f3f5;
    //font-weight: bold;
    border-radius: 4px;
    position: relative;
    padding: 10px 0 10px 10px;
  }
}
</style>
