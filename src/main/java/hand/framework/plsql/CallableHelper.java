/**
 * 拦截器
 * 拦截mybatis代码，替换执行SQL
 * Pany@2015-3-23
 */
package hand.framework.plsql;

import hand.framework.entity.CallableEntity;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts(@Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }))
public class CallableHelper implements Interceptor {
	
	private static Logger logger = Logger.getLogger(CallableHelper.class);
	private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<ResultMapping>(0);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		Object parameterObject = args[1];

		if (!(parameterObject instanceof CallableEntity)) {
			return invocation.proceed();
		}
		CallableEntity ce = (CallableEntity) parameterObject;

		String sql = ce.getSql();

		Map<String, Object> m = ce.getParams();

		SqlSourceBuilder s = new SqlSourceBuilder(ms.getConfiguration());

		SqlSource sqlSource = s.parse(sql, Map.class,null);
		
		BoundSql bs = sqlSource.getBoundSql(m);
		
		MappedStatement qs = newMappedStatement(ms, new BoundSqlSqlSource(bs));
		
	    args[0] = qs;
	    args[1] = ce.getParams();
	    
        MetaObject msObject = SystemMetaObject.forObject(qs);
        
        String sql2 = (String) msObject.getValue("sqlSource.boundSql.sql");
 		
        msObject.setValue("sqlSource.boundSql.sql",sql2);
 		
        logger.info("SQL:"+sql2);
        
        return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}

	private class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

	private MappedStatement newMappedStatement(MappedStatement ms,
											   SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(
				ms.getConfiguration(), ms.getId() + "_hand_excute",
				newSqlSource, ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
			StringBuffer keyProperties = new StringBuffer();
			for (String keyProperty : ms.getKeyProperties()) {
				keyProperties.append(keyProperty).append(",");
			}
			keyProperties.delete(keyProperties.length() - 1,
					keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(),
				ms.getId(), int.class, EMPTY_RESULTMAPPING).build();
		resultMaps.add(resultMap);
		builder.resultMaps(resultMaps);
		builder.resultSetType(ms.getResultSetType());
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

}
