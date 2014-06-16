package com.aug3.yhyc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.base.RespType;
import com.aug3.yhyc.domain.WorkshopDomain;
import com.aug3.yhyc.dto.RequestShop;
import com.aug3.yhyc.dto.WorkshopDTO;

@Path("/shop/")
@XmlRootElement()
@Produces("application/json;charset=UTF-8")
public class WorkshopService extends BaseService {

	private WorkshopDomain shopDomain;

	public WorkshopDomain getShopDomain() {
		return shopDomain;
	}

	public void setShopDomain(WorkshopDomain shopDomain) {
		this.shopDomain = shopDomain;
	}

	/**
	 * 获取社区对应的云仓网点信息
	 * 
	 * @param request
	 * @param token
	 * @param shequ
	 * @return
	 */
	@GET
	@Path("/list")
	public String getWorkshops(@Context HttpServletRequest request,
			@QueryParam("token") String token, @QueryParam("shequ") long shequ,
			@QueryParam("cat") int cat) {

		List<WorkshopDTO> workshops = shopDomain.queryWorkshops(shequ, cat);
		return buidResponseSuccess(workshops);
	}

	@POST
	@Path("/request")
	// @AccessTrace
	// @AccessToken
	public String requestShop(@Context HttpServletRequest request,
			@FormParam("token") String token, @FormParam("shop") String shop) {

		RequestShop reqShop = JSONUtil.fromJson(shop, RequestShop.class);

		if (shopDomain.requestShopExist(reqShop)) {
			return buidResponseResult("duplicate mobile/mail/idcard",
					RespType.DUPLICATE_RECORD);
		} else {
			shopDomain.requestShop(reqShop);

			return buidResponseSuccess("success!");
		}
	}

}
