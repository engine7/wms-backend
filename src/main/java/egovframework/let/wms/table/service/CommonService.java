package egovframework.let.wms.table.service;

import java.util.List;
import java.util.Map;

public interface CommonService {

    List<Map<String, Object>> getColumnInfo(String tableName) throws Exception;

    List<Map<String, Object>> selectList(String tableName, Map<String, Object> params) throws Exception;
    
    Map<String, Object> selectOne(String tableName, Map<String, Object> pk) throws Exception;

    void insert(String tableName, Map<String, Object> data) throws Exception;

    void update(String tableName, Map<String, Object> pk, Map<String, Object> data) throws Exception;

    void delete(String tableName, Map<String, Object> pk) throws Exception;

}
