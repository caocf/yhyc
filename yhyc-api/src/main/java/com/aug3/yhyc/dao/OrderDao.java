package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.OrderItem;
import com.aug3.yhyc.util.IDGenerator;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;

public class OrderDao extends BaseDao {

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
			orderItem.setSid(item.getLong("shop"));
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

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ORDERS).find(
				new BasicDBObject("uid", uid).append("sts", status));

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

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ORDERS).find(new BasicDBObject("_id", orderid));

		Order myorder = null;
		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();
			myorder = getOrderInfo(dbObj);

		}

		return myorder;

	}

	public List<Order> findOrdersByWorkshop(long workshop, int status) {

		List<Order> list = new ArrayList<Order>();

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ORDERS).find(
				new BasicDBObject("items.shop", new BasicDBObject("$in", workshop)).append("sts", status));

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
					orderItem.setSid(shop);
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

		getDBCollection(CollectionConstants.COLL_ORDERS).update(new BasicDBObject("_id", orderid),
				new BasicDBObject("sts", status), false, false, WriteConcern.SAFE);

	}

	public boolean createOrder(Order order) {

		BasicDBObject doc = new BasicDBObject();
		doc.put("_id", IDGenerator.nextOrderID(MongoAdaptor.getDB()));
		doc.put("uid", order.getUid());

		List orders = new ArrayList();
		List<OrderItem> li = order.getItems();
		for (OrderItem item : li) {
			HashMap m = new HashMap();
			m.put("item", item.getId());
			m.put("name", item.getName());
			m.put("shop", item.getSid());
			m.put("num", item.getNum());
			m.put("pp", item.getPrice());
			m.put("mp", item.getOrigprice());

			orders.add(m);
		}
		doc.put("items", orders);

		doc.put("total", 1.1);
		doc.put("ac", Math.round(1.1 / 10));

		Map<String, String> delivery = new HashMap<String, String>();
		DeliveryContact dc = order.getDelivery();
		delivery.put("recip", dc.getRecip());
		delivery.put("addr", dc.getAddr());
		delivery.put("", dc.getMobi());
		delivery.put("tel", dc.getTel());
		doc.put("delivery", delivery);

		Date d = new Date();
		doc.put("ts", d);
		doc.put("msg", order.getMsg());
		doc.put("sts", order.getStatus());

		getDBCollection(CollectionConstants.COLL_ORDERS).insert(doc);

		return true;
	}

}
