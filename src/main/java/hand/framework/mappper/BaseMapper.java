package hand.framework.mappper;


import hand.framework.mybatis.criterion.Condition;

import java.util.List;
import java.util.Map;

public interface BaseMapper<E> {
   
	E getById(int id);
	
	List<E> getAll();

	List<E> getByCondition(Condition condition);
	
//	List<E> getByPage(Condition condition,PageInfo pageInfo);
	
	Map saveOrUpdate(E e);
	
    Integer deleteById(Integer id);
}
