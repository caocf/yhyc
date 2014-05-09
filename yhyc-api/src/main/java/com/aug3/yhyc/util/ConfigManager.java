package com.aug3.yhyc.util;

import com.aug3.sys.props.ConfigProperties;

public class ConfigManager {

	private final static String PROP_NAME = "/yhyc.properties";

	private static ConfigProperties props = null;

	public static ConfigProperties getProperties() {
		if (props == null) {
			props = new ConfigProperties(PROP_NAME);
		}
		return props;
	}
}
