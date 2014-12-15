package com.aug3.yhyc.api;

import java.io.Serializable;

public class Category implements Serializable{

	private int code;

	private String name;

	private int p;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

}
