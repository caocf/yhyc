package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/shequ/")
@XmlRootElement()
public class ShequService {

	/**
	 * 查询社区
	 * 
	 * @param request
	 * @param token
	 * @param q
	 * @return
	 */
	@Produces("application/json")
	@GET
	@Path("/query")
	public String queryShequ(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("q") String q) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	/**
	 * 获取社区对应的云仓网点信息
	 * 
	 * @param request
	 * @param token
	 * @param shequ
	 * @return
	 */
	@Produces("application/json")
	@GET
	@Path("/workshop")
	public String getServiceWorkshops(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("shequ") String shequ) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

}
