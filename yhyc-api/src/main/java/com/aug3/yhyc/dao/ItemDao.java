package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.OrderItem;
import com.aug3.yhyc.valueobj.Comment;
import com.aug3.yhyc.valueobj.Item;
import com.aug3.yhyc.valueobj.Product;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;

public class ItemDao {

	public List<OrderItem> findItems(List<Long> itemIds) {

		List<OrderItem> list = new ArrayList<OrderItem>();

		if (itemIds != null && itemIds.size() > 0) {
			DBCursor dbCur = MongoAdaptor
					.getDB()
					.getCollection(CollectionConstants.COLL_ITEMS)
					.find(new BasicDBObject("_id", new BasicDBObject("$in", itemIds)),
							new BasicDBObject("name", 1).append("pic", 1).append("pp", 1).append("mp", 1));

			BasicDBObject dbObj;
			OrderItem oi;
			while (dbCur.hasNext()) {
				dbObj = (BasicDBObject) dbCur.next();
				oi = new OrderItem();
				oi.setId(dbObj.getLong("_id"));
				oi.setName(dbObj.getString("name"));
				oi.setPrice(dbObj.getDouble("pp"));
				oi.setOrigprice(dbObj.getDouble("mp"));
				// oi.setAc(ac);
				list.add(oi);
			}
		}

		return list;

	}

	public Item findItemByID(Long itemId) {

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_ITEMS)
				.find(new BasicDBObject("_id", itemId));

		BasicDBObject dbObj;
		Item item = null;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			item = new Item();
			item.setId(dbObj.getLong("_id"));
			item.setName(dbObj.getString("name"));
			item.setPp(dbObj.getDouble("pp"));
			item.setMp(dbObj.getDouble("mp"));
			item.setPid(dbObj.getLong("pid"));
			item.setFav(dbObj.getLong("fav"));
			item.setSales(dbObj.getLong("sale"));
		}

		return item;

	}

	public Product findProductByID(Long pid) {

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_PRODUCTS)
				.find(new BasicDBObject("_id", pid));

		BasicDBObject dbObj;
		Product p = null;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			p = new Product();
			p.setId(dbObj.getLong("_id"));
			p.setName(dbObj.getString("name"));
			p.setPic(dbObj.getString("pic"));
			p.setCat(dbObj.getString("cat"));

			// TODO
		}

		return p;

	}

	public List<Item> findItemsByWorkshop(long workshop) {

		List<Item> list = new ArrayList<Item>();

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_ITEMS)
				.find(new BasicDBObject("sid", workshop).append("sts", 1));

		BasicDBObject dbObj;
		Item oi;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			oi = new Item();
			oi.setId(dbObj.getLong("_id"));
			oi.setName(dbObj.getString("name"));
			oi.setPp(dbObj.getDouble("pp"));
			oi.setMp(dbObj.getDouble("mp"));
			oi.setPid(dbObj.getLong("pid"));
			list.add(oi);
		}

		return list;

	}

	public CommentDTO findComments(long itemId, int pn) {

		BasicDBObject qObj = new BasicDBObject("_id", itemId);

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_COMMENTS).find(qObj);

		CommentDTO commentDTO = new CommentDTO();

		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();

			BasicDBList commentlist = (BasicDBList) dbObj.get("comment");
			int start = (pn - 1) * Constants.PAGE_NUM;
			if (start >= commentlist.size()) {
				return commentDTO;
			}
			commentlist = (BasicDBList) commentlist.subList(start, start + Constants.PAGE_NUM);

			for (Object obj : commentlist) {
				BasicDBObject commentObj = (BasicDBObject) obj;
				Comment comment = new Comment();
				comment.setUid(commentObj.getLong("uid"));
				comment.setName(commentObj.getString("name"));
				comment.setContent(commentObj.getString("content"));
				comment.setScore(commentObj.getInt("score"));
				comment.setTs(commentObj.getString("ts"));
				commentDTO.getComments().add(comment);
			}

			commentDTO.setId(itemId);
			commentDTO.setCount(dbObj.getInt("count"));
			commentDTO.setScore(dbObj.getInt("score"));
			commentDTO.setGood(dbObj.getInt("good"));
			commentDTO.setNorm(dbObj.getInt("norm"));
			commentDTO.setBad(dbObj.getInt("bad"));

		}

		return commentDTO;

	}

	public void newComment(long itemId, Comment comment) {

		BasicDBObject sts = new BasicDBObject().append("count", 1).append("score", comment.getScore());
		switch (comment.getScore()) {
		case 4:
		case 5:
			sts.append("good", 1);
			break;
		case 3:
		case 2:
			sts.append("norm", 1);
			break;
		case 1:
			sts.append("bad", 1);
			break;
		}

		BasicDBObject commentObj = new BasicDBObject("comment", new BasicDBObject().append("$each",
				new Comment[] { comment }).append("$position", 0));
		BasicDBObject updateObj = new BasicDBObject().append("$inc", sts).append("$push", commentObj);
		// $addToSet for tags
		MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_COMMENTS)
				.update(new BasicDBObject("_id", itemId), updateObj, true, false, WriteConcern.SAFE);

	}
}
