package com.aug3.yhyc.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CategoryDTO implements Serializable{

	private Map<Integer, List<Category>> categories;

	public Map<Integer, List<Category>> getCategories() {
		return categories;
	}

	public void setCategories(Map<Integer, List<Category>> categories) {
		this.categories = categories;
	}

}
