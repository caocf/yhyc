package com.aug3.yhyc.dto;

import java.util.List;
import java.util.Map;

import com.aug3.yhyc.valueobj.Category;

public class CategoryDTO {

	private Map<Integer, List<Category>> categories;

	public Map<Integer, List<Category>> getCategories() {
		return categories;
	}

	public void setCategories(Map<Integer, List<Category>> categories) {
		this.categories = categories;
	}

}
