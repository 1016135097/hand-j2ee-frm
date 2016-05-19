package hand.framework.dao.impl;

import com.github.pagehelper.Page;
import hand.framework.dao.BaseDao;
import hand.framework.entity.PageInfo;
import hand.framework.entity.PagingEntity;
import hand.framework.mappper.BaseMapper;
import hand.framework.mybatis.criterion.Condition;

import java.util.List;
import java.util.Map;

public abstract class BaseDaoImpl<E> implements BaseDao<E> {
     
	public E getById(int id) {
		return getMapper().getById(id);
	}

	public List<E> getAll() {
		return getMapper().getAll();
	}

	public List<E> getByCondition(Condition condition) {
		System.out.println(Thread.currentThread());
		return getMapper().getByCondition(condition);
	}

	public PagingEntity getByPage(Condition condition, PageInfo pageInfo) {
		List<E> list =  getMapper().getByCondition(condition);
		long total = ((Page)list).getTotal();
		PagingEntity qpe=new PagingEntity(total, list);
		return qpe;
	}

	public Map saveOrUpdate(E e) {
		return getMapper().saveOrUpdate(e);
	}

	public Integer deleteById(Integer id) {
		return getMapper().deleteById(id);
	}
	
	public abstract BaseMapper<E> getMapper();

}
