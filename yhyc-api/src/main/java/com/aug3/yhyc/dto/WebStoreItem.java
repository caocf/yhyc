package com.aug3.yhyc.dto;

import java.io.Serializable;

public class WebStoreItem implements Serializable {

	private String id;
	private String name;
	private String desc;
	private double price;
	private int ac;
	private int invt;
	private String pic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public int getInvt() {
		return invt;
	}

	public void setInvt(int invt) {
		this.invt = invt;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}
