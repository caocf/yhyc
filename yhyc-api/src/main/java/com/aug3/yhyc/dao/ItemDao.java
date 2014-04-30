package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.storage.mongoclient.MongoAdaptor;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.OrderItem;
import com.aug3.yhyc.valueobj.Comment;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

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

	public CommentDTO findComments(long itemId) {

		BasicDBObject qObj = new BasicDBObject("_id", itemId);

		DBCursor dbCur = MongoAdaptor.getDB().getCollection(CollectionConstants.COLL_COMMENTS).find(qObj);

		CommentDTO comments = new CommentDTO();
		Comment comment;

		comments.setId(itemId);

		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();

			comments.setCount(dbObj.getInt("count"));
			comments.setScore(dbObj.getInt("sc"));
			comments.setGood(dbObj.getInt("good"));
			comments.setNorm(dbObj.getInt("norm"));
			comments.setBad(dbObj.getInt("bad"));

			BasicDBList commentlist = (BasicDBList) dbObj.get("comment");
			for (Object obj : commentlist) {
				comment = new Comment();

				comments.getComments().add(comment);
			}

		}

		return comments;

	}

}
