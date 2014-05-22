package com.aug3.yhyc.dao;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class BaseDao {

	private static final int PAGE_SIZE = 10;

	protected int skipSize(int pn) {
		return (pn - 1) * PAGE_SIZE;
	}

	protected int pageSize() {
		return PAGE_SIZE;
	}

	protected DB getDB() {
		return MongoAdaptor.getDB();
	}

	protected DBCollection getDBCollection(String collection) {
		return MongoAdaptor.getDB().getCollection(collection);
	}

}
