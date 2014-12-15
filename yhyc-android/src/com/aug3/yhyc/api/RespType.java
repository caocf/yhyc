package com.aug3.yhyc.api;

/**
 * 
 * @author roger
 * 
 */
public enum RespType {

	// Login
	LOGIN_SUCCESS("998"),

	LOGIN_FAILED("0998"),

	USER_EXIST("990"),

	DUPLICATE_RECORD("991"),

	INVALID_PASSWD("997"),

	NO_PERMISSION("1000"),

	RUNTIME_EXCEPTION("1001"),

	INVALID_PARAMETERS("1002"),

	SUCCESS("200"),

	FAILED("400"),

	;

	private String code;

	private RespType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
