import formatUtil from './formatUtil'
import btn from '@/businessComponents/btn'
import top from '@/businessComponents/top'
import modal from '@/businessComponents/modal'
import breadcrumb from '@/businessComponents/breadcrumb'
import pagination from '@/businessComponents/pagination'
import treeSelect from '@/businessComponents/treeSelect'
import CompletePagination from '@/businessComponents/CompletePagination'
import moment from 'moment'
export default {
  data() {
    return {}
  },
  components: {
    btn,
    top,
    modal,
    pagination,
    breadcrumb,
    treeSelect,
    CompletePagination
  },
  computed: {

  },
  filters: {

  },
  methods: {
    goBack() {
      history.back()
    },
    goPath(name) {
      this.$router.push({
        name: name
      })
    },
    orderField(prop, order) { // 排序
      return order === 'ascending' ? prop : `-${prop}`
    },
    getFormatTime(time) { // 时间戳转换为年月日时分秒
      return moment(time).format('YYYY-MM-DD HH:mm:ss')
    },
    dateTimeFormat(row, column) {
      // （前后端统一）表格里时间格式
      const date = row[column.property]
      if (date) {
        return moment(date).format('YYYY-MM-DD HH:mm:ss')
      } else {
        return ''
      }
    },
    getFormatTimeH(time) { // 时间戳转换为时分秒
      return (time === '' || time === null) ? '' : moment(time).format('HH:mm:ss')
    },
    isJSON(variable) {
      if (variable) {
        try {
          if (typeof variable === 'string') {
            variable = JSON.parse(variable)
          }
          if (Object.prototype.toString.call(variable) === '[object Array]') {
            return true
          }
          const temp = Object.prototype.toString.call(variable).toLowerCase() === '[object object]' ? true : `${variable}`.indexOf('{') > -1
          return temp
        } catch (e) {
          return false
        }
      } else {
        return false
      }
    },
    showMsg(msg, type = 'error') {
      this.$notify({
        message: msg,
        type: type
      })
    },
    // 根据内容是否为空返回带小括号内容
    showSignContent(val) {
      return val ? '(' + val + ')' : ''
    },
    ...formatUtil
  }
}
