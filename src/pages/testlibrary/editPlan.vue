<template>
  <div class="edit-plan">
    <div>
      <el-form ref="search"
               inline
               :model="search"
               label-width="100px">
        <el-form-item label="用例执行结果">
          <el-button v-model="search.result"
                     :plain="this.isPlain0"
                     type="primary"
                     value="0"
                     @click.prevent="setCaseResult(0)">未执行</el-button>
          <el-button v-model="search.result"
                     :plain="this.isPlain1"
                     type="success"
                     value="1"
                     @click.prevent="setCaseResult(1)">通过</el-button>
          <el-button v-model="search.result"
                     :plain="this.isPlain2"
                     type="danger"
                     value="2"
                     @click.prevent="setCaseResult(2)">失败</el-button>
          <el-button v-model="search.result"
                     :plain="this.isPlain3"
                     type="warning"
                     value="3"
                     @click.prevent="setCaseResult(3)">阻塞</el-button>
          <el-button v-model="search.result"
                     :plain="this.isPlain4"
                     type="info"
                     value="4"
                     @click.prevent="setCaseResult(4)">跳过</el-button>
          <!--<el-button v-model="search.result" :plain="item.isPlain" v-for="(item, index) in resultList" :key="index" :label="item.value" :type="item.tag"-->
          <!--:value="item.value" @click.prevent="setCaseResult(item.value)">{{item.label}}-->
          <!--</el-button>-->
        </el-form-item>
        <!--<el-form-item label="用例执行结果">-->
        <!--<el-radio-group fill='#d4237a' v-model="search.result">-->
        <!--<el-radio-button style="margin-right: 20px;border: white" v-for="(item, index) in resultList" :key="index" :label="item.value" :value="item.value" :type="item.tag">{{item.label}}</el-radio-button>-->
        <!--</el-radio-group>-->
        <!--</el-form-item>-->
        <el-form-item label="用例名称"
                      style="width: 100%"
                      prop="name">
          <el-input v-model="search.name"
                    style="width: 300%"
                    disabled />
        </el-form-item>
        <el-form-item label="优先级">
          <el-input v-model="priority"
                    disabled />
        </el-form-item>
        <el-form-item label="用例类型"
                      prop="type">
          <el-input v-model="type"
                    disabled />
        </el-form-item>
        <el-form-item label="所属模块"
                      prop="moduleName">
          <el-input v-model="search.moduleName"
                    disabled />
        </el-form-item>
        <el-form-item label="执行结果备注"
                      prop="resultComment">
          <textarea v-model.trim="search.resultComment"
                    @paste="onPaste"
                    cols="58"
                    rows="5"
                    maxlength="500"
                    placeholder="添加执行结果备注，可粘贴图片上传为附件"
                    size="small"></textarea>
        </el-form-item>
        <el-form-item label="执行结果附件"
                      style="width: 100%">
          <el-upload ref="uploadFiles"
                     :action="uploadUrl()"
                     :data="uploadPara"
                     :before-upload="beforeUploadFiles"
                     :on-success="uploadFilesSuccess"
                     :on-remove="delFile"
                     :on-error="uploadFilesError"
                     :file-list="search.resultFileList"
                     :on-preview="handlePreview">
            <el-button type="primary"
                       plain><i class="el-icon-upload"></i>上传附件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div class="content">
        <el-tabs v-model="paneActiveName">
          <el-tab-pane label="用例执行"
                       name="first">
            <el-table :data="search.stepList">
              <el-table-column prop="stepDesc"
                               label="步骤描述">
              </el-table-column>
              <el-table-column prop="expectResult"
                               label="预期结果">
              </el-table-column>
              <el-table-column prop
                               label="实际结果">
                <template slot-scope="{ row }">
                  <div style="margin-bottom: -18px">
                    <el-form :model="row"
                             :ref="'value_'+row.num"
                             status-icon>
                      <el-form-item prop="value">
                        <el-input placeholder="请输入实际结果"
                                  v-model.trim="row.actualResult"
                                  clearable
                                  size="small" />
                      </el-form-item>
                    </el-form>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop
                               width="120"
                               label="步骤执行结果">
                <template slot-scope="{ row }">
                  <div>
                    <el-select placeholder="请选择步骤执行结果"
                               v-model="row.result"
                               size="small"
                               filterable
                               @change="setResult">
                      <el-option :key="index"
                                 :label="item.label"
                                 :value="item.value"
                                 v-for="(item,index) in resultList"></el-option>
                    </el-select>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="文件"
                       name="second">
            <div v-for="(item,index) in search.fileList"
                 :key="index"
                 @click="download(item)"
                 style="margin-top: 24px">
              <el-link type="primary">{{item.name}}<i class="el-icon-download el-icon--right"></i></el-link>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <btn title="保存"
           type="primary"
           @btnClick="save" />
    </div>
  </div>
</template>
<script>
import operate from './operate'
import axios from 'axios'

export default {
  name: 'global',
  data() {
    return {
      search: {
        planId: '',
        caseId: '',
        result: '',
        resultComment: '',
        user: '',
        name: '',
        id: '',
        stepList: [],
        fileList: []
      },
      type: '',
      priority: '',
      typeList: [],
      tableData: [
        { id: 1 }
      ],
      isPlain0: true,
      isPlain1: true,
      isPlain2: true,
      isPlain3: true,
      isPlain4: true,
      resultList: [
        {
          label: '未执行', value: 0, tag: 'primary'
        },
        {
          label: '通过', value: 1, tag: 'success'
        },
        {
          label: '失败', value: 2, tag: 'danger'
        },
        {
          label: '阻塞', value: 3, tag: 'warning'
        },
        {
          label: '跳过', value: 4, tag: 'info'
        }
      ],
      uploadFileList: [],
      paneActiveName: 'first',
      priorityList: [
        {
          label: '高', value: 3
        },
        {
          label: '中', value: 2
        },
        {
          label: '低', value: 1
        }
      ],
      uploadPara: {
        user: ''
      }
    }
  },
  components: {
    operate
  },
  methods: {
    initViewData(row) {
      this.isPlain0 = true
      this.isPlain1 = true
      this.isPlain2 = true
      this.isPlain3 = true
      this.isPlain4 = true
      if (row.result === 0) {
        this.isPlain0 = false
      } else if (row.result === 1) {
        this.isPlain1 = false
      } else if (row.result === 2) {
        this.isPlain2 = false
      } else if (row.result === 3) {
        this.isPlain3 = false
      } else if (row.result === 4) {
        this.isPlain4 = false
      }
      this.getTypeList(row)
      this.search.caseId = row.caseId
      this.search.id = row.id
      const pri = this.priorityList.filter((item) => {
        if (item.value === row.priority) {
          return item
        }
      })
      this.priority = pri[0].label
      this.getCaseListDetail(row)
    },
    getTypeList(row) {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_TYPE_LIST).then((res) => {
        if (res.code === '000000') {
          const type = res.data.filter((item) => {
            if (item.id === row.type) {
              return item
            }
          })
          this.type = type[0].name
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    getCaseListDetail(row) {
      const para = {
        planId: this.$route.query.id,
        caseId: row.caseId,
        id: this.search.id
      }
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.PLAN_CASE_DETAIL, para).then((res) => {
        if (res.code === '000000') {
          this.search = Object.assign(this.search, res.data)
        } else {
          this.showMsg(res.msg || res.message)
        }
      })
    },
    // 文件下载
    download(obj) {
      window.open(`/testcase/file/download?id=${obj.id}`)
    },
    addField() {
      // 新增字段
      const numb = this.tableData
      const obj = {
        key: '',
        value: '',
        num: numb.length !== 0 ? numb[numb.length - 1].num + 1 : 1
      }
      this.tableData.push(obj)
    },
    save() {
      this.search.planId = this.$route.query.id
      this.search.user = this.$appData.userInfo.nickName

      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.PLAN_CASE_EDIT, this.search).then((res) => {
        if (res.code === '000000') {
          this.showMsg(res.msg || res.message, 'success')
          this.$emit('update')
        } else {
          this.showMsg(res.msg || res.message)
        }
      })
    },
    // 处理执行结果
    setResult() {
      // 有失败则全失败
      const fail = this.search.stepList.filter((item) => {
        return item.result === 2
      })
      if (fail.length > 0) {
        this.search.result = 2
        this.isPlain0 = true
        this.isPlain1 = true
        this.isPlain2 = false
        this.isPlain3 = true
        this.isPlain4 = true
        return
      }
      // 没有失败有阻塞就阻塞
      const stop = this.search.stepList.filter((item) => {
        return item.result === 3
      })
      if (stop.length > 0) {
        this.search.result = 3
        this.isPlain0 = true
        this.isPlain1 = true
        this.isPlain2 = true
        this.isPlain3 = false
        this.isPlain4 = true
        return
      }
      // 没有失败没有阻塞有跳过则跳过
      const skip = this.search.stepList.filter((item) => {
        return item.result === 4
      })
      if (skip.length > 0) {
        this.search.result = 4
        this.isPlain0 = true
        this.isPlain1 = true
        this.isPlain2 = true
        this.isPlain3 = true
        this.isPlain4 = false
        return
      }
      // 全通过就是通过
      const success = this.search.stepList.filter((item) => {
        return item.result === 1
      })
      if (success.length === this.search.stepList.length) {
        this.search.result = 1
        this.isPlain0 = true
        this.isPlain1 = false
        this.isPlain2 = true
        this.isPlain3 = true
        this.isPlain4 = true
        return
      }
    },
    reset() {
      this.tableData.forEach(item => {
        this.$nextTick(() => {
          item.key = ''
          item.value = ''
        })
        if (this.$refs[`key_${item.num}`] !== undefined) {
          this.$refs[`key_${item.num}`].resetFields()
        }
        if (this.$refs[`value_${item.num}`] !== undefined) {
          this.$refs[`value_${item.num}`].resetFields()
        }
      })
      this.table.drivetableData.forEach(item => {
        this.$nextTick(() => {
          item.key = ''
          item.value = ''
        })
        if (this.$refs[`keyd_${item.num}`] !== undefined) {
          this.$refs[`keyd_${item.num}`].resetFields()
        }
        if (this.$refs[`valued_${item.num}`] !== undefined) {
          this.$refs[`valued_${item.num}`].resetFields()
        }
      })
      if (this.isCreate) {
        // 新增和编辑
        this.$refs.SearchC.reset()
      }
      this.driveInfo = {}
    },
    deleteField(index) {
      // 删除
      this.tableData = this.tableData.filter(
        item => item.num !== index
      )
    },
    // 文件上传失败时的钩子
    uploadFilesError(err, file, fileList) {
      this.showMsg('上传失败', 'error')
      console.log(err)
    },
    uploadFilesSuccess(res, file) {
      if (res.code === '000000') {
        this.showMsg(res.msg, 'success')
        // var houzhui = file.name.split('.') // 获取上传文件的后缀
        // var title = document.getElementsByClassName('el-icon-document')[this.search.resultFileList.length - 1]
        // console.log(title)
        // if (houzhui[1] === 'png' || houzhui[1] === 'jpg' || houzhui[1] === 'jpeg' || houzhui[1] === 'gif') {
        //   console.log(JSON.stringify(title.classList))
        //   console.log(typeof (title.classList))
        //   title.classList = ''
        //   title.classList.add('el-icon-picture-outline') // 图片，动图
        //   console.log('到这里了吗？')
        //   console.log(JSON.stringify(title.classList))
        // } else {
        //   title.classList.add('el-icon-document') // 其他默认文档
        // }
        this.search.resultFileList.push(res.data)
      } else {
        this.showMsg(res.msg, 'error')
      }
    },
    // 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeUploadFiles(file) {
      this.uploadPara.caseId = this.search.caseId
      this.uploadPara.planId = this.$route.query.id
      this.uploadPara.planCaseId = this.search.id
      this.uploadPara.type = 2
      this.uploadPara.user = this.$appData.userInfo.nickName
    },
    // 删除文件
    delFile(file, fileList) {
      this.search.resultFileList = fileList
      const para = {
        id: file.id
      }
      this.$axiosUtil.del(this.$appConfig.API, this.$urlConst.FILE_DELETE, para).then((res) => {
        if (res.code === '000000') {
          this.showMsg('删除成功', 'success')
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    // 文件下载
    handlePreview(obj) {
      // 后缀名
      // var suffix = obj.name.split('.') // 获取上传文件的后缀
      // console.log(JSON.stringify(obj))
      // if (suffix[1] === 'png' || suffix[1] === 'jpg' || suffix[1] === 'jpeg' || suffix[1] === 'gif') {
      //   // 打开图片预览
      //   this.viewUrl = [`/testcase/file/download?id=${obj.id}`]
      //   this.imgsVisible = true
      // } else {
      //   window.open(`/testcase/file/download?id=${obj.id}`)
      // }
      window.open(`/testcase/file/download?id=${obj.id}`)
    },
    handleFilesAdd(file) {
      // 创建一个axios实例
      const instance = axios.create({
        // baseURL: HostName,
        withCredentials: true
      })
      this.uploadPara.caseId = this.search.caseId
      this.uploadPara.planId = this.$route.query.id
      this.uploadPara.type = 2
      console.log(this.search)
      console.log(file)
      const param = new FormData()
      param.append('file', file)
      param.append('user', this.$appData.userInfo.nickName)
      param.append('caseId', this.search.caseId)
      param.append('planId', this.$route.query.id)
      param.append('planCaseId', this.search.id)
      param.append('type', 2)
      instance.post('/testcase/file/upload', param).then(res => {
        // console.log(res)
        if (res.data.code === '000000') {
          this.showMsg('操作成功', 'success')
          this.search.resultFileList.push(res.data.data)
        } else {
          this.showMsg(res.data.msg || res.data.message)
        }
      })
    },
    executeEditRow(type, row) {
      this.modal.visiable = true
      this.modal.title = '编辑'
      this.$nextTick(() => {
        this.$refs.operate.initViewData(type, row)
      })
    },
    setCaseResult(result) {
      this.isPlain0 = true
      this.isPlain1 = true
      this.isPlain2 = true
      this.isPlain3 = true
      this.isPlain4 = true
      if (result === 0) {
        this.isPlain0 = false
      } else if (result === 1) {
        this.isPlain1 = false
      } else if (result === 2) {
        this.isPlain2 = false
      } else if (result === 3) {
        this.isPlain3 = false
      } else if (result === 4) {
        this.isPlain4 = false
      }
      this.search.result = result
    },
    onPaste(evt) {
      console.log('on paste', evt)
      const items = evt.clipboardData && evt.clipboardData.items
      let file = null
      if (items && items.length) {
        // 检索剪切板items
        for (var i = 0; i < items.length; i++) {
          if (items[i].type.indexOf('image') !== -1) {
            file = items[i].getAsFile()
          }
        }
      }
      if (!file) {
        // this.$message.error('粘贴内容不是图片')
        return
      }
      this.handleFilesAdd(file)
    },
    uploadUrl() {
      // 返回上传地址
      return this.$appConfig.API.baseUrl + this.$urlConst.FILE_UPLOAD
    }
  }
}

</script>
<style lang="scss">
.edit-plan {
  margin-top: 15px;
  .header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 24px;
  }
  .content {
    margin-top: 24px;
    min-height: 300px;
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
}
</style>
