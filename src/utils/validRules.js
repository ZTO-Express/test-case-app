import moment from 'moment';

const _ = (_type, _message, boolean = true) => {
  const type = _type || 'string';
  const message = _message || `请输入正确的${type}`;
  return {
    type: !boolean ? 'string' : type,
    message,
    trigger: 'blur',
  };
};

const required = (_message, boolean = true) => {
  const message = _message || '必填项';
  return {
    required: boolean,
    message,
    trigger: 'blur',
  };
};

const length = (len, msg, boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !Number(len) || !boolean) {
      callback();
      return;
    }
    if (value.length !== Number(len)) {
      callback(new Error(`请输入${len ? len + '位' : ''}${msg}`));
      return;
    }
    callback();
  },
  trigger: 'blur',
});

const number = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    if (!/^\d+(\.\d+)?$/.test(value)) {
      callback(new Error('请输入数字'));
      return;
    }
    callback();
  },
  trigger: 'blur',
});
const intNumber = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    const reg = new RegExp('^\\d+$');
    if (!reg.test(value)) {
      callback(new Error('请输入整数'));
    } else {
      callback();
    }
  },
  trigger: 'blur',
});
const amount = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    if (!/^\d+(\.\d+)?$/.test(value)) {
      callback(new Error('金额只能为正数数字'));
      return;
    }
    if (!/^\d+(?:\.\d{1,2})?$/.test(value)) {
      callback(new Error('金额最多有两位小数'));
      return;
    }
    callback();
  },
  trigger: 'blur',
});
const scale = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    if (!/^\d+(\.\d+)?$/.test(value)) {
      callback(new Error('只能为正数数字'));
      return;
    }
    if (!/^\d+(?:\.\d{1,2})?$/.test(value)) {
      callback(new Error('最多有两位小数'));
      return;
    }
    if (value > 100 || value < 0.01) {
      callback(new Error('最大不超过100，最小不低于0.01'));
      return;
    }
    callback();
  },
  trigger: 'blur',
});
const range = (min = 0, max = 100, boolean = true) => ({
  validator: (rule, value, callback) => {
    if (value > min || value < max || !boolean) {
      callback();
    } else {
      callback(new Error(`最大不超过${max}，最小不低于${min}`));
    }
  },
  trigger: 'blur',
});
const noValidator = {
  validator: (rule, value, callback) => {
    callback();
  },
  trigger: 'blur',
};
const mobile = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    const reg = new RegExp('^((13|14|15|17|18)[0-9]{1}\\d{8})$');
    if (!reg.test(value)) {
      callback(new Error('请输入正确手机号'));
    } else {
      callback();
    }
  },
  trigger: 'blur',
});
const ip = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    const reg = new RegExp('^[0-9]*\\.?[0-9]*\\.?[0-9]*\\.?[0-9]*\\.?$');
    const ipArr = value.split('#');
    for (let i = 0; i < ipArr.length; i++) {
      const result = ipArr[i];
      if (!reg.test(result)) {
        callback(new Error('请输入正确ip地址'));
        return;
      }
    }
    callback();
  },
  trigger: 'blur',
});
const url = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    const reg = new RegExp('^(https:\\/\\/|http:\\/\\/|ftp:\\/\\/|rtsp:\\/\\/|mms:\\/\\/){0,1}([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$');
    if (!reg.test(value)) {
      callback(new Error('请输入正确网址'));
    } else {
      callback();
    }
  },
  trigger: 'blur',
});
const idCardNum = (boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!value || !boolean) {
      callback();
      return;
    }
    const reg = new RegExp('^[0-9]*[x|X]?$');
    if (!reg.test(value) || value.length < 18) {
      callback(new Error('请输入身份证号'));
    } else {
      callback();
    }
  },
  trigger: 'blur',
});

const validEndTime = (startTime, endTime, refs, boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!boolean) {
      callback();
      return;
    }
    if (endTime) {
      const time = moment(endTime).diff(moment(startTime));
      if (time < 0) {
        callback(new Error('结束时间不能超过开始时间'));
        return;
      }
    }
    callback();
  },
  trigger: 'blur',
});

const validTimeRange = (startTime, endTime, rangeDay, boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!boolean) {
      callback();
      return;
    }
    if (startTime && endTime) {
      const timeGap = moment(endTime).diff(moment(startTime));
      if (timeGap > 86400000 * (rangeDay + 1)) {
        callback(new Error(`最大范围不超过${rangeDay}天`));
        return;
      }
    }
    callback();
  },
  trigger: 'blur',
});

const noCn = (msg = '不支持中文', boolean = true) => ({
  validator: (rule, value, callback) => {
    if (!boolean) callback();
    const reg = new RegExp('[\\u4E00-\\u9FFF]+', 'g');
    if (reg.test(value)) {
      callback(new Error(msg));
      return;
    }
    callback();
  },
  trigger: 'blur',
});

const rules = {
  _,
  required,
  number,
  intNumber,
  noValidator,
  mobile,
  ip,
  url,
  idCardNum,
  amount,
  scale,
  length,
  range,
  validEndTime,
  validTimeRange,
  noCn,
};

rules.$ = (rule, paramA, paramB, paramC) => {
  let returnRule = {};
  let a = paramA;
  let b = paramB;
  let c = paramC;
  if (typeof rule === 'function') {
    const name = rule.toString().match(/function\s*([^(]*)\(/)[1];
    returnRule = Object.assign({ name }, rule(a, b, c));
  }
  if (typeof rule === 'string') {
    if (rule[0] === '_') {
      if (a === false || a === true) {
        b = a;
        a = false;
      }
      const sRule = rule.substring(1);
      returnRule = Object.assign({ name: sRule }, rules._(sRule, a, b));
    } else if (rule === 'required') {
      if (a === false || a === true) {
        b = a;
        a = false;
      }
      returnRule = rules[rule](a, b);
    } else {
      if (a === false || a === true) {
        c = a;
        b = false;
      }
      returnRule = Object.assign({ name: rule }, rules[rule](a, b, c));
    }
  }
  return returnRule;
};
const $ = rules.$;


export { rules };
