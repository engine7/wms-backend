package egovframework.let.wms.component.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DynamicSQLMapper {

    List<Map<String, Object>> executeDynamicSQL(@Param("sql") String sql);
}

