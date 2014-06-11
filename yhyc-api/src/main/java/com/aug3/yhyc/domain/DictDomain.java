package com.aug3.yhyc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aug3.sys.cache.SystemCache;
import com.aug3.yhyc.dao.DictDao;
import com.aug3.yhyc.valueobj.Category;

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

}
