package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.Map;

import com.aug3.yhyc.valueobj.Comment;

public class CommentReq implements Serializable {

	private long orderid;

	private Map<Long, Comment> itemsRating;

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
