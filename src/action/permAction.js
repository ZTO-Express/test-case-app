import urlConst from '../utils/urlConst';
import axiosUtil from '../utils/axiosUtil';
import appConfig from '../appConfig';
import appData from '../appData';

// 用户列表
function findUsers() {
  return axiosUtil.post(appConfig.SSO, urlConst.FIND_USERS, {
    org: appData.userInfo.org,
  });
}

// 用户更新
function updateUser(user, mode) {
  const data = Object.assign({}, user);

  if (data.userPwd) {
    /* eslint-disable */
    data.userPwd = new Buffer(data.userPwd).toString('base64');
  }

  // 如果为空则去掉对象属性
  if (mode === 'update') {
    return axiosUtil.post(appConfig.SSO, urlConst.UPDATE_USER, data);
  } else if (mode === 'create') {
    data.operUserId= appData.userInfo.userId;
    return axiosUtil.post(appConfig.SSO, urlConst.CREATE_USER, data);
  }
  return null;
}

// 删除用户
function deleteUser(user) {
  return axiosUtil.post(appConfig.SSO, urlConst.DELETE_USER, user);
}

// 角色列表
function findRoles(userId) {
  const data = {userId };
  return axiosUtil.post(appConfig.SSO, urlConst.FIND_ROLES, data);
}

function updateRole(role, mode, userId) {
  const data = role;
  if (mode === 'update') {
    return axiosUtil.post(appConfig.SSO, urlConst.UPDATE_ROLE, data);
  } else if (mode === 'create') {
    data.userId = userId;

    return axiosUtil.post(appConfig.SSO, urlConst.CREATE_ROLE, data);
  }
  return null;
}

// 删除列表
function deleteRole(role) {
  return axiosUtil.post(appConfig.SSO, urlConst.DELETE_ROLE, role);
}

// 绑定菜单
function bandMenu(role) {
  return axiosUtil.post(appConfig.SSO, urlConst.BAND_MENU, role);
}

// 菜单列表
function getMenuTree() {
  return axiosUtil.post(appConfig.SSO, urlConst.MENU_TREE, {});
}
function getMenuTreeByRole(roleCode) {
  const data = { roleCode };
  return axiosUtil.post(appConfig.SSO, urlConst.MENU_TREE_BY_ROLE, data);
}
function getUiTreeByRole(roleCode) {
  const data = { roleCode };
  return axiosUtil.post(appConfig.SSO, urlConst.UI_META_TREE, data);
}

function getPmsTreeByRoleCode(roleCode) {
    const data = { roleCode };
    return axiosUtil.post(appConfig.SSO, urlConst.PMS_BY_ROLE, data);
}

// 控件树
function getUiMetaTree() {
  return axiosUtil.post(appConfig.SSO, urlConst.UI_META_TREE, {});
}

// 更新菜单
function updateMenu(menu) {
  const data = {};
  Object.assign(data, menu);
  data.children = null;
  data.child = null;
  data.father = null;
  return axiosUtil.post(appConfig.SSO, urlConst.MENU_UPDATE, data);
}

// 添加菜单
function createMenu(menu) {
  const data = {};
  Object.assign(data, menu);
  data.children = null;
  data.child = null;
  data.father = null;
  return axiosUtil.post(appConfig.SSO, urlConst.MENU_CREATE, data);
}

// 删除菜单
function deleteMenu(id) {
  const data = { id };
  return axiosUtil.post(appConfig.SSO, urlConst.MENU_DELETE, data);
}

// 更新控件
function updateUiMeta(id, controlId, controlName, controlType, menuCode) {
  const data = {
    id, controlId, controlName, controlType, menuCode,
  };
  return axiosUtil.post(appConfig.SSO, urlConst.UI_META_UPDATE, data);
}

function createUiMeta(uiMeta) {
  return axiosUtil.post(appConfig.SSO, urlConst.UI_META_CREATE, uiMeta);
}

function deleteUiMeta(id) {
  const data = { id };
  return axiosUtil.post(appConfig.SSO, urlConst.UI_META_DELETE, data);
}

function updateSlr(id, endParentCode, draggingMenuCode, preMenuCode, aftMenuCode) {
  const data = {
    id, parentCode: endParentCode, menuCode: draggingMenuCode, preMenuCode, aftMenuCode,
  };
  return axiosUtil.post(appConfig.SSO, urlConst.SLR_UPDATE, data);
}

function batchBandUiMeta(params) {
  return axiosUtil.post(appConfig.SSO, urlConst.UI_META_BATCH_CREATE, params);
}

function delUiMetaByRoleCode(roleCode) {
  const data = { roleCode };
  return axiosUtil.post(appConfig.SSO, urlConst.UI_META_DELETE_BY_ROLECODE, data);
}

export default {
  findUsers,
  findRoles,
  updateUser,
  deleteUser,
  getMenuTree,
  getUiMetaTree,
  deleteRole,
  updateRole,
  bandMenu,
  updateMenu,
  createMenu,
  deleteMenu,
  updateUiMeta,
  createUiMeta,
  deleteUiMeta,
  updateSlr,
  batchBandUiMeta,
  getMenuTreeByRole,
  getUiTreeByRole,
  getPmsTreeByRoleCode,
  delUiMetaByRoleCode,
};
