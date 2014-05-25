package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.List;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.util.Qiniu;
import com.aug3.yhyc.valueobj.Comment;
import com.aug3.yhyc.valueobj.Item;
import com.aug3.yhyc.valueobj.Product;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;

public class ItemDao extends BaseDao {

	public List<ItemDTO> findItems(List<Long> itemIds) {

		List<ItemDTO> list = new ArrayList<ItemDTO>();

		if (itemIds != null && itemIds.size() > 0) {
			DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS)
					.find(new BasicDBObject("_id", new BasicDBObject("$in",
							itemIds)),
							new BasicDBObject("name", 1).append("pp", 1)
									.append("mp", 1).append("pid", 1)
									.append("sid", 1).append("act", 1)).sort(
							new BasicDBObject("sid", 1));

			BasicDBObject dbObj;
			while (dbCur.hasNext()) {
				dbObj = (BasicDBObject) dbCur.next();

				list.add(transferDBObj2ItemDTO(dbObj));
			}
		}

		return list;

	}

	public List<ItemDTO> findPromotionItems(Long workshop, int act) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				new BasicDBObject("_id", new BasicDBObject().append("$gte",
						workshop * 1000).append("$lte", workshop * 1000 + 999))
						.append("sid", workshop).append("act", act));

		List<ItemDTO> list = new ArrayList<ItemDTO>();

		BasicDBObject dbObj;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			list.add(transferDBObj2ItemDTO(dbObj));
		}

		return list;

	}

	public List<ItemDTO> findPromotionItems(Long workshop, int act,
			long excludeItem) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				new BasicDBObject("_id", new BasicDBObject()
						.append("$gte", workshop * 1000)
						.append("$lte", workshop * 1000 + 999)
						.append("$ne", excludeItem)).append("sid", workshop)
						.append("act", act));

		List<ItemDTO> list = new ArrayList<ItemDTO>();

		BasicDBObject dbObj;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			list.add(transferDBObj2ItemDTO(dbObj));
		}

		return list;

	}

	public Item findItemByID(Long itemId) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				new BasicDBObject("_id", itemId));

		BasicDBObject dbObj;
		Item item = null;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			item = transferDBObj2Item(dbObj);
		}

		return item;

	}

	@SuppressWarnings("unchecked")
	public Product findProductByID(Long pid) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_PRODUCTS)
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
			p.setTags((List<Integer>) dbObj.get("tags"));
			p.setRef((List<Long>) dbObj.get("ref"));
			p.setDesc(dbObj.getString("desc"));
			p.setSts(dbObj.getInt("sts"));
		}

		return p;

	}

	public List<Item> findItemsByWorkshop(long workshop) {

		List<Item> list = new ArrayList<Item>();

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				new BasicDBObject("_id", new BasicDBObject().append("$gte",
						workshop * 1000).append("$lte", workshop * 1000 + 999))
						.append("sid", workshop).append("sts", 1));

		BasicDBObject dbObj;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			list.add(transferDBObj2Item(dbObj));
		}

		return list;

	}

	private Item transferDBObj2Item(BasicDBObject dbObj) {

		Item item = new Item();
		item.setId(dbObj.getLong("_id"));
		item.setName(dbObj.getString("name"));
		item.setSid(dbObj.getLong("sid"));
		item.setPp(dbObj.getDouble("pp"));
		item.setMp(dbObj.getDouble("mp"));
		item.setPid(dbObj.getLong("pid"));
		item.setAct(dbObj.getInt("act"));
		item.setPic(Qiniu.downloadUrl(item.getPid() + Constants.PNG));
		item.setFav(dbObj.getLong("fav"));
		item.setSales(dbObj.getLong("sale"));
		return item;
	}

	private ItemDTO transferDBObj2ItemDTO(BasicDBObject dbObj) {

		ItemDTO i = new ItemDTO();
		i.setId(dbObj.getLong("_id"));
		i.setSid(dbObj.getLong("sid"));
		i.setPid(dbObj.getLong("pid"));
		i.setName(dbObj.getString("name"));
		i.setPrice(dbObj.getDouble("pp"));
		i.setOrigprice(dbObj.getDouble("mp"));
		i.setAct(dbObj.getInt("act"));
		i.setPic(Qiniu.downloadUrl(i.getPid() + Constants.PNG));
		return i;
	}

	public CommentDTO findComments(long itemId, int pn) {

		BasicDBObject qObj = new BasicDBObject("_id", itemId);

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_COMMENTS)
				.find(qObj);

		CommentDTO commentDTO = new CommentDTO();

		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();

			BasicDBList commentlist = (BasicDBList) dbObj.get("comment");
			int start = skipSize(pn);
			if (start >= commentlist.size()) {
				return commentDTO;
			}
			commentlist = (BasicDBList) commentlist.subList(start, start
					+ pageSize());

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

		BasicDBObject sts = new BasicDBObject().append("count", 1).append(
				"score", comment.getScore());
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

		BasicDBObject commentObj = new BasicDBObject("comment",
				new BasicDBObject().append("$each", new Comment[] { comment })
						.append("$position", 0));
		BasicDBObject updateObj = new BasicDBObject().append("$inc", sts)
				.append("$push", commentObj);
		// $addToSet for tags
		getDBCollection(CollectionConstants.COLL_COMMENTS).update(
				new BasicDBObject("_id", itemId), updateObj, true, false,
				WriteConcern.SAFE);

	}
}
