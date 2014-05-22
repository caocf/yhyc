package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.valueobj.RecipeWizard;
import com.aug3.yhyc.valueobj.Works;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class WorksDao extends BaseDao {

	public boolean createWorks(Works work) {

		DBObject dbObj = new BasicDBObject();
		dbObj.put("title", work.getTitle());
		dbObj.put("ingredients", work.getIngredients());
		dbObj.put("pic", work.getPic());
		List<RecipeWizard> wizards = work.getSteps();
		if (wizards != null) {
			List<HashMap> steps = new ArrayList<HashMap>();
			for (RecipeWizard wiz : wizards) {
				HashMap m = new HashMap();
				m.put("seq", wiz.getSeq());
				m.put("pic", wiz.getPic());
				m.put("desc", wiz.getDesc());
				steps.add(m);
			}
			dbObj.put("steps", steps);
		}
		dbObj.put("fav", work.getFav());
		dbObj.put("mins", work.getMins());
		dbObj.put("summary", work.getSummary());
		dbObj.put("ts", new Date());

		getDBCollection(CollectionConstants.COLL_WORKS).insert(dbObj);
		return false;

	}

	public boolean updateWorks(Works work) {

		DBObject dbObj = new BasicDBObject();
		dbObj.put("_id", work.getId());
		dbObj.put("title", work.getTitle());
		dbObj.put("ingredients", work.getIngredients());

		dbObj.put("mins", work.getMins());
		dbObj.put("summary", work.getSummary());
		dbObj.put("ts", new Date());

		getDBCollection(CollectionConstants.COLL_WORKS).update(new BasicDBObject("_id", work.getId()), dbObj, true,
				false, WriteConcern.SAFE);
		return false;

	}

	public boolean deleteWorks(long uid, long id, boolean admin) {

		if (admin) {
			getDBCollection(CollectionConstants.COLL_WORKS).remove(new BasicDBObject("_id", id));
		} else {
			getDBCollection(CollectionConstants.COLL_WORKS).remove(new BasicDBObject("_id", id).append("author", uid));
		}
		return false;

	}

	public List<Works> find(int pn) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKS).find().sort(new BasicDBObject("_id", -1))
				.skip(skipSize(pn)).limit(pageSize());

		BasicDBObject dbObj;
		Works work;
		List<Works> list = new ArrayList<Works>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			work = getWorksInfo(dbObj);
			list.add(work);
		}

		return list;

	}

	public Works findByID(long id) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKS).find(new BasicDBObject("_id", id));

		Works works = null;
		while (dbCur.hasNext()) {
			BasicDBObject dbObj = (BasicDBObject) dbCur.next();
			works = getWorksInfo(dbObj);

		}

		return works;

	}

	public List<Works> findByID(List<Long> id, int pn) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKS)
				.find(new BasicDBObject("_id", new BasicDBObject("$in", id))).sort(new BasicDBObject("_id", -1))
				.skip(skipSize(pn)).limit(pageSize());

		BasicDBObject dbObj;
		Works work;
		List<Works> list = new ArrayList<Works>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			work = getWorksInfo(dbObj);
			list.add(work);
		}

		return list;

	}

	public List<Works> findByUser(long uid, int pn) {

		DBCursor dbCur = getDBCollection(CollectionConstants.COLL_WORKS).find(new BasicDBObject("author", uid))
				.sort(new BasicDBObject("_id", -1)).skip(skipSize(pn)).limit(pageSize());

		BasicDBObject dbObj;
		Works work;
		List<Works> list = new ArrayList<Works>();

		while (dbCur.hasNext()) {
			dbObj = (BasicDBObject) dbCur.next();
			work = getWorksInfo(dbObj);
			list.add(work);
		}

		return list;

	}

	private Works getWorksInfo(BasicDBObject dbObj) {

		Works works = new Works();

		works.setId(dbObj.getLong("_id"));
		works.setAuthor(dbObj.getLong("author"));
		works.setTitle(dbObj.getString("title"));

		works.setIngredients(((List<String>) dbObj.get("ingredients")));

		works.setPic(dbObj.getString("pic"));

		BasicDBList stepslist = (BasicDBList) dbObj.get("steps");
		List<RecipeWizard> steps = new ArrayList<RecipeWizard>();
		for (Object step : stepslist) {
			BasicDBObject dbStep = (BasicDBObject) step;

			steps.add(new RecipeWizard(dbStep.getInt("seq"), dbStep.getString("pic"), dbStep.getString("desc")));
		}
		works.setSteps(steps);

		works.setFav(dbObj.getLong("fav"));
		works.setMins(dbObj.getInt("mins"));
		works.setSummary(dbObj.getString("summary"));
		works.setTs(dbObj.getDate("ts"));

		return works;
	}
}
