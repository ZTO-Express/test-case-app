<template>
  <div class="vue-json-pretty">
    <div v-if="signal" class="w100 h100 flex flex-just-between">
      <vue-json-pretty
        :path="'res'"
        :data="jsonvalue"
        showLength
        highlightMouseoverNode
        showSelectController
      ></vue-json-pretty>
    </div>
    <div v-else class="w100 h100 flex flex-just-between">
      <div class="input-info">
        <el-input type="textarea" placeholder="请输入内容" v-model="inputvalue" :rows="11"></el-input>
      </div>
      <div style="width: 2%" class="flex flex-col flex-center">
        <div>预览</div>
        <div class="vue-json-pretty">
          <i class="el-icon-right"></i>
        </div>
      </div>
      <div class="json-info" style="border: 1px solid #DCDFE6; max-height: 232px;overflow:auto">
        <vue-json-pretty
          :path="'res'"
          :data="jsonvalue"
          showLength
          highlightMouseoverNode
          showSelectController
        ></vue-json-pretty>
      </div>
    </div>
  </div>
</template>
<script>
import VueJsonPretty from 'vue-json-pretty'
export default {
  props: {
    signal: {
      type: Boolean,
      default: false
    },
    inputval: {
      type: String,
      default: '{}'
    },
    jsonval: {
      validator: (val) => {
        return typeof val === 'object'
      },
      default: () => {}
    }
  },
  data() {
    return {
      timer: null,
      inputvalue: '',
      jsonvalue: {}
    }
  },
  components: {
    VueJsonPretty
  },
  computed: {

  },
  mounted() {
    this.inputvalue = this.inputval
    this.jsonvalue = this.jsonval
    console.log(this.inputvalue, 'inputvalueinputvalue')
  },
  methods: {

  },
  watch: {
    inputvalue() {
      const flag = this.isJSON(this.inputvalue)
      clearTimeout(this.timer)
      this.timer = setTimeout(() => {
        if (!flag) {
          this.showMsg('json格式错误,请校验', 'warning')
          this.$emit('jsonPrettyInfo', { status: false, data: '' })
          return
        }
        this.jsonvalue = JSON.parse(this.inputvalue)
        const obj = {
          status: true,
          data: JSON.parse(JSON.stringify(JSON.parse(this.inputvalue)))
        }
        this.$emit('jsonPrettyInfo', obj)
      }, 1000)
    }
  }

}
</script>
<style lang="scss">
.vue-json-pretty {
  .input-info {
    width: 50%;
  }
  .json-info {
    width: 47%;
    padding: 6px 0;
  }
  .vue-json-pretty-line {
    border: 1px solid #dcdfe6;
    border-radius: 2px;
  }
  .vue-json-pretty {
    .el-icon-right {
      &::before {
        font-size: 24px;
        margin-top: 8px;
      }
    }
  }
}
</style>


