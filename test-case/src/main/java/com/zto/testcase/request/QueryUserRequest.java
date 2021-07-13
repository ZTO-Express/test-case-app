package com.zto.testcase.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class QueryUserRequest {
    @Length(min = 0, max = 50)
    private String userName;
    @Length(min = 0, max = 50)
    private String userId;
    @NotEmpty
    @Length(min = 0, max = 50)
    private String appId;

    @Length(min = 0, max = 20)
    private String userStatus;

    @Length(min = 0, max = 20)
    private String mobile;

}
