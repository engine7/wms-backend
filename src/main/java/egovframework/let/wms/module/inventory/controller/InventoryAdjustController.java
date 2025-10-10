package egovframework.let.wms.module.inventory.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.let.wms.module.inventory.service.InventoryAdjustService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="InventoryAdjustController",description = "재고 조정")
public class InventoryAdjustController {

	/** inventoryService */
	@Resource(name = "inventoryAdjustService")
	private InventoryAdjustService inventoryAdjustService;
	
	/**
	 * 재고조정
	 * @param mberManageVO 재고등록정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "재고조정",
			description = "재고조정",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryAdjustController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping("/inventory/adjust")
	public ResultVO adjustInventory(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVO = new ResultVO();

		Map<String, Object> map = inventoryAdjustService.inventoryAdjust(params);
		
		//Exception 없이 진행시 등록 성공메시지
		resultMap.put("resultMsg", "success.common.insert");
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		
		return resultVO;
	}
	
}
