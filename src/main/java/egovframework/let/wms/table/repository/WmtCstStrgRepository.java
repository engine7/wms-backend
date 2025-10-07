package egovframework.let.wms.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.wms.table.entity.WmtCstStrg;
import egovframework.let.wms.table.entity.WmtCstStrgId;

@Repository
public interface WmtCstStrgRepository extends JpaRepository<WmtCstStrg, WmtCstStrgId> {
}

