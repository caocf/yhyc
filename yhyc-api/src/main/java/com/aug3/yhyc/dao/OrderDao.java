package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.OrderItem;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class OrderDao {

	public List<Order> findOrdersByUid(String uid, int status) {

		List<Order> list = new ArrayList<Order>();

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_ORDERS)
				.find(new BasicDBObject("uid", uid).append("sts", status));

		BasicDBObject dbObj;
		Order oi;
		BasicDBList items;
		OrderItem orderItem;
		BasicDBObject item;
		List<OrderItem> orderItems;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			oi = new Order();

			items = (BasicDBList) dbObj.get("items");
			orderItems = new ArrayList<OrderItem>();
			for (Object eachitem : items) {
				item = (BasicDBObject) eachitem;
				orderItem = new OrderItem();
				orderItem.setId(item.getLong("item"));
				orderItem.setName(item.getString("name"));
				orderItem.setNum(item.getInt("num"));
				orderItem.setPrice(item.getDouble("pp"));
				orderItems.add(orderItem);
			}
			oi.setItems(orderItems);
			oi.setMsg(dbObj.getString("msg"));
			oi.setDt(dbObj.getDate("dt"));

			list.add(oi);
		}

		return list;

	}

}
