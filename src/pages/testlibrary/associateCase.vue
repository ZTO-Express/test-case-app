<template>
  <div class="env-msg-gloaba">
    <el-row :gutter="10"
            class="h100">
      <el-col :xl="5"
              :lg="6"
              class="h100">
        <div class="flex h100"
             id="treeinfos"
             style="height: 565px;width: 270px;min-width: 160px;overflow-x: auto">
          <div class="uscMsg-tree-index flex-1">
            <el-input placeholder="输入关键字进行过滤"
                      v-model="filterText"
                      clearable
                      style="margin-bottom: 8px;width: 60%" />
            <el-tree style="width: 450px"
                     class="filter-tree"
                     :data="treeData"
                     :props="defaultProps"
                     node-key="id"
                     :default-expanded-keys="openkeys"
                     :filter-node-method="filterNode"
                     ref="departmentTree">
              <span class="custom-tree-node"
                    slot-scope="{ node, data }">
                <div class="flex-wrap"
                     @click="handleNodeClick(node)">
                  <span>{{ node.label }}</span>
                </div>
              </span>
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :xl="19"
              :lg="18"
              class="h100">
        <el-card class="box-card h100"
                 shadow="hover">
          <el-form ref="search"
                   inline
                   :model="caseListPara"
                   label-width="80px">
            <el-form-item label="用例名称"
                          prop="name">
              <el-input v-model="caseListPara.name" />
            </el-form-item>
            <el-form-item prop="type"
                          label="用例类型">
              <el-select placeholder="请选择"
                         v-model="caseListPara.type"
                         filterable
                         style="width: 120px">
                <el-option :key="index"
                           :label="item.name"
                           :value="item.id"
                           v-for="(item,index) in typeList"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="维护人"
                          prop="user">
              <el-input v-model="caseListPara.user" />
            </el-form-item>
            <el-form-item prop="priority"
                          label="优先级">
              <el-select placeholder="请选择"
                         v-model="caseListPara.priority"
                         clearable
                         filterable
                         style="width: 100px">
                <el-option :key="index"
                           :label="item.label"
                           :value="item.value"
                           v-for="(item,index) in priorityList"></el-option>
              </el-select>
            </el-form-item>
            <!-- <el-form-item prop="gid" label="显示">
              <el-select placeholder="请选择" v-model="search.showSubModule" size="small" filterable>
                <el-option :key="index" :label="item.name" :value="item.id" v-for="(item,index) in Options" />
              </el-select>
            </el-form-item> -->
            <el-form-item>
              <el-button type="primary"
                         size="small"
                         @click="getCaseList">查询</el-button>
              <el-button size="small"
                         @click="reset">重置</el-button>
              <el-button type="primary"
                         size="small"
                         @click="submit">添加</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="caseList"
                    @select="handleSelectionChange"
                    @select-all="handleSelectionChange">
            <el-table-column type="selection"
                             width="35"
                             label="全选" />
            <el-table-column prop="id"
                             label="ID"
                             width="140"
                             v-if="false"
                             show-overflow-tooltip />
            <el-table-column prop="name"
                             label="用例名称"
                             show-overflow-tooltip />
            <el-table-column prop="updateUser"
                             label="维护人"
                             width="120"
                             show-overflow-tooltip />
            <el-table-column prop="priority"
                             width="80"
                             label="优先级"
                             show-overflow-tooltip>
              <template slot-scope="{ row }">
                <el-tag :type="row.priority === 1 ? 'success' : row.priority === 2 ? 'primary' : 'danger'"
                        disable-transitions>{{row.priority === 1 ? '低' : row.priority === 2 ? '中' : '高'}}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="type"
                             label="用例类型"
                             width="100">
              <template slot-scope="{ row }">
                <!--<el-tag type="info" disable-transitions>{{ row.type === 1 ? '功能测试' : row.type === 2 ? '冒烟测试'-->
                <!--: row.type === 3 ? '性能测试' : row.type === 4 ? '自动化测试'-->
                <!--: row.type === 5 ? '接口测试' : row.type === 6 ? '安装部署'-->
                <!--: row.type === 7 ? '配置相关' : row.type === 8 ? '安全测试' : row.type === 10 ? '典型测试' : '其它'}}</el-tag>-->
                <el-tag type="info"
                        disable-transitions>{{getCaseType(row.type,typeList)}}</el-tag>
              </template>
            </el-table-column>
          </el-table>
          <div class="footer-page">
            <complete-pagination :total="page.total"
                                 :currentPage="page.currentPage"
                                 :pageSize="page.pageSize"
                                 @updatePage="updatePage" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'

export default {
  name: 'global',
  data() {
    return {
      caseListPara: {
        pageNo: 1,
        pageSize: 10,
        name: '',
        type: '',
        priority: '',
        user: '',
        moduleId: '',
        showSubModule: 1
      },
      page: {
        total: 0,
        pageSize: 10,
        currentPage: 1
      },
      typeList: [],
      caseList: [],
      filterText: '', // 输入查询的节点
      openkeys: [], // 默认打开的id
      treeData: [
        {
          id: 1,
          name: '科技与信息中心',
          children: []
        }
      ],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      multipleSelection: [],
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
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  watch: {
    filterText(val) {
      this.$refs.departmentTree.filter(val)
    }
  },
  methods: {
    initViewData(row) {
      this.getModuleList()
      this.getCaseList()
      this.getTypeList()
    },
    // 表格选中
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 用例查询
    getCaseList() {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_TESTCASE_LIST, this.caseListPara).then((res) => {
        if (res.code === '000000') {
          this.page.currentPage = res.data.pageNum
          this.page.pageSize = res.data.pageSize
          this.page.total = res.data.total
          this.caseList = res.data.list
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    // 初始获取数据
    getModuleList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_MODULE_TREE, {}).then((res) => {
        if (res.code === '000000') {
          if (res.data) {
            let arr = []
            arr = res.data
            this.treeData = arr
          } else {
            this.showMsg('数据为空')
          }
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    updatePage(val) {
      this.page.currentPage = val.currentPage
      this.caseListPara.pageSize = val.pageSize
      this.caseListPara.pageNo = val.currentPage
      this.getCaseList()
    },
    // 选中的树节点
    handleNodeClick(data) {
      this.nodeInfo = Object.assign({}, data)
      this.caseListPara.moduleId = data.data.id
      if (data.data.level >= 5) {
        this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_NEXT_MODULES_BY_ID, { id: this.nodeInfo.data.id }).then((res) => {
          if (res.code === '000000') {
            if (res.data.length > 0) {
              this.addedNode = res.data
              this.getArray(this.treeData, this.nodeInfo.data.id)
            } else {
              // this.showMsg('数据为空')
            }
          } else {
            this.showMsg(res.msg || res.message, 'error')
          }
        })
      }
      this.getCaseList()
    },
    getArray(data, id) {
      let arr
      for (var i in data) {
        if (data[i].id === id) {
          arr = data[i]
          arr.children = this.addedNode
          break
        } else {
          this.getArray(data[i].children, id)
        }
      }
      return arr
    },
    // 过滤树节点
    // filterNode(value, data) {
    //   if (!value) return true
    //   return data.name.indexOf(value) !== -1
    // },
    filterNode(value, data, node) {
      if (!value) return true
      let parentNode = node.parent
      let labels = [node.label]
      let level = 1
      while (level < node.level) {
        labels = [...labels, parentNode.label]
        parentNode = parentNode.parent
        level++
      }
      return labels.some(label => label.indexOf(value) !== -1)
    },
    submit() {
      const ids = this.multipleSelection.map((item) => item.id)
      if (ids === null | ids === '' || ids === undefined || ids.length === 0) {
        this.showMsg('请选择测试用例', 'warning')
        return
      }
      const para = {
        planId: this.$route.query.id,
        caseList: ids,
        type: 1,
        user: this.$appData.userInfo.nickName
      }
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.ASSOCIATE_TESTCASE, para).then((res) => {
        if (res.code === '000000') {
          this.showMsg(res.msg || res.message, 'success')
          this.$emit('update')
          const planId = this.$route.query.id
          this.api.getModuleList({ planId }).then((res) => {
            if (res.code === '000000') {
              if (res.data.length > 0) {
                this.treeData = res.data
              } else {
                this.showMsg('暂未关联测试用例', 'warning')
              }
            } else {
              this.showMsg(res.msg || res.message, 'error')
            }
          })
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
        this.multipleSelection = []
      })
    },
    reset() { // 重置
      this.caseListPara.name = ''
      this.caseListPara.type = ''
      this.caseListPara.user = ''
      this.caseListPara.priority = ''
    },
    getTypeList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_TYPE_LIST).then((res) => {
        if (res.code === '000000') {
          this.typeList = res.data
        } else {
          this.showMsg(res.msg || res.message)
        }
      })
    },
    // 根据接口返回测试类型
    getCaseType(id, list) {
      let res = '其他'
      for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
          res = list[i].name
        }
      }
      return res
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
