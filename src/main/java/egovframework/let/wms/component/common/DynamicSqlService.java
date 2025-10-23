package egovframework.let.wms.component.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.let.wms.table.entity.SqlStore;
import egovframework.let.wms.table.repository.SqlStoreRepository;

@Service
public class DynamicSqlService {

	@Autowired
    private SqlSession sqlSession;
	
    @Autowired
    private SqlStoreRepository sqlStoreRepository;

    public List<Map<String, Object>> executeDynamicSql(String sqlId, Map<String, Object> params) {
        String rawSql = getSqlTextById(sqlId);

        String wrappedSql = rawSql.trim();
//        String wrappedSql = rawSql.trim().startsWith("<select") ? rawSql : "<select>" + rawSql + "</select>";

        Configuration configuration = sqlSession.getConfiguration();
        XMLLanguageDriver languageDriver = new XMLLanguageDriver();

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, wrappedSql, Map.class);

        String msId = "dynamicSql_" + sqlId;
        
        synchronized (configuration) {  // thread-safe 추가
            if (!configuration.hasStatement(msId)) {
                ResultMap resultMap = new ResultMap.Builder(configuration, msId + "-InlineResultMap", Map.class, Collections.emptyList()).build();
                MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, SqlCommandType.SELECT)
                    .resultMaps(Collections.singletonList(resultMap))
                    .build();
                configuration.addMappedStatement(ms);
            }
        }

        try {
            return sqlSession.selectList(msId, params); // 실제 실행
        } catch (Exception e) {
            throw new RuntimeException("동적 SQL 실행 실패: " + e.getMessage(), e);
        }
    }
    
    public String getSqlTextById(String sqlId) {
        SqlStore sqlStore = sqlStoreRepository.findBySqlId(sqlId);
        if (sqlStore == null) {
            throw new RuntimeException("해당 SQL_ID에 대한 SQL이 존재하지 않습니다: " + sqlId);
        }
        return sqlStore.getSqlText();
    }
}
