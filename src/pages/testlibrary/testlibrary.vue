<template>
  <div class="testlibrary-index"
       v-loading="loading"
       element-loading-text="拼命导出中"
       element-loading-spinner="el-icon-loading">

    <el-row :gutter="0"
            class="h100">
      <el-col :xl="5"
              :lg="6"
              class="h100">
        <div class="flex h100"
             id="treeinfos"
             style="width: 260px;min-width: 160px;overflow-x: auto">
          <div class="uscMsg-tree-index flex-1">
            <el-input placeholder="输入关键字进行过滤"
                      v-model="filterText"
                      clearable
                      style="margin-bottom: 8px;width: 260px" />
            <el-tree accordion
                     style="width: 600px"
                     class="filter-tree"
                     :data="treeData"
                     :props="defaultProps"
                     node-key="id"
                     :default-expanded-keys="openkeys"
                     :filter-node-method="filterNode"
                     highlight-current
                     ref="departmentTree">
              <span class="custom-tree-node"
                    slot-scope="{ node, data }">
                <div @click="handleNodeClick(node)">
                  <span>{{ node.label }}</span>
                  <el-dropdown trigger="click"
                               @command="operateModule"
                               v-if="!$route.query.id">
                    <span class="el-dropdown-link">
                      <i class="el-icon-setting"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item icon="el-icon-edit"
                                        command="edit">重命名</el-dropdown-item>
                      <el-dropdown-item icon="el-icon-circle-plus-outline"
                                        command="add">新建子模块</el-dropdown-item>
                      <el-dropdown-item icon="el-icon-delete"
                                        command="del">删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
              </span>
            </el-tree>
          </div>
        </div>
      </el-col>
      <el-col :xl="19"
              :lg="18"
              class="h100">
        <div class="right-table">
          <el-card class="box-card h100"
                   shadow="hover">
            <div slot="header"
                 class="clearfix header">
            </div>
            <div slot="header"
                 inline
                 class="clearfix header">
              <div>
                <div v-if="$route.query.id">
                  <span>状态  </span>
                  <el-dropdown @command="statusHandleCommand">
                    <el-button :type="status === 0 ? 'primary' : status === 1 ? 'warning' : 'success'" size="mini"
                               plain>
                      <span>{{status === 0 ? '未开始' : status === 1 ? '进行中' : '已完成'}}</span><i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu style="transform-origin: center top; z-index: 2002; display: none;"
                                      slot="dropdown">
                      <span style="padding: 10px;">更改计划状态:</span>
                      <el-dropdown-item style="margin-top: 15px"
                                        v-for="(item, index) in statusList"
                                        :key="index"
                                        :label="item.name"
                                        :value="item.status"
                                        :command="item">
                        <el-tag :type="item.tag" size="small">{{item.name}}</el-tag>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                  <br />
                  <br />
                </div>
                <div>
                  <el-breadcrumb separator-class="el-icon-arrow-right">
                    <el-breadcrumb-item>所有用例</el-breadcrumb-item>
                    <el-breadcrumb-item v-for="(item, index) in moduleList" :label="item.name" :value="item.level" :key="index" >{{item.name}}</el-breadcrumb-item>
                  </el-breadcrumb>
                </div>
                <br />
                <br />
                <div v-if="$route.query.id">
                  <span style="display: inline-block; margin-left：24px;">
                    <span>通过率：</span>
                    <span>{{search.passRate}}%</span>
                  </span>
                  <el-divider direction="vertical"></el-divider>
                  <span>
                    <span> 已测：</span>
                    <span>{{search.executeNum}}/{{search.totalNum}}</span>
                  </span>
                </div>
              </div>
              <div class="add">
                <div v-if="$route.query.id">
                  <el-button type="primary" plain size="mini" @click="handleOperate('updateUser')"><i class="el-icon-user"></i> 更改执行人</el-button>
                  <el-button type="primary" plain size="mini" @click="handleOperate('updateRes')"><i class="el-icon-finished"></i> 更改执行结果</el-button>
                  <el-button type="primary" plain size="mini" @click="handleOperate('withCase')"><i class="el-icon-connection"></i> 关联用例</el-button>
                  <el-button type="danger" size="mini" @click="handleOperate('delCase')"><i class="el-icon-document-remove"></i> 移除用例</el-button>
                </div>
                <div v-else>
                  <el-dropdown @command="handleCommand"
                               style="margin-right: 10px">
                    <el-button type="primary" size="small" style="height: 32px" plain>
                      <i class="el-icon-upload2"></i> 导入用例<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="first">导入 表格文件(.xlsx)</el-dropdown-item>
                      <el-dropdown-item command="second">导入 思维导图(.xmind)</el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                  <!--<el-button type="primary" plain @click="handleOperate('exportCase')"><i class="el-icon-download"></i> 导出用例</el-button>-->
                  <el-button type="primary" size="small" plain @click="handleOperate('move')"><i class="el-icon-position"></i> 移动用例</el-button>
                  <el-button type="danger" size="small"
                             @click="handleOperate('del')"><i class="el-icon-delete"></i> 删除用例</el-button>
                  <el-button type="primary" size="small"
                             @click="handleOperate('add')"><i class="el-icon-plus"></i> 新建用例</el-button>
                </div>
              </div>
            </div>
            <el-form ref="search"
                     inline
                     :model="search"
                     label-width="80px">
              <el-form-item label="用例名称"
                            prop="caseName">
                <el-input v-model="search.name"
                          clearable />
              </el-form-item>
              <el-form-item prop="type"
                            label="用例类型">
                <el-select placeholder="请选择"
                           v-model="search.type"
                           clearable
                           filterable
                           style="width: 100px">
                  <el-option :key="index"
                             :label="item.name"
                             :value="item.id"
                             v-for="(item,index) in typeList"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="priority"
                            label="优先级">
                <el-select placeholder="请选择"
                           v-model="search.priority"
                           clearable
                           filterable
                           style="width: 100px">
                  <el-option :key="index"
                             :label="item.label"
                             :value="item.value"
                             v-for="(item,index) in priorityList"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="维护人"
                            prop="user">
                <el-input v-model="search.user"
                          clearable />
              </el-form-item>
              <el-form-item prop="gid"
                            label="显示">
                <el-select placeholder="请选择"
                           v-model="search.showSubModule"
                           filterable>
                  <el-option :key="index"
                             :label="item.name"
                             :value="item.id"
                             v-for="(item,index) in Options" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" size="small"
                           @click="searchList">查询</el-button>
                <el-button size="small" @click="reset">重置</el-button>
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
                               width="50"
                               show-overflow-tooltip
                               v-if="false" />
              <el-table-column prop="name"
                               fixed
                               label="用例名称"
                               min-width="300"
                               show-overflow-tooltip />
              <el-table-column prop="updateUser"
                               label="维护人"
                               width="80"
                               show-overflow-tooltip />
              <el-table-column prop="priority"
                               width="60"
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
                  <el-tag type="info" disable-transitions>{{getCaseType(row.type,typeList)}}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="tag"
                               label="用例标签"
                               width="150">
                <template slot-scope="{ row }" v-if = "row.tag">
                  <el-tag type="info"
                  v-for="(tag,index) in row.tag.split(',')"
                  :key="index"
                  disable-transitions>{{getCaseTag(tag,tagList)}}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status"
                               label="用例状态"
                               width="80"
                               show-overflow-tooltip>
                <template slot-scope="{ row }">
                  <el-tag :type="row.status === 0 ? 'danger' : row.status === 1 ? 'success' : 'warning'"
                          disable-transitions>{{ row.status === 0 ? '删除' : row.status === 1 ? '正常' : '停用' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="executeUserName"
                               width="80"
                               label="执行人"
                               show-overflow-tooltip
                               v-if="$route.query.id" />
              <el-table-column prop="result"
                               width="80"
                               label="执行结果"
                               show-overflow-tooltip
                               v-if="$route.query.id">
                <template slot-scope="{ row }">
                  <el-tag
                    :type="row.result === 0 ? 'primary' : row.result === 1 ? 'success'
                    : row.result === 2 ? 'danger' : row.result === 3 ? 'warning'
                    : 'info'"
                    disable-transitions>{{row.result === 0 ? '未执行' : row.result === 1 ? '通过'
                    : row.result === 2 ? '失败' : row.result === 3 ? '阻塞'
                    : '跳过'}}</el-tag>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="操作" width="120">
                <template slot-scope="{ row }">
                  <el-button @click="handleOperate('editPlan', row)" type="primary" v-if="$route.query.id">编辑</el-button>
                  <el-button @click="handleOperate('edit', row)" type="primary" v-else style="margin-left:5px">编辑</el-button>
                  <el-button @click="handleOperate('copyEditCase', row)" type="primary" style="margin-left:5px" v-if="!$route.query.id">复制</el-button>
                  <!--<el-button @click="handleOperate('edit', row)" type="primary" v-else >删除</el-button>-->
                </template>
              </el-table-column>
            </el-table>
            <div class="footer-page">
              <complete-pagination :total="page.totalDataCount"
                                   :currentPage="page.pageNumber"
                                   :pageSize="page.pageSize"
                                   @updatePage="updatePage" />
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
    <modal :visible.sync="modal.visiable"
           :title="modal.title"
           width="60%"
           confirmTitle="保存"
           :showCancle="false"
           :showConfirm="false"
           :showReset="false">
      <template>
        <operate ref="operate"
                 @update="getList" />
      </template>
    </modal>
    <!-- 编辑计划详情 -->
    <modal :visible.sync="modalPlan.visiable"
           :title="modalPlan.title"
           width="60%"
           confirmTitle="保存"
           :showCancle="false"
           :showConfirm="false"
           :showReset="false">
      <template>
        <edit-plan ref="editPlan"
                   @update="getList" />
      </template>
    </modal>
    <!-- 复制用例 -->
    <modal :visible.sync="modalCopyTestCase.visiable"
           :title="modalCopyTestCase.title"
           width="80%"
           confirmTitle="保存"
           :showCancle="false"
           :showConfirm="false"
           :showReset="false">
      <template>
        <copy-edit-case ref="copyEditCase"
                        @update="getList" />
      </template>
    </modal>
    <!-- 关联用例 -->
    <modal style="height: 100%"
           :visible.sync="modalCase.visiable"
           :title="modalCase.title"
           width="80%"
           confirmTitle="保存"
           :showCancle="false"
           :showConfirm="false"
           :showReset="false">
      <template>
        <assocaateCase ref="assocaateCase"
                       @update="getListForWithCase" />
      </template>
    </modal>
    <!-- 修改模块树 -->
    <modal :visible.sync="modalModule.visiable"
           :title="modalModule.title"
           width="40%"
           confirmTitle="保存"
           :showCancle="true"
           :showConfirm="true"
           :showReset="false"
           @confirm="saveModule">
      <template>
        <el-form label-width="80px"
                 style="margin-top: 24px">
          <el-form-item label="模块名称">
            <el-input v-model="moduleInfo.name" />
          </el-form-item>
        </el-form>
      </template>
    </modal>
    <!-- 更改执行人 -->
    <modal :visible.sync="modalUpdateCase.visiable"
           :title="modalUpdateCase.title"
           width="20%"
           confirmTitle="保存"
           :showCancle="true"
           :showConfirm="true"
           :showReset="false"
           @confirm="saveCase">
      <template>
        <el-form label-width="80px"
                 style="width: 100%;margin-top: 24px">
          <el-select style="width: 100%;"
                     placeholder="请选择执行人"
                     v-model="modalUpdateCase.value"
                     size="small"
                     filterable>
            <el-option :key="index"
                       :label="item.displayName"
                       :value="item.id"
                       v-for="(item,index) in userList"></el-option>
          </el-select>
        </el-form>
      </template>
    </modal>
    <!--更改执行结果-->
    <modal :visible.sync="modalUpdateRes.visiable"
           :title="modalUpdateRes.title"
           width="30%"
           confirmTitle="保存"
           :showCancle="true"
           :showConfirm="true"
           :showReset="false"
           @confirm="saveCase">
      <template>
        <el-form ref="search"
                 label-width="90px">
          <br>
          <br>
          <el-form-item>
            <el-button plain
                       size=""
                       v-for="(item, index) in resultList"
                       :key="index"
                       :label="item.value"
                       :type="item.tag"
                       :value="item.value"
                       @click.prevent="setCaseResult(item.value)">{{item.label}}
            </el-button>
          </el-form-item>
        </el-form>
      </template>
    </modal>
<!--上传用例文件-->
    <modal
      style="height: 100%"
      :visible.sync="modalImport.visiable"
      :title="modalImport.title"
      width="50%"
      confirmTitle="保存"
      :showCancle="false"
      :showConfirm="false"
      :showReset="false"
    >
      <template>
        <importFile ref="importFile" @update="getListAfterImport" />
      </template>
    </modal>
    <!--移动用例-->
    <modal
      style="height: 100%"
      :visible.sync="modalMoveCase.visiable"
      :title="modalMoveCase.title"
      width="50%"
      confirmTitle="保存"
      :showCancle="false"
      :showConfirm="false"
      :showReset="false"
    >
      <template>
        <moveCase ref="moveCase" @update="getListForWithCase" />
      </template>
    </modal>
  </div>
</template>
<script>
// import { util } from '@/utils/util'
import operate from './operate';
import importFile from './import';
import assocaateCase from './associateCase';
import moveCase from './moveCase';
import editPlan from './editPlan';
import copyEditCase from './copyEditCase';
// import { mapGetters } from 'vuex';
import axios from 'axios';
import Sortable from 'sortablejs';

export default {
  data() {
    return {
      search: {
        id: '',
        name: '',
        user: '',
        type: '',
        priority: '',
        showSubModule: 1,
        passRate: 0.00,
        executeNum: 0,
        totalNum: 0,
      },
      caseList: [],
      status: 0,
      statusStr: '未开始',
      statusTag: 'primary',
      result: 0,
      page: {
        totalDataCount: 0,
        pageNumber: 1,
        pageSize: 10,
      },
      multipleSelection: [],
      rules: {}, // form 校验
      filterText: '', // 输入查询的节点
      openkeys: [], // 默认打开的id
      treeData: [
        {
          id: 1,
          name: '科技与信息中心',
          children: [],
        },
      ],
      userList: [],
      defaultProps: {
        children: 'children',
        label: 'name',
      },
      Options: [
        { name: '显示子模块用例', id: 1 },
        { name: '隐藏子模块用例', id: 0 },
      ],
      modal: {
        visiable: false,
        title: '新建',
      },
      modalPlan: {
        visiable: false,
        title: '新建',
      },
      modalCopyTestCase: {
        visiable: false,
        title: '新建',
      },
      modalModule: {
        visiable: false,
        title: '新建用例',
        type: 'add',
      },
      modalCase: {
        visiable: false,
        title: '关联用例',
      },
      modalMoveCase: {
        visiable: false,
        title: '移动用例',
      },
      moduleInfo: {
        name: '',
      },
      modalUpdateCase: {
        visiable: false,
        title: '更改执行人',
        value: '',
      },
      modalUpdateStatus: {
        visiable: false,
        title: '更改状态',
        value: '',
      },
      modalUpdateRes: {
        visiable: false,
        title: '更改执行结果',
        value: '',
      },
      modalImport: {
        visiable: false,
        title: '上传用例文件',
      },
      caseListPara: {
        pageNo: 1,
        pageSize: 10,
        moduleId: '',
        showSubModule: 0,
        user: '',
        type: '',
        priority: '',
        name: '',
      },
      addedNode: [],
      statusList: [
        {
          name: '未开始', status: 0, tag: 'primary', command: '未开始',
        },
        {
          name: '进行中', status: 1, tag: 'warning', command: '进行中',
        },
        {
          name: '已完成', status: 2, tag: 'success', command: '已完成',
        },
      ],
      resultList: [
        {
          label: '未执行', value: 0, tag: 'primary',
        },
        {
          label: '通过', value: 1, tag: 'success',
        },
        {
          label: '失败', value: 2, tag: 'danger',
        },
        {
          label: '阻塞', value: 3, tag: 'warning',
        },
        {
          label: '跳过', value: 4, tag: 'info',
        },
      ],
      loading: false,
      typeList: [],
      tagList: [],
      moduleList: [],
      priorityList: [
        {
          label: '高', value: 3,
        },
        {
          label: '中', value: 2,
        },
        {
          label: '低', value: 1,
        },
      ],
    };
  },
  computed: {
    // ...mapGetters(['userInfo']),
  },
  components: {
    operate, editPlan, assocaateCase, importFile, moveCase, copyEditCase, Sortable,
  },
  watch: {
    filterText(val) {
      this.$refs.departmentTree.filter(val);
    },
    $route(to) {
      this.getModuleList();
      this.getList();
    },
  },
  mounted() {
    this.initViewData();
  },
  methods: {
    // 获取测试负责人
    getUserList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_ALL_TESTER).then((res) => {
        if (res.code === '000000') {
          this.userList = res.data;
        } else {
          this.showMsg(res.msg);
        }
      });
    },
    // updateStatus() {
    //   const para = {
    //     id: this.$route.query.id - 0,
    //     state: this.status
    //   }
    //   this.api.updateStatus(para).then((res) => {
    //     if (res.code === '000000') {
    //       this.page.pageNumber = res.data.pageNum
    //       this.page.pageSize = res.data.pageSize
    //       this.page.totalDataCount = res.data.total
    //       this.search = Object.assign(this.search, res.data)
    //     } else {
    //       this.showMsg(res.msg || res.message)
    //     }
    //   })
    // },
    updateRes() {
      const para = {
        planId: this.$route.query.id - 0,
        // executeUser: '',
        caseList: this.multipleSelection.map(item => item.id),
        result: this.res,
        user: this.$appData.userInfo.nickName,
      };
      this.api.updateCaseInfo(para).then((res) => {
        if (res.code === '000000') {
          this.showMsg(res.msg || res.message, 'success');
          this.modalUpdateCase.visiable = false;
          this.getList();
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    updatePage(val) { // 分页
      this.page.pageSize = val.pageSize;
      this.page.pageNumber = val.currentPage;
      this.multipleSelection = [];
      this.getList(); // 获取请求
    },
    initViewData() {
      this.getModuleList();
      this.getList();
      this.getUserList();
      this.getTypeList();
      this.getTagList();
    },
    getTypeList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_TYPE_LIST).then((res) => {
        if (res.code === '000000') {
          this.typeList = res.data;
        } else {
          this.showMsg(res.msg || res.message);
        }
      });
    },
    getTagList() {
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_TYPE_LIST, { type: 2 }).then((res) => {
        if (res.code === '000000') {
          this.tagList = res.data;
        } else {
          this.showMsg(res.msg || res.message);
        }
      });
    },
    searchList() {
      this.modal.visiable = false;
      this.modalPlan.visiable = false;
      this.modalCase.visiable = false;
      this.page.pageNumber = 1;
      if (this.$route.query.id) {
        // this.caseListPara.moduleId = ''
        this.getPlanDetail();
      } else {
        this.getCaseList();
      }
    },
    getList() {
      this.modal.visiable = false;
      this.modalPlan.visiable = false;
      this.modalCase.visiable = false;
      this.modalCopyTestCase.visiable = false;
      if (this.$route.query.id) {
        // this.caseListPara.moduleId = ''
        this.getPlanDetail();
      } else {
        this.getCaseList();
      }
    },
    getListForWithCase() {
      this.modal.visiable = false;
      this.modalPlan.visiable = false;
      this.modalCase.visiable = false;
      this.modalMoveCase.visiable = false;
      if (this.$route.query.id) {
        this.getPlanDetail();
        this.getModuleList();
      } else {
        this.getCaseList();
      }
    },
    // 文件上传保存完之后子组件调用父组件方法
    getListAfterImport() {
      // console.log('文件上传保存完之后回调方法')
      this.modalImport.visiable = false;
      this.getCaseList();
      this.getModuleList();
    },
    getPlanDetail(row) {
      const para = {
        pageNo: this.page.pageNumber,
        pageSize: this.page.pageSize,
        planId: this.$route.query.id,
        moduleId: this.caseListPara.moduleId,
        showChildren: this.search.showSubModule,
        caseName: this.search.name,
        type: this.search.type,
        user: this.search.user,
        priority: this.search.priority,
        orderBy: 'id',
      };
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_PLAN_DETAIL, para).then((res) => {
        if (res.code === '000000') {
          this.page.pageNumber = res.data.caseList.pageNum;
          this.page.pageSize = res.data.caseList.pageSize;
          this.page.totalDataCount = res.data.caseList.total;
          this.status = res.data.state;
          this.caseList = res.data.caseList.list;
          this.search = Object.assign(this.search, res.data);
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // 过滤树节点
    // filterNode(value, data) {
    //   if (!value) return true
    //   return data.name.indexOf(value) !== -1
    // },

    filterNode(value, data, node) {
      if (!value) return true;
      let parentNode = node.parent;
      let labels = [node.label];
      let level = 1;
      while (level < node.level) {
        labels = [...labels, parentNode.label];
        parentNode = parentNode.parent;
        level++;
      }
      return labels.some(label => label.indexOf(value) !== -1);
    },
    // 选中的树节点
    handleNodeClick(data) {
      this.moduleList = [];
      // console.log(this.$refs.tree.getCheckedNodes())
      this.nodeInfo = Object.assign({}, data);
      this.getTreePath(data);
      this.caseListPara.moduleId = data.data.id;
      if (data.data.level >= 5) {
        this.getModuleLevel5List();
      }
      this.page.pageNumber = 1;
      this.getList();
    },
    // 修改更新
    submitForm(name) {
      this.api.updateDepartmentNode(this.search).then((res) => {
        if (res.code === '000000') {
          // 更新成功重刷数据
          this.initViewData();
          // 清空输入
          this.$refs[name].resetFields();
          this.showMsg(res.msg || res.message, 'success');
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // 表格选中
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 点击操作 增加、编辑
    handleOperate(type, row) {
      const obj = {
        add: () => this.executeAddRow(),
        edit: () => this.executeEditRow(row),
        del: () => this.executeDelRow(row),
        editPlan: () => this.executeEditPlanRow(row),
        // copyEdit: () => this.executeCopyEditRow(row),
        copyEditCase: () => this.executeCopyEditCaseRow(row),
        withCase: () => this.executeWithCaseRow(row),
        delCase: () => this.executeDelCaseRow(row),
        updateUser: () => this.executeUpdateUserRow('user', row),
        exportCase: () => this.exportCase(this.caseListPara),
        updateStatus: () => this.executeUpdateStatusRow('status', row),
        updateRes: () => this.executeUpdateResRow('res', row),
        move: () => this.executeMoveRow('move'),
      };
      obj[type].call(this);
    },
    // 执行：增加
    executeAddRow(type, row) {
      if (this.caseListPara.moduleId === null || this.caseListPara.moduleId === undefined || this.caseListPara.moduleId === '') {
        this.showMsg('请选择模块', 'warning');
        return;
      }
      this.modal.visiable = true;
      this.modal.title = '新建用例';
      this.$nextTick(() => {
        this.$refs.operate.initViewData(type, this.caseListPara.moduleId);
        this.drawBodyWrapper = document.querySelector('.el-dialog__body tbody');// 找element的标签
        // console.log('drawBodyWrapper', this.drawBodyWrapper)
        this.rowDrop();
      });
    },
    // 执行：编辑
    executeEditRow(row) {
      this.modal.visiable = true;
      this.modal.title = '编辑用例';
      this.$nextTick(() => {
        this.$refs.operate.initViewData(row, this.caseListPara.moduleId);
        this.drawBodyWrapper = document.querySelector('.el-dialog__body tbody');// 找element的标签
        // console.log('drawBodyWrapper', this.drawBodyWrapper)
        this.rowDrop();
      });
    },
    // 执行：复制编辑
    // executeCopyEditRow(row) {
    //   this.modal.visiable = true
    //   this.modal.title = '复制用例22'
    //   this.$nextTick(() => {
    //     this.$refs.copyEditPlan.initViewData(row, this.caseListPara.moduleId)
    //   })
    // },
    executeCopyEditCaseRow(row) {
      this.modalCopyTestCase.visiable = true;
      this.modalCopyTestCase.title = '复制用例';
      this.$nextTick(() => {
        this.$refs.copyEditCase.initViewData(row);
        // 解决先复制-后编辑问题，获取所有元素，如果是复制的话取最后一个tbody
        const arr = document.querySelectorAll('.el-dialog__body tbody');
        this.drawBodyWrapper = arr[arr.length - 1];
        this.rowDropCopy();
      });
    },
    executeWithCaseRow(row) {
      this.modalCase.visiable = true;
      this.$nextTick(() => {
        this.$refs.assocaateCase.initViewData(row);
      });
    },
    // plan执行：编辑
    executeEditPlanRow(row) {
      this.modalPlan.visiable = true;
      this.modalPlan.title = '编辑用例';
      this.$nextTick(() => {
        this.$refs.editPlan.initViewData(row);
      });
    },
    // 删除用例
    executeDelRow() {
      const id = this.multipleSelection.map(item => item.id);
      if (id === null | id === '' || id === undefined || id.length === 0) {
        this.showMsg('请选择测试用例', 'warning');
        return;
      }
      this.$confirm('确认删除所选用例', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.api.delCase({ ids: id }).then((res) => {
            if (res.code === '000000') {
              this.showMsg(res.msg || res.message, 'success');
              this.getList();
            } else {
              this.showMsg(res.msg || res.message, 'error');
            }
            this.multipleSelection = [];
          });
        });
    },
    // 移动用例
    executeMoveRow() {
      const ids = this.multipleSelection.map(item => item.id);
      if (ids === null | ids === '' || ids === undefined || ids.length === 0) {
        this.showMsg('请选择测试用例', 'warning');
        return;
      }
      this.modalMoveCase.visiable = true;
      this.$nextTick(() => {
        this.$refs.moveCase.initViewData(ids);
      });
      this.multipleSelection = [];
    },
    // 移除用例
    executeDelCaseRow() {
      const ids = this.multipleSelection.map(item => item.id);
      if (ids === null | ids === '' || ids === undefined || ids.length === 0) {
        this.showMsg('请选择测试用例', 'warning');
        return;
      }
      this.$confirm('确认移除所选用例', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          const para = {
            planId: this.$route.query.id,
            caseList: ids,
            type: 0,
            user: this.$appData.userInfo.nickName,
          };
          this.api.associateCase(para).then((res) => {
            if (res.code === '000000') {
              this.showMsg(res.msg || res.message, 'success');
              this.initViewData();
              // this.getList()
            } else {
              this.showMsg(res.msg || res.message, 'error');
            }
          });
        });
    },
    executeUpdateUserRow() {
      const id = this.multipleSelection.map(item => item.id);
      if (id === null | id === '' || id === undefined || id.length === 0) {
        this.showMsg('请选择测试用例', 'warning');
        return;
      }
      this.modalUpdateCase.visiable = true;
    },
    executeUpdateStatusRow() {
      this.modalUpdateStatus.visiable = true;
    },
    executeUpdateResRow() {
      const id = this.multipleSelection.map(item => item.id);
      if (id === null | id === '' || id === undefined || id.length === 0) {
        this.showMsg('请选择测试用例', 'warning');
        return;
      }
      this.modalUpdateRes.visiable = true;
    },
    // 节点操作
    operateModule(type) {
      this.modalModule.type = type;
      if (type === 'del') {
        this.$confirm('是否删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          this.api.delModule({ id: this.nodeInfo.data.id }).then((res) => {
            if (res.code === '000000') {
              this.showMsg(res.msg || res.message, 'success');
              this.getModuleList();
            } else {
              this.showMsg(res.msg || res.message, 'error');
            }
          });
        });
      } else {
        this.modalModule.visiable = true;
        if (type === 'edit') {
          this.modalModule.title = '重命名';
          this.moduleInfo.name = this.nodeInfo.data.name;
        } else {
          this.modalModule.title = '新建模块名';
          this.moduleInfo.name = '';
        }
      }
    },
    // 节点保存
    saveModule() {
      const obj = {
        add: () => this.executeAddModuleRow(),
        edit: () => this.executeEditModuleRow(),
        // del: () => this.executeDelModuleRow()
      };
      obj[this.modalModule.type].call(this);
    },
    saveCase() {
      const para = {
        planId: this.$route.query.id,
        executeUser: this.modalUpdateCase.value,
        caseList: this.multipleSelection.map(item => item.id),
        user: this.$appData.userInfo.nickName,
        result: this.result,
      };
      this.api.updateCaseInfo(para).then((res) => {
        if (res.code === '000000') {
          this.showMsg(res.msg || res.message, 'success');
          this.modalUpdateCase.visiable = false;
          this.modalUpdateRes.visiable = false;
          this.multipleSelection = [];
          this.getList();
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // module新增
    executeAddModuleRow() {
      this.modalModule.visiable = true;
      const para = {
        name: this.moduleInfo.name,
        parentId: this.nodeInfo.data.id,
        user: this.$appData.userInfo.nickName,
        level: this.nodeInfo.level,
      };
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.ADD_MODULE, para).then((res) => {
        console.log('module返回');

        // this.api.addModule(para).then((res) => {
        if (res.code === '000000') {
          this.showMsg(res.msg || res.message, 'success');
          this.modalModule.visiable = false;
          this.getModuleList();
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // module编辑
    executeEditModuleRow() {
      this.modalModule.visiable = true;
      this.modalModule.title = '编辑';
      const para = {
        name: this.moduleInfo.name,
        parentId: this.nodeInfo.data.parentId,
        id: this.nodeInfo.data.id,
      };
      this.api.editModule(para).then((res) => {
        if (res.code === '000000') {
          this.showMsg(res.msg || res.message, 'success');
          this.modalModule.visiable = false;
          this.getModuleList();
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // module删除
    executeDelModuleRow() {
      this.$confirm('是否删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        this.api.delModule({ id: this.nodeInfo.data.id }).then((res) => {
          if (res.code === '000000') {
            this.showMsg(res.msg || res.message, 'success');
            this.getModuleList();
          } else {
            this.showMsg(res.msg || res.message, 'error');
          }
        });
      });
    },
    // 取消
    resetForm(ucsMsg) {
      this.$refs[name].resetFields();
    },
    // 同步部门信息
    updateDepartment() {
      this.api.getQaOrganizationList({}).then((res) => {
        if (res.code === '000000') {
          this.initViewData();
          this.showMsg(res.msg || res.message, 'success');
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // 初始获取数据
    getModuleList() {
      const planId = this.$route.query.id;
      this.$axiosUtil.get(this.$appConfig.API, this.$urlConst.GET_MODULE_TREE, planId).then((res) => {
        if (res.code === '000000') {
          if (res.data.length > 0) {
            this.treeData = res.data;
          } else {
            this.showMsg('暂未关联测试用例', 'warning');
          }
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    getArray(data, id) {
      let arr;
      for (const i in data) {
        if (data[i].id === id) {
          arr = data[i];
          arr.children = this.addedNode;
          break;
        } else {
          this.getArray(data[i].children, id);
        }
      }
      return arr;
    },
    getModuleLevel5List() {
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_NEXT_MODULES_BY_ID, { id: this.nodeInfo.data.id }).then((res) => {
        if (res.code === '000000') {
          if (res.data.length > 0) {
            this.addedNode = res.data;
            this.getArray(this.treeData, this.nodeInfo.data.id);
          } else {
            // this.showMsg('数据为空')
          }
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // 用例查询
    getCaseList() {
      this.caseListPara.pageNo = this.page.pageNumber || 1;
      this.caseListPara.pageSize = this.page.pageSize || 10;
      this.caseListPara.showSubModule = this.search.showSubModule;
      this.caseListPara.name = this.search.name;
      this.caseListPara.type = this.search.type;
      this.caseListPara.user = this.search.user;
      this.caseListPara.priority = this.search.priority;
      this.$axiosUtil.post(this.$appConfig.API, this.$urlConst.GET_TESTCASE_LIST, this.caseListPara).then((res) => {
        if (res.code === '000000') {
          this.page.pageNumber = res.data.pageNum;
          this.page.pageSize = res.data.pageSize;
          this.page.totalDataCount = res.data.total;
          this.caseList = res.data.list;
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    // 文件导入
    handleCommand(command) {
      this.modalImport.visiable = true;
      this.modalImport.title = '上传用例文件';
      this.$nextTick(() => {
        this.$refs.importFile.initViewData(command);
      });
    },
    reset() { // 重置
      this.search.name = '';
      this.search.type = '';
      this.search.user = '';
      this.search.priority = '';
    },
    exportCase(obj) {
      this.loading = true;
      const FormDatas = new FormData();
      if (obj.moduleId === null || obj.moduleId === '' || obj.moduleId === undefined) {
        this.showMsg('请选择模块', 'warning');
        this.loading = false;
        return;
      }
      Object.keys(obj).map((v) => {
        obj[v] = obj[v] ? obj[v] : '';
        FormDatas.append(v, obj[v]);
      });
      // 创建一个axios实例
      const instance = axios.create({
        // baseURL: HostName,
        withCredentials: true,
      });
      instance.post('/testcase/testcase/exportTestCase', FormDatas, { responseType: 'blob' }).then((res) => {
        const temp = res.data;
        if (temp.type === 'application/json') {
          // 错误
          this.showMsg('导出失败', 'error');
          this.loading = false;
        } else {
          // 正常下载
          const blob = new Blob([res.data], {
            type: 'application/xlsx;charset=utf-8',
          });
          const eleLink = document.createElement('a');
          eleLink.download = '导出用例.xlsx';
          eleLink.style.display = 'none';
          // 字符内容转变成blob地址
          eleLink.href = URL.createObjectURL(blob);
          // 触发点击
          document.body.appendChild(eleLink);
          eleLink.click();
          // 然后移除
          URL.revokeObjectURL(eleLink.href);
          document.body.removeChild(eleLink);
          this.loading = false;
        }
      });
    },
    statusHandleCommand(item) {
      this.status = item.status;
      this.statusTag = item.tag;

      const para = {
        id: this.$route.query.id,
        user: this.$appData.userInfo.nickName,
        state: item.status,
      };
      this.api.updateStatus(para).then((res) => {
        if (res.code === '000000') {
          this.showMsg(res.msg || res.message, 'success');
        } else {
          this.showMsg(res.msg || res.message, 'error');
        }
      });
    },
    setCaseResult(result) {
      this.result = result;
    },
    getTreePath(node) {
      if (node) {
        if (node.level === 1) {
          this.moduleList.sort((a, b) => a.level - b.level);
          if (this.moduleList.length > 4) {
            const tempList = [];
            tempList.push(this.moduleList[0]);
            tempList.push({ level: '', name: '...' });
            tempList.push(this.moduleList[this.moduleList.length - 2]);
            tempList.push(this.moduleList[this.moduleList.length - 1]);
            this.moduleList = tempList;
          }
          return;
        }
        const map = {};
        map.level = node.data.level;
        map.name = node.data.name;
        this.moduleList.push(map);
        this.getTreePath(node.parent);
      }
    },
    rowDrop() {
      const tbody = this.drawBodyWrapper;
      const _this = this;
      Sortable.create(tbody, {
        onEnd({ newIndex, oldIndex }) {
          const currRow = _this.$refs.operate.search.tcTestcaseStepList.splice(oldIndex, 1)[0];
          _this.$refs.operate.search.tcTestcaseStepList.splice(newIndex, 0, currRow);
        },
      });
    },
    rowDropCopy() {
      const tbody = this.drawBodyWrapper;
      const _this = this;
      Sortable.create(tbody, {
        onEnd({ newIndex, oldIndex }) {
          const currRow = _this.$refs.copyEditCase.search.tcTestcaseStepList.splice(oldIndex, 1)[0];
          _this.$refs.copyEditCase.search.tcTestcaseStepList.splice(newIndex, 0, currRow);
        },
      });
    },
    // 根据接口返回测试类型
    getCaseType(id, list) {
      let res = '其他';
      for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
          res = list[i].name;
        }
      }
      return res;
    },
    // 根据接口返回测试标签
    getCaseTag(id, list) {
      let res = '其他';
      for (let i = 0; i < list.length; i++) {
        if (list[i].id === parseInt(id)) {
          res = list[i].name;
        }
      }
      return res;
    },
  },
};
</script>
<style lang="scss">
.testlibrary-index {
  height: 100%;
  padding: 10px 15px;
  .header {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .add {
      flex: 1;
      text-align: right;
    }
  }
  .filter-tree {
    width: 100%;
    overflow: auto;
    background: #e8e8e8;
    height: calc(100% - 36px);
    .custom-tree-node {
      display: block;
      width: 100%;
      .flex-wrap {
        display: flex;
        justify-content: space-between;
      }
    }
  }

  // textarea {
  //   resize: none;
  //   min-height: 200px !important;
  // }
}
</style>
