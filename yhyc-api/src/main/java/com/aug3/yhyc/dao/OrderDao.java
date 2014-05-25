package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.Orders;
import com.aug3.yhyc.util.ConfigManager;
import com.aug3.yhyc.util.IDGenerator;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class OrderDao extends BaseDao {

	private static final int delivery_fee = Integer.parseInt(ConfigManager
			.getProperties().getProperty("delivery.fee", "2"));

	private static final int delivery_minimum = Integer.parseInt(ConfigManager
			.getProperties().getProperty("delivery.minimum", "20"));

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

	public Order findByID(long orderid) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ORDERS).find(
				new BasicDBObject("_id", orderid));

		Order myorder = null;
		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();
			myorder = getOrderInfo(dbObj);

		}

		return myorder;

	}

	public List<Order> findByWorkshop(long workshop, int status) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ORDERS).find(
				new BasicDBObject("sid", new BasicDBObject("$in", workshop))
						.append("sts", status));

		List<Order> list = new ArrayList<Order>();

		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();
			Order myorder = getOrderInfo(dbObj);

			list.add(myorder);
		}

		return list;

	}

	public void updateOrderStatus(long orderid, int status) {

		getDBCollection(CollectionConstants.COLL_ORDERS).update(
				new BasicDBObject("_id", orderid),
				new BasicDBObject("sts", status), false, false,
				WriteConcern.SAFE);

	}

	/**
	 * separate order by workshop
	 * 
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean createOrder(Orders order) {

		Map<Long, List<ItemDTO>> orderMap = order.getItems();

		List<DBObject> doclist = new ArrayList<DBObject>();

		for (Long shopid : orderMap.keySet()) {

			BasicDBObject doc = new BasicDBObject();

			doc.put("_id", IDGenerator.nextOrderID(MongoAdaptor.getDB()));
			doc.put("uid", order.getUid());
			doc.put("sid", shopid);

			List orders = new ArrayList();
			List<ItemDTO> li = orderMap.get(shopid);
			double total = 0;
			for (ItemDTO item : li) {
				HashMap m = new HashMap();
				m.put("id", item.getId());
				m.put("name", item.getName());
				m.put("n", item.getNum());
				m.put("pp", item.getPrice());
				orders.add(m);
				total += item.getPrice();
			}
			doc.put("items", orders);

			DeliveryContact dc = order.getDelivery();
			Map delivery = new HashMap();
			delivery.put("recip", dc.getRecip());
			delivery.put("addr", dc.getAddr());
			delivery.put("mobi", dc.getMobi());
			delivery.put("d", dc.getD());
			delivery.put("t", dc.getT());
			if (total < delivery_minimum) {
				delivery.put("fee", delivery_fee);
				total += delivery_fee;
			}
			doc.put("delivery", delivery);

			// calculate and compare with client total
			doc.put("total", total);
			// calculate
			doc.put("ac", Math.round(total / 10));

			Date d = new Date();
			doc.put("ts", d);
			doc.put("msg", order.getMsg());
			doc.put("sts", 0);

			doclist.add(doc);

		}

		if (doclist.size() > 0) {
			getDBCollection(CollectionConstants.COLL_ORDERS).insert(doclist);
		}
		return true;
	}

	private Order getOrderInfo(BasicDBObject dbObj) {

		Order order = new Order();

		order.setId(dbObj.getLong("id"));
		order.setUid(dbObj.getLong("uid"));
		order.setSid(dbObj.getLong("sid"));

		List<ItemDTO> orderItems = new ArrayList<ItemDTO>();
		ItemDTO orderItem;
		BasicDBObject item;

		BasicDBList items = (BasicDBList) dbObj.get("items");
		for (Object eachitem : items) {
			item = (BasicDBObject) eachitem;
			orderItem = new ItemDTO();
			orderItem.setId(item.getLong("id"));
			orderItem.setName(item.getString("name"));
			orderItem.setNum(item.getInt("n"));
			orderItem.setPrice(item.getDouble("pp"));
			orderItems.add(orderItem);
		}
		order.setItems(orderItems);

		BasicDBObject delivery = (BasicDBObject) dbObj.get("delivery");
		if (delivery != null) {
			DeliveryContact contact = new DeliveryContact();
			contact.setRecip(delivery.getString("recip"));
			contact.setAddr(delivery.getString("addr"));
			contact.setMobi(delivery.getString("mobi"));
			contact.setD(delivery.getString("d"));
			contact.setT(delivery.getString("t"));
			contact.setFee(delivery.getInt("fee"));
			order.setDelivery(contact);
		}

		order.setMsg(dbObj.getString("msg"));

		order.setTotal(dbObj.getDouble("total"));
		order.setAc(dbObj.getInt("ac"));
		order.setDt(dbObj.getDate("dt"));
		order.setTs(dbObj.getDate("ts"));

		return order;
	}

}
