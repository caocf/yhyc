package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.WorkshopDTO;
import com.aug3.yhyc.valueobj.Workshop;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

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
			shop.setDist(dbObj.getString("dist"));
			shop.setAddr(dbObj.getString("addr"));
			shop.setTel(dbObj.getString("tel"));
			shop.setStart(dbObj.getString("start"));
			shop.setShequ((List<Long>) dbObj.get("shequ"));
			shop.setCat((List<Integer>) dbObj.get("cat"));

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
			shop.setDist(dbObj.getString("dist"));
			shop.setAddr(dbObj.getString("addr"));
			shop.setTel(dbObj.getString("tel"));
			shop.setStart(dbObj.getString("start"));

			map.put(shop.getId(), shop);
		}

		return map;

	}

}
