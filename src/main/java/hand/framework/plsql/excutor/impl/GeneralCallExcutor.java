package hand.framework.plsql.excutor.impl;


import hand.framework.entity.CallableEntity;
import hand.framework.plsql.excutor.CallableExcutor;
import hand.framework.plsql.mapper.CallableMapper;

public class GeneralCallExcutor implements CallableExcutor {
	
	
	private CallableMapper callableMapper;

	public void excuteCallable(CallableEntity ce){
		callableMapper.excuteCall(ce);
	}
	

	public CallableMapper getCallableMapper() {
		return callableMapper;
	}

	public void setCallableMapper(CallableMapper callableMapper) {
		this.callableMapper = callableMapper;
	}
	
	
}
