package com.aug3.yhyc.domain;

import java.util.Date;
import java.util.List;

import com.aug3.yhyc.valueobj.CartItem;
import com.aug3.yhyc.valueobj.DeliveryContact;

public class OrderDomain {

	// 日期+卖家id+流水号
	private long orderID;

	private long uid;

	// 商品
	private List<CartItem> items;

	// 订单总价
	private double total;

	// 订单积分
	private int ac;

	// 订单收货人
	private DeliveryContact delivery;

	// 下单时间
	private Date ts;

	// 发货时间
	private Date dt;

	// 留言
	private String msg;

	// 待确认/发货中/交易取消/已发货/交易结束
	private int status;

	public boolean newOrder(String uid, String passwd) {
		// uid can be null
		// TODO MD5 validate
		return false;
	}

	/**
	 * 获得用户的递送地址列表
	 * 
	 * @param uid
	 * @return
	 */
	public List<DeliveryContact> fetchContacts(String uid) {
		return null;
	}

}
