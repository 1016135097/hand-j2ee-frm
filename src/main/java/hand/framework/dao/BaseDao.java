package hand.framework.dao;


import hand.framework.entity.PageInfo;
import hand.framework.entity.PagingEntity;
import hand.framework.mybatis.criterion.Condition;

import java.util.List;
import java.util.Map;

public interface BaseDao<E>{

	E getById(int id);
	
	List<E> getAll();

	List<E> getByCondition(Condition condition);
	
	PagingEntity getByPage(Condition condition, PageInfo pageInfo);
	
	Map saveOrUpdate(E e);
	
	Integer deleteById(Integer id);
}
