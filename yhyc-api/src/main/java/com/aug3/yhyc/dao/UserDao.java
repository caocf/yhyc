package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.sys.util.DateUtil;
import com.aug3.sys.util.EncryptUtil;
import com.aug3.yhyc.base.CollectionConstants;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class UserDao {

	public boolean isValidUser(String uid, String passwd) {

		long u = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_USERS)
				.count(new BasicDBObject("_id", uid).append("pwd", passwd));

		if (u == 1) {
			return true;
		}

		return false;
	}

	public static boolean isValidToken(String token) {

		if (EncryptUtil.MD5(DateUtil.formatCurrentDate()).equals(token))
			return true;
		else
			return false;
	}

	public List<Long> findFavorite(long uid) {

		DBCursor cur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_USERS)
				.find(new BasicDBObject("_id", uid), new BasicDBObject("fav", 1));

		List<Long> items = new ArrayList<Long>();

		while (cur.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cur.next();
			BasicDBList favlist = (BasicDBList) dbo.get("fav");
			if (favlist != null && favlist.size() > 0) {

				for (Object itemid : favlist) {
					items.add((Long) itemid);
				}
			}
		}

		return items;
	}

	public List<Long> findShoppingCart(long uid) {

		DBCursor cur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_USERS)
				.find(new BasicDBObject("_id", uid), new BasicDBObject("cart", 1));

		List<Long> items = new ArrayList<Long>();

		while (cur.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cur.next();
			BasicDBList favlist = (BasicDBList) dbo.get("cart");
			if (favlist != null && favlist.size() > 0) {

				for (Object itemid : favlist) {
					items.add((Long) itemid);
				}
			}
		}

		return items;
	}
}
