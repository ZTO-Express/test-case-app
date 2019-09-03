<template>
  <div align="left">
    <div align="right">
      <el-button size="small" type="success" v-permission="'role-page-list/add'" icon="el-icon-plus" @click="create()">
        新增角色
      </el-button>
    </div>
    <br/>
    <el-card class="box-card">
      <el-table
        :data="list"
        border
        style="width: 100%">
        <el-table-column
          label="序号"
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          align="center"
          label="名称">
          <template slot-scope="scope">
            <span>{{scope.row.roleName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="操作员数">
          <template slot-scope="scope">
            <span>{{scope.row.userCount}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="创建时间">
          <template slot-scope="scope">
            <span>{{parsetime(scope.row.createTime)}}</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="状态">
          <template slot-scope="scope">
            <span v-if="scope.row.status === '1'" style="color: #606266">启用</span>
            <span v-if="scope.row.status === '0'" style="color: #909399">停用</span>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="操作"
          fixed="right"
          width="150">
          <template slot-scope="scope"
                    v-if="scope.row.isSystemRole==='0' && scope.row.isMyRole === '0'">
            <el-row>
              <el-col :span="12">
                <el-button type="primary" size="mini" v-permission="'role-page-list/edit'"
                           @click="edit(scope.row)">编辑
                </el-button>
              </el-col>
              <el-col :span="12">
                <el-button type="danger" size="mini" v-permission="'role-page-list/del'"
                           @click="del(scope.row)">删除
                </el-button>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
      <div align="center">
        <el-pagination ref="pagination"
                       @current-change="handleCurrentChange"
                       :current-page.sync="listQuery.page"
                       :page-size="listQuery.size"
                       :total="listQuery.total">
          layout="total, prev, pager, next, jumper"
        </el-pagination>
      </div>
    </el-card>

    <el-dialog :modal-append-to-body="false" class="role-create-dialog" :title="textMap[dialogStatus]"
               :visible.sync="dialogFormVisible" width="800">
      <el-container style="width:100% ; height: 380px">
        <el-main>
          <el-form ref="dataForm" :model="temp" label-width="20%" size="mini" :rules="rules">
            <el-form-item label="角色名称" prop="roleName">
              <el-input clearable v-model="temp.roleName"></el-input>
            </el-form-item>
            <el-form-item label="角色状态" prop="status" align="left">
              <el-switch v-model="temp.status" active-text="启用" active-value="1" inactive-value="0"></el-switch>
            </el-form-item>
            <el-form-item label="角色权限" align="left">
              <el-tree :check-on-click-node="true" :expand-on-click-node="false" ref="tree" :data="menuTree"
                       show-checkbox check-strictly node-key="nodeid" default-expand-all
                       :default-checked-keys="checkedIds" :props="defaultProps" @check-change="checkChange"></el-tree>
            </el-form-item>
          </el-form>
        </el-main>
        <el-footer>
          <div class="control-bar">
            <el-button size="mini" type="primary" @click="save()">确定</el-button>
            <el-button size="mini" @click="dialogFormVisible = false">取消</el-button>
          </div>
        </el-footer>
      </el-container>
    </el-dialog>
  </div>
</template>
<script>
  import moment from 'moment';
  import permAction from '../../../action/permAction';
  import authAction from '../../../action/authAction';

  export default {
    data() {
      return {
        rules: {
          roleName: [
            {required: true, message: '角色名称不能为空', trigger: 'blur'},
            {
              min: 2, max: 16, message: '角色名称格式不合法(2-16个字符)', trigger: 'blur',
            },
          ],
        },
        defaultProps: {
          children: 'child',
          label: 'menuName',
        },
        textMap: {
          update: '编辑角色',
          create: '新增角色',
        },
        statusMap: {
          1: '启用',
          0: '停用',
        },
        temp: {
          appId: this.$appConfig.appId,
          roleCode: '',
          userCount: 0,
          roleName: '',
          status: '',
          createTime: 0,
          isSystemRole: '',
        },
        relatation: {
          appId: this.$appConfig.appId,
          roleCode: '',
          menuCodes: [],
        },
        pmsRequest: [],
        listQuery: {
          page: 1,
          size: 10,
          total: 0,
          sort: '',
        },
        list: [],
        menuTree: [],
        checkedIds: [],
        dialogFormVisible: false,
        dialogStatus: null,
      };
    },
    created() {
      this.getList();
    },

    components: {},
    computed: {},
    methods: {
      // 当前选中节点变化时触发的事件
      checkChange(data, checked, childChecked) {
        // console.log(data.nodeid + checked + ', child:' + childChecked);
        if (!data.child) {
          return;
        }
        // 勾选
        if (checked) {
          data.child.forEach((item) => {
            const checkeds = this.getCheckedKeys();
            checkeds.push(item.nodeid);
            this.setCheckedKeys(checkeds);
          });
        }
        // 取消勾选
        else {
          data.child.forEach((item) => {
            const checkeds = this.getCheckedKeys();
            const index = checkeds.indexOf(item.nodeid);
            if (index > -1) {
              checkeds.splice(index, 1);
              this.setCheckedKeys(checkeds);
            }
          });
        }
      },

      getCheckedKeys() {
        return this.$refs.tree.getCheckedKeys();
      },

      setCheckedKeys(keys) {
        console.log("keys = " + keys.size)
        this.$refs.tree.setCheckedKeys(keys);
      },

      getTree() {
        const tempMenuTree = [];
        let tempUiMetaTree;
        if (this.$appData.userInfo.roles[0].systemRole === '1') {
          permAction.getMenuTree().then((rep) => {
            Object.assign(tempMenuTree, rep.data);
            // 去掉logo节点
            for (let i = 0; i < tempMenuTree.length; i += 1) {
              if (tempMenuTree[i].menuName === this.$appConfig.logoName) {
                tempMenuTree.splice(i, 1);
              }
            }
            permAction.getUiMetaTree().then((rep2) => {
              tempUiMetaTree = rep2.data;
              tempMenuTree.forEach((menu) => {
                this.traverseTempMenuTree(
                  menu,
                  tempUiMetaTree,
                  [],
                );
              });
              this.menuTree = tempMenuTree;
            }).catch((error1) => {
              this.$message({
                type: 'error',
                message: error1.respMessage,
              });
            });
          }).catch((error) => {
            this.$message({
              type: 'error',
              message: error.respMessage,
            });
          });
        } else {
          permAction.getMenuTreeByRole(this.$appData.userInfo.roles[0].roleCode).then((rep) => {
            Object.assign(tempMenuTree, rep.data);
            // 去掉logo节点
            for (let i = 0; i < tempMenuTree.length; i += 1) {
              if (tempMenuTree[i].menuName === this.$appConfig.logoName) {
                tempMenuTree.splice(i, 1);
              }
            }
            permAction.getUiTreeByRole(this.$appData.userInfo.roles[0].roleCode).then((rep3) => {
              tempUiMetaTree = rep3.data;
              tempMenuTree.forEach((menu) => {
                this.traverseTempMenuTree(
                  menu,
                  tempUiMetaTree,
                  [],
                );
              });
              this.menuTree = tempMenuTree;
            }).catch((error1) => {
              this.$message({
                type: 'error',
                message: error1.respMessage,
              });
            });
          }).catch((error) => {
            this.$message({
              type: 'error',
              message: error.respMessage,
            });
          });
        }
      },
      traverseTempMenuTree(menu, uiMetaTree, fathers) {
        menu.fathers = fathers;
        const [...newFathers] = fathers;
        newFathers.push(menu.menuCode);
        menu.nodeid = 'menu' + menu.id;

        // 是否可变更
        if (menu.changeable === 0) {
          menu.disabled = true;
        }

        // 初始化默认勾选
        if (menu.defaultSelectStatus === 1 && this.dialogStatus === 'create') {
          this.checkedIds.push('menu' + menu.id);
        }

        if (this.hasChild(menu)) {
          menu.child.forEach((menuInfo) => {
            this.traverseTempMenuTree(menuInfo, uiMetaTree, newFathers);
          });
        }
        this.traverseMenuTreeAction(menu, uiMetaTree, newFathers);
      },
      hasChild(item) {
        return item.child && item.child.length > 0;
      },
      traverseMenuTreeAction(menu, uiMetaTree, fathers) {
        const uiMetaList = uiMetaTree[menu.menuCode];
        if (uiMetaList) {
          uiMetaList.forEach((uiMeta) => {
            uiMeta.fathers = fathers;
            uiMeta.menuName = uiMeta.controlName;
            menu.child.push(uiMeta);
            uiMeta.nodeid = 'meta' + uiMeta.id;
            if (uiMeta.controlType === 'field') {
              uiMeta.child = [
                {
                  appId: this.$appConfig.appId,
                  isPerm: true,
                  id: uiMeta.id,
                  nodeid: 'meta' + uiMeta.id + 'edit',
                  fieldPermissionType: '0',
                  menuName: '可编缉',
                  fathers,
                },
              ];
            }
          });
        }
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      compare(val1, val2) {
        return val2.createTime - val1.createTime;
      },
      sortList() {
        this.list.sort(this.compare);
      },
      getList() {
        permAction.findRoles(this.$appData.userInfo.userId).then((rep) => {
          this.list = rep.data;
          this.sortList();
          this.listQuery.total = this.list.length;
          this.list = this.list.slice(
            this.listQuery.size * (this.listQuery.page - 1),
            this.listQuery.size * this.listQuery.page,
          );
        }).catch((error) => {
          this.$message({
            type: 'error',
            message: error.respMessage,
          });
        });
      },

      create() {
        this.checkedIds = [];
        this.temp = {
          roleCode: '',
          appId: this.$appConfig.appId,
          userCount: 0,
          roleName: '',
          status: '1',
          createTime: 0,
          isSystemRole: '',
        };
        this.dialogStatus = 'create';
        this.$nextTick(() => {
          this.$refs.dataForm.clearValidate();
        });
        this.getTree();
        this.dialogFormVisible = true;
      },
      getSelectMenus(menu, checkedMenuCode) {
        if (this.hasChild(menu)) {
          menu.child.forEach((subMenu) => {
            this.getSelectMenus(subMenu, checkedMenuCode);
          });
        }
        this.checkedIds.push('menu' + menu.id);
        checkedMenuCode.push(menu.menuCode);
      },
      edit(row) {
        this.checkedIds = [];
        const checkedMenuCode = [];
        this.dialogStatus = 'update';
        Object.assign(this.temp, row);
        permAction.getMenuTreeByRole(row.roleCode).then((rsp) => {
          rsp.data.forEach((menu) => {
            this.getSelectMenus(menu, checkedMenuCode);
          });
          permAction.getPmsTreeByRoleCode(row.roleCode).then((rsp1) => {
            const metas = rsp1.data;
            const keys = Object.getOwnPropertyNames(metas);
            // 过滤父menu不存在子meta存在情况
            keys.forEach((key) => {
              let isExist = false;
              checkedMenuCode.forEach((menuCode) => {
                if (metas[key].menuCode === menuCode) {
                  isExist = true;
                }
              });
              if (isExist) {
                if (metas[key].permType === 'edit') {
                  this.checkedIds.push('meta' + metas[key].uiMetaId + 'edit');
                }
                this.checkedIds.push('meta' + metas[key].uiMetaId);
              }
            });

            this.getTree();
          }).catch((error1) => {
            this.$message({
              type: 'error',
              message: error1.respMessage,
            });
          });
        }).catch((error) => {
          this.$message({
            type: 'error',
            message: error.respMessage,
          });
        });
        this.dialogFormVisible = true;
      },

      getReq(node) {
        let req;
        let isExist = false;
        for (let index = this.pmsRequest.length; index > 0; index -= 1) {
          if (this.pmsRequest[index - 1].uiMetaId === node.id) {
            isExist = true;
            if (node.isPerm) {
              this.pmsRequest[index - 1].fieldPermissionType = '0';
            }
          }
        }
        if (!isExist) {
          req = {
            appId: this.$appConfig.appId,
            roleCode: this.relatation.roleCode,
            uiMetaId: node.id,
            operationShowType: '0',
            fieldPermissionType: node.fieldPermissionType ? node.fieldPermissionType : '2',
          };
        }
        return req;
      },
      getRequest(checkedNodes) {
        let req;
        for (let index = checkedNodes.length; index > 0; index -= 1) {
          req = null;
          const node = checkedNodes[index - 1];
          this.relatation.menuCodes = this.relatation.menuCodes.concat(node.fathers);
          console.log("controlType：" + node.controlType)
          if (node.controlType === 'field') {
            req = this.getReq(node);
            checkedNodes.splice(index - 1, 1);
          } else if (node.controlType === 'oper') {
            req = {
              appId: this.$appConfig.appId,
              roleCode: this.relatation.roleCode,
              uiMetaId: node.id,
              operationShowType: '0',
              fieldPermissionType: '0',
            };
            checkedNodes.splice(index - 1, 1);
          } else if (node.isPerm) {
            req = this.getReq(node);
            checkedNodes.splice(index - 1, 1);
          } else {
            console.log(node.menuCode)
            this.relatation.menuCodes.push(node.menuCode);
          }

          if (req) {
            this.pmsRequest.push(req);
          }
        }
      },
      save() {
        this.$refs.dataForm.validate((valid) => {
          this.pmsRequest = [];
          this.relatation.menuCodes = [];
          console.log("valid = " + valid)
          if (valid) {
            this.temp.appId = this.$appConfig.appId;
            permAction.updateRole(this.temp, this.dialogStatus, this.$appData.userInfo.userId).then((response) => {
              if (this.dialogStatus === 'update') {
                this.relatation.roleCode = response.data.roleCode;
              } else {
                this.relatation.roleCode = response.data;
              }
              const checkedNodes = this.$refs.tree.getCheckedNodes();
              console.log("checkedNodes = " + checkedNodes)
              this.getRequest(checkedNodes);
              const menuCodeSet = new Set([...this.relatation.menuCodes]);
              console.log("menuCodeSet = " + menuCodeSet)
              this.relatation.menuCodes = Array.from(menuCodeSet);
              permAction.delUiMetaByRoleCode(this.relatation.roleCode).then((rsp) => {
                permAction.batchBandUiMeta(this.pmsRequest).catch((error1) => {
                  this.$message({
                    type: 'error',
                    message: error1.respMessage,
                  });
                });
              }).catch((error) => {
                this.$message({
                  type: 'error',
                  message: error.respMessage,
                });
              });
              permAction.bandMenu(this.relatation).catch((error) => {
                this.$message({
                  type: 'error',
                  message: error.respMessage,
                });
              });
              this.dialogFormVisible = false;
              this.getList();
              this.$message({
                type: 'success',
                message: this.textMap[this.dialogStatus] + '成功',
              });
            }).catch((error3) => {
              this.$message({
                type: 'error',
                message: error3.respMessage,
              });
              return false;
            });
          }
          return false;
        });
      },
      del(row) {
        this.$confirm('确定要删除该角色吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          if (row.userCount > 0) {
            this.$message({
              type: 'error',
              message: '该角色下操作员数量不为0',
            });
            return;
          }
          permAction.deleteRole(row).then((response) => {
            this.$message({
              type: 'success',
              message: '删除成功!',
            });
            this.getList();
          }).catch((error) => {
            this.$message({
              type: 'error',
              message: error.respMessage,
            });
          });
        }).catch(() => false);
      },
      parsetime(time) {
        return moment(time).format('YYYY.MM.DD HH:mm:ss');
      },
    },
  };
</script>
<style lang="stylus">
  .control-bar {
    padding-top 20px
    padding-bottom 50px
  }
</style>
