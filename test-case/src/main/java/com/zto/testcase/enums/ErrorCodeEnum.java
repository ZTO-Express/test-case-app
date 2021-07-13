package com.zto.testcase.enums;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum ErrorCodeEnum {

    // 公共状态
    SYSTEM_SUCCESS("000000", "操作成功"),
    SYSTEM_EXCEPTION("-1", "系统异常"),
    PARAM_IS_EMPTY("100001", "请求参数不能为空"),
    DATA_IS_NOT_EXIST("100002", "数据不存在"),
    DATA_IS_EXIST("100003", "数据已存在"),
    MODULE_IS_NOT_EMPTY("100004", "请先删除子模块或模块中的用例"),
    REQUEST_PARAM_ERROR("100005", "必填项为空"),
    OPERATION_FAILURE("100006", "操作失败"),
    EXPORT_ERROR("100007", "导出失败"),
    CANNOT_EXPORT_ROOT_MODULE("100008", "不能导出根节点数据"),
    MODULE_IS_NULL("100009", "模块未选中,请选中模块"),

    // ding ding
    SEND_DING_MSG_EXCEPTION("200001", "发送钉钉消息异常"),
    PLAN_DETAIL_NOT_EXIST("200002", "所查询记录不存在"),

    /**
     * 账号或密码有误，请重试
     */
    ERROR_NAMEORPWD("F40205", "用户名或密码错误"),

    /**
     * 连续重试5次账号将锁定，还有X次机会
     */
    ERROR_LEFTCHANGE("F40204", "用户名或密码错误(连续重试{0}次账号将锁定，还有{1}次机会)"),

    /**
     * 该账号频繁重试已被锁定，请尝试取回密码后重试
     */
    ERROR_LOCKEDWHENLOGIN("F40206", "账户已被锁定，请尝试取回密码"),

    /**
     * 您没有分配角色，无法登陆
     */
    ERROR_EXITS_LOGIN_NAME("F40108", "该用户名已被注册", "loginName"),
    /**
     * 您没有分配角色，无法登陆
     */
    ERROR_EXITS_MOBILE("F40109", "该手机号已被注册", "mobile"),
    /**
     * 您没有分配角色，无法登陆
     */
    ERROR_EXITS_EMAIL("F40110", "该邮箱已被注册", "email"),
    /**
     * 您没有分配角色，无法登陆
     */
    ERROR_REQUIRED("F40101", "账户名不能为空"),


    /**
     * 对不起，您的账号已被停用
     */
    ERROR_LOGIN_NAME_NOT_EXITS("F40207", "对不起，您输入的账号不存在"),
    /**
     * 对不起，您的账号已被停用
     */
    ERROR_OLD_PASSWORD("F40307", "用户名或密码错误"),
    /**
     * 对不起，您的账号已被停用
     */
    ERROR_OLD_PASSWORD_SAME_NEW_PWD("F40306", "新老登录密码相同"),
    /**
     * F40308","参数格式错误"
     **/
    PARAMER_ERROR("F40308", "参数格式错误"),
    /**
     * F40308","参数格式错误"
     **/
    MOBILE_EXITS("F40309", "该手机号已存在"),
    /**
     * F40308","参数格式错误"
     **/
    EMAIL_EXITS("F40310", "该邮箱已存在"),
    /**
     * 对不起，您的账号已被停用
     */
    ERROR_NOT_DATA("F404", "该记录不存在"),

    ERROR_ROLE_EXITS("F41001", "角色已经存在"),
    ERROR_ROLE_NOT_EXITS("F41002", "角色不存在"),
    ERROR_USER_ID_NOT_EXITS("F41003", "用户编号不存在"),
    ERROR_ROLE_INACTIVE("F41004", "角色已禁用"),
    ERROR_USER_HAS_NO_ROLE("F41005", "用户没有分配可用角色"),
    ERROR_ROLE_HAS_USER("F41006", "该角色下已存在操作员,不允许删除"),
    ERROR_ROLE_HAS_SUBROLE("F41007", "该角色下已关联子角色,不允许删除"),

    ERROR_LOGIN_NAME_NOT_EXIST("F41008", "用户名错误"),

    ERROR_ARGS_FORMAT("F40001", "参数格式错误"),

    ERROR_INVALID("F401", "登录已经失效，请重新登录");

    private String errorCode;

    private String errorMsg;

    ErrorCodeEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static ErrorCodeEnum getErrorCodeEnum(String errorCode) {
        Optional<ErrorCodeEnum> op = Stream.of(ErrorCodeEnum.values()).filter(k -> k.getErrorCode().equals(errorCode))
                .findFirst();
        return op.orElse(SYSTEM_EXCEPTION);
    }

    private String val;

    private String msg;

    ErrorCodeEnum(String s, String msg, String fieldName) {
        this.val = s;
        this.msg = msg;
        this.fieldName = fieldName;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    public static ErrorCodeEnum getByFieldName(String fieldName) {
        Optional<ErrorCodeEnum> optionalResultCode = Arrays
                .stream(ErrorCodeEnum.values())
                .filter(resultCode -> Predicate.isEqual(resultCode.fieldName).test(fieldName)).findFirst();
        return optionalResultCode.isPresent() ? optionalResultCode.get() : null;
    }
    }
