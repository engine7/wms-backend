package egovframework.let.wms.table.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import egovframework.let.wms.table.entity.PageConfig;
import egovframework.let.wms.table.entity.WmtCstStrg;
import egovframework.let.wms.table.repository.PageConfigRepository;
import egovframework.let.wms.table.repository.WmtCstStrgRepository;

@Service
public class PageConfigService {

	private final PageConfigRepository repository;
	
	public PageConfigService(PageConfigRepository repository) {
        this.repository = repository;
    }
	
	public List<PageConfig> selectList() {
		return repository.findAllActiveConfigs();
	}
	
}
