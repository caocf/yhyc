package com.aug3.yhyc.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.cache.SystemCache;
import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.dao.DictDao;
import com.aug3.yhyc.dto.DeliveryTime;
import com.aug3.yhyc.util.ConfigManager;
import com.aug3.yhyc.valueobj.Category;
import com.aug3.yhyc.valueobj.Region;
import com.aug3.yhyc.valueobj.Tag;

public class DictDomain {

	private DictDao dictDao;

	public DictDao getDictDao() {
		return dictDao;
	}

	public void setDictDao(DictDao dictDao) {
		this.dictDao = dictDao;
	}

	public Map<Integer, List<Category>> mapCategory() {

		SystemCache sc = new SystemCache();

		Map<Integer, List<Category>> categories = null;

		if (sc.get("categories") == null) {

			List<Category> categorylist = dictDao.findCategories();

			categories = new HashMap<Integer, List<Category>>();
			for (Category cat : categorylist) {

				if (categories.containsKey(cat.getP())) {
					categories.get(cat.getP()).add(cat);
				} else {
					List<Category> newCategory = new ArrayList<Category>();
					newCategory.add(cat);
					categories.put(cat.getP(), newCategory);
				}
			}

			sc.put("categories", categories, 3600 * 6);
		} else {
			categories = (Map<Integer, List<Category>>) sc.get("categories");
		}

		return categories;
	}

	public List<Tag> listTags() {

		return dictDao.findAll();
	}

	public List<Region> listRegion() {

		return dictDao.findRegion();
	}

	public DeliveryTime deliverytime() {

		Calendar c = Calendar.getInstance();
		Date current = new Date();
		c.setTime(current);

		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);

		if (hour > 17) {
			c.add(Calendar.DATE, 1);
		} else if (hour == 17 && min > 15) {
			c.add(Calendar.DATE, 1);
		}

		String firstday = DateUtil.formatDate(c.getTime());

		c.add(Calendar.DATE, 1);
		String nextday = DateUtil.formatDate(c.getTime());
		c.add(Calendar.DATE, 1);
		String next2day = DateUtil.formatDate(c.getTime());

		String[] dates = { firstday, nextday, next2day };

		DeliveryTime dt = new DeliveryTime();

		dt.setDate(dates);

		// 任意时间|6:30-8:00|11:00-13:00|17:00-19:30|指定时间(>80元)
		String deliverytimeStr = ConfigManager.getProperties().getProperty(
				"deliverytime");

		if (StringUtils.isBlank(deliverytimeStr)) {
			dt.setTime(new String[] { "10:00-12:00", "16:30-19:30" });
		} else {
			dt.setTime(deliverytimeStr.split(";"));
		}

		return dt;
	}

}
