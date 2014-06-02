package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.dto.UserPrefs;
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

	public User find(User user) {

		return userDao.find(user);
	}

	public User find(long uid) {

		return userDao.find(uid);
	}

	public UserPrefs getUserPrefs(long uid) {
		return userDao.findPrefs(uid);
	}

	public boolean updateUserPrefs(long uid, String field, List<Long> items,
			int crud) {

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

	public boolean exist(User user) {
		return userDao.exist(user);
	}

	public long register(User user) {
		return userDao.create(user);
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
