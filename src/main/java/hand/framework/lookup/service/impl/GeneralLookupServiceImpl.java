package hand.framework.lookup.service.impl;

import hand.framework.entity.PageInfo;
import hand.framework.entity.PagingEntity;
import hand.framework.lookup.dao.GeneralLookupDao;
import hand.framework.lookup.entity.GeneralLookup;
import hand.framework.lookup.entity.LookupOtherParams;
import hand.framework.lookup.service.GeneralLookupService;
import hand.framework.lookup.utils.LookupManager;
import hand.framework.mybatis.criterion.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("generalLookupService")
public class GeneralLookupServiceImpl implements GeneralLookupService {

	@Autowired
	private GeneralLookupDao generalLookupDao;
	@Autowired
	private LookupManager lookupManager;
	
	public LookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	@Override
	public List<GeneralLookup> getLookup(String type) {
		return generalLookupDao.getLookup(type);
	}

	@Override
	public List<GeneralLookup> getLookupByCondition(Condition condition) {
		return generalLookupDao.getLookupByCondition(condition);
	}

	public GeneralLookupDao getGeneralLookupDao() {
		return generalLookupDao;
	}

	public void setGeneralLookupDao(GeneralLookupDao generalLookupDao) {
		this.generalLookupDao = generalLookupDao;
	}

	@Override
	public PagingEntity getLookupByConditionAndPage(Condition condition, PageInfo pageInfo) {
		return generalLookupDao.getLookupByPage(condition, pageInfo);
	}

	@Override
	public String queryLookup(String lookupId, LookupOtherParams params) {
		return lookupManager.queryLookup(lookupId, params.getOtherParamName(), params.getOthersParam());
	}

}
