import appData from '../appData';

const ACCESS_TOKEN = 'access-token';
const SIDEBAR_ISCOLLAPSE = 'sidebar_iscollapse';
const VISITED_VIEWS = 'visited_views';
const GROUP_DETAIL_SUBPOOL = 'GROUP_DETAIL_SUBPOOL';


function get(key) {
  return window.sessionStorage.getItem(key);
}
function set(key, value) {
  window.sessionStorage.setItem(key, value);
}
function rm(key) {
  return window.sessionStorage.removeItem(key);
}

function getKey(key) {
  const listQueryKey = appData.userInfo.userId;
  return listQueryKey + '.' + key;
}

function userSet(key, value) {
  set(getKey(key), value);
}

function userGet(key) {
  return get(getKey(key));
}

function userRm(key) {
  return rm(getKey(key));
}

function clear() {
  for (let i = window.sessionStorage.length - 1; i >= 0; i -= 1) {
    rm(window.sessionStorage.key(i));
  }
}

export default {
  ACCESS_TOKEN,
  SIDEBAR_ISCOLLAPSE,
  VISITED_VIEWS,
  GROUP_DETAIL_SUBPOOL,

  get,
  set,
  rm,
  userGet,
  userSet,
  userRm,

  clear,
};

