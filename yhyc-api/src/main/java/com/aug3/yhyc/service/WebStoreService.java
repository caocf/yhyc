package com.aug3.yhyc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.base.RespType;
import com.aug3.yhyc.domain.WebStoreDomain;
import com.aug3.yhyc.dto.WebStoreItem;
import com.aug3.yhyc.interceptors.annotation.AccessTrace;

@Path("/webstore/")
@XmlRootElement()
@Produces("application/json")
public class WebStoreService extends BaseService {

	private WebStoreDomain webstoreDomain;

	public WebStoreDomain getWebstoreDomain() {
		return webstoreDomain;
	}

	public void setWebstoreDomain(WebStoreDomain webstoreDomain) {
		this.webstoreDomain = webstoreDomain;
	}

	@POST
	@Path("/iexchg")
	@AccessTrace
	public String integralExchange(@Context HttpServletRequest request,
			@FormParam("uid") long uid, @FormParam("item") String item,
			@FormParam("name") String name, @FormParam("addr") String addr,
			@FormParam("mobi") String mobi) {

		if (uid <= 0) {
			return buildResponseResult("invalid user id",
					RespType.INVALID_PARAMETERS);
		}

		if (StringUtils.isBlank(name) || StringUtils.isBlank(addr)
				|| StringUtils.isBlank(mobi)) {
			return buildResponseResult("name/addr/mobi can not be empty",
					RespType.INVALID_PARAMETERS);
		}

		boolean boo = webstoreDomain.integralExchange(uid, item, name, addr,
				mobi);

		if (boo)
			return buildResponseSuccess("success!");
		else
			return buildResponseResult("failed!", RespType.FAILED);
	}

	@GET
	@Path("/list")
	@AccessTrace
	public String listItems(@Context HttpServletRequest request) {

		List<WebStoreItem> items = webstoreDomain.listItems();

		return this.buildResponseSuccess(items);
	}

}
