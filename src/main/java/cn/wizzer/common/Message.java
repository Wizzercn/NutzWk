package cn.wizzer.common;

import org.nutz.mvc.Mvcs;

import javax.servlet.http.HttpServletRequest;

public class Message {

	private Type type;
	private String content;
	private Object data;

	public enum Type {
		success, warn, error;
	}

	public Message() {
	}

	public Message(Message.Type type, String content,Object data, HttpServletRequest req) {
		this.type = type;
		this.content = Mvcs.getMessage(req, content);
		this.data=data;
	}

	public static Message success(String content,HttpServletRequest req) {
		return new Message(Message.Type.success, content,null,req);
	}

	public static Message success(String content,Object data,HttpServletRequest req) {
		return new Message(Message.Type.success, content,data,req);
	}

	public static Message warn(String content,HttpServletRequest req) {
		return new Message(Message.Type.warn, content,null, req);
	}

	public static Message error(String content,HttpServletRequest req) {
		return new Message(Message.Type.error, content,null, req);
	}

	public Message.Type getType() {
		return this.type;
	}

	public void setType(Message.Type type) {
		this.type = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
