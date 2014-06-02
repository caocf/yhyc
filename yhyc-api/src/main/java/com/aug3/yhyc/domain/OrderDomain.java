package com.aug3.yhyc.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aug3.yhyc.dao.ItemDao;
import com.aug3.yhyc.dao.OrderDao;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.Orders;

public class OrderDomain {

	private OrderDao orderDao;
	private ItemDao itemDao;

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

	/**
	 * 
	 * @param order
	 * @return
	 */
	public boolean newOrder(Orders order) {
		return orderDao.createOrder(order);
	}

	public List<Order> listOrders(long uid, int status) {

		List<Order> orders = orderDao.findOrdersByUid(uid, status);

		Set<Long> itemids = new HashSet<Long>();
		for (Order order : orders) {
			List<ItemDTO> items = order.getItems();
			for (ItemDTO item : items) {
				itemids.add(item.getId());
			}
		}
		Map<Long, ItemDTO> map = itemDao.findItemsMap(itemids);
		for (Order order : orders) {
			List<ItemDTO> items = order.getItems();
			for (ItemDTO item : items) {
				item.setPic(map.get(item.getId()).getPic());
			}
		}

		return orders;
	}

	public List<Order> listOrdersByWorkshop(long workshop, int status) {

		List<Order> orders = orderDao.findByWorkshop(workshop, status);

		return orders;
	}

	public Order showOrder(long orderid) {

		Order order = orderDao.findByID(orderid);

		return order;
	}
	
	public int deliverOrder(long orderid) {

		return orderDao.deliverOrder(orderid);

	}

	public int editOrderStatus(long orderid, int status) {
		return orderDao.updateStatus(orderid, status);
	}

}
