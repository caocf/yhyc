package com.aug3.yhyc.valueobj;

public class CartItem {

	// item id
	private long id;

	// num
	private long num;

	private double price;

	private double origprice;

	// accumulative score, not used for now
	private int ac;

	public long getId() {
		return id;
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
