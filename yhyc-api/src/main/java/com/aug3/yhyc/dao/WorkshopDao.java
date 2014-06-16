package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.RequestShop;
import com.aug3.yhyc.dto.WorkshopDTO;
import com.aug3.yhyc.util.IDGenerator;
import com.aug3.yhyc.util.Qiniu;
import com.aug3.yhyc.valueobj.Workshop;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class WorkshopDao extends BaseDao {

	public List<Workshop> findByShequ(long shequ, int cat) {

		List<Workshop> list = new ArrayList<Workshop>();

		BasicDBObject qObj = new BasicDBObject("shequ", new BasicDBObject(
				"$in", new Long[] { shequ })).append("cat", new BasicDBObject(
				"$in", new Integer[] { cat }));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.find(qObj);

		BasicDBObject dbObj;
		Workshop shop;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			shop = new Workshop();
			shop.setId(dbObj.getLong("_id"));
			shop.setName(dbObj.getString("name"));
			shop.setOwner(dbObj.getString("owner"));
			String pic = dbObj.getString("pic");
			shop.setPic(StringUtils.isBlank(pic) ? "" : Qiniu.downloadUrl(pic,
					Qiniu.getUserDomain()));
			shop.setCity(dbObj.getInt("city"));
			shop.setDist(dbObj.getInt("dist"));
			shop.setAddr(dbObj.getString("addr"));
			shop.setTel(dbObj.getString("tel"));
			shop.setStart(dbObj.getString("start"));
			shop.setShequ((List<Long>) dbObj.get("shequ"));
			shop.setCat((List<Integer>) dbObj.get("cat"));
			shop.setNotice(dbObj.getString("notice"));

			list.add(shop);
		}

		return list;

	}

	public Map<Long, WorkshopDTO> findByID(Collection<Long> ids) {

		BasicDBObject qObj = new BasicDBObject("_id", new BasicDBObject("$in",
				ids));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.find(qObj);

		Map<Long, WorkshopDTO> map = new HashMap<Long, WorkshopDTO>();
		BasicDBObject dbObj;
		WorkshopDTO shop;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			shop = new WorkshopDTO();
			shop.setId(dbObj.getLong("_id"));
			shop.setName(dbObj.getString("name"));
			shop.setOwner(dbObj.getString("owner"));
			shop.setAddr(dbObj.getString("addr"));
			shop.setTel(dbObj.getString("tel"));
			shop.setStart(dbObj.getString("start"));
			shop.setNotice(dbObj.getString("notice"));
			shop.setCat((List<Integer>) dbObj.get("cat"));

			map.put(shop.getId(), shop);
		}

		return map;

	}

	public boolean requestShop(RequestShop shop) {

		DBObject doc = new BasicDBObject();
		doc.put("_id", IDGenerator.nextval(getDB(), "requestshop"));
		doc.put("uid", shop.getUid());
		doc.put("owner", shop.getOwner());
		doc.put("idcard", shop.getIdcard());
		doc.put("tel", shop.getTel());
		doc.put("mail", shop.getMail());
		doc.put("dist", shop.getDist());
		doc.put("busi", shop.getBusi());
		doc.put("exp", shop.getExp());
		doc.put("desc", shop.getDesc());

		getDBCollection(CollectionConstants.COLL_REQSHOP).insert(doc);

		return true;

	}

	public boolean requestShopExist(RequestShop shop) {

		BasicDBList or = new BasicDBList();
		or.add(new BasicDBObject().append("idcard", shop.getIdcard()));
		or.add(new BasicDBObject().append("mail", shop.getMail()));
		or.add(new BasicDBObject().append("tel", shop.getTel()));
		if (shop.getUid() != 0) {
			or.add(new BasicDBObject().append("uid", shop.getUid()));
		}

		DBObject result = getDBCollection(CollectionConstants.COLL_REQSHOP)
				.findOne(new BasicDBObject("$or", or));

		if (result == null) {
			return false;
		}

		return true;

	}

}
