package hand.framework.coa.entity;

import java.math.BigDecimal;

public class COAConfig {
	
	private BigDecimal organizationId;
	
	private String prompt;
	
	private BigDecimal segmentNum;

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public BigDecimal getSegmentNum() {
		return segmentNum;
	}

	public void setSegmentNum(BigDecimal segmentNum) {
		this.segmentNum = segmentNum;
	}

	public BigDecimal getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(BigDecimal organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
