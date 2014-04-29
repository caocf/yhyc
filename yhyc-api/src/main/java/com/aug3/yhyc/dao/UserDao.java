package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class UserDao {

	public List<Long> findFavorite(String uid) {

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

	public List<Long> findShoppingCart(String uid) {

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
