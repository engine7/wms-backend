package egovframework.let.wms.table.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public WmtCstStrg insert(WmtCstStrg entity) {
        return repository.save(entity);
    }

    public WmtCstStrg select(String whCd, String lotNo, String cellNo) {
        return repository.findById(new WmtCstStrgId(whCd, lotNo, cellNo))
                         .orElse(null);
    }
}

