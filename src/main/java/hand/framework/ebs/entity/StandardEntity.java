package hand.framework.ebs.entity;


import hand.framework.ebs.util.EBSContext;
import hand.framework.entity.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public abstract class StandardEntity extends BaseEntity {

	
	public StandardEntity(){
		super();
		populateWHO();
	}
	
	// WHO
	protected BigDecimal createdBy;
	
	protected Timestamp creationDate;
	
	protected BigDecimal lastUpdatedBy;
	
	protected Timestamp lastUpdateDate;
	
	protected BigDecimal lastUpdateLogin;

	public BigDecimal getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(BigDecimal createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public BigDecimal getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(BigDecimal lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public BigDecimal getLastUpdateLogin() {
		return lastUpdateLogin;
	}

	public void setLastUpdateLogin(BigDecimal lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}
	
	public void populateWHO(){
		
		EBSContext context = EBSContext.getInstance();
		
		if(context == null){
			return;
		}
		
		if(createdBy == null){
			setCreatedBy(context.getUserId());
		}
		
		if(lastUpdatedBy == null){
			setLastUpdatedBy(context.getUserId());
		}
		
		Timestamp now = new Timestamp(System.currentTimeMillis()); 
		
		setCreationDate(now);
		setLastUpdateDate(now);
		
		setLastUpdateLogin(context.getLoginId());
		
	}
	
}
