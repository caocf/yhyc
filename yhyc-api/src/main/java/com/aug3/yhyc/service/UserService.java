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
import com.aug3.yhyc.util.ConfigManager;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.aug3.yhyc.valueobj.User;

@Path("/user/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class UserService extends BaseService {

	private UserDomain userDomain;
	private static String user_not_exist = ConfigManager.getProperties()
			.getProperty("user.notexist");
	private static String passwd_not_correct = ConfigManager.getProperties()
			.getProperty("user.passwd.notcorrect");

	@POST
	@Path("/login")
	// @AccessTrace
	// @AccessToken
	public String login(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("user") String user) {

		User u = JSONUtil.fromJson(user, User.class);
		User retUser = userDomain.login(u);
		if (retUser != null) {
			return buildResponseResult(retUser, RespType.LOGIN_SUCCESS);
		} else {
			if (userDomain.isUserExist(u)) {
				return buildResponseResult(passwd_not_correct,
						RespType.LOGIN_FAILED);
			} else {
				return buildResponseResult(user_not_exist,
						RespType.LOGIN_FAILED);
			}

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
			return buildResponseSuccess(uid);
		} else if (ret < 20) {
			return buildResponseResult(ret, RespType.USER_EXIST);
		} else {
			u.setUid(ret);
			userDomain.update(u);
			return buildResponseSuccess(u.getUid());
		}

	}

	@GET
	@Path("/password/change")
	public String modifyPassword(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("mobi") String mobi,
			@QueryParam("mail") String mail, @QueryParam("passwd") String passwd) {

		boolean bool = userDomain.modifyPassword(mobi, mail, passwd);
		if (bool)
			return buildResponseResult("activate the link sent to your mail",
					RespType.SUCCESS);
		else
			return buildResponseResult("failed to modify password",
					RespType.FAILED);
	}

	@GET
	@Path("/password/confirm")
	public String confirmPassword(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("mail") String mail,
			@QueryParam("key") String key) {

		boolean bool = userDomain.confirmPassword(mail, Long.parseLong(key));

		if (bool)
			return ConfigManager.getProperties().getProperty(
					"mail.chgpwd.success");
		else
			return ConfigManager.getProperties()
					.getProperty("mail.chgpwd.fail");
	}

	@GET
	@Path("/avatar")
	public String getAvatar(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		User user = userDomain.find(uid);
		return buildResponseSuccess(user);
	}

	@GET
	@Path("/prefs")
	public String getUserPrefs(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		UserPrefs userPrefs = userDomain.getUserPrefs(uid);
		return buildResponseSuccess(userPrefs);
	}

	@POST
	@Path("/point")
	public String updateUserPoint(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("uid") long uid,
			@FormParam("dist") int district, @FormParam("shequ") long shequ) {

		userDomain.updatePoint(uid, district, shequ);
		return buildResponseSuccess("OK");
	}

	/**
	 * 
	 * @param request
	 * @param token
	 * @param uid
	 * @param field
	 * @param items
	 * @param type
	 *            1. add; 2. remove; 3. update
	 * @return
	 */
	@POST
	@Path("/prefs/update")
	public String addUserPrefs(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("uid") long uid,
			@FormParam("field") String field, @FormParam("items") String items,
			@FormParam("type") int type) {

		boolean result = userDomain.updateUserPrefs(uid, field,
				transfer2Long(items), type);
		return buildResponseSuccess(result);
	}

	// TODO
	@GET
	@Path("/contacts")
	public String fetchContactsList(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("uid") long uid) {

		List<DeliveryContact> contacts = userDomain.fetchContacts(uid);
		return buildResponseSuccess(contacts);
	}

	public UserDomain getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(UserDomain userDomain) {
		this.userDomain = userDomain;
	}

}
