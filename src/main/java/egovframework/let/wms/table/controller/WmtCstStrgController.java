package egovframework.let.wms.table.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.let.wms.table.entity.WmtCstStrg;
import egovframework.let.wms.table.service.WmtCstStrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/wmtCstStrg")
public class WmtCstStrgController {

    private final WmtCstStrgService wmtCstStrgService;

    public WmtCstStrgController(WmtCstStrgService wmtCstStrgService) {
        this.wmtCstStrgService = wmtCstStrgService;
    }

    @Operation(summary = "재고 조회", description = "WH_CD, LOT_NO, CELL_NO로 재고 조회")
    @GetMapping("/select/{whCd}/{lotNo}/{cellNo}")
    public WmtCstStrg selectInventory(
            @PathVariable String whCd,
            @PathVariable String lotNo,
            @PathVariable String cellNo
    ) {
        return wmtCstStrgService.select(whCd, lotNo, cellNo);
    }
    
    @Operation(
            summary = "재고 등록",
            description = "WMT_CST_STRG 테이블에 재고를 등록합니다.",
            security = {@SecurityRequirement(name = "Authorization")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
            @ApiResponse(responseCode = "400", description = "입력값 오류")
    })
    @PostMapping("/insert")
    public WmtCstStrg insertInventory(@RequestBody Map<String, Object> param) {

//        WmtCstStrg entity = new WmtCstStrg();
//        entity.setWhCd((String) param.get("whCd"));
//        entity.setLotNo((String) param.get("lotNo"));
//        entity.setCellNo((String) param.get("cellNo"));
//        entity.setInvnQty(new BigDecimal(param.getOrDefault("invnQty", "0").toString()));
//        entity.setAvlbQty(new BigDecimal(param.getOrDefault("avlbQty", "0").toString()));
//        entity.setAllocQty(new BigDecimal(param.getOrDefault("allocQty", "0").toString()));
//        entity.setHldQty(new BigDecimal(param.getOrDefault("hldQty", "0").toString()));
//        entity.setInsUserId("admin"); // TODO: 실제 로그인 사용자
//        entity.setUpdUserId("admin"); // TODO: 실제 로그인 사용자
//        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        entity.setInsDatetime(now);
//        entity.setUpdDatetime(now);

        return wmtCstStrgService.insert(param);
    }
}
