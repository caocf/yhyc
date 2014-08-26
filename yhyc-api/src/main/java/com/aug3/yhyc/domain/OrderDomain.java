package com.aug3.yhyc.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aug3.yhyc.dao.ItemDao;
import com.aug3.yhyc.dao.OrderDao;
import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.dao.WorkshopDao;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.Orders;
import com.aug3.yhyc.util.AndroidPushService;
import com.aug3.yhyc.util.AsyncJobs;
import com.aug3.yhyc.util.ConfigManager;
import com.aug3.yhyc.valueobj.PushReceiver;

public class OrderDomain {

	private final static String notify_title = ConfigManager.getProperties()
			.getProperty("pushservice.notify.title");
	private final static String notify_desc = ConfigManager.getProperties()
			.getProperty("pushservice.notify.desc");

	private OrderDao orderDao;
	private ItemDao itemDao;
	private UserDao userDao;
	private WorkshopDao shopDao;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public WorkshopDao getShopDao() {
		return shopDao;
	}

	public void setShopDao(WorkshopDao shopDao) {
		this.shopDao = shopDao;
	}

	/**
	 * 
	 * @param order
	 * @return
	 */
	public List<Long> newOrder(Orders orders) {

		List<Long> orderids = orderDao.createOrder(orders);

		postOrder(orders);

		return orderids;

	}

	private void postOrder(final Orders orders) {

		AsyncJobs.submit(new Runnable() {

			@Override
			public void run() {

				// remove item from user cart after order created
				Set<Long> itemids = new HashSet<Long>();
				Set<Long> shops = orders.getItems().keySet();
				for (Long shop : shops) {
					List<ItemDTO> items = orders.getItems().get(shop);
					for (ItemDTO i : items) {
						itemids.add(i.getId());
					}
				}
				userDao.removeUserPrefs(orders.getUid(), "cart", itemids);

				// push notification to mobile
				Set<Long> emps = shopDao.findShopKeeper(shops);

				List<PushReceiver> receivers = userDao.filterPushReceiver(emps);

				String desc_content = notify_desc + " "
						+ orders.getDelivery().getAddr().replace("\n", "");
				Set<String> pushed = new HashSet<String>();
				for (PushReceiver rec : receivers) {
					String id = rec.getChannelId() + ":" + rec.getUserId();
					if (!pushed.contains(id)) {
						pushed.add(id);
						AndroidPushService.sendNotification(rec.getChannelId(),
								rec.getUserId(), notify_title, desc_content);
					}
				}
			}
		});

	}

	public List<Order> listOrders(long uid, int status) {

		List<Order> orders = orderDao.findOrdersByUid(uid, status);

		getOrderDetails(orders);

		return orders;
	}

	public List<Order> listOrdersByID(Collection<Long> orderids, int status) {

		List<Order> orders = orderDao.findOrdersByIds(orderids, status);

		getOrderDetails(orders);

		return orders;
	}

	private void getOrderDetails(List<Order> orders) {

		Set<Long> itemids = new HashSet<Long>();
		for (Order order : orders) {
			List<ItemDTO> items = order.getItems();
			for (ItemDTO item : items) {
				itemids.add(item.getId());
			}
		}
		Map<Long, ItemDTO> map = itemDao.findItemsMap(itemids);
		ItemDTO i = null;
		for (Order order : orders) {
			List<ItemDTO> items = order.getItems();
			for (ItemDTO item : items) {
				i = map.get(item.getId());
				if (i != null) {
					item.setPic(i.getPic());
				}
			}
		}
	}

	public List<Order> listOrdersByWorkshop(long workshop, int status) {

		List<Order> orders = orderDao.findByWorkshop(workshop, status);
		getOrderDetails(orders);

		return orders;
	}

	public Order showOrder(long orderid) {

		Order order = orderDao.findByID(orderid);

		return order;
	}

	public int editOrderStatus(long orderid, int status) {
		return orderDao.updateStatus(orderid, status);
	}

}
