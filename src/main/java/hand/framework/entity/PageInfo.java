package hand.framework.entity;

public class PageInfo {

	private int start;

	private int limit;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageNum() {
		return (start + limit) / limit;
	}
	//
	// public void setPageNum(int pageNum) {
	// this.pageNum = pageNum;
	// }

}
