package com.aug3.yhyc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.dao.DictDao;
import com.aug3.yhyc.dao.ItemDao;
import com.aug3.yhyc.dao.OrderDao;
import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.dao.WorkshopDao;
import com.aug3.yhyc.dto.CommentDTO;
import com.aug3.yhyc.dto.CommentReq;
import com.aug3.yhyc.dto.ItemDTO;
import com.aug3.yhyc.dto.ProductDTO;
import com.aug3.yhyc.dto.ProductItem;
import com.aug3.yhyc.dto.ShopItem;
import com.aug3.yhyc.dto.WorkshopDTO;
import com.aug3.yhyc.valueobj.Comment;
import com.aug3.yhyc.valueobj.Item;
import com.aug3.yhyc.valueobj.Product;

public class ItemDomain {

	private DictDao dictDao;

	private UserDao userDao;

	private WorkshopDao shopDao;

	private ItemDao itemDao;

	private OrderDao orderDao;

	public DictDao getDictDao() {
		return dictDao;
	}

	public void setDictDao(DictDao dictDao) {
		this.dictDao = dictDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public WorkshopDao getShopDao() {
		return shopDao;
	}

	public void setShopDao(WorkshopDao shopDao) {
		this.shopDao = shopDao;
	}

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public boolean updateItem(long workshop, Item item) {
		return itemDao.updateItem(workshop, item);
	}

	public List<Item> findItemsByWorkshop(long workshop) {
		return itemDao.findItemsByWorkshop(workshop);
	}

	public List<Item> findPromotionItemsByWorkshop(long workshop) {
		return itemDao.findPromotionItemsByWorkshop(workshop);
	}

	/**
	 * 
	 * @param workshop
	 * @param cat
	 *            101,102,103,104,105,106
	 * @param type
	 *            type==0 all; type==1 promotion; type==2 season;
	 * @return
	 */
	public List<Item> filterItems(long workshop, int cat, int type,
			boolean allStat) {

		if (type == 0) {
			return itemDao.filterItemsByWorkshop(workshop, cat, allStat);
		} else if (type == 1) {
			return itemDao.filterPromotionItemsByWorkshop(workshop, cat,
					allStat);
		}
		return itemDao.filterItems(workshop, type, allStat);
	}

	public ProductItem findItemByID(long itemID) {

		Item item = itemDao.findItemByID(itemID);

		if (item == null) {
			return null;
		}

		Product p = itemDao.findProductByID(item.getPid());
		ProductDTO prod = transferMap(p);

		List<ItemDTO> promotions = itemDao.findPromotionItems(item.getSid(), 1,
				itemID);

		ProductItem prodItem = new ProductItem();
		prodItem.setItem(item);
		prodItem.setProduct(prod);
		prodItem.setPromotion(promotions);

		return prodItem;
	}

	public long newItem(Item item) {

		return itemDao.newItem(item);

	}

	public CommentDTO findCommentsByItem(long itemId, int pn) {

		return itemDao.findComments(itemId, pn);
	}

	public boolean newComments(CommentReq commentReq) {

		Map<Long, Comment> comments = commentReq.getItemsRating();
		if (comments == null)
			return false;

		int inc_ac = 0;
		for (Long item : comments.keySet()) {
			Comment comment = comments.get(item);

			comment.setTs(DateUtil.formatCurrentDate());

			itemDao.newComment(item, comment);

			if (StringUtils.isNotBlank(comment.getContent())) {
				inc_ac += 1;
			}
		}

		orderDao.updateStatus(commentReq.getOrderid(),
				OrderStatus.COMMENT.getValue());

		if (inc_ac > 0) {
			userDao.increaseUserAc(commentReq.getUid(), inc_ac);
		}

		return true;
	}

	public List<ShopItem> fetchFavorite(long uid) {
		List<Long> favItems = userDao.findFavorite(uid);

		return groupItemsByShop(favItems);

	}

	public List<ShopItem> fetchShoppingCart(long uid) {
		List<Long> items = userDao.findShoppingCart(uid);

		return groupItemsByShop(items);
	}

	public List<Product> listProducts(long workshop, int cat) {

		return itemDao.findProducts(workshop, cat);
	}

	public List<ShopItem> groupItemsByShop(List<Long> itemids) {

		List<ShopItem> result = new ArrayList<ShopItem>();

		if (itemids != null && itemids.size() > 0) {
			List<ItemDTO> items = itemDao.findItems(itemids,
					Constants.ITEM_STATUS_ONSALE);

			Map<Long, List<ItemDTO>> shopitems = new HashMap<Long, List<ItemDTO>>();
			for (ItemDTO each : items) {

				if (shopitems.containsKey(each.getSid())) {
					shopitems.get(each.getSid()).add(each);
				} else {
					List<ItemDTO> itembyshop = new ArrayList<ItemDTO>();
					itembyshop.add(each);
					shopitems.put(each.getSid(), itembyshop);
				}
			}

			Map<Long, WorkshopDTO> shops = shopDao
					.findByIDs(shopitems.keySet());

			for (Long sid : shopitems.keySet()) {
				ShopItem si = new ShopItem();
				si.setShop(shops.get(sid));
				si.setItem(shopitems.get(sid));
				result.add(si);
			}
		}
		return result;
	}

	private ProductDTO transferMap(Product p) {

		ProductDTO prod = new ProductDTO();
		prod.setId(p.getId());
		prod.setName(p.getName());
		prod.setPic(p.getPic());
		prod.setDesc(p.getDesc());
		prod.setCooked(p.getCooked());
		prod.setCpic(p.getCpic());
		prod.setSeason(p.isSeason());

		List<Integer> tagsid = p.getTags();
		if (tagsid != null) {
			List<String> tags = new ArrayList<String>();
			Map<Integer, String> tagMap = dictDao.findTags(tagsid);
			for (Integer id : tagsid) {
				tags.add(tagMap.get(id));
			}
			prod.setTags(tags);
		}

		// if (p.getRef() != null) {
		// List<ItemDTO> refItems = itemDao.findItems(p.getRef());
		// prod.setRef(refItems);
		// }

		return prod;
	}

}
