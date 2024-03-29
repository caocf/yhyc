package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class ItemDao extends BaseDao {

	private final static Logger logger = Logger.getLogger(ItemDao.class);

	public List<ItemDTO> findItems(List<Long> itemIds, int sts) {

		List<ItemDTO> list = new ArrayList<ItemDTO>();

		if (itemIds != null && itemIds.size() > 0) {
			DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS)
					.find(new BasicDBObject("_id", new BasicDBObject("$in",
							itemIds)).append("sts", sts),
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

	public Map<Long, ItemDTO> findItemsMap(Collection<Long> itemIds) {

		Map<Long, ItemDTO> map = new HashMap<Long, ItemDTO>();

		if (itemIds != null && itemIds.size() > 0) {
			DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS)
					.find(new BasicDBObject("_id", new BasicDBObject("$in",
							itemIds)),
							new BasicDBObject("name", 1).append("pp", 1)
									.append("mp", 1).append("pid", 1)
									.append("sid", 1).append("act", 1));

			BasicDBObject dbObj;
			while (dbCur.hasNext()) {
				dbObj = (BasicDBObject) dbCur.next();

				map.put(dbObj.getLong("_id"), transferDBObj2ItemDTO(dbObj));
			}
		}

		return map;

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

		Item item = null;
		BasicDBObject dbObj;
		if (dbCur != null) {
			while (dbCur.hasNext()) {
				dbObj = (BasicDBObject) dbCur.next();
				item = transferDBObj2Item(dbObj);
			}
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
			p.setCat(dbObj.getInt("cat"));
			p.setTags((List<Integer>) dbObj.get("tags"));
			p.setRef((List<Long>) dbObj.get("ref"));
			p.setDesc(dbObj.getString("desc"));
			p.setCooked(dbObj.getString("cook"));
			String caipu = dbObj.getString("cpic");
			if (StringUtils.isNotBlank(caipu)) {
				p.setCpic(getCaipuUrl(dbObj.getString("cpic")));
			}
			p.setSeason(dbObj.getBoolean("season", false));
			p.setSts(dbObj.getInt("sts"));
		}

		return p;

	}

	public List<Product> findProducts(long workshop, int cat) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS)
				.find(new BasicDBObject("sid", workshop),
						new BasicDBObject("pid", 1));
		Set<Long> pids = new HashSet<Long>();
		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();
			pids.add(dbObj.getLong("pid"));
		}

		dbCur = getDBCollection(CollectionConstants.COLL_PRODUCTS).find(
				new BasicDBObject().append("_id",
						new BasicDBObject().append("$gte", cat * 100000)
								.append("$lte", cat * 100000 + 99999)));

		BasicDBObject dbObj;
		List<Product> resultList = new ArrayList<Product>();
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			long id = dbObj.getLong("_id");

			if (!pids.contains(id)) {
				Product p = new Product();
				p.setId(id);
				p.setName(dbObj.getString("name"));
				String pic = dbObj.getString("pic");
				if (pic == null) {
					logger.error("missing pic for product " + id);
				} else {
					p.setPic(getProductPic(pic));
				}
				p.setCat(dbObj.getInt("cat"));
				p.setSeason(dbObj.getBoolean("season", false));
				p.setSts(dbObj.getInt("sts"));

				resultList.add(p);
			}
		}

		return resultList;

	}

	public long newItem(Item item) {

		long id = nextItemID(item.getSid());

		BasicDBObject dbObj = new BasicDBObject().append("_id", id)
				.append("name", item.getName()).append("sid", item.getSid())
				.append("pid", item.getPid()).append("pp", item.getPp())
				.append("mp", item.getMp()).append("act", item.getAct())
				.append("spec", new String[] {}).append("fav", 0L)
				.append("inv", 100L).append("sale", 0L)
				.append("sts", item.getSts());

		getDBCollection(CollectionConstants.COLL_ITEMS).insert(dbObj);

		return id;

	}

	private long nextItemID(long sid) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS)
				.find(new BasicDBObject("sid", sid),
						new BasicDBObject("_id", 1))
				.sort(new BasicDBObject("_id", -1)).limit(1);

		long nid = 0;
		if (dbCur != null) {
			while (dbCur.hasNext()) {
				DBObject dbObj = dbCur.next();
				nid = Long.parseLong(dbObj.get("_id").toString()) + 1;
			}
		}

		if (nid == 0) {
			nid = sid * 1000 + 1;
		}

		return nid;
	}

	public boolean updateItem(long workshop, Item item) {

		WriteResult wr = null;

		BasicDBObject updateObj = new BasicDBObject("$set", new BasicDBObject(
				"name", item.getName()).append("pp", item.getPp())
				.append("mp", item.getMp()).append("sts", item.getSts())
				.append("act", item.getAct()));

		wr = getDBCollection(CollectionConstants.COLL_ITEMS).update(
				new BasicDBObject("_id", item.getId()).append("sid", workshop),
				updateObj, false, false, WriteConcern.SAFE);

		if (wr != null) {
			return wr.getN() > 0;
		}

		return false;
	}

	public List<Item> findItemsByWorkshop(long workshop) {

		List<Item> list = new ArrayList<Item>();

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				new BasicDBObject("_id", new BasicDBObject().append("$gte",
						workshop * 1000).append("$lte", workshop * 1000 + 999))
						.append("sid", workshop).append("sts",
								new BasicDBObject("$gte", 1)));

		BasicDBObject dbObj;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			list.add(transferDBObj2Item(dbObj));
		}

		return list;

	}

	public List<Item> findPromotionItemsByWorkshop(long workshop) {

		List<Item> list = new ArrayList<Item>();

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				new BasicDBObject("_id", new BasicDBObject().append("$gte",
						workshop * 1000).append("$lte", workshop * 1000 + 999))
						.append("sid", workshop).append("act", 1)
						.append("sts", new BasicDBObject("$gte", 1)));

		BasicDBObject dbObj;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			list.add(transferDBObj2Item(dbObj));
		}

		return list;

	}

	public List<Item> filterItemsByWorkshop(long workshop, int cat,
			boolean allStat) {

		List<Item> list = new ArrayList<Item>();

		BasicDBObject queryObj = new BasicDBObject("_id", new BasicDBObject()
				.append("$gte", workshop * 1000).append("$lte",
						workshop * 1000 + 999)).append("sid", workshop).append(
				"pid",
				new BasicDBObject().append("$gte", cat * 100000).append("$lte",
						cat * 100000 + 99999));
		if (!allStat) {
			queryObj.append("sts", new BasicDBObject("$gte", 1));
		}

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				queryObj).sort(new BasicDBObject("sale", -1));

		BasicDBObject dbObj;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			list.add(transferDBObj2Item(dbObj));
		}

		return list;

	}

	public List<Item> filterPromotionItemsByWorkshop(long workshop, int cat,
			boolean allStat) {

		List<Item> list = new ArrayList<Item>();

		BasicDBObject queryObj = new BasicDBObject("_id", new BasicDBObject()
				.append("$gte", workshop * 1000).append("$lte",
						workshop * 1000 + 999))
				.append("sid", workshop)
				.append("act", 1)
				.append("pid",
						new BasicDBObject().append("$gte", cat * 100000)
								.append("$lte", cat * 100000 + 99999));
		if (!allStat) {
			queryObj.append("sts", new BasicDBObject("$gte", 1));
		}

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				queryObj).sort(new BasicDBObject("sale", -1));

		BasicDBObject dbObj;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			list.add(transferDBObj2Item(dbObj));
		}

		return list;

	}

	public List<Item> filterItems(long workshop, int cat, boolean allStat) {

		List<Item> list = new ArrayList<Item>();

		BasicDBObject queryObj = new BasicDBObject("_id", new BasicDBObject()
				.append("$gte", workshop * 1000).append("$lte",
						workshop * 1000 + 999)).append("sid", workshop).append(
				"pid",
				new BasicDBObject().append("$gte", cat * 1000).append("$lte",
						cat * 1000 + 999));
		if (!allStat) {
			queryObj.append("sts", new BasicDBObject("$gte", 1));
		}

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_ITEMS).find(
				queryObj);

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
		item.setPic(getItemUrl(item.getPid()));
		item.setFav(dbObj.getLong("fav"));
		item.setSales(dbObj.getLong("sale"));
		item.setSts(dbObj.getInt("sts"));
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
		i.setPic(getItemUrl(i.getPid()));
		return i;
	}

	public CommentDTO findComments(long itemId, int pn) {

		BasicDBObject qObj = new BasicDBObject("_id", itemId);

		BasicDBObject slice = new BasicDBObject("comment", new BasicDBObject(
				"$slice", new Integer[] { skipSize(pn), pageSize() }));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_COMMENTS)
				.find(qObj, slice);

		CommentDTO commentDTO = new CommentDTO();

		while (dbCur.hasNext()) {

			BasicDBObject dbObj = (BasicDBObject) dbCur.next();

			BasicDBList commentlist = (BasicDBList) dbObj.get("comment");

			for (Object obj : commentlist) {
				BasicDBObject commentObj = (BasicDBObject) obj;
				Comment comment = new Comment();
				comment.setUid(commentObj.getLong("uid"));
				String name = commentObj.getString("name");
				if (StringUtils.isNotBlank(name)) {
					if (name.length() == 11) {
						name = name.replace(name.substring(3, 7), "****");
					} else {
						name = "*" + name.substring(1);
					}
				}
				comment.setName(name);
				comment.setContent(commentObj.getString("content"));
				comment.setScore(commentObj.getInt("score"));
				comment.setTs(commentObj.getString("ts"));
				commentDTO.getComments().add(comment);
			}

			commentDTO.setId(itemId);
			commentDTO.setCount(dbObj.getInt("count"));
			commentDTO.setScore(dbObj.getInt("score"));
			commentDTO.setGood(dbObj.containsField("good") ? dbObj
					.getInt("good") : 0);
			commentDTO.setNorm(dbObj.containsField("norm") ? dbObj
					.getInt("norm") : 0);
			commentDTO.setBad(dbObj.containsField("bad") ? dbObj.getInt("bad")
					: 0);

		}

		return commentDTO;

	}

	public void newComment(long itemId, Comment comment) {

		BasicDBObject incr_sts = new BasicDBObject().append("count", 1).append(
				"score", comment.getScore());
		switch (comment.getScore()) {
		case 4:
		case 5:
			incr_sts.append("good", 1);
			break;
		case 3:
		case 2:
			incr_sts.append("norm", 1);
			break;
		case 1:
			incr_sts.append("bad", 1);
			break;
		}

		// TODO can use $sort to sort array

		DBObject commentObj = new BasicDBObject();
		commentObj.put("uid", comment.getUid());
		commentObj.put("name", comment.getName());
		commentObj.put("content", comment.getContent());
		commentObj.put("score", comment.getScore());
		commentObj.put("ts", comment.getTs());

		BasicDBObject add_commentObj = new BasicDBObject("comment",
				new BasicDBObject().append("$each",
						new DBObject[] { commentObj }).append("$position", 0));

		BasicDBObject updateObj = new BasicDBObject().append("$inc", incr_sts)
				.append("$push", add_commentObj);

		// $addToSet for tags
		getDBCollection(CollectionConstants.COLL_COMMENTS).update(
				new BasicDBObject("_id", itemId), updateObj, true, false,
				WriteConcern.SAFE);

	}

	public String getItemUrl(long pid) {
		return Qiniu.downloadUrl(pid + Constants.PNG, Qiniu.getItemDomain());
	}

	public String getProductPic(String url) {
		return Qiniu.downloadUrl(url, Qiniu.getItemDomain());
	}

	public String getCaipuUrl(String cookPic) {
		return Qiniu.downloadUrl(cookPic, Qiniu.getCaipuDomain());
	}
}
