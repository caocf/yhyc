package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/stats/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class StatService extends BaseService {

	@GET
	@Path("/sales")
	public String listItems(@Context HttpServletRequest request,
			@QueryParam("token") String token) {

		return buildResponseSuccess("");
	}

}
