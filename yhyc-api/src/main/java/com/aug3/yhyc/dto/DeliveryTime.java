package com.aug3.yhyc.dto;

import java.io.Serializable;

public class DeliveryTime implements Serializable {

	private String[] date;
	private String[] time;

	public String[] getDate() {
		return date;
	}

	public void setDate(String[] date) {
		this.date = date;
	}

	public String[] getTime() {
		return time;
	}

	public void setTime(String[] time) {
		this.time = time;
	}

}
