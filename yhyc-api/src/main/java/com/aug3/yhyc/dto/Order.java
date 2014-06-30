package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.aug3.yhyc.valueobj.DeliveryContact;

@SuppressWarnings("serial")
public class Order implements Serializable {

	// 日期+流水号
	private long id;

	private long uid;

	private long sid;

	// 商品
	private List<ItemDTO> items;

	// 订单递送
	private DeliveryContact delivery;

	// 留言
	private String msg;

	// 订单总价
	private double total;

	// 订单积分
	private int ac;

	// 下单时间
	private Date ts;

	// 发货时间
	private Date dt;

	// 0:待确认/1:处理中/2:发货中/3:买家取消/4:卖家取消/5:交易结束/6:已评价
	private int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	public DeliveryContact getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryContact delivery) {
		this.delivery = delivery;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
