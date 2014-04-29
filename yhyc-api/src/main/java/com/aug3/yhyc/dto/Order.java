package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.aug3.yhyc.valueobj.DeliveryContact;

@SuppressWarnings("serial")
public class Order implements Serializable {

	// 日期+卖家id+流水号
	private long orderID;

	private long uid;

	// 商品
	private List<OrderItem> items;

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

	public long getOrderID() {
		return orderID;
	}

	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public DeliveryContact getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryContact delivery) {
		this.delivery = delivery;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
