package com.aug3.yhyc.base;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

	private Constants() {

	}

	public final static int ITEM_STATUS_ONSALE = 1;
	
	public final static int ORDER_STATUS_FINISHED = 99;
	public final static int ORDER_STATUS_ONGOING = 88;

	public final static int FREQ_DAILY = 1;
	public final static int FREQ_WEEKLY = 2;
	public final static int FREQ_MONTHLY = 3;

	public final static String PNG = ".png";

	public static final SimpleDateFormat iso_timestamp_formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.CHINA);

}
