package com.aug3.yhyc.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.util.DateUtil;
import com.aug3.sys.util.EncryptUtil;
import com.aug3.yhyc.base.CollectionConstants;
import com.aug3.yhyc.dto.UserPrefs;
import com.aug3.yhyc.util.IDGenerator;
import com.aug3.yhyc.util.Qiniu;
import com.aug3.yhyc.valueobj.User;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

public class UserDao extends BaseDao {

	private static final List<String> prefs_field = Arrays.asList(new String[] {
			"fav", "cart" });

	public User find(User user) {

		BasicDBObject dbObj = new BasicDBObject();

		if (StringUtils.isNotBlank(user.getMobi())) {
			dbObj.append("mobi", user.getMobi()).append("password",
					user.getPassword());
		} else if (user.getUid() != 0) {
			dbObj.append("_id", user.getUid()).append("password",
					user.getPassword());
		}

		DBObject userObj = getDBCollection(CollectionConstants.COLL_USERS)
				.findOne(dbObj);

		if (userObj != null) {

			BasicDBObject result = (BasicDBObject) userObj;

			return buildUser(result, false);

		} else {
			return null;
		}

	}

	/**
	 * use for get user avatar
	 * 
	 * @param uid
	 * @return
	 */
	public User find(long uid) {

		BasicDBObject dbObj = new BasicDBObject().append("_id", uid);

		DBObject userObj = getDBCollection(CollectionConstants.COLL_USERS)
				.findOne(dbObj);

		if (userObj != null) {

			BasicDBObject result = (BasicDBObject) userObj;

			return buildUser(result, true);
		}

		return null;

	}

	private User buildUser(BasicDBObject dbObj, boolean userPic) {

		User user = new User();
		user.setUid(dbObj.getLong("_id"));
		user.setName(dbObj.getString("name"));
		user.setMobi(dbObj.getString("mobi"));
		user.setMail(dbObj.getString("mail"));
		user.setAc(dbObj.containsField("ac") ? dbObj.getInt("ac") : 10);

		String avatar = dbObj.getString("pic");

		if (StringUtils.isNotBlank(avatar)) {
			user.setHasAvatar(true);

			if (userPic)
				user.setAvatar(Qiniu.downloadUrl(avatar, Qiniu.getUserDomain()));
		}
		return user;
	}

	public static boolean isValidToken(String token) {

		if (EncryptUtil.MD5(DateUtil.formatCurrentDate()).equals(token))
			return true;
		else
			return false;
	}

	public boolean exist(User user) {

		BasicDBList or = new BasicDBList();
		or.add(new BasicDBObject().append("mobi", user.getMobi()));
		or.add(new BasicDBObject().append("mail", user.getMail()));

		long result = getDBCollection(CollectionConstants.COLL_USERS).count(
				new BasicDBObject("$or", or));

		if (result == 0) {
			return false;
		}
		return true;
	}

	public long create(User user) {

		DBObject dbObj = new BasicDBObject();
		long uid = IDGenerator.nextUserID(getDB());
		dbObj.put("_id", uid);
		dbObj.put("name", user.getName());
		dbObj.put("password", user.getPassword());
		dbObj.put("mobi", user.getMobi());
		dbObj.put("mail", user.getMail());
		dbObj.put("fav", new Long[] {});
		dbObj.put("cart", new Long[] {});
		dbObj.put("ac", 10);
		dbObj.put("sts", 1);
		dbObj.put("ts", new Date());

		getDBCollection(CollectionConstants.COLL_USERS).insert(dbObj);
		return uid;
	}

	public List<Long> findFavorite(long uid) {

		DBCursor cur = getDBCollection(CollectionConstants.COLL_USERS).find(
				new BasicDBObject("_id", uid), new BasicDBObject("fav", 1));

		List<Long> items = new ArrayList<Long>();

		while (cur.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cur.next();
			BasicDBList favlist = (BasicDBList) dbo.get("fav");
			if (favlist != null && favlist.size() > 0) {

				for (Object itemid : favlist) {
					items.add((Long) itemid);
				}
			}
		}

		return items;
	}

	public UserPrefs findPrefs(long uid) {

		DBCursor cur = getDBCollection(CollectionConstants.COLL_USERS).find(
				new BasicDBObject("_id", uid),
				new BasicDBObject("fav", 1).append("cart", 1));

		UserPrefs userPrefs = new UserPrefs();

		while (cur.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cur.next();
			userPrefs.setFav((List<Long>) dbo.get("fav"));
			userPrefs.setCart((List<Long>) dbo.get("cart"));

		}

		return userPrefs;
	}

	public boolean addUserPrefs(long uid, String field, List<Long> items) {

		if (!prefs_field.contains(field) || items == null) {
			return false;
		}

		BasicDBObject updateObj = new BasicDBObject();

		if (items.size() == 1) {
			updateObj.append("$addToSet",
					new BasicDBObject(field, items.get(0)));
		} else {
			updateObj.append("$addToSet", new BasicDBObject(field,
					new BasicDBObject("$each", items)));
		}

		getDBCollection(CollectionConstants.COLL_USERS).update(
				new BasicDBObject("_id", uid), updateObj, false, false,
				WriteConcern.SAFE);

		return true;

	}

	public boolean removeUserPrefs(long uid, String field, List<Long> items) {

		if (!prefs_field.contains(field) || items == null) {
			return false;
		}

		BasicDBObject updateObj = new BasicDBObject().append("$pullAll",
				new BasicDBObject(field, items));

		getDBCollection(CollectionConstants.COLL_USERS).update(
				new BasicDBObject("_id", uid), updateObj, false, false,
				WriteConcern.SAFE);

		return true;

	}

	public boolean updateUserPrefs(long uid, String field, List<Long> items) {

		if (!prefs_field.contains(field) || items == null) {
			return false;
		}

		BasicDBObject updateObj = new BasicDBObject().append("$set",
				new BasicDBObject(field, items));

		getDBCollection(CollectionConstants.COLL_USERS).update(
				new BasicDBObject("_id", uid), updateObj, false, false,
				WriteConcern.SAFE);

		return true;

	}

	public List<Long> findFavWorks(long uid) {

		DBCursor cur = getDBCollection(CollectionConstants.COLL_USERS).find(
				new BasicDBObject("_id", uid), new BasicDBObject("works", 1));

		List<Long> items = new ArrayList<Long>();

		while (cur.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cur.next();
			List<Long> favlist = (List<Long>) dbo.get("works");
			if (favlist != null && favlist.size() > 0) {
				items.addAll(favlist);
			}
		}

		return items;
	}

	public List<Long> findShoppingCart(long uid) {

		DBCursor cur = getDBCollection(CollectionConstants.COLL_USERS).find(
				new BasicDBObject("_id", uid), new BasicDBObject("cart", 1));

		List<Long> items = new ArrayList<Long>();

		while (cur.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cur.next();
			BasicDBList cartList = (BasicDBList) dbo.get("cart");
			if (cartList != null && cartList.size() > 0) {

				for (Object itemid : cartList) {
					items.add((Long) itemid);
				}
			}
		}

		return items;
	}
}
