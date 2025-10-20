package egovframework.let.wms.component.common;

import java.util.HashMap;
import java.util.Map;

public class PageDefaultMap extends HashMap<String, Object> {

    public PageDefaultMap() {
        put("pageIndex", 1);
        put("pageSize", 10);
        calculateOffsets();
    }

    public PageDefaultMap(Map<String, Object> params) {
        this();
        if (params != null) {
            this.putAll(params);
        }
        calculateOffsets();
    }

    public int getPageIndex() {
        Object val = get("pageIndex");
        return val != null ? Integer.parseInt(val.toString()) : 1;
    }

    public int getPageSize() {
        Object val = get("pageSize");
        return val != null ? Integer.parseInt(val.toString()) : 10;
    }

    public int getFirstIndex() {
        Object val = get("firstIndex");
        return val != null ? Integer.parseInt(val.toString()) : 0;
    }

    public int getRecordCountPerPage() {
        Object val = get("recordCountPerPage");
        return val != null ? Integer.parseInt(val.toString()) : getPageSize();
    }

    /** 
     * pageIndex(1-based), pageSize 기반으로  
     * firstIndex (offset), recordCountPerPage (limit) 계산 및 세팅 
     */
    public void calculateOffsets() {
        int pageIndex = getPageIndex();
        int pageSize = getPageSize();

        int firstIndex = (pageIndex - 1) * pageSize;
        if(firstIndex < 0) firstIndex = 0;

        put("firstIndex", firstIndex);              // 쿼리 OFFSET 용
        put("recordCountPerPage", pageSize);        // 쿼리 LIMIT 용
    }
}