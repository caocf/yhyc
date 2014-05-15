package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.OrderItem;
import com.aug3.yhyc.util.IDGenerator;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;

public class OrderDao {

	private Order getOrderInfo(BasicDBObject dbObj) {

		BasicDBList items = (BasicDBList) dbObj.get("items");
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		OrderItem orderItem;
		BasicDBObject item;
		Order myorder = new Order();

		for (Object eachitem : items) {
			item = (BasicDBObject) eachitem;
			orderItem = new OrderItem();
			orderItem.setId(item.getLong("item"));
			orderItem.setShop(item.getLong("shop"));
			orderItem.setName(item.getString("name"));
			orderItem.setNum(item.getInt("num"));
			orderItem.setPrice(item.getDouble("pp"));
			orderItems.add(orderItem);
		}
		myorder.setItems(orderItems);

		BasicDBObject delivery = (BasicDBObject) dbObj.get("delivery");

		if (delivery != null) {
			DeliveryContact contact = new DeliveryContact();
			contact.setRecip(delivery.getString("recip"));
			contact.setAddr(delivery.getString("addr"));
			contact.setMobi(delivery.getString("mobi"));
			contact.setTel(delivery.getString("tel"));
			myorder.setDelivery(contact);
		}
		myorder.setTotal(dbObj.getDouble("total"));
		myorder.setMsg(dbObj.getString("msg"));
		myorder.setDt(dbObj.getDate("dt"));
		myorder.setTs(dbObj.getDate("ts"));

		return myorder;
	}

	public List<Order> findOrdersByUid(String uid, int status) {

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_ORDERS)
				.find(new BasicDBObject("uid", uid).append("sts", status));

		BasicDBObject dbObj;
		Order myorder;
		List<Order> list = new ArrayList<Order>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			myorder = getOrderInfo(dbObj);
			list.add(myorder);
		}

		return list;

	}

	public Order findOrder(long orderid) {

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_ORDERS)
				.find(new BasicDBObject("_id", orderid));

		Order myorder = null;
		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();
			myorder = getOrderInfo(dbObj);

		}

		return myorder;

	}

	public List<Order> findOrdersByWorkshop(long workshop, int status) {

		List<Order> list = new ArrayList<Order>();

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_ORDERS)
				.find(new BasicDBObject("items.shop", new BasicDBObject("$in", workshop)).append("sts", status));

		BasicDBObject dbObj;
		Order myorder;
		BasicDBList items;
		OrderItem orderItem;
		BasicDBObject item;
		List<OrderItem> orderItems;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			myorder = new Order();

			items = (BasicDBList) dbObj.get("items");
			orderItems = new ArrayList<OrderItem>();
			for (Object eachitem : items) {
				item = (BasicDBObject) eachitem;
				long shop = item.getLong("shop");
				if (shop == workshop) {
					orderItem = new OrderItem();
					orderItem.setId(item.getLong("item"));
					orderItem.setShop(shop);
					orderItem.setName(item.getString("name"));
					orderItem.setNum(item.getInt("num"));
					orderItem.setPrice(item.getDouble("pp"));
					orderItems.add(orderItem);
				}
			}

			BasicDBObject delivery = (BasicDBObject) dbObj.get("delivery");

			if (delivery != null) {
				DeliveryContact contact = new DeliveryContact();
				contact.setRecip(delivery.getString("recip"));
				contact.setAddr(delivery.getString("addr"));
				contact.setMobi(delivery.getString("mobi"));
				contact.setTel(delivery.getString("tel"));
				myorder.setDelivery(contact);
			}
			myorder.setItems(orderItems);
			myorder.setTotal(dbObj.getDouble("total"));
			myorder.setMsg(dbObj.getString("msg"));
			myorder.setDt(dbObj.getDate("dt"));
			myorder.setTs(dbObj.getDate("ts"));

			list.add(myorder);
		}

		return list;

	}

	public void updateOrderStatus(long orderid, int status) {

		MongoAdaptor
				.getDB()
				.getCollection(CollectionConstants.COLL_ORDERS)
				.update(new BasicDBObject("_id", orderid), new BasicDBObject("sts", status), false, false,
						WriteConcern.SAFE);

	}

	public boolean createOrder(Order order) {

		List<Order> list = new ArrayList<Order>();

		String today = DateUtil.formatCurrentDate();
		order.setOrderID(Long.parseLong(today + IDGenerator.nextval(MongoAdaptor.getDB(), today, "orderid")));
		
		MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_ORDERS).save(new BasicDBObject());

		BasicDBObject dbObj;
		Order myorder;
		BasicDBList items;
		OrderItem orderItem;
		BasicDBObject item;
		List<OrderItem> orderItems;
//		while (dbCur.hasNext()) {
//			dbObj = (BasicDBObject) dbCur.next();
//			myorder = new Order();
//
//			items = (BasicDBList) dbObj.get("items");
//			orderItems = new ArrayList<OrderItem>();
//			for (Object eachitem : items) {
//				item = (BasicDBObject) eachitem;
//				long shop = item.getLong("shop");
//				if (shop == workshop) {
//					orderItem = new OrderItem();
//					orderItem.setId(item.getLong("item"));
//					orderItem.setShop(shop);
//					orderItem.setName(item.getString("name"));
//					orderItem.setNum(item.getInt("num"));
//					orderItem.setPrice(item.getDouble("pp"));
//					orderItems.add(orderItem);
//				}
//			}
//
//			BasicDBObject delivery = (BasicDBObject) dbObj.get("delivery");
//
//			if (delivery != null) {
//				DeliveryContact contact = new DeliveryContact();
//				contact.setRecip(delivery.getString("recip"));
//				contact.setAddr(delivery.getString("addr"));
//				contact.setMobi(delivery.getString("mobi"));
//				contact.setTel(delivery.getString("tel"));
//				myorder.setDelivery(contact);
//			}
//			myorder.setItems(orderItems);
//			myorder.setTotal(dbObj.getDouble("total"));
//			myorder.setMsg(dbObj.getString("msg"));
//			myorder.setDt(dbObj.getDate("dt"));
//			myorder.setTs(dbObj.getDate("ts"));
//
//			list.add(myorder);
//		}
//
//		return list;
return true;
	}

}
