package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.base.RespType;
import com.aug3.yhyc.domain.AppDomain;
import com.aug3.yhyc.domain.UserDomain;
import com.aug3.yhyc.dto.AppInfo;
import com.aug3.yhyc.dto.AppInfoExt;
import com.aug3.yhyc.dto.AppInfoShop;
import com.aug3.yhyc.interceptors.annotation.AccessTrace;

@Path("/app/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class AppService extends BaseService {

	private AppDomain appDomain;

	private UserDomain userDomain;

	public AppDomain getAppDomain() {
		return appDomain;
	}

	public void setAppDomain(AppDomain appDomain) {
		this.appDomain = appDomain;
	}

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

	/**
	 * @deprecated use getAppInfoExt instead, 17users
	 * 
	 * @param request
	 * @param ver
	 * @return
	 */
	@GET
	@Path("/info")
	@AccessTrace
	public String getAppInfo(@Context HttpServletRequest request,
			@QueryParam("ver") int ver) {

		AppInfo app = appDomain.getAppInfo(ver);
		return buildResponseSuccess(app);
	}

	@GET
	@Path("/welcome")
	@AccessTrace
	public String welcomeToApp(@Context HttpServletRequest request) {

		String ver = request.getHeader("ver");
		if (StringUtils.isBlank(ver)) {
			return buildResponseResult("param [ver] lost",
					RespType.INVALID_PARAMETERS);
		}

		String uuid = request.getHeader("uuid");
		if (StringUtils.isNotBlank(uuid)) {
			if (userDomain.uuidInBlackList(uuid)) {
				return null;
			}
		}

		String ostype = request.getHeader("ostype");
		AppInfoExt app = null;
		if ("ios".equalsIgnoreCase(ostype)) {
			app = appDomain.getAppInfoIOS(Integer.parseInt(ver));
		} else {
			app = appDomain.getAppInfoAndroid(Integer.parseInt(ver));
		}

		return buildResponseSuccess(app);
	}

	@GET
	@Path("/shopmgr/info")
	@AccessTrace
	public String getShopMgrAppInfo(@Context HttpServletRequest request) {

		String ver = request.getHeader("ver");
		if (StringUtils.isBlank(ver)) {
			return buildResponseResult("param [ver] lost",
					RespType.INVALID_PARAMETERS);
		}

		AppInfoShop app = appDomain.getShopMgrAppInfo(Integer.parseInt(ver));
		return buildResponseSuccess(app);
	}

}
