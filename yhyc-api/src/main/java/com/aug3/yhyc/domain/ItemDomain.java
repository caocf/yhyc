package com.aug3.yhyc.domain;

import com.aug3.yhyc.dao.ItemDao;
import com.aug3.yhyc.dto.CommentDTO;

public class ItemDomain {

	private ItemDao itemDao;

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public CommentDTO getComments(long itemId) {

		return itemDao.findComments(itemId);
	}

}
