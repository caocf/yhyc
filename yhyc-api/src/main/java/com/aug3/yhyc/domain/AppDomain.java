package com.aug3.yhyc.domain;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.dto.AppInfo;
import com.aug3.yhyc.util.ConfigManager;

public class AppDomain {

	public AppInfo getAppInfo(int ver) {

		AppInfo app = new AppInfo();

		if (ver > 0) {

			int verCode = ConfigManager.getProperties().getIntProperty(
					"app.ver.code");

			if (verCode > ver) {

				app.setVerCode(verCode);
				app.setVerName(ConfigManager.getProperties().getProperty(
						"app.ver.name"));

				String changelog = ConfigManager.getProperties().getProperty(
						"app.ver.changelog");
				if (StringUtils.isNotBlank(changelog))
					app.setChangelog(changelog);

				String downloadUrl = ConfigManager.getProperties().getProperty(
						"app.download.url");
				app.setUrl(downloadUrl);

			}

		}

		return app;
	}
	
	public AppInfo getShopMgrAppInfo(int ver) {

		AppInfo app = new AppInfo();

		if (ver > 0) {

			int verCode = ConfigManager.getProperties().getIntProperty(
					"mgr.app.ver.code");

			if (verCode > ver) {

				app.setVerCode(verCode);
				app.setVerName(ConfigManager.getProperties().getProperty(
						"mgr.app.ver.name"));

				String changelog = ConfigManager.getProperties().getProperty(
						"mgr.app.ver.changelog");
				if (StringUtils.isNotBlank(changelog))
					app.setChangelog(changelog);

				String downloadUrl = ConfigManager.getProperties().getProperty(
						"mgr.app.download.url");
				app.setUrl(downloadUrl);

			}

		}

		return app;
	}

}
