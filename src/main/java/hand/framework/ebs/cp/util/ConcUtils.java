package hand.framework.ebs.cp.util;

import hand.framework.ebs.util.EBSContext;
import hand.framework.ebs.util.UrlUtils;
import hand.framework.plsql.util.CallableManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ConcUtils {
	
	private CallableManager callableManager;

	public CallableManager getCallableManager() {
		return callableManager;
	}

	public void setCallableManager(CallableManager callableManager) {
		this.callableManager = callableManager;
	}
	
	public BigDecimal runAndWaitRequest(String applShortName,
										String programName,
										Vector params){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("p_application", applShortName);
		map.put("p_program", programName);
		
		// CHR(0)
		char[] c = new char[1];
    	c[0] = 0;
    	String chr0 = new String(c);
		
		int size = params.size();
		for(int i = 0; i < size; i++){
			map.put("p_argument"+(i+1), params.get(i));
		}
		
		for(int i = size + 1; i <=100 - size; i ++){
			map.put("p_argument"+(i), chr0);
		}
		
		callableManager.excuteCall("cux_fnd_request.submit_request", map);
		
	  	BigDecimal requestId = (BigDecimal) map.get("x_request_id");
	  	return requestId;
	}
	
	public String getOutputURL(BigDecimal requestId){
		StringBuilder buffer = new StringBuilder();
		EBSContext context = EBSContext.getInstance();
		buffer.append(context.getAgent()).append("OA.jsp?akRegionApplicationId=0&akRegionCode=FNDCPREQUESTVIEWPAGE&REQUESTID=");
		buffer.append(requestId);
		buffer.append("&OUTPUT=Y");
		
		String secureUrl = UrlUtils.generateSecureURL(buffer.toString());
		return secureUrl;
	}
	
}
