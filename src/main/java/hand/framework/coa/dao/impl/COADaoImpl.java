package hand.framework.coa.dao.impl;

import hand.framework.coa.entity.COAConfig;
import hand.framework.dao.impl.BaseDaoImpl;
import hand.framework.mappper.BaseMapper;

public class COADaoImpl extends BaseDaoImpl<COAConfig>{

	private BaseMapper<COAConfig> coaMapper;
	
	public COADaoImpl() {
	}

	@Override
	public BaseMapper<COAConfig> getMapper() {
		return coaMapper;
	}

	public BaseMapper<COAConfig> getCoaMapper() {
		return coaMapper;
	}

	public void setCoaMapper(BaseMapper<COAConfig> coaMapper) {
		this.coaMapper = coaMapper;
	}

	
	

}
