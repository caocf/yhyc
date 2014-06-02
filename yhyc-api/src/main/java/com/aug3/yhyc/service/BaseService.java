package com.aug3.yhyc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.base.RespObj;
import com.aug3.yhyc.base.RespType;
import com.google.gson.GsonBuilder;

public abstract class BaseService {

	protected String buidResponseSuccess(Object message) {
		return buidResponseResult(message, RespType.SUCCESS);
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

	protected List<Long> transfer2Long(String commaSeparated) {
		String[] array = commaSeparated.split(",");
		List<Long> result = new ArrayList<Long>();
		for (String s : array) {
			result.add(Long.parseLong(s));
		}
		return result;
	}

}
