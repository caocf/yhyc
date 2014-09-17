package com.aug3.yhyc.util;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.aug3.sys.util.DateUtil;
import com.aug3.sys.util.RandomUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoException;

public class IDGenerator {

	private static final DecimalFormat orderdf = new DecimalFormat("00000");

	private static final DecimalFormat udf = new DecimalFormat("00000000");

	public static synchronized long nextval(DB db, String name, String key) {
		long value = 0;
		try {
			BasicDBObject query = new BasicDBObject("name", name);
			BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject(
					key, 1l));
			return Long
					.valueOf(db
							.getCollection("ids")
							.findAndModify(query, null, null, false, update,
									true, true).get(key).toString()
							.replace(".0", ""));
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static synchronized long nextval(DB db, String name) {
		long value = 0;
		try {
			BasicDBObject query = new BasicDBObject("name", name);
			BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject(
					"value", 1l));
			return Long
					.valueOf(db
							.getCollection("ids")
							.findAndModify(query, null, null, false, update,
									true, true).get("value").toString()
							.replace(".0", ""));
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static synchronized long nextval(DB db, String name, String period,
			String key) {
		long value = 0;
		try {
			BasicDBObject query = new BasicDBObject().append("name", name)
					.append("p", period);
			BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject(
					key, 1l));
			return Long
					.valueOf(db
							.getCollection("ids")
							.findAndModify(query, null, null, false, update,
									true, true).get(key).toString()
							.replace(".0", ""));
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static long nextOrderID(DB db) {
		String today = DateUtil.formatCurrentDate().replaceAll("-", "");
		return Long.parseLong(today.substring(2)
				+ orderdf.format(IDGenerator.nextval(db, "orderid",
						today.substring(0, 6), today.substring(6)))
				+ RandomUtil.getRandom(0, 9));
		// remove random and increase orderdf in the future
	}

	public static long nextUserID(DB db) {
		return Long
				.parseLong((Calendar.getInstance().get(Calendar.YEAR) - 2000)
						+ udf.format(IDGenerator.nextval(db, "userid")));
	}

}
