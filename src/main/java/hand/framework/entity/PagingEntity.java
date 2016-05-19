package hand.framework.entity;

import java.util.List;

public class PagingEntity {
	
	private long totalCount;
	
	private List<?> valueList;
	
	public PagingEntity(long totalCount, List<?> valueList) {
		super();
		this.totalCount = totalCount;
		this.valueList = valueList;
	}

	public PagingEntity() {
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<?> getValueList() {
		return valueList;
	}

	public void setValueList(List<?> valueList) {
		this.valueList = valueList;
	}
	
	

}
