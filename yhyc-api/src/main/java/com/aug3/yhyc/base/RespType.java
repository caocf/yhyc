package com.aug3.yhyc.base;

/**
 * 正常返回code：200-299 重定向code：300-399 客户端错误code：400-499 服务器错误code：500-599
 * 通用业务code：10100-10199 权限code：10200-10299 oauth认证code：10300-10399
 * 
 * @author roger
 * 
 */
public enum RespType {

	// Login
	LOGIN_SUCCESS("998"),

	LOGIN_FAILED("0998"),

	USER_EXIST("990"),

	INVALID_PASSWD("997"),

	NO_PERMISSION("1000"),

	RUNTIME_EXCEPTION("1001"),

	INVALID_PARAMETERS("1002"),

	SUCCESS("200"),

	// REDIRECT_PERMANTENTLY("301"),
	//
	// REDIRECT_PROXY_REQUIRED("305"),
	//
	// REDIRECT_TEMMPORARILY("307"),
	//
	// BAD_REQUEST("400"),
	//
	// UNAUTHORIZED_REQUEST("401"),
	//
	// FORBIDDEN_REQUEST("403"),
	//
	// NOT_FOUND("404"),
	//
	// METHOD_NOT_ALLOWED("405"),
	//
	// PROXY_UNAUTHORIZED_REQUEST("407"),
	//
	// REQUEST_TIMEOUT("408"),
	//
	// INTERNAL_SERVER_ERROR("500"),
	//
	// BAD_GATEWAY("502"),
	//
	// SERVICE_UNAVAILABLE("503"),
	//
	// GATEWAY_TIMEOUT("504"),
	//
	// HTTP_VERSION_NOT_SUPPORTED("505"),

	;

	private String code;

	private RespType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
