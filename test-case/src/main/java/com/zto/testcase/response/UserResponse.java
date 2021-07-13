package com.zto.testcase.response;

import lombok.Data;

@Data
public class UserResponse extends BaseResponse {

    public UserResponse() {
        super();
    }

    public UserResponse(String respCode, String respMessage) {
        super(respCode, respMessage);
    }

    private String data;
}
