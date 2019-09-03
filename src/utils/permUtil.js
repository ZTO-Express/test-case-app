import appData from '../appData';

function hasPerm(suspect) {
  if (appData.userInfo.roles[0].systemRole === '1') {
    return true;
  }
  return appData.pms[suspect];
}

export default {
  hasPerm,
};
