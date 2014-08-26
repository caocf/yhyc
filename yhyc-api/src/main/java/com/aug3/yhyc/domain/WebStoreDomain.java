package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.dao.WebStoreDao;
import com.aug3.yhyc.dto.WebStoreItem;

public class WebStoreDomain {

	private WebStoreDao webstoreDao;

	public WebStoreDao getWebstoreDao() {
		return webstoreDao;
	}

	public void setWebstoreDao(WebStoreDao webstoreDao) {
		this.webstoreDao = webstoreDao;
	}

	public List<WebStoreItem> listItems() {

		return webstoreDao.findAll();
	}

	public boolean integralExchange(long uid, String item, String name,
			String addr, String mobi) {

		return webstoreDao.addIntegralExchange(uid, item, name, addr, mobi);
	}

}
