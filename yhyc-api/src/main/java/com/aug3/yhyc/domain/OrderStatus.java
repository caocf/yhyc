package com.aug3.yhyc.domain;

public enum OrderStatus {

	NEW(0), PROCESSING(1), DELIVERING(2), CANCELED(3), CANCELED_BY_SELLER(4), SUCCEED(
			5), COMMENT(6);

	private int sts;

	private OrderStatus(int sts) {
		this.sts = sts;
	}

	public int getValue() {
		return sts;
	}

}
