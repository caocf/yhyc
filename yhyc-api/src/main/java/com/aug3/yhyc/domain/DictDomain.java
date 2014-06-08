package com.aug3.yhyc.domain;

import java.util.ArrayList;
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

		Map<Integer, List<Category>> categories = (Map<Integer, List<Category>>) sc
				.get("categories");

		if (categories == null) {
			List<Category> categorylist = dictDao.findCategories();

			for (Category cat : categorylist) {

				if (categories.containsKey(cat.getCode())) {
					categories.get(cat.getCode()).add(cat);
				} else {
					List<Category> newCategory = new ArrayList<Category>();
					newCategory.add(cat);
					categories.put(cat.getCode(), newCategory);
				}
			}

			sc.put("categories", categories, 3600 * 6);
		}

		return categories;
	}

}
