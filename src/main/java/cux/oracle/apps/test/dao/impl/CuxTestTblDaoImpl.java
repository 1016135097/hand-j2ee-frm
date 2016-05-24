package cux.oracle.apps.test.dao.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import hand.framework.dao.impl.BaseDaoImpl;
import hand.framework.mappper.BaseMapper;
import cux.oracle.apps.test.entity.CuxTestTblEO;
import cux.oracle.apps.test.mapper.CuxTestTblMapper;
@Repository("cuxTestTblDao")
public class CuxTestTblDaoImpl extends BaseDaoImpl<CuxTestTblEO> {

	@Autowired
	private CuxTestTblMapper cuxTestTblMapper;
	public CuxTestTblMapper getCuxTestTblMapper() {
		return cuxTestTblMapper;
	}

	public void  setCuxTestTblMapper (CuxTestTblMapper cuxTestTblMapper) {
		this.cuxTestTblMapper = cuxTestTblMapper ;
	}

	@Override
	public BaseMapper<CuxTestTblEO> getMapper() {
		return this.cuxTestTblMapper ;
	}

}
