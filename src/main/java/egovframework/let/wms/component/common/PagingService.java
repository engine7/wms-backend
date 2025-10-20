package egovframework.let.wms.component.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.wms.component.inventory.service.InventoryService;

@Component
public class PagingService {

    /**
     * 페이징 공통 처리. (VO나 Map 등 원하는 paramType을 T로 줄 수 있음)
     */
    public <T> PageResult<Map<String, Object>> getPagedResult(
            Function<PageDefaultMap, List<Map<String, Object>>> listFetcher,
            Function<PageDefaultMap, Integer> countFetcher,
            PageDefaultMap pageMap // ← 수정
    ) {
        // 리스트 쿼리 결과 & 총 카운트 쿼리
        List<Map<String, Object>> resultList = listFetcher.apply(pageMap);
        int totalCount = countFetcher.apply(pageMap);

        return new PageResult<>(resultList, totalCount);
    }

    /**
     * pageMap(혹은 VO) 기반으로 PaginationInfo 생성
     */
    public ResultVO makePagedResultVO(PageResult<?> pageResult, PageDefaultMap pageMap, Object user) {
//    public ResultVO makePagedResultVO(PageResult<?> pageResult, PageDefaultMap pageMap, Object user, Object groupIdResult) {
        Map<String, Object> resultMap = new HashMap<>();

        /** pageing */
        PaginationInfo paginationInfo = new PaginationInfo();
        paginationInfo.setCurrentPageNo(pageMap.getPageIndex());
        paginationInfo.setRecordCountPerPage(pageMap.getPageSize());
        paginationInfo.setPageSize(10); // 혹은 pageMap.getOrDefault("pageSize", 10)

        // totalCount 전달
        paginationInfo.setTotalRecordCount(pageResult.getTotalCount());

        resultMap.put("user", user);
//        resultMap.put("groupId_result", groupIdResult);
        resultMap.put("resultList", pageResult.getResultList());
        resultMap.put("paginationInfo", paginationInfo);

        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
        resultVO.setResult(resultMap);
        return resultVO;
    }
}