package com.dr.framework.common.entity;

import org.springframework.util.StringUtils;

/**
 * 用来封装返回数据的
 */
public class ResultEntity {
    /**
     * 200	请求成功
     * 400	缺少请求参数
     * 401	token校验失败，非法请求
     * 403	用户未登录
     * 500	服务器内部错误
     */
    private String code;
    private String message;
    private boolean success;
    private Object data;

    public static ResultEntity success() {
        return new ResultEntity(true, null, null);
    }

    public static ResultEntity success(Object data) {
        return new ResultEntity(true, null, data);
    }


    public static ResultEntity success(String message, Object data) {
        return new ResultEntity(true, message, data);
    }

    public static ResultEntity error(String msg) {
        return new ResultEntity(false, msg, null);
    }

    public static ResultEntity error(Object data) {
        return new ResultEntity(false, null, data);
    }

    public static ResultEntity error(String message, Object data) {
        return new ResultEntity(false, message, data);
    }

    public static ResultEntity error(String message, String code, Object data) {
        return new ResultEntity(false, message, code, data);
    }


    public ResultEntity(boolean success, String message, Object data) {
        this.success = success;
        if (success) {
            this.code = "200";
        } else {
            this.code = "500";
        }
        if (StringUtils.isEmpty(message)) {
            if (success) {
                this.message = "请求成功！";
            } else {
                this.code = "请求失败，服务器内部错误！";
            }
        } else {
            this.message = message;
        }
        this.data = data;
    }

    public ResultEntity(boolean success, String message, String code, Object data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
