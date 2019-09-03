<template>
  <el-row :gutter="12" style="width: 100%">

    <el-dialog
      title="请选择要创建的内容"
      :visible.sync="nodeCreateDialogVisible"
      width="30%"
      :modal-append-to-body="false"
      align="center">
      <el-button
        size="mini"
        icon="icon-nav-list"
        @click="nodeTypeSelect('MENU')">
        菜 单
      </el-button>
      <el-button
        size="mini"
        icon="icon-setup-o"
        @click="nodeTypeSelect('CONTROLLER')">
        控 件
      </el-button>
    </el-dialog>
    <el-col :span="10" :offset="0">
      <el-card shadow="hover">
        <!--菜单树管理 开始-->
        <div class="tree-container">
          <div align="left">
            <i class="icon-nav-list"></i>
            菜单
            <i class="icon-setup-o"></i>
            控件
            <el-button
              size="mini"
              @click="treeSql()">
              导出SQL
            </el-button>
          </div>
          <el-input clearable
            placeholder="输入关键字进行过滤"
            v-model="filterText">
          </el-input>
          <el-tree
            :data="treeData"
            node-key="id"
            default-expand-all
            :expand-on-click-node="false"
            :props="defaultProps"
            draggable
            @node-drop="handleNodeDrop"
            :filter-node-method="filterNode"
            ref="tree"
          >
                        <span class="custom-tree-node" slot-scope="{ node, data }" v-if="data.menuName !== $appConfig.logoName">
                            <span>
                                <i v-if="data.nodeType === 'MENU'" class="icon-nav-list"></i>
                                <i v-if="data.nodeType === 'CONTROLLER'" class="icon-setup-o"></i>
                                {{ node.label }}</span>
                        <span>
                            <el-button
                              v-permission="'menu-page-index/edit'"
                              v-if="data.menuCode !== 'root'"
                              type="text"
                              @click="() => edit(node, data)"
                              style="color: #409EFF">
                                <i class="el-icon-edit-outline"></i>
                            </el-button>

                            <el-button
                              v-permission="'menu-page-index/add'"
                              v-if="data.nodeType === 'MENU'"
                              type="text"
                              @click="() => append(node, data)"
                              style="color: #67C23A">
                                <i class="el-icon-circle-plus"></i>
                            </el-button>

                            <el-button
                              v-permission="'menu-page-index/del'"
                              v-if="data.menuCode !== 'root'"
                              type="text"
                              @click="() => remove(node, data)"
                              style="color: #F56C6C">
                                <i class="el-icon-delete"></i>
                            </el-button>
                        </span>
                        </span>
          </el-tree>
        </div>
      </el-card>
    </el-col>
    <!--菜单树管理 结束-->
    <icon-list :trigger="iconListTrigger" @chooseIcon="chooseIcon"></icon-list>
    <!--修改表单 开始-->
    <el-col :span="14">
      <el-card shadow="hover" class="menu-form">
        <el-tabs
          :active-name="activeTabName"
          tab-position="top"
          type="card">
          <el-tab-pane
            label="菜单"
            name="MENU"
            :disabled="activeTabName!=='MENU'"
          >
            <el-form label-position="right"
                     label-width="80px"
                     :model="temp"
                     ref="menuForm"
                     :rules="rules">
              <el-form-item label="菜单名称" prop="menuName">
                <el-input clearable v-model="temp.menuName" maxlength='20'/>
              </el-form-item>
              <el-form-item label="菜单图标" prop="icon">
                <el-tooltip class="item" effect="dark" content="点击查看图标名称" placement="top-end">
                  <el-input clearable v-model="temp.icon" maxlength='20'>
                    <template slot="prepend">icon-</template>
                    <el-button slot="append" icon="el-icon-search" @click="goToIconFont"/>
                  </el-input>
                </el-tooltip>
              </el-form-item>
              <el-form-item v-permission="'menu-page-index/url'" label="菜单URL" prop="url">
                <el-input clearable v-model="temp.url" maxlength='50'/>
              </el-form-item>

              <el-form-item label="默认勾选" prop="defaultSelectStatus">
                <el-radio-group v-model="temp.defaultSelectStatus">
                  <el-radio label='1'>是</el-radio>
                  <el-radio label='0'>否</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="勾选权限" prop="changeable">
                <el-radio-group v-model="temp.changeable">
                  <el-radio label='1'>可更改</el-radio>
                  <el-radio label='0'>不可更改</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item>
                <el-button
                  type="primary"
                  size="small"
                  @click="submit('menuForm')">
                  {{btnAction.name}}
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane
            label="控件"
            name="CONTROLLER"
            :disabled="activeTabName!=='CONTROLLER'"
          >
            <el-form label-position="left"
                     label-width="80px"
                     :model="temp"
                     ref="controlForm"
                     :rules="rules">
              <el-form-item label="控件名称" prop="controlName">
                <el-input clearable v-model="temp.controlName" maxlength='20'/>
              </el-form-item>
              <el-form-item label="控件标识" prop="controlId">
                <el-input clearable v-model="temp.controlId" maxlength='50'/>
              </el-form-item>
              <el-form-item label="控件类型" prop="controlType">
                <el-radio-group
                  size="small"
                  v-model="temp.controlType">
                  <el-radio label='field' border>字 段</el-radio>
                  <el-radio label='oper' border>操 作</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  size="small"
                  @click="submit('controlForm')">
                  {{btnAction.name}}
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-col>
    <!--修改表单 结束-->

  </el-row>
</template>
<script>
import permAction from '../../../action/permAction';
import authAction from '../../../action/authAction';
import iconList from './icon-list.vue';

export default {
  data() {
    return {
      iconListTrigger: false,
      nodeCreateDialogVisible: false,
      activeTabName: 'MENU',
      btnAction: {
        name: '确 定',
        key: '',
      },
      temp: {
        id: '',
        menuName: '',
        icon: '',
        url: '',
        defaultSelectStatus: '0',
        changeable: '1',
        menuCode: '',
        parentCode: '',
        controlName: '',
        controlType: 'field',
        controlId: '',
      },
      // 表单校验
      rules: {
        menuName: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' },
          {
            min: 2, max: 20, message: '菜单名称长度不合法', trigger: 'blur',
          },
        ],
        icon: [
          {
            min: 2, max: 20, message: '菜单图标长度不合法', trigger: 'blur',
          },
        ],
        url: [
          {
            min: 2, max: 50, message: '菜单URL长度不合法', trigger: 'blur',
          },
        ],
        defaultSelectStatus: [
          { required: true, message: '请选择', trigger: 'change' },
        ],
        changeable: [
          { required: true, message: '请选择', trigger: 'change' },
        ],
        controlName: [
          { required: true, message: '请输入控件名称', trigger: 'blur' },
          {
            min: 2, max: 50, message: '控件名称长度不合法', trigger: 'blur',
          },
        ],

        controlId: [
          { required: true, message: '请输入控件标识', trigger: 'blur' },
          {
            min: 3, max: 50, message: '控件标识长度不合法', trigger: 'blur',
          },
        ],

        controlType: [
          { required: true, message: '请选择控件类型', trigger: 'blur' },
        ],
      },
      filterText: '',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'menuName',
      },
      menus: this.$appData.menus,
      treeSqlText: '',
    };
  },
  created() {
    this.refreshTree();
  },
  components: { iconList },
  computed: {},
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },

    menus() {
      this.refreshTree();
    },
  },
  methods: {
    treeSql() {
      this.treeSqlText = '';
      this.sqlNode({}, this.treeData[0]);

      const textarea = document.createElement('textarea');
      document.body.appendChild(textarea);
      textarea.innerHTML = this.treeSqlText;
      textarea.select();
      try {
        if (document.execCommand('copy', false, null)) {
          this.$message({ type: 'success', showClose: true, message: '成功复制SQL到剪贴板' });
        } else {
          this.$message({ type: 'error', showClose: true, message: '导出SQL失败' });
        }
      } catch (err) {
        this.$message({ type: 'error', showClose: true, message: '该操作不适用于当前浏览器' });
      }
      textarea.remove();
    },
    sqlNode(father, node) {
      if (node.menuCode !== 'root') {
        if (node.nodeType === 'CONTROLLER') {
          this.treeSqlText += "call ifpay_center.proc_uimeta('" + this.$appConfig.appId + "','" + father.menuName + "','" + node.controlName + "','" + node.controlId + "','" + node.controlType + "');";
        } else {
          this.treeSqlText += "call ifpay_center.proc_menu('" + this.$appConfig.appId + "','" + this.getMenuLevel(node) + "','" + node.url + "','" + node.icon + "');";
        }
        this.treeSqlText += '\n';
      }

      if (node.children) {
        node.children.forEach((child) => {
          this.sqlNode(node, child);
        });
      }
    },
    getMenuLevel(node) {
      const array = [];
      const traceMenu = (node) => {
        if (node.father && node.father.menuCode !== 'root') {
          traceMenu(node.father);
        }
        array.push(node.menuName);
      };
      traceMenu(node);
      while (array.length < 3) {
        array.push('');
      }
      return array.join("','");
    },
    goToIconFont() {
      this.iconListTrigger = !this.iconListTrigger;
    },
    chooseIcon(icon) {
      this.temp.icon = icon;
    },
    // 获取菜单树
    getTree() {
      authAction.loadAppData(this.$appData.userInfo.userId);
    },

    refreshTree() {
      const tempMenuTree = [];
      let tempUiMetaTree = [];

      Object.assign(tempMenuTree, this.menus);
      // 去掉logo节点
      for (let i = 0; i < tempMenuTree.length; i += 1) {
        if (tempMenuTree[i].menuName === this.$appConfig.logoName) {
          tempMenuTree.splice(i, 1);
        }
      }

      permAction.getUiMetaTree().then((rep) => {
        tempUiMetaTree = rep.data;
        for (let i = 0; i < tempMenuTree.length; i += 1) {
          this.traverseTempMenuTree(
            tempMenuTree[i],
            tempUiMetaTree,
            this.traverseTempMenuTree,
          );
        }
        const root = [{}];
        root[0].menuName = '根菜单';
        root[0].menuCode = 'root';
        root[0].nodeType = 'MENU';
        root[0].children = tempMenuTree;
        this.treeData = root;
      }).catch((error) => {
        // console.log(error);
        this.$message({
          type: 'error',
          message: error.respMessage,
        });
      });
    },

    traverseTempMenuTree(menu, uiMetaTree, self) {
      menu.nodeType = 'MENU'; // 标记是菜单节点
      menu.children = [];
      if (this.hasChild(menu)) {
        menu.children.push(...menu.child);
        menu.child.forEach((menuInfo) => {
          self(menuInfo, uiMetaTree, self);
        });
      }

      const uiMetas = uiMetaTree[menu.menuCode];
      if (uiMetas) {
        uiMetas.forEach((uiMeta) => {
          uiMeta.menuName = uiMeta.controlName;
          uiMeta.nodeType = 'CONTROLLER'; // 标记节点是控件
          menu.children.push(uiMeta);
        });
      }
    },

    hasChild(item) {
      return item.child && item.child.length > 0;
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.menuName.indexOf(value) !== -1;
    },

    nodeTypeSelect(flag) {
      if (flag === 'CONTROLLER' && this.temp.menuCode === 'root') {
        this.$message.error('根菜单不允许添加控件！');
        return;
      }
      this.nodeCreateDialogVisible = false;
      this.activeTabName = flag;
      this.temp.nodeType = flag;
    },

    edit(node, data) {
      this.btnAction.key = 'UPDATE';
      this.btnAction.name = '更新';
      this.temp = Object.assign({}, data);
      this.activeTabName = data.nodeType;
      if (data.changeable !== undefined) {
        this.temp.changeable = data.changeable.toString();
      }
      if (data.defaultSelectStatus !== undefined) {
        this.temp.defaultSelectStatus = data.defaultSelectStatus.toString();
      }
    },

    append(node, data) {
      this.nodeCreateDialogVisible = true;
      this.btnAction.key = 'APPEND';
      this.btnAction.name = '增加';
      this.$refs.menuForm.resetFields();
      this.$refs.controlForm.resetFields();
      this.temp.appId = this.$appConfig.appId;
      this.temp.menuCode = data.menuCode;
      this.temp.parentCode = data.menuCode;
    },

    remove(node, data) {
      this.$confirm('此操作将永久删除该菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        if (data.nodeType === 'MENU') {
          if (this.hasChild(data)) {
            this.$message({
              type: 'error',
              message: '有子节点，不允许删除！',
            });
          } else {
            permAction.deleteMenu(data.id)
              .then((resp) => {
                this.$message({
                  type: 'success',
                  message: '删除成功!',
                });
                // 刷新
                this.getTree();
              }).catch((error) => {
                this.$message({
                  type: 'warning',
                  message: '删除失败!',
                });
                // 刷新
                this.getTree();
              });
          }
        } else if (data.nodeType === 'CONTROLLER') {
          permAction.deleteUiMeta(data.id)
            .then((resp) => {
              this.$message({
                type: 'success',
                message: '删除成功!',
              });
              // 刷新
              this.getTree();
            }).catch((error) => {
              this.$message({
                type: 'warning',
                message: '删除失败!',
              });
              // 刷新
              this.getTree();
            });
        } else {
          // console.log('删除???');
          this.$message({
            type: 'warning',
            message: '删除失败!',
          });
        }
      }).catch((error) => {
        this.$message({
          type: 'info',
          message: '已取消删除',
        });
      });
    },

    submit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 更新操作
          if (this.btnAction.key === 'UPDATE') {
            if (this.temp.nodeType === 'MENU') { // 更新菜单
              permAction.updateMenu(this.temp)
                .then((rep) => {
                  if (rep.data === 1) {
                    this.$message({
                      type: 'success',
                      message: '更新成功！',
                    });
                    this.temp = {
                      id: '',
                      menuName: '',
                      icon: '',
                      url: '',
                      defaultSelectStatus: '0',
                      changeable: '1',
                      menuCode: '',
                      parentCode: '',
                      controlName: '',
                      controlType: 'field',
                      controlId: '',
                    };
                    // 刷新
                    this.getTree();
                    this.refreshBtn();
                  } else {
                    this.$message({
                      type: 'warning',
                      message: '无任何更新！',
                    });
                  }
                })
                .catch((error) => {
                  this.$message.error(error.respMessage);
                });
            } else if (this.temp.nodeType === 'CONTROLLER') { // 更新控件
              permAction.updateUiMeta(
                this.temp.id,
                this.temp.controlId,
                this.temp.controlName,
                this.temp.controlType,
                this.temp.menuCode,
              )
                .then((resp) => {
                  if (resp.data === 1) {
                    this.$message({
                      type: 'success',
                      message: '更新成功！',
                    });
                    this.temp = {
                      id: '',
                      menuName: '',
                      icon: '',
                      url: '',
                      defaultSelectStatus: '0',
                      changeable: '1',
                      menuCode: '',
                      parentCode: '',
                      controlName: '',
                      controlType: 'field',
                      controlId: '',
                    };
                    // 刷新
                    this.getTree();
                    this.refreshBtn();
                  } else {
                    this.$message({
                      type: 'warning',
                      message: '无任何更新！',
                    });
                  }
                }).catch((error) => {
                  this.$message.error(error.respMessage);
                });
            }
          }

          // 增加操作
          if (this.btnAction.key === 'APPEND') {
            if (this.temp.nodeType === 'MENU') {
              permAction.createMenu(this.temp)
                .then((rep) => {
                  this.$message({
                    type: 'success',
                    message: '添加成功！',
                  });
                  // 刷新
                  this.getTree();
                  this.refreshBtn();
                  this.temp = {
                    id: '',
                    menuName: '',
                    icon: '',
                    url: '',
                    defaultSelectStatus: '0',
                    changeable: '1',
                    menuCode: '',
                    parentCode: '',
                    controlName: '',
                    controlType: 'field',
                    controlId: '',
                  };
                })
                .catch((error) => {
                  this.$message.error(error.respMessage);
                  // 刷新
                  this.getTree();
                });
            } else if (this.temp.nodeType === 'CONTROLLER') {
              permAction.createUiMeta(this.temp)
                .then((rep) => {
                  this.$message({
                    type: 'success',
                    message: '添加成功！',
                  });
                  // 刷新
                  this.getTree();
                  this.refreshBtn();
                  this.temp = {
                    id: '',
                    menuName: '',
                    icon: '',
                    url: '',
                    defaultSelectStatus: '0',
                    changeable: '1',
                    menuCode: '',
                    parentCode: '',
                    controlName: '',
                    controlType: 'field',
                    controlId: '',
                  };
                })
                .catch((error) => {
                  this.$message.error(error.respMessage);
                  // 刷新
                  this.getTree();
                });
            } else {
              // console.log('nothing...');
            }
          }

          // 未操作
          if (this.btnAction.key === '') {
            this.$message.warning('请先在左侧树中选择操作');
          }
        }
      });
    },

    // 初始化按钮
    refreshBtn() {
      this.btnAction.name = '确定';
      this.btnAction.key = '';
      this.activeTabName = 'MENU';
      this.$refs.menuForm.resetFields();
    },

    // 拖拽排序
    handleNodeDrop(draggingNode, dropNode, dropType, ev) {
      if (draggingNode && draggingNode.data.nodeType === 'CONTROLLER') {
        this.$message.warning('控件不允许拖拽！');
        this.getTree();
        return;
      }

      this.$confirm('移动菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        let preMenuCode = '';
        let aftMenuCode = '';
        let endParentCode = dropNode.data.parentCode;
        const draggingMenuCode = draggingNode.data.menuCode;
        if (dropType === 'after') {
          preMenuCode = dropNode.data.menuCode;
        } else if (dropType === 'before') {
          aftMenuCode = dropNode.data.menuCode;
        } else if (dropType === 'inner') {
          endParentCode = dropNode.data.menuCode;
        }
        permAction.updateSlr(
          draggingNode.data.id,
          endParentCode,
          draggingMenuCode,
          preMenuCode,
          aftMenuCode,
        )
          .then((resp) => {
            this.$message.success('更新成功！');
            this.getTree();
          }).catch((error) => {
            this.$message.error('对不起，更新排序失败');
            this.getTree();
          });
      }).catch(() => {
        this.$message.info('已取消拖拽');
        this.getTree();
      });
    },
  },
};
</script>

<style scoped>
  .menu-form {
    position: fixed;
    width: 40%;
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
