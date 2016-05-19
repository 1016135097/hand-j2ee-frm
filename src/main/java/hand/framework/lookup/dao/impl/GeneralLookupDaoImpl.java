package hand.framework.lookup.dao.impl;

import com.github.pagehelper.Page;
import hand.framework.entity.PageInfo;
import hand.framework.entity.PagingEntity;
import hand.framework.lookup.dao.GeneralLookupDao;
import hand.framework.lookup.entity.GeneralLookup;
import hand.framework.lookup.mapper.GeneralLookupMapper;
import hand.framework.mybatis.criterion.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("generalLookupDao")
public class GeneralLookupDaoImpl implements GeneralLookupDao {

	@Autowired
	private GeneralLookupMapper generalLookupMapper;
	
	@Override
	public List<GeneralLookup> getLookup(String type) {
		return generalLookupMapper.getLookup(type);
	}

	@Override
	public List<GeneralLookup> getLookupByCondition(Condition condition) {
		return generalLookupMapper.getLookupByCondition(condition);
	}

	@Override
	public PagingEntity getLookupByPage(Condition condition, PageInfo pageInfo){
		List<GeneralLookup> list = generalLookupMapper.getLookupByCondition(condition);
		long total = ((Page)list).getTotal();
		
		PagingEntity pe = new PagingEntity(total,list);
		return pe;
	}
	
	public GeneralLookupMapper getGeneralLookupMapper() {
		return generalLookupMapper;
	}

	public void setGeneralLookupMapper(GeneralLookupMapper generalLookupMapper) {
		this.generalLookupMapper = generalLookupMapper;
	}
	
	

}
