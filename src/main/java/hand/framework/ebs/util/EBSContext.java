package hand.framework.ebs.util;

//import oracle.apps.fnd.security.HMAC;

import java.math.BigDecimal;
import java.util.Map;

public class EBSContext {
	
	private static ThreadLocal<EBSContext> contextLocal = new ThreadLocal<EBSContext>();
	
	private BigDecimal userId;
	
	private BigDecimal sessionId;
	
	private String userName;
	
	private String modeCode;
	
	private BigDecimal responsibilityId;
	
	private BigDecimal orgId;
	
	private BigDecimal functionId;
	
	private String nlsLanguage;
	
	private String dateFormatMask;
	
	private BigDecimal responsibilityApplicationId;
	
	private BigDecimal loginId;
	
	private String agent;
	
	private byte[] macKey;
	
	//private HMAC hmac;
	
	private String langCode;
	
	private Map<String, Object> sessionInfo;
	
	public BigDecimal getUserId() {
		return userId;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getModeCode() {
		return modeCode;
	}

	public void setModeCode(String modeCode) {
		this.modeCode = modeCode;
	}

	public BigDecimal getResponsibilityId() {
		return responsibilityId;
	}

	public void setResponsibilityId(BigDecimal responsibilityId) {
		this.responsibilityId = responsibilityId;
	}

	public BigDecimal getOrgId() {
		return orgId;
	}

	public void setOrgId(BigDecimal orgId) {
		this.orgId = orgId;
	}

	public BigDecimal getFunctionId() {
		return functionId;
	}

	public void setFunctionId(BigDecimal functionId) {
		this.functionId = functionId;
	}

	public String getNlsLanguage() {
		return nlsLanguage;
	}

	public void setNlsLanguage(String nlsLanguage) {
		this.nlsLanguage = nlsLanguage;
	}

	public String getDateFormatMask() {
		return dateFormatMask;
	}

	public void setDateFormatMask(String dateFormatMask) {
		this.dateFormatMask = dateFormatMask;
	}

	public BigDecimal getResponsibilityApplicationId() {
		return responsibilityApplicationId;
	}

	public void setResponsibilityApplicationId(
			BigDecimal responsibilityApplicationId) {
		this.responsibilityApplicationId = responsibilityApplicationId;
	}

	public BigDecimal getSessionId() {
		return sessionId;
	}

	public void setSessionId(BigDecimal sessionId) {
		this.sessionId = sessionId;
	}
	
	public static void setContext(EBSContext context){
		contextLocal.set(context);
	}
	
	public BigDecimal getLoginId() {
		return loginId;
	}

	public void setLoginId(BigDecimal loginId) {
		this.loginId = loginId;
	}

	public static EBSContext getInstance(){
		return contextLocal.get();
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public byte[] getMacKey() {
		return macKey;
	}

	public void setMacKey(byte[] macKey) {
		this.macKey = macKey;
	}
	
/*
	public HMAC getHmac() {
		return hmac;
	}

	public void setHmac(HMAC hmac) {
		this.hmac = hmac;
	}
*/
	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public Map<String, Object> getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(Map<String, Object> sessionInfo) {
		this.sessionInfo = sessionInfo;
	}
	

//	@Override
//	public String toString() {
//		return "EBSContext [userId=" + userId + ", sessionId=" + sessionId
//				+ ", responsibilityId=" + responsibilityId + ", orgId=" + orgId
//				+ ", functionId=" + functionId + ", nlsLanguage=" + nlsLanguage
//				+ ", responsibilityApplicationId="
//				+ responsibilityApplicationId + ", loginId=" + loginId + "]";
//	}

}
