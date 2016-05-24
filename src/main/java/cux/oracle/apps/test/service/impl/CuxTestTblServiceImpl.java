package cux.oracle.apps.test.service.impl;

import cux.oracle.apps.test.service.CuxTestTblService;
import hand.framework.dao.BaseDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cux.oracle.apps.test.entity.CuxTestTblEO;

@Service("cuxTestTblService")
public class CuxTestTblServiceImpl implements CuxTestTblService {

	@Autowired
	public BaseDao<CuxTestTblEO> cuxTestTblDao;
	
	@Override
	public void save(CuxTestTblEO eo) {
		cuxTestTblDao.saveOrUpdate(eo);
	}

	@Override
	public List<CuxTestTblEO> getAll() {
		return cuxTestTblDao.getAll();
	}

	
}
