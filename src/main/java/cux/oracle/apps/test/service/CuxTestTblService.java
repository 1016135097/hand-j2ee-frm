package cux.oracle.apps.test.service;

import cux.oracle.apps.test.entity.CuxTestTblEO;

import java.util.List;

public interface CuxTestTblService {

	public void save(CuxTestTblEO eo);
	
	public List<CuxTestTblEO> getAll();
	
}
