package com.aug3.yhyc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.base.RespObj;
import com.aug3.yhyc.base.RespType;
import com.google.gson.GsonBuilder;

public abstract class BaseService {

	protected String buildResponseSuccess(Object message) {
		return buildResponseResult(message, RespType.SUCCESS);
	}

	/**
	 * 
	 * @Title: buidResponseResult
	 * @Description:
	 * @param message
	 * @param responseTyp
	 * @return
	 */
	protected String buildResponseResult(Object message, RespType responseType) {
		RespObj returnDTO = new RespObj();
		returnDTO.setCode(responseType.getCode());
		returnDTO.setType(responseType.toString());
		returnDTO.setMessage(message);
		return JSONUtil.toJson(returnDTO);
	}

	protected String buildResponseResult(Object message, RespType responseType,
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

	protected List<Long> transfer2Long(String commaSeparated) {
		List<Long> result = new ArrayList<Long>();
		if (StringUtils.isBlank(commaSeparated)) {
			return result;
		}
		String[] array = commaSeparated.split(",");
		for (String s : array) {
			result.add(Long.parseLong(s));
		}
		return result;
	}

}
