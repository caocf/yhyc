package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.dao.ShequDao;
import com.aug3.yhyc.valueobj.Shequ;

public class ShequDomain {

	private ShequDao shequDao;

	public ShequDao getShequDao() {
		return shequDao;
	}

	public void setShequDao(ShequDao shequDao) {
		this.shequDao = shequDao;
	}

	public List<Shequ> queryShequ(String city, String q) {
		return shequDao.findShequ(city, q);
	}

}
