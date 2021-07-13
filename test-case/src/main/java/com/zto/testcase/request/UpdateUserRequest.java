package com.zto.testcase.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateUserRequest extends BaseUserRequest {
    /**
     * 修改前的现密码
     **/
    @Length(min = 0, max = 50)
    private String oldUserPwd;
}
