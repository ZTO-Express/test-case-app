<template>
  <div class="edit-plan">
    <div>
      <el-form ref="search" inline :model="search" label-width="100px">
        <el-form-item label="用例执行结果">
          <el-button v-model="search.result" :plain="this.isPlain0" type="primary" value="0" @click.prevent="setCaseResult(0)">未执行</el-button>
          <el-button v-model="search.result" :plain="this.isPlain1" type="success" value="1" @click.prevent="setCaseResult(1)">通过</el-button>
          <el-button v-model="search.result" :plain="this.isPlain2" type="danger" value="2" @click.prevent="setCaseResult(2)">失败</el-button>
          <el-button v-model="search.result" :plain="this.isPlain3" type="warning" value="3" @click.prevent="setCaseResult(3)">阻塞</el-button>
          <el-button v-model="search.result" :plain="this.isPlain4" type="info" value="4" @click.prevent="setCaseResult(4)">跳过</el-button>
          <!--<el-button v-model="search.result" :plain="item.isPlain" v-for="(item, index) in resultList" :key="index" :label="item.value" :type="item.tag"-->
          <!--:value="item.value" @click.prevent="setCaseResult(item.value)">{{item.label}}-->
          <!--</el-button>-->
        </el-form-item>
        <!--<el-form-item label="用例执行结果">-->
          <!--<el-radio-group fill='#d4237a' v-model="search.result">-->
            <!--<el-radio-button style="margin-right: 20px;border: white" v-for="(item, index) in resultList" :key="index" :label="item.value" :value="item.value" :type="item.tag">{{item.label}}</el-radio-button>-->
          <!--</el-radio-group>-->
        <!--</el-form-item>-->
        <el-form-item label="用例名称" style="width: 100%" prop="name">
          <el-input v-model="search.name" style="width: 300%" disabled/>
        </el-form-item>
        <el-form-item label="优先级">
          <el-input v-model="priority" disabled/>
        </el-form-item>
        <el-form-item label="用例类型" prop="type">
          <el-input v-model="type" disabled/>
        </el-form-item>
        <el-form-item label="所属模块" prop="moduleName">
          <el-input v-model="search.moduleName" disabled/>
        </el-form-item>
      </el-form>
      <div class="content">
        <el-tabs v-model="paneActiveName">
          <el-tab-pane label="用例执行" name="first">
            <zto-table :tableData="search.stepList">
              <el-table-column prop="stepDesc" label="步骤描述">
              </el-table-column>
              <el-table-column prop="expectResult" label="预期结果">
              </el-table-column>
              <el-table-column prop label="实际结果">
                <template slot-scope="{ row }">
                  <div style="margin-bottom: -18px">
                    <el-form :model="row" :ref="'value_'+row.num" status-icon>
                      <el-form-item prop="value">
                        <el-input placeholder="请输入实际结果" v-model.trim="row.actualResult" clearable
                                  size="small"/>
                      </el-form-item>
                    </el-form>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop width="120" label="步骤执行结果">
                <template slot-scope="{ row }">
                  <div>
                    <el-select placeholder="请选择步骤执行结果" v-model="row.result" size="small" filterable
                               @change="setResult">
                      <el-option :key="index" :label="item.label" :value="item.value"
                                 v-for="(item,index) in resultList"></el-option>
                    </el-select>
                  </div>
                </template>
              </el-table-column>
            </zto-table>
          </el-tab-pane>
          <el-tab-pane label="文件" name="second">
            <div v-for="(item,index) in search.fileList" :key="index" @click="download(item)"
                 style="margin-top: 24px">
              <el-link type="primary">{{item.name}}<i class="el-icon-download el-icon--right"></i></el-link>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <btn title="保存" type="primary" @btnClick="save"/>
    </div>
  </div>
</template>
<script>
  import { mapGetters } from 'vuex'
  import operate from './operate'

  export default {
    name: 'global',
    data() {
      return {
        search: {
          planId: '',
          caseId: '',
          result: '',
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
        ]
      }
    },
    components: {
      operate
    },
    computed: {
      ...mapGetters(['userInfo'])
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
        const pri = this.priorityList.filter((item) => {
          if (item.value === row.priority) {
            return item
          }
        })
        this.priority = pri[0].label
        this.getCaseListDetail(row)
      },
      getTypeList(row) {
        this.api.getTypeList().then(res => {
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
          caseId: row.id
        }
        this.api.getCaseListDetail(para).then(res => {
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
        this.search.user = this.userInfo.displayName
        this.api.editPlan(this.search).then(res => {
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
        this.showMsg('上传失败', 'warning')
        console.log(err)
      },
      // 文件上传成功时的钩子
      uploadFilesSuccess(res) {
        if (res.code === '000000') {
          this.showMsg(res.msg, 'success')
          // 清空上传列表
          this.uploadFileList = []
        } else {
          this.showMsg(res.msg, 'warning')
        }
      },
      // 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传
      beforeUploadFiles(file) {
        return new Promise((resolve, reject) => {
          // 上传大小 不大于 10M
          const fileSize = file.size / 1024 / 1024
          // 判断上传格式
          const extensionList = ['jar']
          const extensionArr = file.name.split('.')
          const extension = extensionArr[extensionArr.length - 1]
          if (extensionList.indexOf(extension) < 0) {
            this.showMsg('只允许上传 .jar 文件', 'warning')
            reject()
          } else if (fileSize > 10) {
            this.showMsg('上传超出 10M', 'warning')
            reject()
          } else {
            resolve()
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
