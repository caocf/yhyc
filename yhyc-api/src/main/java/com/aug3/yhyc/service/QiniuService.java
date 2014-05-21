package com.aug3.yhyc.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.codec.EncoderException;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.aug3.sys.rs.response.RespType;
import com.aug3.yhyc.util.ConfigManager;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.URLUtils;

@Path("/qiniu/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class QiniuService extends BaseService {

	private static final Logger log = Logger.getLogger(QiniuService.class);
	private static final String accesskey = ConfigManager.getProperties().getProperty("storage.key.access");
	private static final String secretkey = ConfigManager.getProperties().getProperty("storage.key.secret");
	private static final String domain = ConfigManager.getProperties().getProperty("storage.domain");
	private static final String bucket = ConfigManager.getProperties().getProperty("storage.bucket");

	@GET
	@Path("/uptoken")
	public String uptoken(@Context HttpServletRequest request, @QueryParam("token") String token) {

		Config.ACCESS_KEY = accesskey;
		Config.SECRET_KEY = secretkey;
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);

		// 请确保该bucket已经存在
		PutPolicy putPolicy = new PutPolicy(bucket);
		putPolicy.expires = 3600;

		String uptoken = "";
		try {
			uptoken = putPolicy.token(mac);
		} catch (AuthException e) {
			log.error(e.getMessage());
		} catch (JSONException e) {
			log.error(e.getMessage());
		}

		return this.buidResponseResult(uptoken, RespType.SUCCESS);
	}

	/**
	 * [GET] http://<domain>/<key>?token=<downloadToken>
	 * 
	 * @param request
	 * @param token
	 * @param key
	 * 
	 * @return
	 */
	@GET
	@Path("/urls")
	public String downloadUrl(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("fn") String fn) {

		Config.ACCESS_KEY = accesskey;
		Config.SECRET_KEY = secretkey;
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);

		String[] keys = fn.split(",");
		Map<String, String> urlMap = new HashMap<String, String>();

		for (String key : keys) {
			String baseUrl = null;
			try {
				baseUrl = URLUtils.makeBaseUrl(domain, "<key>");
			} catch (EncoderException e) {
				log.error(e.getMessage());
			}
			GetPolicy getPolicy = new GetPolicy();
			getPolicy.expires = 3600;

			String downloadUrl = null;
			try {
				downloadUrl = getPolicy.makeRequest(baseUrl, mac);
			} catch (AuthException e) {
				log.error(e.getMessage());
			}
			urlMap.put(key, downloadUrl);
		}

		return this.buidResponseResult(urlMap, RespType.SUCCESS);
	}

}
