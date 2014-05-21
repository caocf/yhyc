package com.aug3.yhyc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.sys.rs.response.RespType;
import com.aug3.yhyc.domain.UserDomain;
import com.aug3.yhyc.dto.OrderItem;
import com.aug3.yhyc.valueobj.DeliveryContact;

@Path("/user/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class UserService extends BaseService {

	private UserDomain userDomain;

	@POST
	@Path("/login")
	// @AccessTrace
	// @AccessToken
	public boolean login(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("uid") String uid,
			@FormParam("p") String p) {
		return userDomain.isValidUser(uid, p);
	}

	// TODO
	@GET
	@Path("/contacts")
	public String fetchContactsList(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") String uid) {

		List<DeliveryContact> contacts = userDomain.fetchContacts(uid);
		return this.buidResponseResult(contacts, RespType.SUCCESS);
	}

	@GET
	@Path("/favorite")
	public String fetchFavorite(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") String uid) {

		List<OrderItem> fav = userDomain.fetchFavorite(Long.parseLong(uid));
		return buidResponseResult(fav, RespType.SUCCESS);
	}

	@GET
	@Path("/shoppingcart")
	public String fetchShoppingCart(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") String uid) {

		List<OrderItem> cart = userDomain
				.fetchShoppingCart(Long.parseLong(uid));
		return buidResponseResult(cart, RespType.SUCCESS);
	}

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

}
