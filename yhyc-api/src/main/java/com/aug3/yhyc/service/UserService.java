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

import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.base.RespType;
import com.aug3.yhyc.domain.UserDomain;
import com.aug3.yhyc.dto.UserPrefs;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.aug3.yhyc.valueobj.User;

@Path("/user/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class UserService extends BaseService {

	private UserDomain userDomain;

	@POST
	@Path("/login")
	// @AccessTrace
	// @AccessToken
	public String login(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("user") String user) {

		User retUser = userDomain.login(JSONUtil.fromJson(user, User.class));
		if (retUser != null) {
			return buidResponseResult(retUser, RespType.LOGIN_SUCCESS);
		} else {
			return buidResponseResult("N", RespType.LOGIN_FAILED);
		}
	}

	@POST
	@Path("/register")
	// @AccessTrace
	// @AccessToken
	public String register(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("user") String user) {

		User u = JSONUtil.fromJson(user, User.class);
		long ret = userDomain.exist(u);

		if (ret == 0) {
			long uid = userDomain.register(u);
			return buidResponseSuccess(uid);
		} else if (ret < 20) {
			return buidResponseResult(ret, RespType.USER_EXIST);
		} else {
			u.setUid(ret);
			userDomain.update(u);
			return buidResponseSuccess(u.getUid());
		}

	}

	@GET
	@Path("/avatar")
	public String getAvatar(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		User user = userDomain.find(uid);
		return buidResponseSuccess(user);
	}

	@GET
	@Path("/prefs")
	public String getUserPrefs(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		UserPrefs userPrefs = userDomain.getUserPrefs(uid);
		return buidResponseSuccess(userPrefs);
	}

	@POST
	@Path("/prefs/update")
	public String addUserPrefs(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("uid") long uid,
			@FormParam("field") String field, @FormParam("items") String items,
			@FormParam("type") int type) {

		boolean result = userDomain.updateUserPrefs(uid, field,
				transfer2Long(items), type);
		return buidResponseSuccess(result);
	}

	// TODO
	@GET
	@Path("/contacts")
	public String fetchContactsList(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		List<DeliveryContact> contacts = userDomain.fetchContacts(uid);
		return buidResponseSuccess(contacts);
	}

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

}
