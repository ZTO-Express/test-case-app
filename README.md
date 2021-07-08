# Test Case Management
* 测试用例管理平台，是一个前后端分离的项目。主要提供了测试用例与执行计划的管理与维护。
-------------------------------------------------------------------------
[![npm package](https://img.shields.io/npm/v/vue.svg)](https://www.npmjs.com/package/vue)
[![MIT](https://img.shields.io/dub/l/vibe-d.svg?style=flat-square)](http://opensource.org/licenses/MIT)
[![js-standard-style](https://img.shields.io/badge/code%20style-standard-brightgreen.svg)](http://standardjs.com)


## Browser Support

| ![Chrome](https://raw.github.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png) | ![Firefox](https://raw.github.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png) | ![Safari](https://raw.github.com/alrra/browser-logos/master/src/safari/safari_48x48.png) | ![Opera](https://raw.github.com/alrra/browser-logos/master/src/opera/opera_48x48.png) | ![Edge](https://raw.github.com/alrra/browser-logos/master/src/edge/edge_48x48.png) | ![IE](https://raw.github.com/alrra/browser-logos/master/src/archive/internet-explorer_9-11/internet-explorer_9-11_48x48.png) |
| ---------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| Latest ✔                                                                                 | Latest ✔                                                                                    | Latest ✔                                                                                 | Latest ✔                                                                              | Latest ✔                                                                           | 10+ ✔                                                                                                                        |

## Suggestion

* 为了保证代码规范和可维护性，以及一些语法不规范造成的常见报错,请勿必参考[vue 风格指南](https://cn.vuejs.org/v2/style-guide/)

## Features

* 基于[vue(v2.5.13)](https://github.com/vuejs)，[element-ui(v2.13.2)](https://github.com/ElemeFE/element)，[mockJs](https://github.com/nuysoft/Mock) 等实现的单屏单页面应用项目模板。
* 基于[vue-router(v3.0)](https://github.com/vuejs/vue-router)来实现动态加载 model。
* 基于[vuex](https://github.com/vuejs/vuex)来实现集中式存储管理应用的所有组件的状态。
* 使用[webpack](https://github.com/webpack/webpack)本地调试和构建，其中 mock 功能实现模拟数据请求。
* Visual Studio Code 工具，eslint 插件开发统一规范代码。

# 功能介绍
# 1、首页
* 展示周期、负责人相关计划及用例数图表

# 2、设置

### 2.1 菜单管理
* 可新增、编辑、删除菜单或控件
  * 菜单需要配置：菜单URL，如：testlibrary，点击可跳转到对应页面
  * 控件需要配置：控件标识，如：testlibrary/move，在前端对应代码中添加如下代码：v-permission="'testlibrary/move'"，可用作权限分配

### 2.2 角色管理
* 可新增、编辑、删除角色；
* 根据不同角色可自定义分配权限，权限可配置到每个控件

### 2.3 操作员管理
* 可新增、编辑、删除操作员；

### 2.4 安全中心
* 可修改登录密码；

# 3、用例库

### 3.1 模块树
* 树形展示模块节点，支持新增、编辑、删除

### 3.2 导入用例
* 支持xls、xmind格式，模板需上传到自己的文件服务器。模板及其操作指南：/test-case/src/main/resources/template/

### 3.3 导出用例
* 导出xls格式，不支持导出根节点数据

### 3.4 批量移动、复制用例
* 支持批量移动、复制用例

### 3.5 删除用例
* 删除用例

### 3.6 新建用例
* 新建用例及其用例步骤，支持上传文件

# 4、执行计划

### 新建执行计划
* 新建、编辑、删除执行计划

### 搜索执行计划
* 搜索执行计划；默认显示与我相关，支持搜索所有执行计划

## Build Setup

```bash
# 克隆项目
git clone http://****

# 移出后台工程
将test-case（后台服务）整个目录移出克隆的项目，独立部署

# 安装依赖
npm install

# 本地开发 启动项目
npm run dev
```
