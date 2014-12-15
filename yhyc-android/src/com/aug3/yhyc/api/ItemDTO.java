package com.aug3.yhyc.api;

import java.io.Serializable;

public class ItemDTO implements Serializable {

	// item id : workshopid + 分类号+流水号
	private long id;

	private long sid;

	// 商品名
	private String name;

	private String pic;

	private int act;

	// num
	private int num;

	private double price;

	private double origprice;

	// accumulative score, not used for now
	private int ac;

	public long getId() {
		return id;
	}

	public long getSid() {
		return sid;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
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
