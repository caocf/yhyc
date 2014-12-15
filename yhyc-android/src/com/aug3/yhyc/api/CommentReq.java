package com.aug3.yhyc.api;

import java.io.Serializable;
import java.util.Map;

public class CommentReq implements Serializable {

	private long uid;

	private long orderid;

	private Map<Long, Comment> itemsRating;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getOrderid() {
		return orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public Map<Long, Comment> getItemsRating() {
		return itemsRating;
	}

	public void setItemsRating(Map<Long, Comment> itemsRating) {
		this.itemsRating = itemsRating;
	}

}
