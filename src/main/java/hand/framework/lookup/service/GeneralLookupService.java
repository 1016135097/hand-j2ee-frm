package hand.framework.lookup.service;


import hand.framework.entity.PageInfo;
import hand.framework.entity.PagingEntity;
import hand.framework.lookup.entity.GeneralLookup;
import hand.framework.lookup.entity.LookupOtherParams;
import hand.framework.mybatis.criterion.Condition;

import java.util.List;

public interface GeneralLookupService {

	List<GeneralLookup> getLookup(String type);
	
	List<GeneralLookup> getLookupByCondition(Condition condition);

    PagingEntity getLookupByConditionAndPage(Condition condition, PageInfo pageInfo);
    
    String queryLookup(String lookupId, LookupOtherParams params);
}
