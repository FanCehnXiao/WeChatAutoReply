package com.dbnewyouth.mingyue.utils.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Web接口统一返回结果
 * @author xinfeng
 * @since 2019-01-22
 */
public class Result implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	/** 业务状态码 */
	private static Map<String, String> map = new HashMap<String, String>();
	
	/** 200 操作成功 */
	public static String SUCC = "200";
	/** -1 操作失败 */
	public static String FAIL = "-1";
	
	static {
		map.put(SUCC, "成功");
		map.put(FAIL, "失败");
	}
	
	/** 状态码 */
	private String code = "";
	/** 状态描述 */
	private String message = "";
	/** 业务数据 */
	private Object data = "";

	/** 构造方法 */
	public Result() {
		super();
	}
	
	/**
	 * 构造方法
	 * @param code		状态码
	 */
	public Result(String code) {
		super();
		this.code = code;
		this.message = map.get(code);
	}
	
	/**
	 * 构造方法
	 * @param code		状态码
	 * @param message	状态描述
	 */
	public Result(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	/**
	 * 构造方法
	 * @param code		状态码
	 * @param message	状态描述
	 * @param data		业务数据
	 */
	public Result(String code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/** 状态码 */
	public String getCode() {
		return code;
	}

	/** 状态码 */
	public void setCode(String code) {
		this.code = code;
	}

	/** 状态描述 */
	public String getMessage() {
		return message;
	}

	/** 状态描述 */
	public void setMessage(String message) {
		this.message = message;
	}

	/** 业务数据 */
	public Object getData() {
		return data;
	}

	/** 业务数据 */
	public void setData(Object data) {
		this.data = data;
	}

	public static Result get(String code){
		return new Result(code);
	}
}
