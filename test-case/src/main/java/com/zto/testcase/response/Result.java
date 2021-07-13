package com.zto.testcase.response;

import com.zto.testcase.enums.ErrorCodeEnum;
import java.io.Serializable;
import lombok.Data;

@Data
public class Result<R> implements Serializable {

    private String code;

    private String msg;

    private R data;

    public static <R> Result<R> success(R data) {
        Result<R> result = new Result<>();
        result.setCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
        result.setMsg(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
        result.setData(data);
        return result;
    }

    public static <R> Result<R> success() {
        Result<R> result = new Result<>();
        result.setCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
        result.setMsg(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorMsg());
        return result;
    }

    public static <R> Result<R> successMsg(String msg) {
        Result<R> result = new Result<>();
        result.setCode(ErrorCodeEnum.SYSTEM_SUCCESS.getErrorCode());
        result.setMsg(msg);
        return result;
    }

    public static <R> Result<R> error(String code, String msg) {
        Result<R> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <R> Result<R> error(ErrorCodeEnum errorCodeEnum) {
        Result<R> result = new Result<>();
        result.setCode(errorCodeEnum.getErrorCode());
        result.setMsg(errorCodeEnum.getErrorMsg());
        return result;
    }

    public static <R> Result<R> throwable(String code, Throwable throwable) {
        Result<R> result = new Result<>();
        result.setCode(code);
        result.setMsg(throwable.getClass().getName() + ", " + throwable.getMessage());
        return result;
    }

}
