package com.aug3.yhyc.service;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.rs.response.RespObj;
import com.aug3.sys.rs.response.RespType;
import com.aug3.sys.util.JSONUtil;
import com.google.gson.GsonBuilder;

public abstract class BaseService {

	/**
	 * 当操作成功并且不需要返回任何数据时使用，例如数据增加情况
	 */
	public static final RespObj SUCCESS = new RespObj();

	static {
		SUCCESS.setCode(RespType.SUCCESS.getCode());
		SUCCESS.setType(RespType.SUCCESS.name());
	}

	/**
	 * 
	 * @Title: buidResponseResult
	 * @Description:
	 * @param message
	 * @param responseTyp
	 * @return
	 */
	protected String buidResponseResult(Object message, RespType responseType) {
		RespObj returnDTO = new RespObj();
		returnDTO.setCode(responseType.getCode());
		returnDTO.setType(responseType.toString());
		returnDTO.setMessage(message);
		return JSONUtil.toJson(returnDTO);
	}

	protected String buidResponseResult(Object message, RespType responseType,
			boolean disableHtmlEscaping) {
		
		RespObj returnDTO = new RespObj();
		returnDTO.setCode(responseType.getCode());
		returnDTO.setType(responseType.toString());
		returnDTO.setMessage(message);

		if (disableHtmlEscaping) {
			return new GsonBuilder().disableHtmlEscaping().create()
					.toJson(returnDTO);
		}
		return JSONUtil.toJson(returnDTO);
	}

	protected int getPageNo(String pn) {
		int page = 1;
		if (StringUtils.isNotBlank(pn)) {
			try {
				page = Integer.parseInt(pn);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		return page;
	}

}
