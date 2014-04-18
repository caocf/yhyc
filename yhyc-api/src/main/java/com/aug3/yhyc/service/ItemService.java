package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/item/")
@XmlRootElement()
@Produces("application/json")
public class ItemService {

	@POST
	@Path("/new")
	// @AccessTrace
	// @AccessToken
	public boolean newOrder(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("uid") String uid, @FormParam("order") String orderObj) {
		// TODO: change to order object
		return false;
	}

	@GET
	@Path("/list")
	public String listOrders(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("workshop") String workshop) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

}
