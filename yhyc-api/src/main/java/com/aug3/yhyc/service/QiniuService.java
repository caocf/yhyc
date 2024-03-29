package com.aug3.yhyc.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.yhyc.base.RespType;
import com.aug3.yhyc.util.Qiniu;

@Path("/qiniu/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class QiniuService extends BaseService {

	@GET
	@Path("/uptoken")
	public String uptoken(@Context HttpServletRequest request,
			@QueryParam("bucket") String bucket, @QueryParam("key") String key) {

		if ("item".equalsIgnoreCase(bucket)) {
			bucket = Qiniu.getItemBucket();
		} else if ("user".equalsIgnoreCase(bucket)) {
			bucket = Qiniu.getUserBucket();
		} else {
			return buildResponseResult("wrong parameter bucket",
					RespType.INVALID_PARAMETERS);
		}

		String uptoken = Qiniu.uptoken(bucket, key);

		return buildResponseSuccess(uptoken);
	}

	/**
	 * [GET] http://<domain>/<key>?token=<downloadToken>
	 * 
	 * {"code":"200","type":"SUCCESS","message":{"10101001.png":
	 * "http://my.qiniudn.com/10101001.png?e=1400866075&token=WUA4UlKtv54qSQCpXxDXSs3r3IZG4zNPLwYDY4Gt:gzei63ttZDKp3Yq87N2rHkYRC5s="
	 * }}
	 * 
	 * @param request
	 * @param token
	 * @param key
	 * 
	 * @return
	 */
	@GET
	@Path("/urls")
	public String downloadUrl(@Context HttpServletRequest request,
			@QueryParam("fn") String fn) {

		Map<String, String> urlMap = Qiniu.downloadUrls(fn,
				Qiniu.getItemDomain());

		return buildResponseResult(urlMap, RespType.SUCCESS, true);
	}

}
