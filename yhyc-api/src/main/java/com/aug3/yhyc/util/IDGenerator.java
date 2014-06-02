package com.aug3.yhyc.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

import com.aug3.sys.util.DateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoException;

public class IDGenerator {

	private static final DecimalFormat df = new DecimalFormat("000000");

	private static final DecimalFormat udf = new DecimalFormat("00000000");

	private static final Random random = new Random();

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

	public static long nextOrderID(DB db) {
		String today = DateUtil.formatCurrentDate();
		return Long.parseLong(today.substring(2).replaceAll("-", "")
				+ df.format(IDGenerator.nextval(db, today, "orderid")
						+ random.nextInt(10)));
	}

	public static long nextUserID(DB db) {
		return Long
				.parseLong((Calendar.getInstance().get(Calendar.YEAR) - 2000)
						+ udf.format(IDGenerator.nextval(db, "userid")));
	}

}
