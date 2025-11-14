package egovframework.let.wms.table.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import egovframework.let.wms.table.service.CommonService;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("commonService")
public class CommonServiceImpl extends EgovAbstractServiceImpl implements CommonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getColumnInfo(String tableName) throws Exception {
        String sql = "SELECT COLUMN_NAME AS columnName, COLUMN_COMMENT AS columnComment " +
                     "FROM INFORMATION_SCHEMA.COLUMNS " +
                     "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ? " +
                     "ORDER BY ORDINAL_POSITION";
        return jdbcTemplate.queryForList(sql, tableName);
    }

    @Override
    public List<Map<String, Object>> selectList(String tableName, Map<String, Object> params) throws Exception {
        // Note: This basic implementation does not support WHERE clauses for the list query.
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public Map<String, Object> selectOne(String tableName, Map<String, Object> pk) throws Exception {
        List<Object> pkValues = new ArrayList<>(pk.values());
        String whereClause = pk.keySet().stream()
                               .map(key -> key + " = ?")
                               .collect(Collectors.joining(" AND "));
        String sql = "SELECT * FROM " + tableName + " WHERE " + whereClause;
        return jdbcTemplate.queryForMap(sql, pkValues.toArray());
    }

    @Override
    public void insert(String tableName, Map<String, Object> data) throws Exception {
        String columns = String.join(", ", data.keySet());
        String placeholders = StringUtils.collectionToCommaDelimitedString(
            data.keySet().stream().map(k -> "?").collect(Collectors.toList()));
        
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
        
        jdbcTemplate.update(sql, data.values().toArray());
    }

    @Override
    public void update(String tableName, Map<String, Object> pk, Map<String, Object> data) throws Exception {
        List<Object> values = new ArrayList<>();
        
        String setClause = data.keySet().stream()
                               .map(key -> {
                                   values.add(data.get(key));
                                   return key + " = ?";
                               })
                               .collect(Collectors.joining(", "));

        String whereClause = pk.keySet().stream()
                               .map(key -> {
                                   values.add(pk.get(key));
                                   return key + " = ?";
                               })
                               .collect(Collectors.joining(" AND "));

        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereClause;
        
        jdbcTemplate.update(sql, values.toArray());
    }

    @Override
    public void delete(String tableName, Map<String, Object> pk) throws Exception {
        List<Object> pkValues = new ArrayList<>(pk.values());
        String whereClause = pk.keySet().stream()
                               .map(key -> key + " = ?")
                               .collect(Collectors.joining(" AND "));
        String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
        jdbcTemplate.update(sql, pkValues.toArray());
    }
}
