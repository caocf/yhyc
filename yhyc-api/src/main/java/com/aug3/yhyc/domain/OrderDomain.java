package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.valueobj.Contact;
import com.aug3.yhyc.valueobj.Item;

public class OrderDomain {

	// 日期+卖家id+流水号
	private String orderID;
	
	private String uid;
	
	private List<Item> items;
	
	

	public boolean isValidUser(String uid, String passwd) {

		// TODO MD5 validate
		return false;
	}

	public List<Contact> fetchContacts(String uid) {
		return null;
	}

	

}
