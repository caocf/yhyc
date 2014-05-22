package com.aug3.yhyc.domain;

import java.util.List;

import com.aug3.yhyc.dao.UserDao;
import com.aug3.yhyc.dao.WorksDao;
import com.aug3.yhyc.valueobj.Works;

public class WorksDomain {

	private WorksDao worksDao;

	private UserDao userDao;

	public WorksDao getWorksDao() {
		return worksDao;
	}

	public void setWorksDao(WorksDao worksDao) {
		this.worksDao = worksDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public boolean uploadWorks(Works works) {
		return worksDao.createWorks(works);
	}
	
	public boolean updateWorks(Works works) {
		return worksDao.updateWorks(works);
	}
	
	public boolean deleteWorks(long uid, long id, boolean admin) {
		return worksDao.deleteWorks(uid, id, admin);
	}

	public List<Works> listWorks(int pn) {

		List<Works> works = worksDao.find(pn);

		return works;
	}

	public Works showWorks(long id) {

		Works works = worksDao.findByID(id);

		return works;
	}

	public List<Works> listWorksByUser(long uid, int pn) {

		List<Works> works = worksDao.findByUser(uid, pn);

		return works;
	}

	public List<Works> listFavWorksByUser(long uid, int pn) {

		List<Long> ids = userDao.findFavWorks(uid);
		if (ids == null)
			return null;

		List<Works> works = worksDao.findByID(ids, pn);

		return works;
	}

}
