package com.budwk.app.base.result;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/12/21.
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private Object data;
    private long time;

    public Result() {
        this.time = System.currentTimeMillis();
    }

    public static Result NEW() {
        return new Result();
    }


    public Result addCode(int code) {
        this.code = code;
        return this;
    }

    public Result addMsg(String msg) {
        if (Strings.isBlank(msg) || Mvcs.getActionContext() == null || Mvcs.getActionContext().getRequest() == null || Mvcs.getMessage(Mvcs.getActionContext().getRequest(), msg) == null) {
            this.msg = Strings.sNull(msg);
        } else {
            this.msg = Mvcs.getMessage(Mvcs.getActionContext().getRequest(), msg);
        }
        return this;
    }

    public Result addData(Object data) {
        this.data = data;
        return this;
    }

    public Result(int code, String msg, Object data) {
        this.code = code;
        if (Strings.isBlank(msg) || Mvcs.getActionContext() == null || Mvcs.getActionContext().getRequest() == null || Mvcs.getMessage(Mvcs.getActionContext().getRequest(), msg) == null) {
            this.msg = Strings.sNull(msg);
        } else {
            this.msg = Mvcs.getMessage(Mvcs.getActionContext().getRequest(), msg);
        }
        this.data = data;
        this.time = System.currentTimeMillis();
    }

    public static Result success(String content) {
        return new Result(0, content, null);
    }

    public static Result success(String content, Object data) {
        return new Result(0, content, data);
    }

    public static Result success(Object data) {
        return new Result(0, "system.success", data);
    }

    public static Result error(int code, String content) {
        return new Result(code, content, null);
    }

    public static Result error(String content) {
        return new Result(1, content, null);
    }

    public static Result success() {
        return new Result(0, "system.success", null);
    }

    public static Result error() {
        return new Result(1, "system.error", null);
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public static Result condition(boolean flag) {
        return flag ? success("system.error") : error("system.error");
    }

    public String toJsonString() {
        return Json.toJson(this, JsonFormat.compact());
    }

}
