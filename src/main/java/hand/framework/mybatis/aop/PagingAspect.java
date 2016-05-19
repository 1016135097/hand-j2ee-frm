package hand.framework.mybatis.aop;

import hand.framework.entity.PageInfo;
import hand.framework.mybatis.pagehelper.PageHelper;
import org.aspectj.lang.ProceedingJoinPoint;

public class PagingAspect {

	public Object doAround(ProceedingJoinPoint jp) throws Throwable{
	    Object[] args = jp.getArgs();
		for(Object obj:args){
			if(obj instanceof PageInfo){
				PageInfo pi = (PageInfo)obj;
				int pageNum = pi.getPageNum();
				int numPerPage = pi.getLimit();
				PageHelper.startPage(pageNum, numPerPage);
			}
		}
		
		return jp.proceed(args);
	}
}
