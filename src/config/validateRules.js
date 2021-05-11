/*
 * 所有表单需要的验证规则
 * 复杂的验证规则在util中的validate.js可以创建符合返回callback的函数
 * 只要el-form-item prop等于以下的验证规则并且开启表单的规则验证:rules=指定validateXXX"即可
 * 为了防止污染页面上的data减少创建不必要数据建议创建多个常量来引入到指定的页面中
 */
export const strRegex = '^((https|http|ftp|rtsp|mms)?://)' +
  '?(([0-9a-zA-Z_!~*().&=+$%-]+: )?[0-9a-zA-Z_!~*().&=+$%-]+@)?' + '(([0-9]{1,3}.){3}[0-9]{1,3}' + '|' + '([0-9a-zA-Z_!~*()-]+.)*' + '([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z].' + '[a-z]{2,6})' + '(:[0-9]{1,4})?' + '((/?)|' + '(/[0-9a-zA-Z_!~*().;?:@&=+$,%#-]+)+/?)$'
export const validateLogin = {
  password: [{
    required: true,
    message: '密码必须填写',
    trigger: 'blur'
  }],
  userName: [{
    required: true,
    message: '用户名不能为空',
    trigger: 'blur'
  }],
  // 用户-组 创建用户
  createName: [{
    required: true,
    message: '不能为空',
    trigger: 'blur'
  }],
  options: [{
    required: true,
    message: '请选择',
    trigger: 'change'
  }],
  tel: [{
    required: true,
    message: '不能为空',
    trigger: 'blur'
  },
  {
    pattern: /^1[34578]\d{9}$/,
    message: '手机号码有误，请重填',
    trigger: 'blur'
  }
  ],
  email: [{
    required: true,
    message: '不能为空',
    trigger: 'blur'
  },
  {
    pattern: /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/,
    message: '格式有误，请重填',
    trigger: 'blur'
  }
  ],
  lengthCheck: [{
    required: true,
    message: '请输入',
    trigger: 'blur'
  },
  {
    min: 1,
    max: 100000,
    message: '长度在 1 到 100000 个字符',
    trigger: 'blur'
  }
  ],
  lengthCheckLarger: [{
    required: true,
    message: '请输入',
    trigger: 'blur'
  },
  {
    min: 1,
    max: 3000,
    message: '长度在 1 到 3000 个字符',
    trigger: 'blur'
  }
  ],
  url: [{
    pattern: strRegex,
    message: 'url格式有误,请重新输入',
    trigger: 'blur'
  }],
  numbedr: [{
    pattern: /^[1-9]\d*$/,
    message: '输入格式不正确',
    trigger: 'blur'
  }],
  ip: [{
    required: true,
    message: '不能为空',
    trigger: 'blur'
  },
  {
    pattern: /^((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^([\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^::([\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^([\da-fA-F]{1,4}:):([\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^([\da-fA-F]{1,4}:){2}:([\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^([\da-fA-F]{1,4}:){3}:([\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^([\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$|^([\da-fA-F]{1,4}:){7}[\da-fA-F]{1,4}$|^:((:[\da-fA-F]{1,4}){1,6}|:)$|^[\da-fA-F]{1,4}:((:[\da-fA-F]{1,4}){1,5}|:)$|^([\da-fA-F]{1,4}:){2}((:[\da-fA-F]{1,4}){1,4}|:)$|^([\da-fA-F]{1,4}:){3}((:[\da-fA-F]{1,4}){1,3}|:)$|^([\da-fA-F]{1,4}:){4}((:[\da-fA-F]{1,4}){1,2}|:)$|^([\da-fA-F]{1,4}:){5}:([\da-fA-F]{1,4})?$|^([\da-fA-F]{1,4}:){6}:$/,
    message: '格式有误，请输入有效的ipv4/ipv6',
    trigger: 'blur'
  }
  ],
  port: [{
    required: true,
    message: '不能为空',
    trigger: 'blur'
  },
  {
    pattern: /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,
    message: '请输入0到65535的整数',
    trigger: 'blur'
  }
  ],
  name: [{
    required: true,
    message: '名称不能为空',
    trigger: ['change']
  }]
}
