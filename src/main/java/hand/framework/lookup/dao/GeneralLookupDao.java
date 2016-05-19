package hand.framework.lookup.dao;


import hand.framework.entity.PageInfo;
import hand.framework.entity.PagingEntity;
import hand.framework.lookup.entity.GeneralLookup;
import hand.framework.mybatis.criterion.Condition;

import java.util.List;

public interface GeneralLookupDao {

	List<GeneralLookup> getLookup(String type);
	
	List<GeneralLookup> getLookupByCondition(Condition condition);
	
    PagingEntity getLookupByPage(Condition condition, PageInfo pageInfo);
}
