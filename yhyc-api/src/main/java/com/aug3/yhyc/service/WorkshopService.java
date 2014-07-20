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
			@QueryParam("shequ") long shequ, @QueryParam("cat") int cat) {

		List<WorkshopDTO> workshops = shopDomain.queryWorkshops(shequ, cat);
		return buildResponseSuccess(workshops);
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
	@Path("/default")
	public String getDefaultWorkshops(@Context HttpServletRequest request,
			@QueryParam("shequ") long shequ) {

		List<WorkshopDTO> workshops = shopDomain.queryDefaultWorkshops(shequ);
		return buildResponseSuccess(workshops);
	}

	@GET
	@Path("/my")
	public String getMyWorkshops(@Context HttpServletRequest request,
			@QueryParam("uid") long uid) {

		List<WorkshopDTO> workshops = shopDomain.findWorkshops(uid);
		return buildResponseSuccess(workshops);
	}

	@GET
	@Path("/some")
	public String getSomeShop(@Context HttpServletRequest request,
			@QueryParam("uid") long uid, @QueryParam("workshop") long workshop) {

		WorkshopDTO result = shopDomain.getShopByID(uid, workshop);
		return buildResponseSuccess(result);
	}

	@GET
	@Path("/show")
	public String getShopInfo(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop) {

		WorkshopDTO result = shopDomain.getShopInfo(workshop);
		return buildResponseSuccess(result);
	}

	@GET
	@Path("/announce")
	public String getShopAnnouncement(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop) {

		String notice = shopDomain.getShopAnnouncement(workshop);
		return buildResponseSuccess(notice);
	}

	@POST
	@Path("/announce")
	public String updateShopAnnouncement(@Context HttpServletRequest request,
			@FormParam("workshop") long workshop,
			@FormParam("announcement") String announcement) {

		boolean boo = shopDomain.updateShopAnnouncement(workshop, announcement);
		return buildResponseSuccess(boo);
	}

	@POST
	@Path("/request")
	public String requestShop(@Context HttpServletRequest request,
			@FormParam("shop") String shop) {

		RequestShop reqShop = JSONUtil.fromJson(shop, RequestShop.class);

		if (shopDomain.requestShopExist(reqShop)) {
			return buildResponseResult("duplicate mobile/mail/idcard",
					RespType.DUPLICATE_RECORD);
		} else {
			shopDomain.requestShop(reqShop);

			return buildResponseSuccess("success!");
		}
	}

	@GET
	@Path("/stats")
	public String getShopStats(@Context HttpServletRequest request,
			@QueryParam("workshop") long workshop) {

		String notice = shopDomain.getShopStats(workshop);
		return buildResponseSuccess(notice);
	}

}
