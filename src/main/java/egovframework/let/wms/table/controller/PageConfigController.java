package egovframework.let.wms.table.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.let.wms.table.entity.PageConfig;
import egovframework.let.wms.table.entity.WmtCstStrg;
import egovframework.let.wms.table.service.PageConfigService;
import egovframework.let.wms.table.service.WmtCstStrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/pageConfig")
public class PageConfigController {
	
	private final PageConfigService pageConfigService;

    public PageConfigController(PageConfigService pageConfigService) {
        this.pageConfigService = pageConfigService;
    }
    
    @Operation(summary = "페이지 리스트 조회", description = "페이지 리스트 조회",
            security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/selectList")
    public List<PageConfig> selectInventory() {
        return pageConfigService.selectList();
    }

}
