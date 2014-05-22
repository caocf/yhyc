package com.aug3.yhyc.dao;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.mongodb.DBCollection;

public class BaseDao {

	private static final int PAGE_SIZE = 10;

	public int skipSize(int pn) {
		return (pn - 1) * PAGE_SIZE;
	}

	public int pageSize() {
		return PAGE_SIZE;
	}

	public DBCollection getDBCollection(String collection) {
		return MongoAdaptor.getDB().getCollection(collection);
	}

}
