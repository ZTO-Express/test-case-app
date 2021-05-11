import vkbeautify from 'vkbeautify'
export default {
  formatXml(xml) {
    return vkbeautify.xml(xml)
  },
  formatXml1(text) {
    // 去掉多余的空格
    text = '\n' + text.replace(/(<\w+)(\s.*?>)/g, function($0, name, props) {
      return name + ' ' + props.replace(/\s+(\w+=)/g, ' $1')
    }).replace(/>\s*?</g, '>\n<')
    // 把注释编码
    text = text.replace(/\n/g, '\r').replace(/<!--(.+?)-->/g, function($0, text) {
      var ret = '<!--' + escape(text) + '-->'
      return ret
    }).replace(/\r/g, '\n')

    // 调整格式
    var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg
    var nodeStack = []
    var output = text.replace(rgx, ($0, all, name, isBegin, isCloseFull1, isCloseFull2, isFull1, isFull2) => {
      var isClosed = (isCloseFull1 === '/') || (isCloseFull2 === '/') || (isFull1 === '/') || (isFull2 === '/')
      var prefix = ''
      if (isBegin === '!') {
        prefix = this.getPrefix(nodeStack.length)
      } else {
        if (isBegin !== '/') {
          prefix = this.getPrefix(nodeStack.length)
          if (!isClosed) {
            nodeStack.push(name)
          }
        } else {
          nodeStack.pop()
          prefix = this.getPrefix(nodeStack.length)
        }
      }
      var ret = '\n' + prefix + all
      return ret
    })
    var outputText = output.substring(1)
    // 把注释还原并解码
    outputText = outputText.replace(/\n/g, '\r').replace(/(\s*)<!--(.+?)-->/g, function($0, prefix, text) {
      if (prefix.charAt(0) === '\r') {
        prefix = prefix.substring(1)
      }
      text = unescape(text).replace(/\r/g, '\n')
      var ret = '\n' + prefix + '<!--' + text.replace(/^\s*/mg, prefix) + '-->'
      return ret
    })
    return outputText.replace(/\s+$/g, '').replace(/\r/g, '\r\n')
  },
  getPrefix(prefixIndex) {
    var span = '    '
    var output = []
    for (var i = 0; i < prefixIndex; ++i) {
      output.push(span)
    }
    return output.join('')
  },
  validateXML(xmlContent) {
    // errorCode 0是xml正确，1是xml错误，2是无法验证
    var [xmlDoc, errorMessage, errorCode] = [0, 0, 0]
    // code for IE
    // code for Mozilla, Firefox, Opera, chrome, safari,etc.
    if (document.implementation.createDocument) {
      var parser = new DOMParser()
      xmlDoc = parser.parseFromString(xmlContent, 'text/xml')
      var error = xmlDoc.getElementsByTagName('parsererror')
      if (error.length > 0) {
        if (xmlDoc.documentElement.nodeName === 'parsererror') {
          errorCode = 1
          errorMessage = xmlDoc.documentElement.childNodes[0].nodeValue
        } else {
          errorCode = 1
          errorMessage = xmlDoc.getElementsByTagName('parsererror')[0].innerHTML
        }
      } else {
        errorMessage = '格式正确'
      }
    } else {
      errorCode = 2
      errorMessage = '浏览器不支持验证，无法验证xml正确性'
    }
    return {
      'msg': errorMessage,
      'error_code': errorCode
    }
  },
  formatJson(json, options) {
    var reg = null
    var formatted = ''
    var pad = 0
    var PADDING = '    '
    // optional settings
    options = options || {}
    // remove newline where '{' or '[' follows ':'
    options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true)
    // use a space after a colon
    options.spaceAfterColon = options.spaceAfterColon !== false

    // begin formatting...

    // make sure we start with the JSON as a string
    if (typeof json !== 'string') {
      json = JSON.stringify(json)
    }
    // parse and stringify in order to remove extra whitespace
    // json = JSON.stringify(JSON.parse(json));可以除去多余的空格
    json = JSON.parse(json)
    json = JSON.stringify(json)
    // add newline before and after curly braces
    reg = /([\{\}])/g
    json = json.replace(reg, '\r\n$1\r\n')
    // add newline before and after square brackets
    reg = /([\[\]])/g
    json = json.replace(reg, '\r\n$1\r\n')
    // add newline after comma
    reg = /(\,)/g
    json = json.replace(reg, '$1\r\n')
    // remove multiple newlines
    reg = /(\r\n\r\n)/g
    json = json.replace(reg, '\r\n')
    // remove newlines before commas
    reg = /\r\n\,/g
    json = json.replace(reg, ',')
    // optional formatting...
    if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
      reg = /\:\r\n\{/g
      json = json.replace(reg, ':{')
      reg = /\:\r\n\[/g
      json = json.replace(reg, ':[')
    }
    if (options.spaceAfterColon) {
      reg = /\:/g
      json = json.replace(reg, ': ')
    }
    $.each(json.split('\r\n'), function(index, node) {
      var i = 0
      var indent = 0
      var padding = ''

      if (node.match(/\{$/) || node.match(/\[$/)) {
        indent = 1
      } else if (node.match(/\}/) || node.match(/\]/)) {
        if (pad !== 0) {
          pad -= 1
        }
      } else {
        indent = 0
      }
      for (i = 0; i < pad; i++) {
        padding += PADDING
      }
      formatted += padding + node + '\r\n'
      pad += indent
    })
    return formatted
  }
}
