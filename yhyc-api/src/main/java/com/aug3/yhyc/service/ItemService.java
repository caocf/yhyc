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

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.rs.response.RespType;
import com.aug3.yhyc.domain.ItemDomain;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.CommentReq;
import com.aug3.yhyc.dto.ItemDTO;
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
	// TODO
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

		if (StringUtils.isBlank(workshop)) {
			return buidResponseResult("invlid param workshop", RespType.PARAMETEREXCEPTION);
		}
		List<Item> items = itemDomain.findItemsByWorkshop(Long.parseLong(workshop));
		return buidResponseResult(items, RespType.SUCCESS);
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

		ItemDTO result = itemDomain.findItemByID(Long.parseLong(item));
		return buidResponseResult(result, RespType.SUCCESS);
	}

	@GET
	@Path("/comments")
	public String listComments(@Context HttpServletRequest request, @QueryParam("token") String token,
			@QueryParam("item") String item, String pn) {

		if (StringUtils.isBlank(pn)) {
			pn = "1";
		}
		CommentDTO comments = itemDomain.findCommentsByItem(Long.parseLong(item), Integer.parseInt(pn));
		return buidResponseResult(comments, RespType.SUCCESS);
	}

	@POST
	@Path("/comment/new")
	public void newComment(@Context HttpServletRequest request, @FormParam("token") String token,
			@FormParam("") CommentReq commentReq) {

		itemDomain.newComments(commentReq);
	}

}
