<template>
  <div class="env-msg-gloaba">
    <div>
      <el-form ref="search"
               inline
               label-width="80px"
               style="margin-bottom: 20px">
        <span>选择模块</span>
      </el-form>
    </div>
    <div class="flex h100"
         id="treeinfos"
         style="height: 500px;overflow-x: auto">
      <div class="uscMsg-tree-index flex-1">
        <el-input placeholder="输入关键字进行过滤"
                  v-model="filterText"
                  clearable
                  style="margin-bottom: 8px;" />
        <el-tree :data="treeData"
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
    <div>
      <el-button type="primary" size="mini"
                 @click="submit">保存</el-button>
    </div>
  </div>
</template>
<script>

export default {
  name: 'global',
  data() {
    return {
      moduleId: '',
      moduleName: '',
      multipleSelection: [],
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
      }
    }
  },
  components: {
  },
  computed: {

  },
  watch: {
    filterText(val) {
      this.$refs.departmentTree.filter(val)
    }
  },
  methods: {
    initViewData(ids) {
      this.multipleSelection = ids
      this.getModuleList()
    },
    // 初始获取数据
    getModuleList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_MODULE_TREE, {}).then((res) => {
        if (res.code === '000000') {
          if (res.data) {
            const arr = res.data
            this.treeData = arr
          } else {
            this.showMsg('数据为空')
          }
        } else {
          this.showMsg(res.msg || res.message, 'error')
        }
      })
    },
    // 选中的树节点
    handleNodeClick(data) {
      this.nodeInfo = Object.assign({}, data)
      this.moduleId = data.data.id
      this.moduleName = data.data.name
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
      if (this.moduleId === null || this.moduleId === undefined || this.moduleId === '') {
        this.showMsg('请选择模块', 'warning')
        return
      }
      this.$confirm('确认复制所选用例到[' + this.moduleName + ']模块下?', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const para = {
          ids: this.multipleSelection,
          moduleId: this.moduleId,
          user: this.$appData.userInfo.nickName
        }
        this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.COPY_TESTCASE, para).then((res) => {
          if (res.code === '000000') {
            this.$emit('update')
            this.showMsg(res.msg || res.message, 'success')
            this.multipleSelection = []
          } else {
            this.showMsg(res.msg || res.message, 'error')
          }
        })
      })
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
