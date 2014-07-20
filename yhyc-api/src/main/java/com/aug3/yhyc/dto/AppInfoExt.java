package com.aug3.yhyc.dto;

public class AppInfoExt extends AppInfo {

	private String token;
	private boolean in_review;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isIn_review() {
		return in_review;
	}

	public void setIn_review(boolean in_review) {
		this.in_review = in_review;
	}

}
