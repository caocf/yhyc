package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.domain.OrderStatus;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.Orders;
import com.aug3.yhyc.util.Arith;
import com.aug3.yhyc.util.AsyncJobs;
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

	private static final Logger log = Logger.getLogger(OrderDao.class);

	private static final int delivery_fee = Integer.parseInt(ConfigManager
			.getProperties().getProperty("delivery.fee", "2"));

	private static final int delivery_minimum = Integer.parseInt(ConfigManager
			.getProperties().getProperty("delivery.minimum", "30"));

	public List<Order> findOrdersByUid(long uid, int status) {

		BasicDBObject query = new BasicDBObject("uid", uid);

		addStatusCondition(query, status);

		DBCursor dbCur = null;

		if (status == Constants.ORDER_STATUS_FINISHED) {
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

		if (status == Constants.ORDER_STATUS_FINISHED) {
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
	 * 
	 * @param workshop
	 * @param status
	 * @param freq
	 *            -- daily, weekly, monthly
	 * @return
	 */
	public List<Order> find(long workshop, int status, int freq) {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		if (Constants.FREQ_WEEKLY == freq) {
			c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		} else if (Constants.FREQ_MONTHLY == freq) {
			c.set(Calendar.DAY_OF_MONTH, 1);
		}

		Date start = c.getTime();

		BasicDBObject query = new BasicDBObject("sid", workshop).append("ts",
				new BasicDBObject("$gte", start));

		if (status == Constants.ORDER_STATUS_ONGOING) {
			query.append("sts",
					new BasicDBObject("$lt", OrderStatus.CANCELED.getValue()));
		} else if (status == Constants.ORDER_STATUS_FINISHED) {// 结束
			query.append("sts", new BasicDBObject("$gt",
					OrderStatus.CANCELED_BY_SELLER.getValue()));
		} else {
			query.append("sts", status);
		}

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ORDERS).find(
				query);

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
		if (status == Constants.ORDER_STATUS_ONGOING) {
			dbObj.append("sts",
					new BasicDBObject("$lt", OrderStatus.CANCELED.getValue()));
		} else if (status == Constants.ORDER_STATUS_FINISHED) {// 结束
			dbObj.append("sts",
					new BasicDBObject("$gte", OrderStatus.CANCELED.getValue()));
		} else {
			dbObj.append("sts", status);
		}
	}

	public int updateStatus(final long orderid, int status) {

		WriteResult wr = null;

		BasicDBObject updateObj = new BasicDBObject("$set", new BasicDBObject(
				"sts", status));

		if (status == OrderStatus.CANCELED.getValue()
				|| status == OrderStatus.CANCELED_BY_SELLER.getValue()) {

			wr = getDBCollection(CollectionConstants.COLL_ORDERS).update(
					new BasicDBObject("_id", orderid).append("sts",
							OrderStatus.NEW.getValue()), updateObj, false,
					false, WriteConcern.SAFE);

			// 订单取消，商品返回购物车
			AsyncJobs.submit(new Runnable() {

				@Override
				public void run() {
					try {

						DBCursor dbCur = getDBCollection(
								CollectionConstants.COLL_ORDERS).find(
								new BasicDBObject("_id", orderid),
								new BasicDBObject().append("uid", 1).append(
										"items.id", 1));

						if (dbCur.hasNext()) {
							BasicDBObject lastOrder = (BasicDBObject) dbCur
									.next();
							long uid = lastOrder.getLong("uid");
							BasicDBList dblist = (BasicDBList) lastOrder
									.get("items");
							List<Long> itemids = new ArrayList<Long>();
							int n = dblist.size();
							for (int i = 0; i < n; i++) {
								BasicDBObject dbObj = (BasicDBObject) dblist
										.get(i);
								itemids.add(dbObj.getLong("id"));
							}

							// update user accumulative score
							getDBCollection(CollectionConstants.COLL_USERS)
									.update(new BasicDBObject("_id", uid),
											new BasicDBObject("$addToSet",
													new BasicDBObject().append(
															"cart",
															new BasicDBObject(
																	"$each",
																	itemids))),
											false, false);
						}

					} catch (Throwable e) {
						log.error("cancel order error: " + e.getMessage());
					}
				}
			});

		} else {

			wr = getDBCollection(CollectionConstants.COLL_ORDERS).update(
					new BasicDBObject("_id", orderid), updateObj, false, false,
					WriteConcern.SAFE);

			if (status == OrderStatus.SUCCEED.getValue()) {

				AsyncJobs.submit(new Runnable() {

					@Override
					public void run() {
						try {

							DBCursor dbCur = getDBCollection(
									CollectionConstants.COLL_ORDERS).find(
									new BasicDBObject("_id", orderid),
									new BasicDBObject().append("uid", 1)
											.append("ac", 1));

							if (dbCur.hasNext()) {
								BasicDBObject lastOrder = (BasicDBObject) dbCur
										.next();
								long uid = lastOrder.getLong("uid");
								int ac = lastOrder.getInt("ac");

								// update user accumulative score
								getDBCollection(CollectionConstants.COLL_USERS)
										.update(new BasicDBObject("_id", uid),
												new BasicDBObject("$inc",
														new BasicDBObject("ac",
																ac)), false,
												false);
							}

						} catch (Throwable e) {
							log.error("success order error: " + e.getMessage());
						}
					}
				});
			}
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
	public List<Long> createOrder(final Orders order) {

		Map<Long, List<ItemDTO>> orderMap = order.getItems();

		List<DBObject> doclist = new ArrayList<DBObject>();
		List<Long> orderids = new ArrayList<Long>();

		final Map<Integer, List<Long>> statsMap = new HashMap<Integer, List<Long>>();

		for (Long shopid : orderMap.keySet()) {

			BasicDBObject doc = new BasicDBObject();

			long orderid = IDGenerator.nextOrderID(MongoAdaptor.getDB());
			doc.put("_id", orderid);
			doc.put("uid", order.getUid());
			doc.put("sid", shopid);

			List orders = new ArrayList();
			List<ItemDTO> li = orderMap.get(shopid);
			double total = 0;
			HashMap m = null;
			for (ItemDTO item : li) {
				m = new HashMap();
				m.put("id", item.getId());
				m.put("name", item.getName());
				int num = item.getNum();
				m.put("n", num);
				m.put("pp", item.getPrice());
				orders.add(m);
				total = Arith.add(total, item.getPrice() * num);

				if (statsMap.containsKey(num)) {
					statsMap.get(num).add(item.getId());
				} else {
					List<Long> ilist = new ArrayList<Long>();
					ilist.add(item.getId());
					statsMap.put(num, ilist);
				}
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
			int ac = (int) Math.round(total / 10);
			doc.put("ac", ac);

			Date d = new Date();
			doc.put("ts", d);
			doc.put("msg", order.getMsg());
			doc.put("sts", 0);

			doclist.add(doc);
			orderids.add(orderid);

		}

		if (doclist.size() > 0) {
			// insert new order info
			getDBCollection(CollectionConstants.COLL_ORDERS).insert(doclist);

			AsyncJobs.submit(new Runnable() {

				@Override
				public void run() {
					try {

						// update sales amount
						Set<Entry<Integer, List<Long>>> entries = statsMap
								.entrySet();
						for (Entry<Integer, List<Long>> entry : entries) {
							getDBCollection(CollectionConstants.COLL_ITEMS)
									.update(new BasicDBObject().append("_id",
											new BasicDBObject().append("$in",
													entry.getValue())),
											new BasicDBObject().append("$inc",
													new BasicDBObject("sale",
															entry.getKey())),
											false, true);
						}
					} catch (Throwable e) {
						log.info(e.getMessage());
					}
				}
			});

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
