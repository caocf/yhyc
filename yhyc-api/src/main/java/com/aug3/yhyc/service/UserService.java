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

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.base.RespType;
import com.aug3.yhyc.domain.UserDomain;
import com.aug3.yhyc.dto.UserPrefs;
import com.aug3.yhyc.interceptors.annotation.AccessTrace;
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
	private static String verifycode_not_correct = ConfigManager
			.getProperties().getProperty("user.verifycode.notcorrect");
	// 您今天已尝试过3次，如无法收到验证码，请联系客服
	private static String verifycode_times_over = ConfigManager.getProperties()
			.getProperty("user.verifycode.times.over");
	// 验证码生成错误
	private static String verifycode_generate_error = ConfigManager
			.getProperties().getProperty("user.verifycode.generate.error");

	/**
	 * @deprecated use mobie verify instead
	 * 
	 * @param request
	 * @param token
	 * @param user
	 * @return
	 */
	@POST
	@Path("/login")
	public String login(@Context HttpServletRequest request,
			@FormParam("user") String user) {

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

	/**
	 * @deprecated use mobile verify instead
	 * 
	 * @param request
	 * @param token
	 * @param user
	 * @param uuid
	 * @return
	 */
	@POST
	@Path("/register")
	public String register(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("user") String user,
			@FormParam("uuid") String uuid) {

		if (StringUtils.isBlank(uuid)) {
			uuid = request.getHeader("uuid");
		}

		String ostype = request.getHeader("ostype");

		User u = JSONUtil.fromJson(user, User.class);
		long ret = userDomain.exist(u);

		if (ret == 0) {
			long uid = userDomain.register(u, uuid, ostype);
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
	@Path("/new/temp")
	public String createTempUser(@Context HttpServletRequest request,
			@QueryParam("uuid") String uuid) {

		if (StringUtils.isBlank(uuid)) {
			uuid = request.getHeader("uuid");
		}

		User user = userDomain.registerTempUser(uuid);

		return buildResponseSuccess(user);

	}

	@GET
	@Path("/mobile/verification")
	@AccessTrace
	public String generateVerification(@Context HttpServletRequest request,
			@QueryParam("mobi") String mobi, @QueryParam("uuid") String uuid) {

		if (StringUtils.isBlank(uuid)) {
			uuid = request.getHeader("uuid");
		}

		String ostype = request.getHeader("ostype");

		int ret = userDomain.generateVerification(mobi, uuid, ostype);
		if (ret == 1)
			return buildResponseSuccess("ok");
		else if (ret == 0)
			return buildResponseResult(verifycode_generate_error,
					RespType.FAILED);
		else
			return buildResponseResult(verifycode_times_over, RespType.FAILED);

	}

	@GET
	@Path("/mobile/verify")
	public String verifyMobile(@Context HttpServletRequest request,
			@QueryParam("mobi") String mobi,
			@QueryParam("verifyCode") String verifyCode) {

		boolean boo = userDomain.verifyMobile(mobi, verifyCode);
		if (boo) {
			User u = userDomain.getUserInfo(mobi);
			return buildResponseSuccess(u);
		} else
			return buildResponseResult(verifycode_not_correct,
					RespType.LOGIN_FAILED);
	}

	@GET
	@Path("/mobile/bind")
	@AccessTrace
	public String bindMobile(@Context HttpServletRequest request,
			@QueryParam("mobi") String mobi,
			@QueryParam("verifyCode") String verifyCode,
			@QueryParam("uuid") String uuid, @QueryParam("uid") long uid) {

		if (StringUtils.isBlank(uuid)) {
			uuid = request.getHeader("uuid");
		}

		boolean boo = userDomain.bindMobile(mobi, verifyCode, uuid, uid);
		if (boo) {
			User u = userDomain.getUserInfo(mobi);
			return buildResponseSuccess(u);
		} else
			return buildResponseResult(verifycode_not_correct, RespType.FAILED);
	}

	@GET
	@Path("/password/change")
	public String modifyPassword(@Context HttpServletRequest request,
			@QueryParam("mobi") String mobi, @QueryParam("mail") String mail,
			@QueryParam("passwd") String passwd) {

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
			@QueryParam("mail") String mail, @QueryParam("key") String key) {

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
			@QueryParam("uid") long uid) {

		User user = userDomain.find(uid);
		return buildResponseSuccess(user);
	}

	@GET
	@Path("/prefs")
	public String getUserPrefs(@Context HttpServletRequest request,
			@QueryParam("uid") long uid) {

		UserPrefs userPrefs = userDomain.getUserPrefs(uid);

		try {
			userDomain.loginStats(uid);
		} catch (Throwable t) {
		}

		return buildResponseSuccess(userPrefs);
	}

	@POST
	@Path("/point")
	public String updateUserPoint(@Context HttpServletRequest request,
			@FormParam("uid") long uid, @FormParam("dist") int district,
			@FormParam("shequ") long shequ) {

		userDomain.updatePoint(uid, district, shequ);
		return buildResponseSuccess("OK");
	}

	@POST
	@Path("/name")
	public String updateUserName(@Context HttpServletRequest request,
			@FormParam("uid") long uid, @FormParam("name") String name) {

		userDomain.updateUserName(uid, name);
		return buildResponseSuccess("OK");
	}

	@GET
	@Path("/tags")
	public String getUserTags(@Context HttpServletRequest request,
			@QueryParam("uid") long uid) {

		List<Integer> tags = userDomain.findTags(uid);
		return buildResponseSuccess(tags);
	}

	@POST
	@Path("/tags")
	public String updateUserTags(@Context HttpServletRequest request,
			@FormParam("uid") long uid, @FormParam("tags") String tags) {

		userDomain.updateTags(uid, transfer2Integer(tags));
		return buildResponseSuccess("OK");
	}

	@POST
	@Path("/sugguest")
	public String complaintAndSugguestion(@Context HttpServletRequest request,
			@FormParam("uid") long uid, @FormParam("name") String name,
			@FormParam("mobi") String mobi, @FormParam("mail") String mail,
			@FormParam("content") String content) {

		if (uid == 0 && StringUtils.isBlank(name) && StringUtils.isBlank(mobi)
				&& StringUtils.isBlank(mail) && StringUtils.isBlank(content)) {
			return buildResponseResult("empty parameters",
					RespType.INVALID_PARAMETERS);
		}

		userDomain.complaintAndSugguestion(uid, name, mobi, mail, content);
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
	@AccessTrace
	public String addUserPrefs(@Context HttpServletRequest request,
			@FormParam("uid") long uid, @FormParam("field") String field,
			@FormParam("items") String items, @FormParam("type") int type) {

		boolean result = userDomain.updateUserPrefs(uid, field,
				transfer2Long(items), type);
		return buildResponseSuccess(result);
	}

	/**
	 * Push service bind
	 * 
	 * @param request
	 * @param uid
	 * @param channelId
	 * @param userId
	 * @param type
	 * @return
	 */
	@POST
	@Path("/pushservice/bind")
	public String bindPushServiceReceiver(@Context HttpServletRequest request,
			@FormParam("uid") long uid,
			@FormParam("channelId") String channelId,
			@FormParam("userId") String userId, @FormParam("appid") String appid) {

		try {
			userDomain.bindPushReceiver(uid, channelId, userId);
		} catch (Exception e) {
		}
		return buildResponseSuccess("OK");
	}

	// TODO
	@GET
	@Path("/contacts")
	public String fetchContactsList(@Context HttpServletRequest request,
			@QueryParam("uid") long uid) {

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
