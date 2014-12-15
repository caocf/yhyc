package com.aug3.yhyc.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserPrefs implements Serializable {

	private List<Long> fav;

	private List<Long> cart;

	private int ac;

	private Map<String, String> urls;

	// version of client
	private int verCode;
	private String verName;

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

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public Map<String, String> getUrls() {
		return urls;
	}

	public void setUrls(Map<String, String> urls) {
		this.urls = urls;
	}

	public int getVerCode() {
		return verCode;
	}

	public void setVerCode(int verCode) {
		this.verCode = verCode;
	}

	public String getVerName() {
		return verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

}
