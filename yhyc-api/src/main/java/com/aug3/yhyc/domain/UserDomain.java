package com.aug3.yhyc.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.cache.SystemCache;
import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.dto.UserPrefs;
import com.aug3.yhyc.util.ConfigManager;
import com.aug3.yhyc.util.NotificationService;
import com.aug3.yhyc.util.Qiniu;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.aug3.yhyc.valueobj.User;

public class UserDomain {

	private List<String> whitelist = Arrays.asList(ConfigManager
			.getProperties().getProperty("user.whitelist").split(","));

	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User login(User user) {

		return userDao.login(user);
	}

	public boolean isUserExist(User user) {

		return userDao.isUserExist(user);
	}

	public User find(long uid) {

		return userDao.find(uid);
	}

	public UserPrefs getUserPrefs(long uid) {

		UserPrefs userPrefs = userDao.findUserPrefs(uid);

		Map<String, String> urls = new HashMap<String, String>();

		String adurls = ConfigManager.getProperties().getProperty("ad.urls");
		boolean adon = ConfigManager.getProperties().getBooleanProperty(
				"ad.urls.on");
		if (adon && StringUtils.isNotBlank(adurls)) {
			String[] files = adurls.split(";");
			for (String fn : files) {
				String[] kv = fn.split("#");
				if (kv.length == 2 && StringUtils.isNotBlank(kv[1])) {
					urls.put(kv[0],
							Qiniu.downloadUrl(kv[1], Qiniu.getAppDomain()));
				}
			}
		}

		if (!urls.isEmpty()) {
			userPrefs.setUrls(urls);
		}

		return userPrefs;
	}

	public void updatePoint(long uid, int district, long shequ) {
		userDao.updatePoint(uid, district, shequ);
	}

	public boolean updateUserPrefs(long uid, String field,
			Collection<Long> items, int crud) {

		switch (crud) {
		case 1:
			return userDao.addUserPrefs(uid, field, items);
		case 2:
			return userDao.removeUserPrefs(uid, field, items);
		case 3:
			return userDao.updateUserPrefs(uid, field, items);
		}

		return false;

	}

	public void bindPushReceiver(long uid, String channelId, String userId) {
		userDao.bindPushReceiver(uid, channelId, userId);
	}

	public boolean uuidInBlackList(String uuid) {
		return userDao.uuidInBlacklist(uuid);
	}

	public long exist(User user) {
		return userDao.exist(user);
	}

	public long register(User user, String uuid, String ostype) {
		return userDao.create(user, uuid, ostype);
	}

	public boolean modifyPassword(String mobi, String mail, String passwd) {
		return userDao.modifyPassword(mobi, mail, passwd);
	}

	public boolean confirmPassword(String mail, long t) {
		return userDao.confirmPassword(mail, t);
	}

	/**
	 * TODO use distributed cache
	 * 
	 * @param mobi
	 * @return 0: 失败 1：成功 2：超过次数
	 */
	public int generateVerification(String mobi, String uuid, String ostype) {

		if (userDao.mobileInBlacklist(mobi)) {
			return 0;
		}

		if (whitelist.contains(mobi)) {
			// used for testing
			// verifyCode = mobi.substring(0, 4);
		} else {

			SystemCache sc = new SystemCache();
			String cacheKey = mobi + DateUtil.getCurrentDate();
			if (sc.containsKey(cacheKey)) {
				int c = (Integer) sc.get(cacheKey);
				if (c > 3) {
					return 2;
				}
			} else {
				sc.put(cacheKey, 0);
			}

			String verifyCode = NotificationService.sendSMS(mobi);

			if (verifyCode != null) {
				userDao.updateVerifyCode(mobi, verifyCode, uuid, ostype);

				int c = (Integer) sc.get(cacheKey);
				sc.put(cacheKey, c + 1, 3600 * 6);
			} else {
				return 0;
			}
		}

		return 1;
	}

	public boolean verifyMobile(String mobi, String verifyCode) {
		if (whitelist.contains(mobi)) {
			if (mobi.substring(0, 4).equals(verifyCode))
				return true;
			else
				return false;
		}
		return userDao.login(mobi, verifyCode);
	}

	public boolean bindMobile(String mobi, String verifyCode, String uuid,
			long uid) {
		userDao.bindMobile(mobi, verifyCode, uuid, uid);
		return true;
	}

	public User getUserInfo(String mobi) {
		return userDao.findByMobile(mobi);
	}

	public void update(User user) {
		userDao.update(user);
	}

	public void updateUserName(long uid, String name) {

		userDao.updateUserName(uid, name);

	}

	public long registerTempUser(User user) {
		long ret = userDao.existByMobile(user.getMobi());
		if (ret == 0) {
			return userDao.createTemp(user);
		} else {
			return ret;
		}
	}

	public User registerTempUser(String uuid) {
		return userDao.createTemp(uuid);
	}

	public List<Integer> findTags(long uid) {

		return userDao.findTags(uid);
	}

	public boolean updateTags(long uid, Collection<Integer> tags) {

		return userDao.updateTags(uid, tags);

	}

	public boolean complaintAndSugguestion(long uid, String name, String mobi,
			String mail, String content) {

		return userDao.addComplaintAndSugguestion(uid, name, mobi, mail,
				content);

	}

	/**
	 * TODO
	 * 
	 * @param uid
	 * @return
	 */
	public List<DeliveryContact> fetchContacts(long uid) {
		return null;
	}

}
