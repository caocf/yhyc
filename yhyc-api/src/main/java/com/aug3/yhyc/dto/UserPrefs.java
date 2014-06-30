package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserPrefs implements Serializable {

	private List<Long> fav;

	private List<Long> cart;

	private Map<String, String> urls;

	public List<Long> getFav() {
		return fav;
	}

	public void setFav(List<Long> fav) {
		this.fav = fav;
	}

	public List<Long> getCart() {
		return cart;
	}

	public void setCart(List<Long> cart) {
		this.cart = cart;
	}

	public Map<String, String> getUrls() {
		return urls;
	}

	public void setUrls(Map<String, String> urls) {
		this.urls = urls;
	}

}
