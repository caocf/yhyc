package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.yhyc.dto.Order;

@Path("/order/")
@XmlRootElement()
@Produces("application/json")
public class OrderService {

	@POST
	@Path("/new")
	// @AccessTrace
	// @AccessToken
	public boolean newOrder(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("uid") String uid, @FormParam("") Order order) {
		// TODO: change to order object
		return false;
	}

	@GET
	@Path("/list")
	public String listOrders(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid, @QueryParam("status") String status) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	/**
	 * 获取销售订单列表
	 * 
	 * @param request
	 * @param token
	 * @param workshop
	 * @return
	 */
	@GET
	@Path("/sales/todo")
	public String listSalesTodoOrders(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("workshop") String workshop) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	@GET
	@Path("/sales/finished")
	public String listSalesFinishedOrders(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("workshop") String workshop) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	@GET
	@Path("/show")
	public String showOrder(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("orderid") String orderid) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	/**
	 * change status, e.g. cancel, delivering, finished
	 * 
	 * @param request
	 * @param token
	 * @param orderid
	 * @param status
	 * @return
	 */
	@PUT
	@Path("/status")
	public String editStatus(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("orderid") String orderid, @QueryParam("status") String status) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

}
