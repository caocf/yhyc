package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.Map;

import com.aug3.yhyc.valueobj.Comment;

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
