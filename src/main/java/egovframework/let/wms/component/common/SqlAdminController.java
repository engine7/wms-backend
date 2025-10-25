package egovframework.let.wms.component.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sql")
public class SqlAdminController {

    @Autowired
    private SqlCache sqlCache;

    @PostMapping("/reload")
    public String reloadSqlCache() {
        sqlCache.reload();
        return "SQL cache reloaded successfully";
    }
}
