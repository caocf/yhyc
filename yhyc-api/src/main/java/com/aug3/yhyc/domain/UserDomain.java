package com.aug3.yhyc.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.dto.UserPrefs;
import com.aug3.yhyc.util.ConfigManager;
import com.aug3.yhyc.util.Qiniu;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.aug3.yhyc.valueobj.User;

public class UserDomain {

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
				urls.put(fn.substring(0, fn.length() - 4),
						Qiniu.downloadUrl(fn, Qiniu.getCaipuDomain()));
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

	public long exist(User user) {
		return userDao.exist(user);
	}

	public long register(User user) {
		return userDao.create(user);
	}

	public boolean modifyPassword(String mobi, String mail, String passwd) {
		return userDao.modifyPassword(mobi, mail, passwd);
	}

	public boolean confirmPassword(String mail, long t) {
		return userDao.confirmPassword(mail, t);
	}

	public void update(User user) {
		userDao.update(user);
	}

	public long registerTempUser(User user) {
		long ret = userDao.existByMobile(user.getMobi());
		if (ret == 0) {
			return userDao.createTemp(user);
		} else {
			return ret;
		}
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
