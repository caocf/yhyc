package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.aug3.yhyc.valueobj.Category;

public class CategoryDTO implements Serializable {

	private Map<Integer, List<Category>> categories;

	public Map<Integer, List<Category>> getCategories() {
		return categories;
	}

	public void setCategories(Map<Integer, List<Category>> categories) {
		this.categories = categories;
	}

}
