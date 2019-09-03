import appData from '../appData';

function get(key) {
  return window.localStorage.getItem(key);
}
function set(key, value) {
  window.localStorage.setItem(key, value);
}
function rm(key) {
  return window.localStorage.removeItem(key);
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

export default {
  get,
  set,
  rm,
  userGet,
  userSet,
  userRm,
};

