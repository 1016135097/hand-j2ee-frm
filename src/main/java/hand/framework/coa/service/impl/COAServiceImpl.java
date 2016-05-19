package hand.framework.coa.service.impl;

import hand.framework.coa.entity.COAConfig;
import hand.framework.coa.service.COAService;
import hand.framework.dao.BaseDao;
import hand.framework.ebs.exception.InvalidOPExcetion;
import hand.framework.mybatis.criterion.Condition;
import hand.framework.plsql.util.CallableManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class COAServiceImpl implements COAService{

	private BaseDao<COAConfig> coaDao;

	private CallableManager callableManager;
	
	@Override
	public List<COAConfig> getCoaConfigs(BigDecimal orgId) {
		Condition c = new Condition();
		c.createCriteria().addCriterion("organizationId=",orgId,"organizationId");
		return coaDao.getByCondition(c);
	}
	
	public Integer getCCID(BigDecimal orgId,String segments){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", orgId);
		params.put("segments", segments);
		callableManager.excuteCall("hand.framework.coa.getCCID", params);
		
		BigDecimal ccid = (BigDecimal) params.get("x_ccid");
		String msg = (String) params.get("x_msg");
		
		if(ccid == null || ccid.intValue() == 0 || ccid .intValue()== -1){
			throw new InvalidOPExcetion(msg);
		}
		
		return ccid.intValue();
	}
	
	public String getCOADesc(BigDecimal orgId,BigDecimal ccid){
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", orgId);
		params.put("x_ccid", ccid);
		callableManager.excuteCall("cux_common_utl.get_account_segements_desc", params);
		
		String desc = (String) params.get("x_account_desc");
		return desc;
	}
	public BaseDao<COAConfig> getCoaDao() {
		return coaDao;
	}

	public void setCoaDao(BaseDao<COAConfig> coaDao) {
		this.coaDao = coaDao;
	}

	public CallableManager getCallableManager() {
		return callableManager;
	}

	public void setCallableManager(CallableManager callableManager) {
		this.callableManager = callableManager;
	}
	
	

	
}
