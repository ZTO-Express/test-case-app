<!--  -->
<template>
  <div class="tree-select">
    <treeselect
      :normalizer="normalizer"
      placeholder="请选择"
      v-model="treeData"
      :multiple="multiple"
      :options="options"
      :default-expand-level="1"
      @select="handleInput"
      :clearable="false"
    ></treeselect>
  </div>
</template>

<script>

import treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
function bnameTolabel(node) {
  //   去掉children=[]的children属性
  if (node.children && !node.children.length) {
    delete node.children
  }
  return {
    label: node.bname
  }
}
export default {
  name: '',
  components: {
    treeselect
  },
  data() {
    return {

    }
  },
  props: {
    normalizer: {
      type: Function,
      default: bnameTolabel
    },
    multiple: {
      type: Boolean,
      default: false
    },
    value: {
      type: String | Number,
      default: ''
    },
    options: {
      default: () => []
    }
  },
  watch: {
    value(v) {
    }
  },
  computed: {
    treeData: {
      get() {
        const _value = this.value === '' ? null : this.value
        return _value
      },
      set(val) {
      }
    }
  },
  methods: {
    handleInput(e) {
      console.log('change tree data', e)
      this.$emit('changeVal', e.id)
    }
  },
  created() {

  },
  mounted() {

  }
}
</script>
<style lang='scss'>
.vue-treeselect__control {
  max-width: 200px;
  height: 32px;
  border-radius: 0;
}
.vue-treeselect__menu-container {
  width: 260px;
}
</style>
