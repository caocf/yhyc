package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.dao.OrderDao;
import com.aug3.yhyc.dto.Order;

public class OrderDomain {

	private OrderDao orderDao;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public boolean newOrder(Order order) {
		return orderDao.createOrder(order);
	}

	public List<Order> listOrders(String uid, int status) {

		List<Order> orders = orderDao.findOrdersByUid(uid, status);

		return orders;
	}

	public List<Order> listOrdersByWorkshop(long workshop, int status) {

		List<Order> orders = orderDao.findOrdersByWorkshop(workshop, status);

		return orders;
	}

	public Order showOrder(long orderid) {

		Order order = orderDao.findOrder(orderid);

		return order;
	}

	public void editOrderStatus(long orderid, int status) {
		orderDao.updateOrderStatus(orderid, status);
	}

}
