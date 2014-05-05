package com.aug3.yhyc.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aug3.yhyc.dao.ShequDao;
import com.aug3.yhyc.dto.WorkshopDTO;
import com.aug3.yhyc.valueobj.Shequ;
import com.aug3.yhyc.valueobj.Workshop;

public class ShequDomain {

	private ShequDao shequDao;

	public ShequDao getShequDao() {
		return shequDao;
	}

	public void setShequDao(ShequDao shequDao) {
		this.shequDao = shequDao;
	}

	public List<Shequ> queryShequ(String city, String q) {
		return shequDao.findShequ(city, q);
	}

	public List<WorkshopDTO> queryWorkshops(String shequ) {

		List<Workshop> list = shequDao.findWorkshops(Long.parseLong(shequ));

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
			dto.setDist(shop.getDist());
			dto.setAddr(shop.getAddr());
			dto.setTel(shop.getTel());
			dto.setStart(shop.getStart());

			List<Shequ> sqlist = new ArrayList<Shequ>();
			for (Long id : shop.getShequ()) {
				sqlist.add(shequMap.get(id));
			}
			dto.setShequ(sqlist);
		}
		return result;
	}

}