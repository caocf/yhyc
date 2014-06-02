package com.aug3.yhyc.valueobj;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Works implements Serializable {

	private long id;

	private long author;

	private String title;

	// 食材, use array of tags is better
	private List<String> ingredients;

	private String pic;

	private List<RecipeWizard> steps;

	private long fav;

	// time spent in minutes
	private int mins;

	// 小贴士
	private String summary;

	private Date ts;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public List<RecipeWizard> getSteps() {
		return steps;
	}

	public void setSteps(List<RecipeWizard> steps) {
		this.steps = steps;
	}

	public long getAuthor() {
		return author;
	}

	public void setAuthor(long author) {
		this.author = author;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public long getFav() {
		return fav;
	}

	public void setFav(long fav) {
		this.fav = fav;
	}

	public int getMins() {
		return mins;
	}

	public void setMins(int mins) {
		this.mins = mins;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
