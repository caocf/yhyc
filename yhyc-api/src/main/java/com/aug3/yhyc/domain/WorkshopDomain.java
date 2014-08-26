package com.aug3.yhyc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aug3.yhyc.dao.ShequDao;
import com.aug3.yhyc.dao.WorkshopDao;
import com.aug3.yhyc.dto.RequestShop;
import com.aug3.yhyc.dto.WorkshopDTO;
import com.aug3.yhyc.valueobj.Classification;
import com.aug3.yhyc.valueobj.Shequ;
import com.aug3.yhyc.valueobj.Workshop;

public class WorkshopDomain {

	private WorkshopDao shopDao;

	private ShequDao shequDao;

	public WorkshopDao getShopDao() {
		return shopDao;
	}

	public void setShopDao(WorkshopDao shopDao) {
		this.shopDao = shopDao;
	}

	public ShequDao getShequDao() {
		return shequDao;
	}

	public void setShequDao(ShequDao shequDao) {
		this.shequDao = shequDao;
	}

	public List<WorkshopDTO> queryWorkshops(long shequ, int cat) {

		List<Workshop> list = shopDao.findByShequ(shequ, cat);

		List<WorkshopDTO> result = new ArrayList<WorkshopDTO>();
		if (list != null && !list.isEmpty()) {
			result = mapWorkshop2DTO(list);
		}

		return result;
	}

	public List<WorkshopDTO> queryDefaultWorkshops(long shequ) {

		List<Workshop> list = shopDao.findByShequ(shequ);

		List<WorkshopDTO> result = new ArrayList<WorkshopDTO>();
		if (list != null && !list.isEmpty()) {
			result = mapWorkshop2DTO(list);
		}

		return result;
	}

	public List<Classification> classification() {

		List<Classification> list = shopDao.findAllClassification();

		return list;
	}

	public List<WorkshopDTO> findWorkshops(long uid) {

		List<Workshop> list = shopDao.findShopByUserID(uid);

		List<WorkshopDTO> result = new ArrayList<WorkshopDTO>();
		if (list != null && !list.isEmpty()) {
			result = mapWorkshop2DTO(list);
		}

		return result;
	}

	private List<WorkshopDTO> mapWorkshop2DTO(List<Workshop> list) {
		Set<Long> shequids = new HashSet<Long>();
		for (Workshop shop : list) {
			shequids.addAll(shop.getShequ());
		}
		Map<Long, Shequ> shequMap = null;
		if (shequids.size() > 0) {
			shequMap = shequDao.findShequ(shequids);
		} else {
			shequMap = new HashMap<Long, Shequ>();
		}

		List<WorkshopDTO> result = new ArrayList<WorkshopDTO>();
		WorkshopDTO dto = null;

		for (Workshop shop : list) {
			dto = new WorkshopDTO();
			dto.setId(shop.getId());
			dto.setName(shop.getName());
			dto.setOwner(shop.getOwner());
			dto.setPic(shop.getPic());
			dto.setAddr(shop.getAddr());
			dto.setTel(shop.getTel());
			dto.setStart(shop.getStart());
			dto.setNotice(shop.getNotice());
			dto.setCat(shop.getCat());

			List<Shequ> sqlist = new ArrayList<Shequ>();
			for (Long id : shop.getShequ()) {
				sqlist.add(shequMap.get(id));
			}
			dto.setShequ(sqlist);
			result.add(dto);
		}
		return result;
	}

	public boolean updateShopAnnouncement(long shop, String announcement) {
		return shopDao.updateShopAnnouncement(shop, announcement);
	}

	public WorkshopDTO getShopByID(long uid, long shop) {
		return shopDao.findByID(uid, shop);
	}

	public WorkshopDTO getShopInfo(long shop) {
		return shopDao.find(shop);
	}

	public String getShopAnnouncement(long shop) {
		return shopDao.getShopAnnouncement(shop);
	}

	public String getShopStats(long shop) {
		return shopDao.getShopAnnouncement(shop);
	}

	public boolean requestShop(RequestShop shop) {
		return shopDao.requestShop(shop);
	}

	public boolean requestShopExist(RequestShop shop) {

		return shopDao.requestShopExist(shop);

	}

}
