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
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class WorkshopDao extends BaseDao {

	public List<Workshop> findByShequ(long shequ, int cat) {

		List<Workshop> list = new ArrayList<Workshop>();

		BasicDBObject qObj = new BasicDBObject("shequ", new BasicDBObject(
				"$in", new Long[] { shequ })).append("cat", new BasicDBObject(
				"$in", new Integer[] { cat }));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.find(qObj);

		BasicDBObject dbObj;

		while (dbCur.hasNext()) {

			dbObj = (BasicDBObject) dbCur.next();

			list.add(newWorkshopByDBObj(dbObj));
		}

		return list;

	}

	public List<Workshop> findShopByUserID(long uid) {

		List<Workshop> list = new ArrayList<Workshop>();

		BasicDBList or = new BasicDBList();
		or.add(new BasicDBObject().append("owner", uid));
		or.add(new BasicDBObject().append("emp", new BasicDBObject("$in",
				new Long[] { uid })));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.find(new BasicDBObject("$or", or));

		BasicDBObject dbObj;

		while (dbCur.hasNext()) {

			dbObj = (BasicDBObject) dbCur.next();

			list.add(newWorkshopByDBObj(dbObj));
		}

		return list;

	}

	public Map<Long, WorkshopDTO> findByIDs(Collection<Long> ids) {

		BasicDBObject qObj = new BasicDBObject("_id", new BasicDBObject("$in",
				ids));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.find(qObj);

		Map<Long, WorkshopDTO> map = new HashMap<Long, WorkshopDTO>();
		BasicDBObject dbObj;
		WorkshopDTO shop;
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			shop = newWorkshopDTO(dbObj);

			map.put(shop.getId(), shop);
		}

		return map;

	}

	public WorkshopDTO findByID(long workshop) {

		BasicDBObject qObj = new BasicDBObject("_id", workshop);

		DBObject resultObj = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.findOne(qObj);

		if (resultObj != null) {
			BasicDBObject dbObj = (BasicDBObject) resultObj;

			return newWorkshopDTO(dbObj);
		}

		return null;

	}

	public WorkshopDTO findByID(long uid, long workshop) {

		BasicDBList or = new BasicDBList();
		or.add(new BasicDBObject().append("owner", uid));
		or.add(new BasicDBObject().append("emp", new BasicDBObject("$in",
				new Long[] { uid })));

		BasicDBObject qObj = new BasicDBObject("_id", workshop).append("$or",
				or);

		DBObject resultObj = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.findOne(qObj);

		if (resultObj != null) {
			BasicDBObject dbObj = (BasicDBObject) resultObj;

			return newWorkshopDTO(dbObj);
		}

		return null;

	}

	public WorkshopDTO find(long workshop) {

		BasicDBObject qObj = new BasicDBObject("_id", workshop);

		DBObject resultObj = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.findOne(qObj);

		if (resultObj != null) {
			BasicDBObject dbObj = (BasicDBObject) resultObj;

			return newWorkshopInfo(dbObj);
		}

		return null;

	}

	public boolean updateShopAnnouncement(long shop, String announcement) {
		WriteResult wr = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.update(new BasicDBObject("_id", shop),
						new BasicDBObject("$set", new BasicDBObject("notice",
								announcement)), false, false, WriteConcern.SAFE);

		if (wr != null) {
			return wr.getN() > 0;
		}

		return false;
	}

	public String getShopAnnouncement(long shop) {
		DBObject dbObj = getDBCollection(CollectionConstants.COLL_WORKSHOP)
				.findOne(new BasicDBObject("_id", shop));

		if (dbObj != null) {
			return ((BasicDBObject) dbObj).getString("notice");
		} else {
			return "";
		}
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

	private Workshop newWorkshopByDBObj(BasicDBObject dbObj) {

		Workshop shop = new Workshop();
		shop.setId(dbObj.getLong("_id"));
		shop.setName(dbObj.getString("name"));
		shop.setOwner(dbObj.getLong("owner"));
		shop.setEmp((List<Long>) dbObj.get("emp"));
		String pic = dbObj.getString("pic");
		shop.setPic(StringUtils.isBlank(pic) ? "" : Qiniu.downloadUrl(pic,
				Qiniu.getUserDomain()));
		shop.setCity(dbObj.getInt("city"));
		shop.setDist(dbObj.getInt("dist"));
		shop.setAddr(dbObj.getString("addr"));

		String tel = dbObj.getString("mobi");
		if (StringUtils.isBlank(tel)) {
			if (dbObj.containsField("tel"))
				tel = dbObj.getString("tel");
			else
				tel = "";
		}
		shop.setTel(tel);
		shop.setStart(dbObj.getString("start"));
		shop.setShequ((List<Long>) dbObj.get("shequ"));
		shop.setCat((List<Integer>) dbObj.get("cat"));
		shop.setNotice(dbObj.getString("notice"));

		return shop;
	}

	private WorkshopDTO newWorkshopDTO(BasicDBObject dbObj) {

		WorkshopDTO shop = getShopInfo(dbObj);

		return shop;
	}

	private WorkshopDTO newWorkshopInfo(BasicDBObject dbObj) {

		WorkshopDTO shop = getShopInfo(dbObj);

		String fn = dbObj.containsField("ad") ? dbObj.getString("ad") : "";
		if (StringUtils.isNotBlank(fn)) {
			shop.setAd(Qiniu.downloadUrl(fn, Qiniu.getUserDomain()));
		} else {
			shop.setAd("");
		}

		return shop;
	}

	private WorkshopDTO getShopInfo(BasicDBObject dbObj) {

		WorkshopDTO shop = new WorkshopDTO();
		shop.setId(dbObj.getLong("_id"));
		shop.setName(dbObj.getString("name"));
		shop.setOwner(dbObj.getLong("owner"));
		shop.setWho(dbObj.getString("who"));
		shop.setAddr(dbObj.getString("addr"));
		shop.setTel(dbObj.containsField("mobi") ? dbObj.getString("mobi")
				: dbObj.getString("tel"));
		shop.setStart(dbObj.getString("start"));
		shop.setNotice(dbObj.getString("notice"));
		shop.setCat((List<Integer>) dbObj.get("cat"));

		return shop;
	}

}
