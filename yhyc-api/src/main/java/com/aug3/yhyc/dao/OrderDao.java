package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.domain.OrderStatus;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.Orders;
import com.aug3.yhyc.util.Arith;
import com.aug3.yhyc.util.ConfigManager;
import com.aug3.yhyc.util.IDGenerator;
import com.aug3.yhyc.valueobj.DeliveryContact;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class OrderDao extends BaseDao {

	private static final int delivery_fee = Integer.parseInt(ConfigManager
			.getProperties().getProperty("delivery.fee", "2"));

	private static final int delivery_minimum = Integer.parseInt(ConfigManager
			.getProperties().getProperty("delivery.minimum", "30"));

	public List<Order> findOrdersByUid(long uid, int status) {

		BasicDBObject query = new BasicDBObject("uid", uid);

		addStatusCondition(query, status);

		DBCursor dbCur = null;

		if (status == 99) {
			dbCur = getDBCollection(CollectionConstants.COLL_ORDERS)
					.find(query).sort(new BasicDBObject("_id", -1)).limit(30);
		} else {
			dbCur = getDBCollection(CollectionConstants.COLL_ORDERS)
					.find(query).sort(new BasicDBObject("_id", -1));
		}

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

	public List<Order> findOrdersByIds(Collection<Long> orderids, int status) {

		BasicDBObject query = new BasicDBObject("_id", new BasicDBObject("$in",
				orderids));

		addStatusCondition(query, status);

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ORDERS).find(
				query).sort(new BasicDBObject("_id", -1));

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

		BasicDBObject query = new BasicDBObject("sid", workshop);

		addStatusCondition(query, status);

		DBCursor dbCur = null;

		if (status == 99) {
			dbCur = getDBCollection(CollectionConstants.COLL_ORDERS)
					.find(query).sort(new BasicDBObject("_id", -1)).limit(100);
		} else {
			dbCur = getDBCollection(CollectionConstants.COLL_ORDERS)
					.find(query).sort(new BasicDBObject("_id", -1));
		}

		List<Order> list = new ArrayList<Order>();

		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();
			Order myorder = getOrderInfo(dbObj);

			list.add(myorder);
		}

		return list;

	}

	/**
	 * 88 -- ongoing;
	 * 
	 * 99 -- finished;
	 * 
	 * @param dbObj
	 * @param status
	 */
	private void addStatusCondition(BasicDBObject dbObj, int status) {
		if (status == 88) {
			dbObj.append("sts",
					new BasicDBObject("$lt", OrderStatus.CANCELED.getValue()));
		} else if (status == 99) {// 结束
			dbObj.append("sts",
					new BasicDBObject("$gte", OrderStatus.CANCELED.getValue()));
		} else {
			dbObj.append("sts", status);
		}
	}

	public int updateStatus(long orderid, int status) {

		WriteResult wr = null;

		BasicDBObject updateObj = new BasicDBObject("$set", new BasicDBObject(
				"sts", status));
		if (status == OrderStatus.CANCELED.getValue()
				|| status == OrderStatus.CANCELED_BY_SELLER.getValue()) {
			wr = getDBCollection(CollectionConstants.COLL_ORDERS).update(
					new BasicDBObject("_id", orderid).append("sts",
							OrderStatus.NEW.getValue()), updateObj, false,
					false, WriteConcern.SAFE);

		} else {
			wr = getDBCollection(CollectionConstants.COLL_ORDERS).update(
					new BasicDBObject("_id", orderid), updateObj, false, false,
					WriteConcern.SAFE);
		}

		if (wr != null) {
			return wr.getN();
		}

		return 0;
	}

	/**
	 * separate order by workshop
	 * 
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> createOrder(Orders order) {

		Map<Long, List<ItemDTO>> orderMap = order.getItems();

		List<DBObject> doclist = new ArrayList<DBObject>();
		List<Long> orderids = new ArrayList<Long>();

		for (Long shopid : orderMap.keySet()) {

			BasicDBObject doc = new BasicDBObject();

			long orderid = IDGenerator.nextOrderID(MongoAdaptor.getDB());
			doc.put("_id", orderid);
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
				total = Arith.add(total, item.getPrice());
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
				total = Arith.add(total, delivery_fee);
			} else {
				delivery.put("fee", 0);
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
			orderids.add(orderid);

		}

		if (doclist.size() > 0) {
			getDBCollection(CollectionConstants.COLL_ORDERS).insert(doclist);
		}
		return orderids;
	}

	private Order getOrderInfo(BasicDBObject dbObj) {

		Order order = new Order();

		order.setId(dbObj.getLong("_id"));
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
			contact.setFee(delivery.containsField("fee") ? delivery
					.getInt("fee") : 0);
			order.setDelivery(contact);
		}

		order.setMsg(dbObj.getString("msg"));

		order.setTotal(dbObj.getDouble("total"));
		order.setAc(dbObj.getInt("ac"));
		order.setDt(dbObj.getDate("dt"));
		order.setTs(dbObj.getDate("ts"));
		order.setStatus(dbObj.getInt("sts"));

		return order;
	}

}
