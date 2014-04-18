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

@Path("/user/")
@XmlRootElement()
@Produces("application/json")
public class UserService {

	@POST
	@Path("/login")
	// @AccessTrace
	// @AccessToken
	public boolean login(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("uid") String uid, @FormParam("pass") String pass) {
		return false;
	}

	@GET
	@Path("/address")
	public String getAddressList(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	@GET
	@Path("/favorite")
	public String getFavorite(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	@GET
	@Path("/shoppingcart")
	public String viewShoppingCart(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

}
