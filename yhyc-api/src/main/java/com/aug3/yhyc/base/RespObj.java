package com.aug3.yhyc.base;

import java.io.Serializable;

public class RespObj implements Serializable {

	private String code;

	private String type;

	private Object message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public void setType(String type) {
		this.type = type;
	}
}
