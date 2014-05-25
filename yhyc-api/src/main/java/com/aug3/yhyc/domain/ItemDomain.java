package com.aug3.yhyc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aug3.sys.util.DateUtil;
import com.aug3.yhyc.dao.DictDao;
import com.aug3.yhyc.dao.ItemDao;
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

	public List<Item> findItemsByWorkshop(long workshop) {
		return itemDao.findItemsByWorkshop(workshop);
	}

	public ProductItem findItemByID(long itemID) {
		Item item = itemDao.findItemByID(itemID);

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

	public CommentDTO findCommentsByItem(long itemId, int pn) {

		return itemDao.findComments(itemId, pn);
	}

	public boolean newComments(CommentReq commentReq) {

		String items = commentReq.getItems();
		if (items == null)
			return false;

		String[] parts = items.split(",");
		if (parts == null) {
			return false;
		} else {
			Comment comment = new Comment();

			if (commentReq.getUid() != null)
				comment.setUid(Long.parseLong(commentReq.getUid()));
			comment.setName(commentReq.getName());
			comment.setScore(Integer.parseInt(commentReq.getScore()));
			comment.setContent(commentReq.getContent());
			comment.setTs(DateUtil.formatCurrentDate());

			for (int i = 0; i < parts.length; i++) {
				itemDao.newComment(Long.parseLong(parts[i]), comment);
			}

			return true;
		}
	}

	public List<ShopItem> fetchFavorite(long uid) {
		List<Long> favItems = userDao.findFavorite(uid);

		return groupItemsByShop(favItems);

	}

	public List<ShopItem> fetchShoppingCart(long uid) {
		List<Long> items = userDao.findShoppingCart(uid);

		return groupItemsByShop(items);
	}

	private List<ShopItem> groupItemsByShop(List<Long> itemids) {

		List<ShopItem> result = new ArrayList<ShopItem>();

		if (itemids != null && itemids.size() > 0) {
			List<ItemDTO> items = itemDao.findItems(itemids);

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

			Map<Long, WorkshopDTO> shops = shopDao.findByID(shopitems.keySet());

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

		List<Integer> tagsid = p.getTags();
		if (tagsid != null) {
			List<String> tags = new ArrayList<String>();
			Map<Integer, String> tagMap = dictDao.findTags(tagsid);
			for (Integer id : tagsid) {
				tags.add(tagMap.get(id));
			}
		}

		// if (p.getRef() != null) {
		// List<ItemDTO> refItems = itemDao.findItems(p.getRef());
		// prod.setRef(refItems);
		// }

		return prod;
	}

}
