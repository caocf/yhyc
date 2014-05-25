package com.aug3.yhyc.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aug3.yhyc.base.CollectionConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class DictDao extends BaseDao {

	private static Map<Integer, String> tags = null;

	public Map<Integer, String> findTags(Collection<Integer> tagids) {

		if (tags == null) {

			BasicDBObject qObj = new BasicDBObject("_id", new BasicDBObject(
					"$in", tagids));

			DBCursor dbCur = getDBCollection(CollectionConstants.COLL_TAGS)
					.find(qObj);

			BasicDBObject dbObj;

			tags = new ConcurrentHashMap<Integer, String>();

			while (dbCur.hasNext()) {
				dbObj = (BasicDBObject) dbCur.next();

				tags.put(dbObj.getInt("_id"), dbObj.getString("name"));
			}

		}
		return tags;
	}
}
