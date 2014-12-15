package com.aug3.yhyc.model;

import com.aug3.yhyc.base.Constants;

public class ShoppingCartItem extends GroupListItemBase {

	private long gid;
	private String url;
	private int act;
	private double price;
	private double origPrice;
	private int quantity = 1;

	public long getGid() {
		return gid;
	}

	public void setGid(long gid) {
		this.gid = gid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getOrigPrice() {
		return origPrice;
	}

	public void setOrigPrice(double origPrice) {
		this.origPrice = origPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getId()).append(Constants.COMMA).append(getTitle())
				.append(Constants.COMMA).append(getGid())
				.append(Constants.COMMA).append(getQuantity())
				.append(Constants.COMMA).append(getPrice());
		return sb.toString();

	}

}
