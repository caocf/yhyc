package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.valueobj.Category;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class DictDao extends BaseDao {

	public Map<Integer, String> findTags(Collection<Integer> tagids) {

		BasicDBObject qObj = new BasicDBObject("_id", new BasicDBObject("$in",
				tagids));

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_TAGS).find(
				qObj);

		BasicDBObject dbObj;

		Map<Integer, String> tags = new ConcurrentHashMap<Integer, String>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			tags.put(dbObj.getInt("code"), dbObj.getString("name"));
		}

		return tags;
	}

	public List<Category> findCategories() {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_CATEGORIES)
				.find().sort(new BasicDBObject().append("code", 1));

		BasicDBObject dbObj;

		List<Category> categories = new ArrayList<Category>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			Category cat = new Category();
			cat.setCode(dbObj.getInt("code"));
			cat.setName(dbObj.getString("name"));
			cat.setP(dbObj.getInt("p"));

			categories.add(cat);
		}

		return categories;
	}
}
