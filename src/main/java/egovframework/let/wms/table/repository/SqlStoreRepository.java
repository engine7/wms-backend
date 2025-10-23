package egovframework.let.wms.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egovframework.let.wms.table.entity.SqlStore;

@Repository
public interface SqlStoreRepository extends JpaRepository<SqlStore, String> {

    // sqlId로 데이터 조회
    SqlStore findBySqlId(String sqlId);
}