package egovframework.let.wms.module.inventory.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.let.wms.table.entity.WmtCstStrg;
import egovframework.let.wms.table.service.WmtCstStrgService;

@Service("inventoryAdjustService")
public class InventoryAdjustServiceImpl implements InventoryAdjustService {
	
	@Resource(name = "wmtCstStrgService")
    private WmtCstStrgService wmtCstStrgService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public Map<String, Object> inventoryAdjust(Map<String, Object> params) throws Exception {
		
		String whCd = String.valueOf(params.get("whCd"));
		String lotNo = String.valueOf(params.get("lotNo")); 
		String cellNo = String.valueOf(params.get("cellNo"));
		
		WmtCstStrg wmtCstStrg = wmtCstStrgService.select(whCd, lotNo, cellNo);
		
		// Biz Logic Start >>>
		// 1. 기존수량에서 가감 +- 인수 (재고수량유형코드)
		// 2. 재고수량 = 가용수량 + 할당수량 + 보류수량
		
		Map<String, Object> resultMap = objectMapper.convertValue(wmtCstStrg, Map.class);
		
		// 파라미터
		String invnQtyTypeCd = String.valueOf(params.get("invnQtyTypeCd"));
		int adjustQty = Integer.parseInt(String.valueOf(params.get("adjustQty")));
		
		// 기존재고
		int avlbQty = wmtCstStrg.getAvlbQty().intValue();
		int allocQty = wmtCstStrg.getAllocQty().intValue();
		int hldQty = wmtCstStrg.getHldQty().intValue();
		
		if ("HLD".equals(invnQtyTypeCd)) {
			hldQty = hldQty + adjustQty;
		} else if ("ALLOC".equals(invnQtyTypeCd)) {
			allocQty = allocQty + adjustQty;
		} else {	// AVLB
			avlbQty = avlbQty  + adjustQty;
		}
		
		int invnQty = avlbQty + allocQty + hldQty;
		
		resultMap.put("hldQty", hldQty);
		resultMap.put("allocQty", allocQty);
		resultMap.put("avlbQty", avlbQty);
		resultMap.put("invnQty", invnQty);
		
		// Biz Logic End <<<
		
		wmtCstStrgService.insert(resultMap);
		
		return resultMap;
	}
	
}
