package com.aug3.yhyc.dto;

import java.io.Serializable;

public class OrderItem implements Serializable {

	// item id : workshopid + 分类号+流水号
	private long id;

	private long shop;

	// 商品名
	private String name;

	// num
	private long num;

	private double price;

	private double origprice;

	// accumulative score, not used for now
	private int ac;

	public long getId() {
		return id;
	}

	public long getShop() {
		return shop;
	}

	public void setShop(long shop) {
		this.shop = shop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getOrigprice() {
		return origprice;
	}

	public void setOrigprice(double origprice) {
		this.origprice = origprice;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

}
