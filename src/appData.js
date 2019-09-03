import sessionUtil from './utils/sessionUtil';
// 初始化菜单状态
let sidebarIscollapse = sessionUtil.get(sessionUtil.SIDEBAR_ISCOLLAPSE);
if (undefined === sidebarIscollapse || sidebarIscollapse === null) {
  sidebarIscollapse = false;
} else sidebarIscollapse = sidebarIscollapse === 'true';

export default {
  pms: {},
  menus: [],
  userInfo: {},
  sidebar: {
    isCollapse: sidebarIscollapse,
    routerMenuMap: {},
    menuMap: {},
  },
};
