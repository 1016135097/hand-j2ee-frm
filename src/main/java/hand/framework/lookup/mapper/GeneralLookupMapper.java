package hand.framework.lookup.mapper;


import hand.framework.lookup.entity.GeneralLookup;
import hand.framework.mybatis.criterion.Condition;

import java.util.List;

public interface GeneralLookupMapper {
	
	List<GeneralLookup> getLookup(String type);
	
	List<GeneralLookup> getLookupByCondition(Condition condition);
}
