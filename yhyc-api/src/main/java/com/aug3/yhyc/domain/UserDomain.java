package com.aug3.yhyc.domain;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.dao.ItemDao;
import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.dto.OrderItem;
import com.aug3.yhyc.valueobj.DeliveryContact;

public class UserDomain {

	private UserDao userDao;

	private ItemDao itemDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public boolean isValidUser(String uid, String passwd) {

		if (StringUtils.isBlank(passwd)) {
			return false;
		}

		String userpass = "";

		if (passwd.equalsIgnoreCase(userpass)) {
			return true;
		}
		return false;
	}

	public List<DeliveryContact> fetchContacts(String uid) {
		return null;
	}

	public List<OrderItem> fetchFavorite(String uid) {
		List<Long> items = userDao.findFavorite(uid);
		return itemDao.findItems(items);
	}

	public List<OrderItem> fetchShoppingCart(String uid) {
		List<Long> items = userDao.findShoppingCart(uid);
		return itemDao.findItems(items);
	}

}
