package com.aug3.yhyc.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

	// ����+��ˮ��
	private long id;

	private long uid;

	private long sid;

	// ��Ʒ
	private List<ItemDTO> items;

	// ��������
	private DeliveryContact delivery;

	// ����
	private String msg;

	// �����ܼ�
	private double total;

	// ��������
	private int ac;

	// �µ�ʱ��
	private Date ts;

	// ����ʱ��
	private Date dt;

	// 0:��ȷ��/1:��ȷ��/2:������/3:����ȡ��/4:�ѷ���/5:���׽���
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