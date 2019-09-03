<template>
  <div id="main" class="table-with-download">
    <el-card :shadow="shadow">
      <div v-if="colRules.download" class="download list-download-btn table-header-font-common" align="right">
        <i class="el-icon-download"></i><a href="javascript:void(0)" @click="download">导出列表</a>
        <p>{{colRules.download.tips }}</p>
        <slot name="jump"></slot>
      </div>
      <div align="left" class="summary table-summary" v-if="colRules.data.summary">
        <template v-for="col in colRules.data.summary.attrMap">
          <p v-if="col.ext&&col.ext.display">{{col.label + " : " + col.value}}</p>&nbsp
          <el-row type="flex" justify="left" v-if="col.display">
            <div v-for="oper in col.buttom.opers">
              <el-col>
                <el-button @click="oper.fun()" :size="oper.size" :type="oper.type">
                  {{oper.label}}
                </el-button>
                &nbsp;
              </el-col>
            </div>
          </el-row>
        </template>
      </div>
      <el-table :data="colRules.data.content" style="width: 100%" border size="mini"
                v-loading="loading"
                :row-class-name="rowClassName"
                :span-method="spanFun"
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                @selection-change="handleSelectionChange"
      >
        <template v-for="col in colRules.data.header">
          <el-table-column v-if="col.type==='expand'" type="expand" fixed="left" width="50">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item :label="expand.label" :key="expand.key" v-for="expand in col.expands">

                  <el-popover v-if="expand.isNeedPopFun ? expand.isNeedPopFun(props.row[expand.key]):false"
                              width="200"
                              trigger="hover">
                    <span class="expend-pop-span">
                      {{
                      expand.formatter ? expand.formatter(getExpandValue(props.row, expand.key))
                        : getExpandValue(props.row, expand.key)
                      }}
                    </span>
                    <p slot="reference" style="color: deepskyblue">{...}</p>
                  </el-popover>
                  <span class="expend-common-span" v-else>
                    {{
                    expand.formatter ? expand.formatter(getExpandValue(props.row, expand.key))
                      : getExpandValue(props.row, expand.key)
                    }}
                  </span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column v-if="col.type==='selection'" type=selection fixed="left"
                           width="50"></el-table-column>
          <el-table-column v-if="col.type==='select'" type=selection fixed="left" width="50"
                           :selectable="col.fun"></el-table-column>
          <el-table-column v-if="col.type==='a'&&hasPerm(col.permission)" :label="col.label">
            <template slot-scope="scope">
              <div oncontextmenu="return false">
                <a href="javascript:void(0)" @click="col.fun(scope.row)" size="mini">
                {{scope.row[col.key]}}
              </a>
              </div>
            </template>
          </el-table-column>
          <el-table-column v-if="col.type==='link'&&col.display" :prop="col.key" :sortable="col.sortable"
                           :label="col.label">
            <template slot-scope="scope">
              <span oncontextmenu="return false">
                  <router-link
                    :to="{name:col.link?col.link:col.linkFun(scope.row), params:getParams(scope.row,col.link?null:col.linkFun(scope.row)),query:getQuery(scope.row,col.link)}">
                      {{scope.row[col.key]}}
                  </router-link>
              </span>
            </template>
          </el-table-column>
          <el-table-column v-if="col.type==='normal'&&col.display" :prop="col.key" :sortable="col.sortable"
                           :label="col.label" :formatter="col.formatter"></el-table-column>
          <el-table-column v-if="col.type==='textExpand'&&col.display" :prop="col.key" :sortable="col.sortable"
                           :label="col.label" :formatter="col.formatter">
            <template slot-scope="scope">
              <el-popover v-if="getRef(scope.row[col.key])==='{...}'"
                          width="200"
                          trigger="hover">
                <p>{{scope.row[col.key].replace(/,/g, ',\n')}}</p>
                <p slot="reference" style="color: deepskyblue">{{getRef(scope.row[col.key])}}</p>
              </el-popover>
              <p v-else="getRef(scope.row[col.key])==='...'">{{scope.row[col.key]}}</p>
            </template>
          </el-table-column>
          <el-table-column v-if="col.type==='textPop'&&col.display" :prop="col.key" :sortable="col.sortable"
                           :label="col.label" :formatter="col.formatter">
            <template slot-scope="scope">
              <el-popover v-if="col.isNeedPopFun(scope.row[col.key])"
                          width="200"
                          trigger="hover">
                {{scope.row[col.key]}}
                <p slot="reference" style="color: deepskyblue">{{col.showKeyFun(scope.row[col.key])}}</p>
              </el-popover>
              <p v-else="getRef(scope.row[col.key])==='...'">{{scope.row[col.key]}}</p>
            </template>
          </el-table-column>
          <el-table-column v-if="col.type==='copy'" :label="col.label" fixed="right" width="58">
            <template slot-scope="scope">
              <el-button circle size="mini" icon="el-icon-document"
                         v-clipboard:copy="getText(scope.row)"
                         v-clipboard:success="onCopy"
                         v-clipboard:error="onError"
              ></el-button>
            </template>
          </el-table-column>

          <el-table-column v-if="col.type==='oper'" :label="col.label" fixed="right" width="120px">
            <template slot-scope="scope">
              <div v-if="countDisplay(col.opers,scope.row) === 1">
                <div class="opers" v-for="oper in col.opers" >
                  <div v-if="oper.dispaly(scope.row)&&hasPerm(oper.permission)">
                    <el-button  @click="oper.fun(scope.row)"  size="mini" :type="oper.type" >
                      {{oper.label}}
                    </el-button>
                  </div>
                </div>
              </div>

              <div v-if="countDisplay(col.opers,scope.row) > 1">
                <el-dropdown @command="handleChange">
                  <el-button type="primary" size="mini">
                    更多操作<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <div class="opers" v-for="oper in col.opers">
                      <div v-if="oper.dispaly(scope.row)&&hasPerm(oper.permission)">
                        <el-dropdown-item v-if="initFun(oper.label+ scope.$index,oper.fun, scope)"
                                          :command="oper.label + scope.$index">{{oper.label}}
                        </el-dropdown-item>
                      </div>
                    </div>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>

          <el-table-column v-if="col.type==='opers'" :label="col.label" fixed="right" :width="getOperWidth(col.opers)">
            <template slot-scope="scope">
              <el-row type="flex" justify="center">
                <div v-for="oper in col.opers">
                  <el-col v-if="oper.dispaly(scope.row)&&hasPerm(oper.permission)">
                    <el-button @click="oper.fun(scope.row)" size="mini" :type="oper.type">
                      {{oper.label}}
                    </el-button>
                    &nbsp;
                  </el-col>
                </div>
              </el-row>
            </template>
          </el-table-column>
        </template>
      </el-table>

      <div align="center" v-if="paginationVisable">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="listQuery.currentPage"
          layout="total,sizes, prev, pager, next, jumper"
          :page-sizes="listQuery.sizes"
          :page-size="listQuery.pageSize"
          :total="listQuery.total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import bus from '@/assets/eventBus';
import ElInput from '../../../node_modules/element-ui/packages/input/src/input.vue';
import ElCheckbox from '../../../node_modules/element-ui/packages/checkbox/src/checkbox.vue';


export default {
  props: {
    loading: {
      type: Boolean,
      default: false,
    },
    buttonLoading: {
      type: Boolean,
      default: false,
    },
    rowClassName: {
      type: Function,
    },
    spanFun: {
      type: Function,
    },
    initFlag: {
      type: Boolean,
      default: true,
    },
    shadow: {
      type: String,
      default: 'always',
    },
    paginationVisable: {
      type: Boolean,
      default: true,
    },
    colRules: {
      type: Object,
      default: {
        download: {},
        data: {
          summary: {
            title: '',
            attrMap: [],
          },
          header: [],
          footer: [],
          content: [],
          title: '',
        },
      },
    },
    listQuery: {
      type: Object,
      default: {
        sizes: [10, 20, 30, 40],
        currentPage: 1,
        pageSize: 10,
        total: 0,
        orderProp: null,
        order: null,
      },
    },
  },
  mounted() {
    if (this.initFlag) {
      this.$emit('getListQuery', this.listQuery);
    }
  },
  data() {
    return {
      funMap: {},
      /** 使用样例：
         *
         * colRules:{
            download: {tips:'仅可导出1天的数据，最多可导出五万条记录'};
            table:{},
            header: [
                {type: 'selection'},
                selection使用说明:
                1.import bus from '@/assets/eventBus';
                2.定义一个selections:[]接收选中的值
                3.mounted钩子中添加:
                var self = this
                bus.$on('selectionChange',function(selections) {
                    self.selections = selections
                })
                {type: 'expand',expands:[{label:'xinming',key:'channelNo'},{label:'a',key:'channelNo'},{label:'b',key:'bb'},{label:'c',key:'cc'}]},
                {label: 'a', display:true, key: 'channelNo',type:'link',link:'standbyEdit'},
                {label: 'b', display:true, key: 'bb', type: 'normal',sortable: true},
                {label: 'c', display:true, key: 'cc', type: 'normal'},
                {label: '复制', type: 'copy'},
                {label: '操作',  type: 'oper',opers:[
                    {label:'删除1',type:'primary',dispaly:'true',permission:'trx-mgmt-list/paymentFix',fun:function (row) {
                        alert(row.id)
                    }},
                    {label:'删除2',type:'primary',dispaly:'true',fun:function (row) {
                        alert(row.id)
                    }},
                ]},
            ],
            content:[
                {
                    channelNo: 'channelNo1',
                    bb: 'bbbb',
                    cc: 'ccccc',
                    dd: 'dddfffdd',
                },
            ]
        }
         */
    };
  },
  components: {
  	ElCheckbox,
    ElInput,
  },
  computed: {},
  watch: {
    colRules(val, oldval) {
      this.listQuery.total = parseInt(val.data.summary.attrMap.totalCount.value);
    },
  },
  methods: {
    countDisplay(opers, row) {
      let count = 0;
      opers.forEach((oper) => {
        if (oper.dispaly(row) && this.hasPerm(oper.permission)) {
          count++;
        }
      });
      return count;
    },
    getOperWidth(opers) {
      if (opers.length === 1) {
        return 120;
      }
      return 90 * opers.length;
    },
    getParams(row, name) {
      if (name === null) {
        row.backName = this.$route.name;
        return row;
      } else if (name === 'orderDetail') {
        var params = {
          orderId: row.orderId,
          isShow: true,
          startTime: this.$formatters.formatTime(new Date(row.orderCreateDate - 14 * 24 * 60 * 60 * 1000), 'YYYY-MM-DD'),
          endTime: this.$formatters.formatTime(new Date(row.orderCreateDate + 14 * 24 * 60 * 60 * 1000), 'YYYY-MM-DD'),
          backName: 'riskTrxApprove',
        };
        return params;
      } else if (name === 'agentPayOrder') {
        var params = {
          detailId: row.orderId == undefined ? row.detailId : row.orderId,
          searchTime: this.$formatters.formatTime(new Date(row.orderCreateDate - 24 * 60 * 60 * 1000), 'YYYY-MM-DD') + ',' + this.$formatters.formatTime(new Date(row.orderCreateDate), 'YYYY-MM-DD'),
        };
        return params;
      }
      row.backName = this.$route.name;
      return row;
    },
    getQuery(row, name) {
      let query = null;
      if (name === 'customerOperation') {
        query = { type: 'detail', customerNo: row.seller_id, flag: 'normal' };
      }
      return query;
    },
    initFun(name, fun, scope) {
      this.funMap[name] = {
        fun,
        params: [scope.row],
      };
      return true;
    },
    handleChange(name) {
      const oper = this.funMap[name];
      if (oper) {
        oper.fun(...oper.params);
      }
    },
    handleSelectionChange(selections) {
      bus.$emit('selectionChange', selections);
    },
    hasPerm(permission) {
      if (!permission) {
        return true;
      }
      return this.$permUtil.hasPerm(permission);
    },
    download() {
      this.$emit('download');
    },
    handleSizeChange(size) {
      this.listQuery.pageSize = size;
      this.$emit('getListQuery', this.listQuery);
    },
    handleCurrentChange(current) {
      this.listQuery.currentPage = current;
      this.$emit('getListQuery', this.listQuery);
    },
    onCopy(e) {
      this.$message.success('已复制');
    },
    onError(e) {
      this.$message.error('复制失败' + e);
    },
    getExpandValue(row, key) {
      let msg = '';
      const exparr = key.split('.');
      if (exparr.length === 1) {
        msg = row[exparr[0]];
      } else {
        msg = row[exparr[0]][exparr[1]];
      }
      return msg;
    },
    getText(row) {
      let msg = '';
      this.colRules.data.header.forEach((col) => {
        if (col.type === 'normal' || col.type === 'texTExpand') {
          const arr = col.key.split('.');
          let value;
          if (arr.length === 1) {
            value = row[col.key];
            if (!value) {
              value = '';
            }
            msg += col.label + ':' + value + ',';
          } else {
            value = row[arr[0]][arr[1]];
            if (!value) {
              value = '';
            }
            msg += col.label + ':' + value + ',';
          }
        }
      });
      if (msg.length > 0) {
        return msg.substring(0, msg.length - 1);
      }
    },

    getRef(data) {
      if (data != null && data.indexOf(',') > -1) {
        return '{...}';
      }
      return data;
    },
  },
};
</script>
<style>
.table-with-download .demo-table-expand {
  float: left;
  font-size: 0;
}

.table-with-download .demo-table-expand label {
  float: left;
  width: 90px;
  color: #99a9bf;
}

.table-with-download .el-form-item__label {
  font-size: 12px;
}

.table-with-download .el-form-item__content {
  font-size: 12px;
  float: left;
}

.table-with-download .demo-table-expand .el-form-item {
  float: left;
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.table-with-download .el-table__expanded-cell .el-form {
  width: 100%;
}

.table-with-download .summary p {
  font-size: 75%;
  display: inline;
}

.table-with-download .download p {
  font-size: 75%;
  margin-bottom: 0px;
  margin-top: 0px;
}
.table-with-download .download span {
  font-size: 75%;
  margin-bottom: 0px;
  margin-top: 0px;
}

.table-with-download .summary {
  padding-bottom: 10px;
}

.table-with-download th div {
  text-align: center;
}

.table-with-download td div {
  text-align: center;
}
.el-popover {
  word-wrap: break-word;
}

</style>
