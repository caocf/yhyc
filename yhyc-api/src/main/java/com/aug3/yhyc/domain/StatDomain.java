package com.aug3.yhyc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aug3.yhyc.dao.OrderDao;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.Order;
import com.aug3.yhyc.dto.ShopStats;
import com.aug3.yhyc.dto.TopItem;
import com.aug3.yhyc.util.Arith;

public class StatDomain {

	private OrderDao orderDao;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public ShopStats getShopDailyStats(long uid, long workshop, int status,
			int freq) {

		List<Order> orders = orderDao.find(workshop, status, freq);

		Map<Long, TopItem> stats = new HashMap<Long, TopItem>();

		int size = orders.size();
		double total = 0;

		for (Order order : orders) {
			total = Arith.add(total, order.getTotal());

			List<ItemDTO> items = order.getItems();

			for (ItemDTO i : items) {
				if (stats.containsKey(i.getId())) {
					TopItem ti = stats.get(i.getId());
					ti.setVolume(ti.getVolume() + i.getNum());
					stats.put(i.getId(), ti);
				} else {
					TopItem ti = new TopItem();
					ti.setId(i.getId());
					ti.setName(i.getName());
					ti.setVolume(i.getNum());
					stats.put(i.getId(), ti);
				}
			}
		}

		List<Map.Entry<Long, TopItem>> sortedStats = getSortedHashtableBySalesVolume(stats);

		if (sortedStats.size() > 20) {
			sortedStats = sortedStats.subList(0, 20);
		}

		List<TopItem> tops = new ArrayList<TopItem>();
		for (Map.Entry<Long, TopItem> stat : sortedStats) {
			tops.add(stat.getValue());
		}

		ShopStats ss = new ShopStats();
		ss.setVolume(size);
		ss.setSale(total);
		ss.setTopsales(tops);

		return ss;

	}

	public static List<Map.Entry<Long, TopItem>> getSortedHashtableBySalesVolume(
			Map<Long, TopItem> h) {

		List<Map.Entry<Long, TopItem>> l = new ArrayList<Map.Entry<Long, TopItem>>(
				h.entrySet());

		Collections.sort(l, new Comparator<Map.Entry<Long, TopItem>>() {
			public int compare(Map.Entry<Long, TopItem> o1,
					Map.Entry<Long, TopItem> o2) {
				return (o2.getValue().getVolume() - o1.getValue().getVolume());
			}
		});

		return l;
	}

}
