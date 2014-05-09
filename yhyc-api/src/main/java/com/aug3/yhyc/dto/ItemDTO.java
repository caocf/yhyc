package com.aug3.yhyc.dto;

import java.io.Serializable;

import com.aug3.yhyc.valueobj.Item;
import com.aug3.yhyc.valueobj.Product;

public class ItemDTO implements Serializable {

	private Item item;

	// 对应产品
	private Product product;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
