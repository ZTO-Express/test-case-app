<template>
    <div align="left">
        <div align="right">
            <el-button type="primary" size="small" v-permission="'user-page-list/add'" icon="el-icon-plus" @click="create()">新增操作员</el-button>
        </div>
        <br/>
        <el-card class="box-card">
            <div align="left">
                <el-select
                        size="mini"
                        v-model="selectRole"
                        placeholder="按角色筛选"
                        @change="handleCurrentChange(1)"
                        clearable>
                    <el-option
                            v-for="item in opts"
                            :label="item.roleName"
                            :value="item.roleCode"
                            :key="item.roleCode"
                    >
                    </el-option>
                </el-select>
            </div>
            <br/>
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
                        label="登录名">
                    <template slot-scope="scope">
                        <span>{{scope.row.userName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="角色">
                    <template slot-scope="scope">
                        <span>{{scope.row.roles[0].roleName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="姓名">
                    <template slot-scope="scope">
                        <span>{{scope.row.nickName}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="手机号">
                    <template slot-scope="scope">
                        <span>{{scope.row.mobile}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="邮箱">
                    <template slot-scope="scope">
                        <span>{{scope.row.email}}</span>
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
                        <span v-if="scope.row.status === '2'" style="color: #606266">{{statusMap[scope.row.status]}}</span>
                        <span v-if="scope.row.status === '3'" style="color: #909399">{{statusMap[scope.row.status]}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="操作"
                        fixed="right"
                        width="150">
                    <template slot-scope="scope"
                              v-if="scope.row.roles[0].isSystemRole!=='1'&&$appData.userInfo.roles[0].roleCode!==scope.row.roles[0].roleCode">
                        <el-row>
                            <el-col :span="12">
                                <el-button type="primary" size="mini" v-permission="'user-page-list/edit'"
                                           @click="edit(scope.row)">编辑
                                </el-button>
                            </el-col>
                            <el-col :span="12">
                                <el-button type="danger" size="mini" v-permission="'user-page-list/del'"
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
        <el-dialog :modal-append-to-body="false" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="40%" @close="closeDialog">
            <el-form
                    ref="dataForm"
                    :model="temp"
                    label-width="20%"
                    :rules="rules"
            >
                <el-form-item label="指定角色" prop="roleCode" align="left">
                    <el-select v-model="temp.roleCode">
                        <el-option
                                v-for="item in opts2"
                                :label="item.roleName"
                                :value="item.roleCode"
                                :key="item.roleCode"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="登录名" prop="userName">
                    <el-input clearable v-model="temp.userName" :disabled="dialogStatus === 'update'" maxlength='50'></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="userPwd">
                    <el-input clearable v-model="temp.userPwd" :placeholder="placeholder" maxlength='50'></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="nickName">
                    <el-input clearable v-model="temp.nickName" maxlength='50'></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="mobile">
                    <el-input clearable v-model="temp.mobile" maxlength='11'></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input clearable v-model="temp.email"  maxlength='50'></el-input>
                </el-form-item>
                <el-form-item label="账号状态" prop="userStatus" align="left">
                    <el-switch
                            v-model="temp.userStatus"
                            active-text="启用"
                            active-value="2"
                            inactive-value="3"
                    >
                    </el-switch>
                </el-form-item>

                <el-form-item align="left">
                    <el-button type="primary" @click="save()">确定</el-button>
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
import moment from 'moment'
import permAction from '../../../action/permAction'

export default {
  data() {
    return {
      createPwdExp: [
        {
          required: true, min: 6, max: 50, message: '密码长度必须6-50位', trigger: 'blur'
        },
        { pattern: /(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+/, message: '密码必须包含字母与数字', trigger: 'blur' },
        { pattern: /.*[^\w\d]+.*/, message: '密码必须包含其他字符', trigger: 'blur' }
      ],
      updatePwdExp: [
        { pattern: /(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+/, message: '密码必须包含字母与数字', trigger: 'blur' },
        { pattern: /.*[^\w\d]+.*/, message: '密码必须包含其他字符', trigger: 'blur' }
      ],
      rules: {
        roleCode: [
          { required: true, message: '请指定角色', trigger: 'blur' }
        ],
        userName: [
          { required: true, message: '登录名不能为空', trigger: 'blur' },
          {
            required: true, min: 4, max: 16, message: '长度必须4-16', trigger: 'blur'
          },
          { pattern: /^[a-zA-Z0-9_-]{4,16}$/, message: '用户名不合法', trigger: 'blur' },
          { pattern: /^.*[^\d].*$/, message: '不能是纯数字', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '姓名不能为空', trigger: 'blur' },
          {
            min: 2, max: 10, message: '姓名长度必须2-10位', trigger: 'blur'
          }
        ],
        userPwd: null,
        mobile: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          { pattern: /^1[3|4|5|7|8|9][0-9]\d{8}$/, message: '手机号不合法', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          {
            pattern: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/,
            message: 'email不合法',
            trigger: 'blur'
          }

        ]
      },
      textMap: {
        update: '编辑操作员',
        create: '新增操作员'
      },
      statusMap: {
        2: '启用',
        3: '停用'
      },
      temp: {
        appId: this.$appConfig.appId,
        userId: '',
        userName: '',
        roleCode: '',
        roles: [
          {
            roleCode: '',
            roleName: '',
            isSystemRole: ''
          }
        ],
        nickName: '',
        mobile: '',
        userPwd: '',
        email: '',
        createTime: null,
        status: '',
        userStatus: ''
      },
      listQuery: {
        page: 1,
        size: 10,
        total: 0,
        sort: ''
      },
      list: [],
      oriList: [],
      placeholder: '',
      opts: [],
      opts2: [],
      dialogFormVisible: false,
      dialogStatus: null,
      selectRole: ''
    }
  },
  created() {
    this.getRoleList()
  },
  components: {},
  computed: {},
  methods: {
    closeDialog() {
      this.$refs.dataForm.resetFields()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getList()
    },
    compare(val1, val2) {
      return val2.createTime - val1.createTime
    },
    sortList() {
      this.list.sort(this.compare)
    },
    getRoleList() {
      permAction.findRoles(this.$appData.userInfo.userId).then((response) => {
        const [...temp] = response.data
        this.opts = response.data
        this.opts2 = temp
        // todo use array filter
        // 过滤停用角色
        for (let i = 0; i < this.opts2.length; i += 1) {
          if (this.opts2[i].status === '0' || this.opts2[i].roleCode === this.$appData.userInfo.roles[0].roleCode) {
            this.opts2.splice(i, 1)
          }
        }
        this.getList()
      }).catch((error) => {
        this.$message({
          type: 'error',
          message: error.respMessage
        })
      })
    },
    getList() {
      permAction.findUsers().then((rep) => {
        this.list = []
        this.oriList = rep.data
        if (this.selectRole === '') {
          this.opts.forEach((opt) => {
            this.oriList.forEach((user) => {
              if (user.roles[0].roleCode === opt.roleCode) {
                this.list.unshift(user)
              }
            })
          })
        } else {
          this.oriList.forEach((user) => {
            if (user.roles[0].roleCode === this.selectRole) {
              this.list.unshift(user)
            }
          })
        }
        this.sortList()
        this.listQuery.total = this.list.length
        this.list = this.list.slice(
          this.listQuery.size * (this.listQuery.page - 1),
          this.listQuery.size * this.listQuery.page,
        )
      }).catch((error) => {
        this.$message({
          type: 'error',
          message: error.respMessage
        })
      })
    },
    create() {
      this.temp = {
        appId: this.$appConfig.appId,
        userId: '',
        userName: '',
        roleCode: '',
        roles: [
          {
            roleCode: '',
            roleName: '',
            isSystemRole: ''
          }
        ],
        nickName: '',
        mobile: '',
        userPwd: '',
        email: '',
        createTime: null,
        status: '2',
        userStatus: '2'
      }
      this.placeholder = '不少于6位的字母、数字和其他字符的组合'
      this.dialogStatus = 'create'
      this.rules.userPwd = this.createPwdExp
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })

      this.getRoleList()
      this.dialogFormVisible = true
    },
    save() {
      debugger
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          permAction.updateUser(this.temp, this.dialogStatus).then((response) => {
            this.dialogFormVisible = false
            this.$refs.dataForm.resetFields()
            this.$message({
              type: 'success',
              message: this.textMap[this.dialogStatus] + '成功'
            })
            this.getList()
            return true
          }).catch((error) => {
            this.$message({
              type: 'error',
              message: error.respMessage
            })
            return false
          })
        }
        return false
      })
    },
    edit(row) {
      this.placeholder = '修改密码输入新密码即可'
      this.dialogStatus = 'update'
      this.rules.userPwd = this.updatePwdExp
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
      this.list.forEach((user) => {
        if (user.userId === row.userId) {
          Object.assign(this.temp, user)
        }
      })
      this.temp.roleCode = this.temp.roles[0].roleCode
      this.temp.userStatus = this.temp.status
      this.getRoleList()
      this.dialogFormVisible = true
    },
    del(row) {
      this.$confirm('确定要删除该操作员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        permAction.deleteUser(row).then((response) => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.getList()
          return true
        }).catch((error) => {
          this.$message({
            type: 'error',
            message: error.respMessage
          })
          return false
        })
      }).catch(() => false)
    },
    parsetime(time) {
      return moment(time).format('YYYY.MM.DD HH:mm:ss')
    }
  }
}
</script>
