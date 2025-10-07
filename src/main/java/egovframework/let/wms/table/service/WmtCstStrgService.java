package egovframework.let.wms.table.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.let.wms.table.entity.WmtCstStrg;
import egovframework.let.wms.table.entity.WmtCstStrgId;
import egovframework.let.wms.table.repository.WmtCstStrgRepository;

@Service
public class WmtCstStrgService {

    private final WmtCstStrgRepository repository;

    public WmtCstStrgService(WmtCstStrgRepository repository) {
        this.repository = repository;
    }

    @Transactional("transactionManager")
    public WmtCstStrg insert(Map<String, Object> param) {
//    	// Map → Entity 변환
//    	ObjectMapper objectMapper = new ObjectMapper();
//        WmtCstStrg entity = objectMapper.convertValue(param, WmtCstStrg.class);
//
//        // 자동으로 생성일/수정일 넣기
//        String now = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        entity.setInsDatetime(now);
//        entity.setUpdDatetime(now);
//
//        // 기본 사용자
//        if (entity.getInsUserId() == null) entity.setInsUserId("admin");
//        if (entity.getUpdUserId() == null) entity.setUpdUserId("admin");
    	
        return repository.save(param);
    }

    public WmtCstStrg select(String whCd, String lotNo, String cellNo) {
        return repository.findById(new WmtCstStrgId(whCd, lotNo, cellNo))
                         .orElse(null);
    }
}

