<template>
  <el-card style="width:100%;height:100%;">
    <el-form :inline="true" size="mini">
      <el-form-item :key="detail.name" class="form-item" v-for="detail in details" :label="detail.label">
        <el-input v-if="detail.type === 'text' && !detail.org"
                  v-model="detail.value"
                  :placeholder="detail.placeholder"
                  :clearable="detail.clearable"
                  :editable="detail.editable"
                  v-on:change="handleChangeSession(detail.session,detail.value)"
        >
        </el-input>
        <el-date-picker v-if="detail.type ==='datePicker' && !detail.org"
                        v-model="detail.value"
                        :format="detail.displayFormat"
                        :placeholder="detail.placeholder"
                        :picker-options="detail.option"
                        :clearable="detail.clearable"
                        :editable="detail.editable"
                        v-on:change="handleChangeSession(detail.session,detail.value,detail.type)"
        >
        </el-date-picker>
        <el-date-picker v-if="detail.type ==='daterange'  && !detail.org" type="daterange"
                        v-model="detail.value"
                        :format="detail.displayFormat"
                        :start-placeholder="detail.placeholderStart"
                        :range-separator="detail.placeholderTo"
                        :end-placeholder="detail.placeholderEnd"
                        :picker-options="detail.option"
                        :clearable="detail.clearable"
                        :editable="detail.editable"
                        v-on:change="handleChangeSession(detail)">
        </el-date-picker>
        <el-date-picker v-if="detail.type ==='datetimerange'  && !detail.org" type="datetimerange"
                        v-model="detail.value"
                        :format="detail.displayFormat"
                        :start-placeholder="detail.placeholderStart"
                        :range-separator="detail.placeholderTo"
                        :end-placeholder="detail.placeholderEnd"
                        :picker-options="detail.option"
                        :clearable="detail.clearable"
                        :editable="detail.editable"
                        v-on:change="handleChangeSession(detail)">
        </el-date-picker>
        <el-select v-if="detail.type === 'selector'  && !detail.org"
                   :placeholder="detail.placeholder"
                   v-model="detail.value"
                   @change="handleChangeSession(detail.session,detail.value,detail.type)"
                   clearable filterable>
          <el-option v-for="option in detail.options"
                     :label="option.value" :key="option.label" :value="option.label"></el-option>
        </el-select>
        <div v-if="detail.type === 'range'  && !detail.org">
          <el-input style="width: 80px; float:left"
                    v-model="detail.value[0]"
                    :placeholder="detail.placeholder[0]"
                    :clearable="detail.clearable"
                    :editable="detail.editable"
                    v-on:change="handleChangeSession(detail.session,detail.value)"
          ></el-input>
          <el-input style="width: 80px; float:left"
                    v-model="detail.value[1]"
                    :placeholder="detail.placeholder[1]"
                    :clearable="detail.clearable"
                    :editable="detail.editable"
                    v-on:change="handleChangeSession(detail.session,detail.value)"
          ></el-input>
        </div>
      </el-form-item>
      <el-form-item class="form-item">
        <el-button type="primary" size="mini" v-on:click="search()" :loading="this.loading" icon="el-icon-search">搜索
        </el-button>
        <el-button type="default" size="mini" v-on:click="clear()" icon="el-icon-close">清空</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
  import moment from "moment";

  export default {
    data() {
      return {
        defaultValue: {},
      }
    },
    props: {
      details: {
        Type: Object,
        defaultValue: {},
      },
      loading: {
        Type: Boolean,
      }
    },
    mounted() {
      this.details.map((object, key) => {
        if (object.value !== "") {
          this.defaultValue[object.name] = object.value;
        }
        //session
        if (object.session !== null
          && object.session !== ""
          && this.$sessionUtil.get(object.session) !== null) {
          if (
            object.type === "datetimerange"
            || object.type === "daterange"
          ) {
            let dates = this.$sessionUtil.get(object.session).split(",");
            if (dates.length > 1) {

              object.value = [dates[0], dates[1]];
            }
          }
          else if (
            object.type === "selector"
          ) {
            if (this.$sessionUtil.get(object.session) === "") {
              object.value = this.$sessionUtil.get(object.session);
            } else if (!isNaN(Number(this.$sessionUtil.get(object.session)))) {
              object.value = Number(this.$sessionUtil.get(object.session));
            } else {
              object.value = this.$sessionUtil.get(object.session);
            }
          } else if (
            object.type === "text"
          ) {
            object.value = this.$sessionUtil.get(object.session);
          } else if (
            object.type === "range"
          ) {
            let session = this.$sessionUtil.get(object.session).split(",");
            if (session.length > 1) {
              object.value = [session[0], session[1]];
            }
          }
        }
      });
    },
    methods: {
      search() {
        this.$emit('getList');
      },
      clear() {
        let keys = Object.keys(this.defaultValue);
        for (let i = 0, length = this.details.length; i < length; i++) {
          if (keys.indexOf(this.details[i].name.toString()) >= 0) {
            if (this.details[i].isNow) {
              if (this.details[i].type === "datetimerange"
                || this.details[i].type === "daterange") {
                this.details[i].value = [this.defaultValue[this.details[i].name][0], new Date()];
              }
            } else {
              this.details[i].value = this.defaultValue[this.details[i].name];
            }
            this.handleChangeSession(this.details[i].session, this.details[i].value);
          } else {
            this.details[i].value = "";
            this.handleChangeSession(this.details[i].session, this.details[i].value);
          }
        }
      },
      handleChangeSession(detail) {
        let key = detail.session;
        let value = detail.value;
        let type = detail.type;

        if (
          detail.isReset !== undefined
          && !detail.isReset
          && type === "datetimerange"
          && value === null
        ) {
          detail.value = this.defaultValue[detail.name];
          return;
        }
        if (key
          && key === null
          && key === "") {
          return;
        }
        if (
          type === "datetimerange"
          && value
          && value.length > 1
          && moment(value[1]).hours().toString() === "0"
          && moment(value[1]).minutes().toString() === "0"
          && moment(value[1]).seconds().toString() === "0"
        ) {
          value[1] = new Date(moment(value[1]).hours(23).minutes(59).seconds(59).valueOf());
        }
        this.$sessionUtil.set(key, value);
      }
    }
  }
</script>


<style scoped>
  .form-item {
    margin: 5px 5px 5px 0;
  }
</style>
