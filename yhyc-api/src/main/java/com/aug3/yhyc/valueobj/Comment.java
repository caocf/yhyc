package com.aug3.yhyc.valueobj;

public class Comment {

	// 用户 id -- 用户号, 匿名用户为-1
	private long uid;

	// 手机号
	private long mobi;

	// 打分
	private int score;

	// 标签 1.
	private int[] tag;

	private String content;

	// 时间 yyyy-MM-dd hh:mm
	private String ts;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getMobi() {
		return mobi;
	}

	public void setMobi(long mobi) {
		this.mobi = mobi;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int[] getTag() {
		return tag;
	}

	public void setTag(int[] tag) {
		this.tag = tag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

}
