<template>
  <div class="env-msg-gloaba">
    <el-form ref="search" :model="search" label-width="120px" :rules="baseInfoRules">
      <el-form-item label="部门" prop="bid">
        <el-cascader
          class="elcascader"
          ref="elcascader"
          v-model="search.bid"
          :options="bidOptions"
          placeholder="请选择部门"
          :show-all-levels="false"
          :props="{ checkStrictly: true, expandTrigger: 'hover' }"
          filterable
          style="width: 100%"
          @change="handleBusinessChange"
        ></el-cascader>
      </el-form-item>
      <el-form-item label="项目" prop="pid">
        <el-select
          v-model="search.pid"
          filterable
          placeholder="请选择项目"
          style="width: 100%"
          @change="handlePidChange"
        >
          <el-option
            v-for="item in pidOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="版本" prop="vid">
        <el-select v-model="search.vid" filterable placeholder="请选择版本" style="width: 100%" @change="setVersionName">
          <el-option v-for="item in versionOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="测试阶段" prop="stageId">
        <el-select placeholder="请选择测试阶段" v-model="search.stageId" size="small" filterable style="width: 100%" @change="setStageName">
          <el-option :key="index" :label="item.name" :value="item.id" v-for="(item,index) in stageList"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="执行计划负责人" prop="executeUser">
        <el-select placeholder="请选择执行计划负责人" v-model="search.executeUser" size="small" filterable style="width: 100%">
          <el-option :key="index" :label="item.nickName" :value="item.id" v-for="(item,index) in userList"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="执行计划名称" prop="planName">
        <el-input placeholder="请输入执行计划名称" v-model="search.planName" clearable maxlength="100"/>
      </el-form-item>
    </el-form>
    <div>
      <btn title="保存" type="primary" @btnClick="save" />
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { validateLogin } from '@/config/validateRules'
export default {
  name: 'global',
  data() {
    return {
      search: {
        id: '',
        planName: '',
        user: '',
        executeUser: '',
        stageId: '',
        stage: '',
        bid: '',
        pid: '',
        vid: ''
      },
      pName: '',
      vName: '',
      stageName: '',
      tableData: [],
      bidOptions: [],
      pidOptions: [],
      versionOptions: [],
      userList: [], // 执行计划负责人
      stageList: [],
      baseInfoRules: {
        planName: validateLogin.name,
        executeUser: validateLogin.options,
        stageId: validateLogin.options,
        bid: validateLogin.options,
        pid: validateLogin.options,
        vid: validateLogin.options
      }
    }
  },
  components: {},
  computed: {
    ...mapGetters(['userInfo'])
  },
  methods: {
    initViewData(row) {
      this.search = {
        id: '',
        planName: '',
        executeUser: '',
        stageId: '',
        stage: '',
        bid: '',
        pid: '',
        vid: ''
      }
      if (row) {
        this.search = row
        this.pName = row.pname
        this.vName = row.vname
        this.stageName = row.stageName
        this.updatePlanName()
      } else {
        this.pName = ''
        this.vName = ''
        this.stageName = ''
      }
      this.initBusinessData()
      this.getUserList()
      this.getTestStageList()
    },
    save() {
      this.$refs['search'].validate(valid => {
        // 校验基础信息
        if (valid) {
          this.search.user = this.$appData.userInfo.nickName
          this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.SAVE_PLAN, this.search).then((res) => {
            if (res.code === '000000') {
              this.showMsg(res.msg || res.message, 'success')
              this.$emit('update')
            } else {
              this.showMsg(res.msg || res.message, 'error')
            }
            this.search.planName = ''
          })
        } else {
          return false
        }
      })
    },
    // 获取测试负责人
    getUserList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_ALL_TESTER).then((res) => {
        if (res.code === '000000') {
          this.userList = res.data
        } else {
          this.showMsg(res.msg)
        }
      })
    },
    // 获取测试阶段
    getTestStageList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_ALL_STAGE).then((res) => {
        if (res.code === '000000') {
          this.stageList = res.data
        } else {
          this.showMsg(res.msg)
        }
      })
    },
    // 部门：下拉选中项
    handleBusinessChange(e) {
      this.search.pid = ''
      this.search.vid = ''
      this.versionOptions = []
      if (e.length > 0) {
        this.search.bid = e[e.length - 1]
      } else {
        this.search.bid = ''
        return
      }
      this.$refs.elcascader.dropDownVisible = false
      this.getProjectList(this.search.bid)
      this.pName = ''
      this.vName = ''
      this.updatePlanName()
    },
    // 项目：部门对应的项目
    getProjectList(id) {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_PROJECT_LIST_BY_BID + '?id=' + id).then((res) => {
        if (res.code === '000000') {
          this.pidOptions = res.data.map(e => {
            const obj = {
              value: e.id,
              label: e.pname
            }
            return obj
          })
          if (this.search.pid) {
            this.getVersionByPid(this.search.pid)
          }
        } else {
          this.showMsg(res.msg)
        }
      })
    },
    // 递归处理部门数据
    formatBusinessData(data) {
      const result = data.map(e => {
        const obj = {
          value: e.id,
          label: e.bname
        }
        if (e.children && e.children.length > 0) {
          obj.children = this.formatBusinessData(e.children)
        }
        return obj
      })
      return result
    },
    getVersionByPid(id) {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_VERSION_BY_PID + '?pId=' + id).then((res) => {
        if (res.code === '000000') {
          this.versionOptions = res.data.map((e) => {
            const obj = {
              value: e.id,
              label: e.name
            }
            return obj
          })
        } else {
          this.showMsg(res.msg)
        }
      })
    },
    handlePidChange() {
      this.search.vid = ''
      if (this.search.pid.length === 0) {
        return
      }
      this.getVersionByPid(this.search.pid)
      let objTemp = {}
      objTemp = this.pidOptions.find(item => {
        return item.value === this.search.pid
      })
      this.pName = objTemp.label
      this.vName = ''
      this.updatePlanName()
    },
    // 初始化部门数据
    initBusinessData() {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_BUSINESS_LINE_TREE).then((res) => {
        if (res.code === '000000') {
          this.bidOptions = this.formatBusinessData(res.data.children)
          if (this.search.id) {
            this.getProjectList(this.search.bid)
          }
        } else {
          this.showMsg(res.msg)
        }
      })
    },
    setStageName() {
      let obj = {}
      obj = this.stageList.find(item => {
        return item.id === this.search.stageId
      })
      this.stageName = obj.name
      this.updatePlanName()
    },
    setVersionName() {
      let obj = {}
      obj = this.versionOptions.find(item => {
        return item.value === this.search.vid
      })
      this.vName = obj.label
      this.updatePlanName()
    },
    updatePlanName() {
      this.search.planName = this.pName + this.vName + this.stageName + '执行计划'
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
