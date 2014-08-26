package com.aug3.yhyc.domain;

import org.apache.commons.lang.StringUtils;

import com.aug3.yhyc.dto.AppInfo;
import com.aug3.yhyc.dto.AppInfoExt;
import com.aug3.yhyc.dto.AppInfoShop;
import com.aug3.yhyc.util.ConfigManager;

public class AppDomain {

	/**
	 * @deprecated use getAppInfoExt instead
	 * 
	 * @param ver
	 * @return
	 */
	public AppInfo getAppInfo(int ver) {

		AppInfo app = new AppInfo();

		if (ver > 0) {

			int verCode = ConfigManager.getProperties().getIntProperty(
					"android.app.ver.code");

			if (verCode > ver) {

				app.setVerCode(verCode);
				app.setVerName(ConfigManager.getProperties().getProperty(
						"android.app.ver.name"));

				String changelog = ConfigManager.getProperties().getProperty(
						"android.app.ver.changelog");
				if (StringUtils.isNotBlank(changelog))
					app.setChangelog(changelog);

				String downloadUrl = ConfigManager.getProperties().getProperty(
						"android.app.download.url");
				app.setUrl(downloadUrl);

			}

		}

		return app;
	}

	public AppInfoExt getAppInfoAndroid(int ver) {

		AppInfoExt app = new AppInfoExt();

		if (ver > 0) {

			int verCode = ConfigManager.getProperties().getIntProperty(
					"android.app.ver.code");

			if (verCode > ver) {

				app.setVerCode(verCode);
				app.setVerName(ConfigManager.getProperties().getProperty(
						"android.app.ver.name"));

				String changelog = ConfigManager.getProperties().getProperty(
						"android.app.ver.changelog");
				if (StringUtils.isNotBlank(changelog))
					app.setChangelog(changelog);

				String downloadUrl = ConfigManager.getProperties().getProperty(
						"android.app.download.url");
				app.setUrl(downloadUrl);

			}

		}

		return app;
	}

	public AppInfoExt getAppInfoIOS(int ver) {

		AppInfoExt app = new AppInfoExt();

		if (ver > 0) {

			int currentVer = ConfigManager.getProperties().getIntProperty(
					"ios.now.app.ver.code");
			String downloadUrl = ConfigManager.getProperties().getProperty(
					"ios.app.download.url");
			
			app.setUrl(downloadUrl);
			
			if (ver <= currentVer) {
				app.setVerCode(currentVer);
				app.setVerName(ConfigManager.getProperties().getProperty(
						"ios.now.app.ver.name"));

				String changelog = ConfigManager.getProperties().getProperty(
						"ios.now.app.ver.changelog");
				if (StringUtils.isNotBlank(changelog))
					app.setChangelog(changelog);

				

				boolean in_review = ConfigManager.getProperties()
						.getBooleanProperty("ios.now.app.ver.inreview");
				app.setIn_review(in_review);
			} else {
				int reviewVer = ConfigManager.getProperties().getIntProperty(
						"ios.rev.app.ver.code");
				app.setVerCode(reviewVer);
				app.setVerName(ConfigManager.getProperties().getProperty(
						"ios.rev.app.ver.name"));

				String changelog = ConfigManager.getProperties().getProperty(
						"ios.rev.app.ver.changelog");
				if (StringUtils.isNotBlank(changelog))
					app.setChangelog(changelog);

				boolean in_review = ConfigManager.getProperties()
						.getBooleanProperty("ios.rev.app.ver.inreview");
				app.setIn_review(in_review);
			}

		}

		return app;
	}

	public AppInfoShop getShopMgrAppInfo(int ver) {

		AppInfoShop app = new AppInfoShop();

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

			app.setBkey(ConfigManager.getProperties().getProperty(
					"baidu.dev.key"));

		}

		return app;
	}

}
