package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.WebStoreItem;
import com.aug3.yhyc.util.Qiniu;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class WebStoreDao extends BaseDao {

	/**
	 * 根据shequ id列表获取对象
	 * 
	 * @param idset
	 * @return
	 */
	public List<WebStoreItem> findAll() {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WEBSTORE)
				.find(new BasicDBObject("sts", 1)).sort(
						new BasicDBObject("ac", 1));

		BasicDBObject dbObj;
		List<WebStoreItem> list = new ArrayList<WebStoreItem>();
		WebStoreItem item;

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			item = new WebStoreItem();
			item.setId(dbObj.get("_id").toString());
			item.setName(dbObj.getString("name"));
			item.setDesc(dbObj.getString("desc"));
			item.setPrice(dbObj.getDouble("price"));
			item.setAc(dbObj.getInt("ac"));
			item.setInvt(dbObj.getInt("invt"));
			item.setPic(Qiniu.downloadUrl(dbObj.getString("pic"),
					Qiniu.getAppDomain()));
			list.add(item);
		}

		return list;

	}

	public boolean addIntegralExchange(long uid, String item, String name,
			String addr, String mobi) {

		BasicDBObject queryUser = new BasicDBObject("_id", uid);

		DBObject userObj = getDBCollection(CollectionConstants.COLL_USERS)
				.findOne(queryUser);

		if (userObj == null) {
			return false;
		}

		int myac = (Integer) userObj.get("ac");

		BasicDBObject queryItem = new BasicDBObject("_id", new ObjectId(item));

		DBObject itemObj = getDBCollection(CollectionConstants.COLL_WEBSTORE)
				.findOne(queryItem);
		if (itemObj == null) {
			return false;
		}

		int itemac = ((Long) itemObj.get("ac")).intValue();

		if (myac < itemac) {
			return false;
		}

		BasicDBObject dbObj = new BasicDBObject().append("uid", uid)
				.append("itemid", item).append("name", name)
				.append("ac", itemac).append("addr", addr).append("mobi", mobi)
				.append("ts", new Date());

		WriteResult wr = getDBCollection(CollectionConstants.COLL_EXCHGRECORDS)
				.insert(dbObj);

		getDBCollection(CollectionConstants.COLL_USERS).update(
				queryUser,
				new BasicDBObject("$set",
						new BasicDBObject("ac", myac - itemac)), false, false);

		// getDBCollection(CollectionConstants.COLL_WEBSTORE).update(queryItem,
		// new BasicDBObject("$dec", new BasicDBObject("invt", 1)), false,
		// false);
		return true;
	}

}
