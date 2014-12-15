package com.aug3.yhyc.api;

import java.io.Serializable;
import java.util.List;

public class ProductItem implements Serializable {

	private Item item;

	// 对应产品
	private ProductDTO product;

	private List<ItemDTO> promotion;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public List<ItemDTO> getPromotion() {
		return promotion;
	}

	public void setPromotion(List<ItemDTO> promotion) {
		this.promotion = promotion;
	}

}
