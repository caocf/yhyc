package com.aug3.yhyc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.yhyc.domain.ShequDomain;
import com.aug3.yhyc.valueobj.Shequ;

@Path("/shequ/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class ShequService extends BaseService {

	private ShequDomain shequDomain;

	public ShequDomain getShequDomain() {
		return shequDomain;
	}

	public void setShequDomain(ShequDomain shequDomain) {
		this.shequDomain = shequDomain;
	}

	/**
	 * 查询社区
	 * 
	 * @param request
	 * @param token
	 * @param q
	 * @return
	 */
	@GET
	@Path("/query")
	public String queryShequ(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("city") int city,
			@QueryParam("dist") int dist, @QueryParam("q") String q) {

		List<Shequ> result = shequDomain.queryShequ(city, dist, q);
		return buildResponseSuccess(result);
	}

}
