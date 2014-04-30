package com.aug3.yhyc.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.bind.annotation.XmlRootElement;

import com.aug3.sys.rs.response.RespType;
import com.aug3.yhyc.domain.ItemDomain;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.valueobj.Item;

@Path("/item/")
@XmlRootElement()
@Produces("application/json")
public class ItemService extends BaseService {

	private ItemDomain itemDomain;

	public ItemDomain getItemDomain() {
		return itemDomain;
	}

	public void setItemDomain(ItemDomain itemDomain) {
		this.itemDomain = itemDomain;
	}

	@POST
	@Path("/new")
	// @AccessTrace
	// @AccessToken
	public boolean newItem(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("uid") String uid, @FormParam("") Item itemObj) {
		// TODO: change to order object
		return false;
	}

	@GET
	@Path("/list")
	public String listItems(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("workshop") String workshop) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	/**
	 * show items, multiple items separated by comma
	 * 
	 * @param request
	 * @param token
	 * @param item
	 * @return
	 */
	@GET
	@Path("/show")
	public String showItem(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("item") String item) {

		// return this.buidResponseResult(dtos, RespType.SUCCESS);
		return null;
	}

	@GET
	@Path("/comments")
	public String listComments(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("item") String item) {

		CommentDTO comments = itemDomain.getComments(Long.parseLong(item));
		return buidResponseResult(comments, RespType.SUCCESS);
	}

	@POST
	@Path("/comment/new")
	public boolean newComment(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("uid") String uid, @FormParam("comment") String commentObj) {
		// TODO: change to order object
		return false;
	}

}
