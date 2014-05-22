package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.valueobj.DeliveryContact;

public class UserDomain {

	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public boolean isValidUser(String uid, String passwd) {

		return userDao.isValidUser(uid, passwd);
	}

	/**
	 * TODO
	 * 
	 * @param uid
	 * @return
	 */
	public List<DeliveryContact> fetchContacts(String uid) {
		return null;
	}

}
