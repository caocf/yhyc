package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.List;

public class ShopStats implements Serializable {

	// 订单量
	private int volume;
	// 销售额
	private double sale;

	// 排名前20的商品
	private List<TopItem> topsales;

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public double getSale() {
		return sale;
	}

	public void setSale(double sale) {
		this.sale = sale;
	}

	public List<TopItem> getTopsales() {
		return topsales;
	}

	public void setTopsales(List<TopItem> topsales) {
		this.topsales = topsales;
	}

}
