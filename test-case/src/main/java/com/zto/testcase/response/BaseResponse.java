package com.zto.testcase.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse {

    private String respCode;

    private String respMessage;

    public BaseResponse() {

    }

    public BaseResponse(String respCode, String respMessage) {
        this.respCode = respCode;
        this.respMessage = respMessage;
    }
}
