package hand.framework.entity;

import java.util.Map;

public class CallableEntity {

	private String sql;
	
	private Map<String,Object> params;

	public CallableEntity(){
		super();
	}
	public CallableEntity(String sql, Map<String, Object> params) {
		super();
		this.sql = sql;
		this.params = params;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
}
