package egovframework.let.wms.component.common;

import java.util.List;

public class PageResult <T> {

	private List<T> resultList;
	private int totalCount;
	
	public PageResult(List<T> resultList, int totalCount) {
		this.resultList = resultList;
		this.totalCount = totalCount;
	}
	
	public List<T> getResultList() {
		return resultList;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
}
