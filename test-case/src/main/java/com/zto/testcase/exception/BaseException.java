package com.zto.testcase.exception;

import com.zto.testcase.enums.ErrorCodeEnum;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -7339281910096541147L;

    /**
     * 异常错误代码，使用4位字符串， 第一位代表产生异常的系统代码 后三位代表具体的错误代码含义 错误代码由具体的常量定义
     */
    protected String errorCode;

    /**
     * 异常错误信息，由实际抛出异常的类定义
     */
    protected String errorMsg;

    /**
     * 根异常，保持异常链
     */
    protected Throwable caused;

    public BaseException(String errorCode, String errorMsg) {
        super(errorMsg);

        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorCode, Throwable caused) {
        super(caused);

        this.errorCode = errorCode;
        this.caused = caused;
    }

    public BaseException(String errorCode, String errorMsg, Throwable caused) {
        super(errorMsg, caused);

        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.caused = caused;
    }

    public BaseException(ErrorCodeEnum resultEnum) {
        this.errorCode = resultEnum.getErrorCode();
        this.errorMsg = resultEnum.getErrorMsg();
    }

    /**
     * 获得异常的错误代码
     *
     * @return the errorCode
     */
    public String getErrorCode() {
        /** 如果异常定义了错误代码 */
        if (errorCode != null && !"".equals(errorCode.trim())) {
            return errorCode;
        }

        /**
         * 如果没有定义错误代码,并且该异常是一个间接异常 则返回根异常的错误代码
         */
        if (caused != null) {
            if (caused instanceof BaseException) {
                BaseException causedException = (BaseException) caused;
                return causedException.getErrorCode();
            } else {
                return errorCode;
            }
        }

        return errorCode;
    }

    /**
     * 设置异常的错误代码
     *
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 获得异常的错误信息
     *
     * @return the errorMsg
     */
    public String getErrorMsg() {
        /** 如果异常定义了错误信息 */
        if (errorMsg != null && !"".equals(errorMsg)) {
            return errorMsg;
        }

        /**
         * 如果没有定义错误信息,并且该异常是一个间接异常 则返回根异常的错误信息
         */
        if (caused != null) {
            if (caused instanceof BaseException) {
                BaseException causedException = (BaseException) caused;
                return causedException.getErrorMsg();
            } else {
                return errorMsg;
            }
        }

        return errorMsg;
    }

    /**
     * 设置异常的错误信息
     *
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * 获得根异常
     *
     * @return the caused
     */
    public Throwable getCaused() {
        return caused;
    }

    /**
     * 设置根异常
     *
     * @param caused the caused to set
     */
    public void setCaused(Throwable caused) {
        this.caused = caused;
    }


}
