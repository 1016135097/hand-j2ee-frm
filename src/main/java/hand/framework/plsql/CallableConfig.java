package hand.framework.plsql;


import hand.framework.entity.CallableEntity;

public class CallableConfig {
//
	private String id;
	
	private String sql;
	
	private CallableEntity callableEntity;

	public CallableConfig(){
		super();
	}
	
	public CallableConfig(String id, String sql) {
		super();
		this.id = id;
		this.sql = sql;
		callableEntity = new CallableEntity(sql, null);
	}

	public String getId() {
		return id;
	}

	public String getSql() {
		return sql;
	}

	public CallableEntity getCallableEntity() {
		return callableEntity;
	}
	
	
	
	
}
