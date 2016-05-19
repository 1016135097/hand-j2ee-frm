package hand.framework.mybatis.pagehelper.parser.impl;

import com.github.pagehelper.Page;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;

public class OracleParser extends AbstractParser {
    @Override
    public String getPageSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 150);
        sqlBuilder.append("select * from ( select tmp_page.*  from ( ");
        
        sqlBuilder.append("select rownum as row_id,t.* from(");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ）t ) tmp_page where rownum <= ? ) where row_id > ?");
        return sqlBuilder.toString();
    }

	@Override
    public Map setPageParameter(MappedStatement ms, Object parameterObject, BoundSql boundSql, Page page) {
        Map paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
        paramMap.put(PAGEPARAMETER_FIRST, page.getEndRow());
        paramMap.put(PAGEPARAMETER_SECOND, page.getStartRow());
        return paramMap;
    }
	
	@Override
	public String getCountSql(String sql) {
		String countSql  = super.getCountSql(sql);
		return countSql;
	}
}
