package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.valueobj.Category;
import com.aug3.yhyc.valueobj.Region;
import com.aug3.yhyc.valueobj.Tag;
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

	public List<Tag> findAll() {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_TAGS).find();

		BasicDBObject dbObj;

		List<Tag> list = new ArrayList<Tag>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();

			Tag tag = new Tag();
			tag.setCode(dbObj.getInt("code"));
			tag.setName(dbObj.getString("name"));
			
			list.add(tag);
			
		}

		return list;
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

	public List<Region> findRegion() {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_REGIONS)
				.find().sort(new BasicDBObject("level", 1));

		BasicDBObject dbObj;
		Region region;

		List<Region> list = new ArrayList<Region>();

		Set<Integer> levelOne = new HashSet<Integer>();
		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			region = new Region();
			region.setId(dbObj.getInt("_id"));
			region.setName(dbObj.getString("name"));
			region.setP(dbObj.getInt("p"));
			region.setLevel(dbObj.getInt("level"));
			region.setSts(dbObj.containsField("sts") ? dbObj.getInt("sts") : 0);
			region.setShequ(dbObj.containsField("shequ") ? dbObj
					.getLong("shequ") : 0);

			if (region.getLevel() == 0) {
				if (region.getSts() == 1) {
					list.add(region);
					levelOne.add(region.getId());
				}
			} else {
				if (levelOne.contains(region.getP())) {
					list.add(region);
				}
			}
		}

		return list;

	}
}
