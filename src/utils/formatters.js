import moment from 'moment';

function formatDate(time, format) {
  const defaultFormat = format || 'YYYY-MM-DD';
  if (time !== '' && time !== null && time !== undefined) {
    return moment(time).format(defaultFormat);
  }
}
function formatTime(time, format) {
  const defaultFormat = format || 'YYYY-MM-DD HH:mm:ss';
  if (time !== '' && time !== null && time !== undefined) {
    return moment(time).format(defaultFormat);
  }
}

function stringfyPhoneNum(phoneNum) {
  if (phoneNum) {
    return phoneNum.substr(0, 3) + '****' + phoneNum.substr(7, 4);
  }
  return '';
}


/**
 * 数字转换千位符
 */
function numFormat(num) {
  if (num === null || num === undefined) {
    return '';
  }
  return String(num).replace(/,/g, '')
    .replace(/(\d+\.?\d+)|\d+/g, n => Number(n).toFixed(2))
    .replace(/\d+/g, n => n.replace(/(\d)(?=(\d{3})+$)/g, $1 => $1 + ','));
}

/**
 * 金额格式化转换成元
 * @param amount 金额
 * @param unit 金额单位
 */
function moneyFormat(amount, unit) {
  if (!unit || unit.toUpperCase() === 'FEN') {
    return Number(amount / 100).toFixed(2);
  }
  if (unit.toUpperCase() === 'YUAN') {
    return Number(amount).toFixed(2);
  }
}

/**
 * 数组去重
 * 实现思路：获取没重复的最右一值放入新数组。
 * （检测到有重复值时终止当前循环同时进入顶层循环的下一轮判断）
 * @param array
 */
function uniq(array) {
  let temp = [];
  let index = [];
  let l = array.length;
  for (let i = 0; i < l; i++) {
    for (let j = i + 1; j < l; j++) {
      if (array[i] === array[j]) {
        i++;
        j = i;
      }
    }
    temp.push(array[i]);
    index.push(i);
  }
  console.log(">>>>>>>>>>>>>>>>>>>>"+index);
  console.log(">>>>>>>>>>>>>>>>>>>>"+temp);
  return temp;
}

export default {
  formatDate,
  formatTime,
  stringfyPhoneNum,
  numFormat,
  moneyFormat,
  uniq,
};
