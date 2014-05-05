package com.aug3.yhyc.domain;

import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.dao.ItemDao;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.CommentReq;
import com.aug3.yhyc.valueobj.Comment;

public class ItemDomain {

	private ItemDao itemDao;

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public CommentDTO getComments(long itemId, int pn) {

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
