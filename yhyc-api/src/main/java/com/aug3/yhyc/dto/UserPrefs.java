package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.List;

public class UserPrefs implements Serializable {

	private List<Long> fav;

	private List<Long> cart;

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

}
