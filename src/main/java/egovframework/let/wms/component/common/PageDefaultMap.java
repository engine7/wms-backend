package egovframework.let.wms.component.common;

import java.util.HashMap;
import java.util.Map;

public class PageDefaultMap extends HashMap<String, Object> {

	public PageDefaultMap() {
		put("pageIndex", 1);
		put("pageSize", 10);
	}

	public PageDefaultMap(Map<String, Object> params) {
		this();
		
		if (params != null) {
			this.putAll(params);
		}
		
		calculateOffset();
	}
	
	public int getPageIndex () {
		return Integer.parseInt(get("pageIndex").toString());
	}
	
	public int getPageSize() {
		return Integer.parseInt(get("pageSize").toString());
	}
	
	public int getOffset() {
		return Integer.parseInt(get("offset").toString());
	}
	
	public void calculateOffset() {
		int pageIndex = getPageIndex();
		int pageSize = getPageSize();
		
		put("offset", pageIndex - 1 * pageSize);
	}
	
}
