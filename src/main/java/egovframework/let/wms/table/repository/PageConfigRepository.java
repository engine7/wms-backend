package egovframework.let.wms.table.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import egovframework.let.wms.table.entity.PageConfig;

import java.util.List;

@Repository
public interface PageConfigRepository extends JpaRepository<PageConfig, String> {

    /**
     * use_yn = 'Y'인 설정 리스트 조회
     */
    @Query("SELECT p FROM PageConfig p WHERE p.useYn = 'Y'")
    List<PageConfig> findAllActiveConfigs();
}