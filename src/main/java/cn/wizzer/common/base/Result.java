package cn.wizzer.common.base;

import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

/**
 * Created by wizzer on 2016/6/22.
 */
public class Result {

    private int code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = Strings.isBlank(msg) ? "" : Mvcs.getMessage(Mvcs.getActionContext().getRequest(), msg);
        this.data = data;
    }

    public static Result success(String content) {
        return new Result(0, content, null);
    }

    public static Result success(String content, Object data) {
        return new Result(0, content, data);
    }

    public static Result error(int code, String content) {
        return new Result(code, content, null);
    }

    public static Result error(String content) {
        return new Result(1, content, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
