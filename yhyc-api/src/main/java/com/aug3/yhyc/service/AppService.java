package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.yhyc.domain.AppDomain;
import com.aug3.yhyc.dto.AppInfo;

@Path("/app/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class AppService extends BaseService {

	private AppDomain appDomain;

	public AppDomain getAppDomain() {
		return appDomain;
	}

	public void setAppDomain(AppDomain appDomain) {
		this.appDomain = appDomain;
	}

	@GET
	@Path("/info")
	public String getAppInfo(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("ver") int ver) {

		AppInfo app = appDomain.getAppInfo(ver);
		return buildResponseSuccess(app);
	}

	@GET
	@Path("/shopmgr/info")
	public String getShopMgrAppInfo(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("ver") int ver) {

		AppInfo app = appDomain.getShopMgrAppInfo(ver);
		return buildResponseSuccess(app);
	}

}
