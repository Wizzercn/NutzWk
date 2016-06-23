package cn.wizzer.common.base;

import org.nutz.mvc.Mvcs;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by wizzer on 2016/6/22.
 */
public class Result {

	private int code;
	private String msg;
	private Object data;

	public Result() {
	}

	public Result(int code, String msg, Object data, HttpServletRequest req) {
		this.code = code;
		this.msg = Mvcs.getMessage(req, msg);
		this.data=data;
	}

	public static Result success(String content,HttpServletRequest req) {
		return new Result(0, content,null,req);
	}

	public static Result success(String content,Object data,HttpServletRequest req) {
		return new Result(0, content,data,req);
	}

	public static Result error(int code,String content,HttpServletRequest req) {
		return new Result(code, content,null, req);
	}

	public static Result error(String content,HttpServletRequest req) {
		return new Result(1, content,null, req);
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
