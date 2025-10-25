package egovframework.let.wms.component.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DB에 저장된 SQL(<script> 포함)을 실행하는 동적 MyBatis Runner
 * - MyBatis의 XMLLanguageDriver를 이용해 <if>, <where>, <foreach> 문법 그대로 사용 가능
 * - page, pageSize 인수를 감지해 페이징 SQL 자동 추가
 * - SQL_STORE 테이블과 함께 캐싱 구조로 활용 가능
 */
@Component
public class DynamicMyBatisRunner {

    private final Configuration configuration;
    private final DataSource dataSource;

    @Autowired
    public DynamicMyBatisRunner(SqlSessionFactory sqlSessionFactory) {
        this.configuration = sqlSessionFactory.getConfiguration();
        this.dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
    }

    /**
     * DB에 저장된 SQL 텍스트를 실행한다.
     * @param sqlText MyBatis <script> 구문이 포함된 SQL
     * @param params 실행 파라미터 (Map)
     * @param dbType DB 타입 (mysql, postgres, oracle 등)
     */
    public List<Map<String, Object>> execute(String sqlText, Map<String, Object> params, String dbType) throws SQLException {

        // 0️⃣ SQL 정규화 (여러 줄 SQL → 한 줄)
        sqlText = normalizeSql(sqlText);

        // 1️⃣ 페이징 처리 (page, pageSize 감지)
        sqlText = applyPaging(sqlText, params, dbType);

        // 2️⃣ MyBatis XML 파서로 <script> 태그 처리
        XMLLanguageDriver langDriver = new XMLLanguageDriver();
        DynamicSqlSource sqlSource = (DynamicSqlSource) langDriver.createSqlSource(configuration, sqlText, Map.class);

        // 3️⃣ BoundSql 생성 (파라미터 바인딩 포함)
        BoundSql boundSql = sqlSource.getBoundSql(params);

        // 4️⃣ JDBC 실행
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(boundSql.getSql())) {

            // 파라미터 바인딩
            int index = 1;
            for (ParameterMapping pm : boundSql.getParameterMappings()) {
                String property = pm.getProperty();
                Object value = params.get(property);
                ps.setObject(index++, value);
            }

            ResultSet rs = ps.executeQuery();
            return mapResult(rs);
        }
    }

    /** ResultSet → List<Map> 변환 (카멜케이스 변환 포함) */
    private List<Map<String, Object>> mapResult(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String colName = meta.getColumnLabel(i);           // DB 컬럼명
                String camelCase = toCamelCase(colName);          // 카멜케이스 변환
                row.put(camelCase, rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    /** DB 종류별 페이징 SQL 자동 처리 */
    private String applyPaging(String sql, Map<String, Object> params, String dbType) {
        if (params == null) return sql;
        Object pageObj = params.get("page");
        Object sizeObj = params.get("pageSize");

        if (pageObj == null || sizeObj == null) return sql;

        int page = Integer.parseInt(pageObj.toString());
        int pageSize = Integer.parseInt(sizeObj.toString());
        int offset = (page - 1) * pageSize;

        dbType = (dbType == null) ? "mysql" : dbType.toLowerCase();

        switch (dbType) {
            case "mysql":
                return sql + " LIMIT " + offset + ", " + pageSize;
            case "postgres":
                return sql + " OFFSET " + offset + " LIMIT " + pageSize;
            case "oracle":
                return "SELECT * FROM ( SELECT a.*, ROWNUM rnum FROM (" + sql + ") a WHERE ROWNUM <= "
                        + (offset + pageSize) + " ) WHERE rnum > " + offset;
            default:
                return sql;
        }
    }
    
    /** 스네이크 케이스 → 카멜케이스 변환 */
    private String toCamelCase(String str) {
        if (str == null) return null;
        str = str.toLowerCase();  // WH_CD -> wh_cd
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                upperNext = true;
            } else {
                sb.append(upperNext ? Character.toUpperCase(c) : c);
                upperNext = false;
            }
        }
        return sb.toString();
    }

    /** 여러 줄 SQL → 한 줄로 정규화 */
    private String normalizeSql(String sql) {
        if (sql == null) return null;
        // 줄바꿈과 탭 제거
        sql = sql.replaceAll("[\\r\\n\\t]+", " ");
        // /* ... */ 주석 제거
        sql = sql.replaceAll("/\\*.*?\\*/", " ");
        // -- 주석 제거
        sql = sql.replaceAll("--.*?(\\r?\\n|$)", " ");
        return sql.trim();
    }
}
