package com.aug3.yhyc.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.aug3.yhyc.valueobj.Comment;

public class CommentDTO implements Serializable {

	private long id;

	// comment number
	private int count;
	// total score
	private int score;

	// number of goods
	private int good;
	// number of norm
	private int norm;
	// number of bad
	private int bad;

	private List<Comment> comments = new ArrayList<Comment>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

	public int getNorm() {
		return norm;
	}

	public void setNorm(int norm) {
		this.norm = norm;
	}

	public int getBad() {
		return bad;
	}

	public void setBad(int bad) {
		this.bad = bad;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
