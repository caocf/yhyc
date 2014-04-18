package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.valueobj.Contact;
import com.aug3.yhyc.valueobj.Item;
import com.aug3.yhyc.valueobj.User;

public class UserDomain {

	private User user;

	// 用户联系方式
	private List<Contact> contacts;

	// 用户收藏
	private List<Item> fav;

	// 购物车
	private List<Item> cart;

	// 积分
	private long ac;

	// 抵用券
	// private List<Ticker>

	public boolean isValidUser(String uid, String passwd) {

		// TODO MD5 validate
		return false;
	}

	public List<Contact> fetchContacts(String uid) {
		return null;
	}

	public List<Item> fetchFavorite(String uid) {
		return null;
	}

	public List<Item> fetchShoppingCart(String uid) {
		return null;
	}

}
