package egovframework.let.wms.component.common;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SqlCache {

    private final Map<String, String> sqlCache = new HashMap<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        loadSqlCache();
    }

    public void loadSqlCache() {
        String sql = "SELECT SQL_ID, SQL_TEXT FROM SQL_STORE";
        jdbcTemplate.query(sql, rs -> {
            sqlCache.put(rs.getString("SQL_ID"), rs.getString("SQL_TEXT"));
        });
        System.out.println("✅ SQL Cache Loaded: " + sqlCache.size() + " entries");
    }

    public String getSql(String sqlId) {
        return sqlCache.get(sqlId);
    }

    public void reload() {
        sqlCache.clear();
        loadSqlCache();
    }
}
