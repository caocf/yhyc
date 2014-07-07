package com.aug3.yhyc.dto;

import java.io.Serializable;

public class AppInfo implements Serializable {

	// version of client
	private int verCode;
	private String verName;

	private String changelog;

	private String url;

	public int getVerCode() {
		return verCode;
	}

	public void setVerCode(int verCode) {
		this.verCode = verCode;
	}

	public String getVerName() {
		return verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

	public String getChangelog() {
		return changelog;
	}

	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
