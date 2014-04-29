package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.OrderItem;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class ItemDao {

	public List<OrderItem> findItems(List<Long> itemIds) {

		List<OrderItem> list = new ArrayList<OrderItem>();

		if (itemIds != null && itemIds.size() > 0) {
			DBCursor dbCur = MongoAdaptor
					.getDB()
					.getCollection(CollectionConstants.COLL_ITEMS)
					.find(new BasicDBObject("_id", new BasicDBObject("$in", itemIds)),
							new BasicDBObject("name", 1).append("pic", 1).append("pp", 1).append("mp", 1));

			BasicDBObject dbObj;
			OrderItem oi;
			while (dbCur.hasNext()) {
				dbObj = (BasicDBObject) dbCur.next();
				oi = new OrderItem();
				oi.setId(dbObj.getLong("_id"));
				oi.setName(dbObj.getString("name"));
				oi.setPrice(dbObj.getDouble("pp"));
				oi.setOrigprice(dbObj.getDouble("mp"));
				// oi.setAc(ac);
				list.add(oi);
			}
		}

		return list;

	}

}
