package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.dao.ItemDao;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.CommentReq;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.valueobj.Comment;
import com.aug3.yhyc.valueobj.Item;
import com.aug3.yhyc.valueobj.Product;

public class ItemDomain {

	private ItemDao itemDao;

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public List<Item> findItemsByWorkshop(long workshop) {
		return itemDao.findItemsByWorkshop(workshop);
	}

	public ItemDTO findItemByID(long itemID) {
		Item item = itemDao.findItemByID(itemID);
		Product p = itemDao.findProductByID(item.getPid());
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setItem(item);
		itemDTO.setProduct(p);
		return itemDTO;
	}

	public CommentDTO findCommentsByItem(long itemId, int pn) {

		return itemDao.findComments(itemId, pn);
	}

	public boolean newComments(CommentReq commentReq) {

		String items = commentReq.getItems();
		if (items == null)
			return false;

		String[] parts = items.split(",");
		if (parts == null) {
			return false;
		} else {
			Comment comment = new Comment();

			if (commentReq.getUid() != null)
				comment.setUid(Long.parseLong(commentReq.getUid()));
			comment.setName(commentReq.getName());
			comment.setScore(Integer.parseInt(commentReq.getScore()));
			comment.setContent(commentReq.getContent());
			comment.setTs(DateUtil.formatCurrentDate());

			for (int i = 0; i < parts.length; i++) {
				itemDao.newComment(Long.parseLong(parts[i]), comment);
			}

			return true;
		}
	}

}
