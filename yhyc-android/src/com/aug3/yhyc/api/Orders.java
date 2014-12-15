package com.aug3.yhyc.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Orders implements Serializable {

	private long uid;

	// ��Ʒ
	private Map<Long, List<ItemDTO>> items;

	// ��������
	private DeliveryContact delivery;

	// ����
	private String msg;

	// �����ܼ�
	private double total;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public Map<Long, List<ItemDTO>> getItems() {
		return items;
	}

	public void setItems(Map<Long, List<ItemDTO>> items) {
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

}