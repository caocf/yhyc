package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.util.RegexUtils;
import com.aug3.yhyc.valueobj.Shequ;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class ShequDao extends BaseDao {

	public List<Shequ> findShequ(String city, String q) {

		BasicDBObject qObj = new BasicDBObject("city", city);
		if (StringUtils.isNotBlank(q)) {
			if (RegexUtils.isLetter(q)) {
				// TODO db.shequ.ensureIndex( {py: "text"} )
				// qObj.append("$text", new BasicDBObject("$search", q));
				qObj.append("py", RegexUtils.wildMatch(q));
			} else {
				qObj.append("name", RegexUtils.wildMatch(q));
			}
		}

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_SHEQU).find(
				qObj);

		BasicDBObject dbObj;
		Shequ sq;

		List<Shequ> list = new ArrayList<Shequ>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			sq = new Shequ();
			sq.setId(dbObj.getLong("_id"));
			sq.setPy(dbObj.getString("py"));
			sq.setName(dbObj.getString("name"));
			sq.setCity(dbObj.getString("city"));
			sq.setDist(dbObj.getString("dist"));
			sq.setAddr(dbObj.getString("addr"));
			sq.setDesc(dbObj.getString("desc"));
			list.add(sq);
		}

		return list;

	}

	/**
	 * 根据shequ id列表获取对象
	 * 
	 * @param idset
	 * @return
	 */
	public Map<Long, Shequ> findShequ(Collection<Long> idset) {

		BasicDBObject qObj = new BasicDBObject("_id", new BasicDBObject("$in",
				idset));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_SHEQU).find(
				qObj);

		BasicDBObject dbObj;
		Shequ sq;

		Map<Long, Shequ> resultMap = new HashMap<Long, Shequ>();
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			sq = new Shequ();
			sq.setId(dbObj.getLong("_id"));
			sq.setPy(dbObj.getString("py"));
			sq.setName(dbObj.getString("name"));
			sq.setCity(dbObj.getString("city"));
			sq.setDist(dbObj.getString("dist"));
			sq.setAddr(dbObj.getString("addr"));
			sq.setDesc(dbObj.getString("desc"));
			resultMap.put(sq.getId(), sq);
		}

		return resultMap;

	}

}
