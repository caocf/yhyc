package com.aug3.yhyc.api;

public class DeliveryContact {
	// �ռ���
	private String recip;

	// �ռ���ַ
	private String addr;

	// �ֻ�
	private String mobi;

	// want delivery day
	private String d;

	// want delivery time
	private String t;

	// ��ݷ�
	private int fee;

	public String getRecip() {
		return recip;
	}

	public void setRecip(String recip) {
		this.recip = recip;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getMobi() {
		return mobi;
	}

	public void setMobi(String mobi) {
		this.mobi = mobi;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

}
