package com.aug3.yhyc.api;

import java.io.Serializable;
import java.util.List;

public class ShopItem implements Serializable {

	private WorkshopDTO shop;
	private List<ItemDTO> item;

	public WorkshopDTO getShop() {
		return shop;
	}

	public void setShop(WorkshopDTO shop) {
		this.shop = shop;
	}

	public List<ItemDTO> getItem() {
		return item;
	}

	public void setItem(List<ItemDTO> item) {
		this.item = item;
	}

}