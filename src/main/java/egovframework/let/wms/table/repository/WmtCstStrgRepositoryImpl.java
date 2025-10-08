package egovframework.let.wms.table.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.let.wms.table.entity.WmtCstStrg;

@Repository
public class WmtCstStrgRepositoryImpl implements WmtCstStrgRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WmtCstStrg save(Map<String, Object> param) {
        WmtCstStrg entity = objectMapper.convertValue(param, WmtCstStrg.class);

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        entity.setInsDatetime(now);
        entity.setUpdDatetime(now);
        if (entity.getInsUserId() == null) entity.setInsUserId("admin");
        if (entity.getUpdUserId() == null) entity.setUpdUserId("admin");

        em.merge(entity);
        return entity;
    }
}
