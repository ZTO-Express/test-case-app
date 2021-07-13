package com.zto.testcase.exception;

import com.zto.testcase.enums.ErrorCodeEnum;

/**
 * @Author: wxc
 * @Description:
 * @Date: Create in 上午9:35 2021/4/23
 */
public class TemplateException extends BaseException {
    public TemplateException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public TemplateException(String errorCode, Throwable caused) {
        super(errorCode, caused);
    }

    public TemplateException(String errorCode, String errorMsg, Throwable caused) {
        super(errorCode, errorMsg, caused);
    }

    public TemplateException(ErrorCodeEnum resultEnum) {
        super(resultEnum);
    }
}
