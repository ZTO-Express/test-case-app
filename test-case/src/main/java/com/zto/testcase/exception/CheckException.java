package com.zto.testcase.exception;

import com.zto.testcase.enums.FieldChinaeseEnum;

public class CheckException extends RuntimeException {

    protected String respCode;
    protected String respMessage;

    public CheckException(FieldChinaeseEnum fieldChinaeseEnum) {
        respCode = fieldChinaeseEnum.getErrorCode();
        respMessage = fieldChinaeseEnum.getChinaese();
    }
}
