package hand.framework.coa.service;

import hand.framework.coa.entity.COAConfig;

import java.math.BigDecimal;
import java.util.List;

public interface COAService {

	List<COAConfig> getCoaConfigs(BigDecimal orgId);
	
	Integer getCCID(BigDecimal orgId, String segments);
	
	String getCOADesc(BigDecimal orgId, BigDecimal ccid);
}
