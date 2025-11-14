package egovframework.let.wms.table.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.RestController;

import egovframework.let.wms.table.service.CommonService;

@RestController
@RequestMapping("/api/common")
@SecurityRequirement(name = "Authorization")
public class CommonController {

    @Resource(name = "commonService")
    private CommonService commonService;

    @GetMapping("/{tableName}/columns")
    public ResponseEntity<List<Map<String, Object>>> getColumnInfo(@PathVariable String tableName) throws Exception {
        List<Map<String, Object>> columnInfo = commonService.getColumnInfo(tableName);
        return ResponseEntity.ok(columnInfo);
    }

//    @Operation(summary = "테이블 리스트 조회", description = "테이블 조회",
//            security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping("/{tableName}")
    public ResponseEntity<List<Map<String, Object>>> getList(@PathVariable String tableName, @RequestParam Map<String, Object> params) throws Exception {
        List<Map<String, Object>> list = commonService.selectList(tableName, params);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{tableName}/detail")
    public ResponseEntity<Map<String, Object>> getOne(@PathVariable String tableName, @RequestBody Map<String, Object> pk) throws Exception {
        Map<String, Object> data = commonService.selectOne(tableName, pk);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/{tableName}")
    public ResponseEntity<Void> create(@PathVariable String tableName, @RequestBody Map<String, Object> data) throws Exception {
        commonService.insert(tableName, data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{tableName}")
    public ResponseEntity<Void> update(@PathVariable String tableName, @RequestBody Map<String, Object> payload) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, Object> pk = (Map<String, Object>) payload.get("pk");
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        commonService.update(tableName, pk, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tableName}")
    public ResponseEntity<Void> delete(@PathVariable String tableName, @RequestBody Map<String, Object> pk) throws Exception {
        commonService.delete(tableName, pk);
        return ResponseEntity.ok().build();
    }
}
