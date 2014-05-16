package com.aug3.yhyc.service;

import java.util.List;

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

import com.aug3.sys.rs.response.RespType;
import com.aug3.sys.util.JSONUtil;
import com.aug3.yhyc.domain.OrderDomain;
import com.aug3.yhyc.dto.Order;

@Path("/order/")
@XmlRootElement()
@Produces("application/json")
public class OrderService extends BaseService {

	private OrderDomain orderDomain;

	public OrderDomain getOrderDomain() {
		return orderDomain;
	}

	public void setOrderDomain(OrderDomain orderDomain) {
		this.orderDomain = orderDomain;
	}

	@POST
	@Path("/new")
	// @AccessTrace
	// @AccessToken
	public String newOrder(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("uid") String uid, @FormParam("order") String order) {
		orderDomain.newOrder(JSONUtil.fromJson(order, Order.class));
		return buidResponseResult("success!", RespType.SUCCESS);
	}

	@GET
	@Path("/list")
	public String listOrders(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("uid") String uid, @QueryParam("status") String status) {

		List<Order> orders = orderDomain.listOrders(uid, Integer.parseInt(status));

		return this.buidResponseResult(orders, RespType.SUCCESS);
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
	@Path("/sales")
	public String listSalesOrders(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("workshop") String workshop, @QueryParam("status") String status) {

		List<Order> orders = orderDomain.listOrdersByWorkshop(Long.parseLong(workshop), Integer.parseInt(status));
		return this.buidResponseResult(orders, RespType.SUCCESS);
	}

	@GET
	@Path("/show")
	public String showOrder(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("orderid") String orderid) {

		Order order = orderDomain.showOrder(Long.parseLong(orderid));
		return this.buidResponseResult(order, RespType.SUCCESS);
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

		orderDomain.editOrderStatus(Long.parseLong(orderid), Integer.parseInt(status));
		return this.buidResponseResult("success", RespType.SUCCESS);
	}

}
