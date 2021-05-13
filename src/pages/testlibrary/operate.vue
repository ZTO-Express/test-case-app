<template>
  <div class="env-msg-gloaba">
    <el-form ref="search"  :model="search" label-width="80px" :rules="baseInfoRules">
      <el-form-item label="用例名称" prop="name">
        <el-input v-model="search.name" placeholder="请输入用例名称" clearable maxlength=256 />
      </el-form-item>
      <el-form-item prop="priority" label="优先级">
        <el-select placeholder="请选择优先级" v-model="search.priority" filterable>
          <el-option :key="index" :label="item.label" :value="item.value" v-for="(item,index) in priorityList"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="type" label="用例类型">
        <el-select placeholder="请选择用例类型" v-model="search.type" filterable>
          <el-option :key="index" :label="item.name" :value="item.id" v-for="(item,index) in typeList"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="precondition" label="前置条件">
        <el-input type="textarea" :rows="2" v-model="search.precondition"  placeholder="请输入前置条件" />
      </el-form-item>
      <el-form-item prop="comment" label="操作步骤" >
        <div class="global-body">
          <el-table :data="search.tcTestcaseStepList">
            <el-table-column prop="stepNumber" width="50" label="编号">
              <template slot-scope="scope">
                {{scope.$index + 1}}
              </template>
            </el-table-column>
            <el-table-column label="步骤描述">
              <template slot-scope="{ row }">
                <el-form :model="row" :ref="'key_'+row.num" status-icon>
                  <el-form-item prop="stepDesc">
                    <el-input type="textarea" placeholder="请输入步骤描述" v-model.trim="row.stepDesc" maxlength=1024 clearable />
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="expectResult" label="预期结果">
              <template slot-scope="{ row }">
                <el-form :model="row" :ref="'value_'+row.num" status-icon>
                  <el-form-item prop="value">{{row.value}}
                    <el-input type="textarea" placeholder="请输入预期结果" v-model.trim="row.expectResult" maxlength=1024 clearable />
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80px">
              <template slot="header" slot-scope="{ row }" >
                <div class="operation-header">
                  <span>操作</span>
                  <el-button type="primary" icon="el-icon-plus" @click="addField"></el-button>
                </div>
              </template>
              <template slot-scope="{ row }">
                <div>
                  <el-button type="danger" icon="el-icon-delete" @click="deleteField(row)"></el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-form-item>
      <el-form-item prop="comment" label="备注" >
        <el-input type="textarea" :rows="2" v-model="search.comment" placeholder="请输入备注" clearable maxlength=1024 />
      </el-form-item>
      <el-form-item  label="文件">
        <el-upload ref="uploadFiles" :action="uploadUrl()" :data="uploadPara" :before-upload="beforeUploadFiles" :on-success="uploadFilesSuccess" :on-remove="delFile" :on-error="uploadFilesError" :file-list="uploadFileList" :on-preview="handlePreview">
          <el-button type="primary" plain><i class="el-icon-upload"></i>上传文件</el-button> (上传文件最多5个、单个文件最大30M)
        </el-upload>
      </el-form-item>
    </el-form>
    <div>
      <el-button type="primary" @click="submit">保存</el-button>
    </div>
  </div>
</template>
<script>
import { validateLogin } from '@/config/validateRules'

export default {
  name: 'global',
  data() {
    return {
      search: {
        name: '',
        comment: '',
        priority: 3,
        precondition: '',
        fileIds: [],
        user: '',
        status: 1,
        type: 1,
        moduleId: '',
        tcTestcaseStepList: []
      },
      uploadFileList: [],
      uploadPara: {
        user: ''
      },
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
      typeList: [],
      type: 'add',
      baseInfoRules: {
        name: validateLogin.name,
        type: validateLogin.options,
        priority: validateLogin.options
      }
    }
  },
  components: {
  },
  computed: {
  },
  methods: {
    initViewData(row, id) {
      this.reset()
      this.type = row ? 'edit' : 'add'
      this.getTypeList()
      if (row) {
        this.search.name = row.name
        this.search.comment = row.comment
        this.search.priority = row.priority
        this.search.precondition = row.precondition
        this.search.moduleId = row.moduleId
        this.search.id = row.id
        this.search.type = row.type
        this.getCaseDetail(row)
        this.getCaseFileList(row)
      } else {
        this.search.id = ''
        this.search.moduleId = id
        this.search.status = 1
        // 默认3个步骤
        this.search.tcTestcaseStepList = []
        for (let i = 0; i < 3; i++) {
          this.addField()
        }
      }
    },
    getCaseDetail(row) {
      const para = {
        id: row.id
      }
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_CASE_DETAIL, para).then((res) => {
        if (res.code === '000000') {
          if (res.data === null || res.data.length === 0) {
            this.search.tcTestcaseStepList = []
            for (let i = 0; i < 3; i++) {
              this.addField()
            }
          } else {
            // console.log('编辑进来的步骤：', JSON.stringify(res.data))
            this.search.tcTestcaseStepList = res.data
          }
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    getCaseFileList(row) {
      const para = {
        id: row.id
      }
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_FILE_LIST_BY_CASE_ID, para).then((res) => {
        if (res.code === '000000') {
          this.uploadFileList = res.data
        } else {
          this.showMsg(res.msg || res.message)
        }
      })
    },
    getTypeList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_TYPE_LIST).then((res) => {
        if (res.code === '000000') {
          this.typeList = res.data
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    addField() {
      // 新增字段
      const numb = this.search.tcTestcaseStepList
      const obj = {
        id: numb.length !== 0 ? numb[numb.length - 1].stepNumber + 1 : 1,
        stepNumber: numb.length !== 0 ? numb[numb.length - 1].stepNumber + 1 : 1,
        stepDesc: '',
        expectResult: ''
      }
      this.search.tcTestcaseStepList.push(obj)
    },
    submit() {
      this.$refs.search.validate((valid) => {
        // 校验基础信息
        if (valid) {
          this.search.user = this.$appData.userInfo.nickName
          this.search.fileIds = this.uploadFileList.map(item => item.id)
          const tempTestcaseStepList = []
          this.search.tcTestcaseStepList.forEach((item, index) => {
            if (item.stepDesc !== '' && item.stepDesc !== null) {
              // 删除id元素，id元素只用来拖拽，传到后端会更改ID
              delete item.id
              tempTestcaseStepList.push(item)
              item.stepNumber = index + 1
            }
          })
          this.search.tcTestcaseStepList = tempTestcaseStepList
          // console.log('tempTestcaseStepList==', JSON.stringify(tempTestcaseStepList))
          if (this.type === 'add') {
            this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.ADD_TEST_CASE, this.search).then((res) => {
              if (res.code === '000000') {
                this.showMsg(res.msg || res.message, 'success')
                this.$emit('update')
              } else {
                this.showMsg(res.msg || res.message, 'error')
              }
            })
          } else {
            this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.EDIT_TEST_CASE, this.search).then((res) => {
              if (res.code === '000000') {
                this.showMsg(res.msg || res.message, 'success')
                this.$emit('update')
              } else {
                this.showMsg(res.msg || res.message, 'error')
              }
            })
          }
        } else {
          return false
        }
      })
    },
    reset() {
      this.$refs.search.resetFields()
      // this.search = {
      //   name: '',
      //   comment: '',
      //   priority: 3,
      //   precondition: '',
      //   fileIds: [],
      //   user: '',
      //   status: 1,
      //   type: 1,
      //   moduleId: '',
      //   tcTestcaseStepList: []
      // }
      this.uploadFileList = []
    },
    deleteField(row) {
      if (this.search.tcTestcaseStepList.length === 1) {
        this.showMsg('必须保留一条操作步骤', 'warning')
        return
      }
      // 删除
      if (isNaN(row.stepNumber)) {
        this.search.tcTestcaseStepList.pop()
      } else {
        this.search.tcTestcaseStepList = this.search.tcTestcaseStepList.filter(
          item => item.stepNumber !== row.stepNumber
        )
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
          this.showMsg('删除成功', 'success')
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    editValue(row, type = '') {
      this.table.editData = row
      this.table.visiable = true
      if (type === '') {
        this.isJsonEditValue = this.isJSON(row.value)
        this.jsonData = this.isJSON(row.value) ? JSON.parse(row.value) : row.value
      } else {
        this.isJsonEditValue = this.isJSON(row.valued)
        this.jsonData = this.isJSON(row.valued) ? JSON.parse(row.valued) : row.valued
      }
      this.isDriveOrEnv = type === ''
    },
    // 文件上传失败时的钩子
    uploadFilesError(err, file, fileList) {
      this.showMsg('上传失败', 'warning')
      console.log(err)
    },
    // 文件上传成功时的钩子
    uploadFilesSuccess(res) {
      if (res.code === '000000') {
        this.showMsg(res.msg, 'success')
        this.uploadFileList.push(res.data)
      } else {
        this.showMsg(res.msg, 'warning')
      }
    },
    // 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传
    beforeUploadFiles(file) {
      if (this.type === 'edit') {
        this.uploadPara.caseId = this.search.id
        this.uploadPara.type = 1
      }
      this.uploadPara.user = this.$appData.userInfo.nickName
    },
    // 删除上传文件
    handleRemove(file, fileList) {
      this.uploadFileList = JSON.parse(JSON.stringify(fileList))
    },
    // 文件下载
    handlePreview(obj) {
      window.open(this.$appConfig.API.baseUrl + this.$urlConst.FILE_DOWNLOAD + `?id=${obj.id}`)
    },
    uploadUrl() {
      // 返回上传地址
      return this.$appConfig.API.baseUrl + this.$urlConst.FILE_UPLOAD
    }
  }
}

</script>

<style lang="scss">
.env-msg-gloaba {
  margin-top: 15px;

  .search {
    margin-bottom: 10px;
    line-height: 32px;
    display: flex;
    justify-content: space-between;
  }

  .global-body {
    .el-form-item--mini.el-form-item {
      margin: 1px 0 1px 0;
    }

    .operation-header {
      display: flex;
      justify-content: space-between;

      .btnBG {
        height: 21px;
        /*margin-top: 10px;*/
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
