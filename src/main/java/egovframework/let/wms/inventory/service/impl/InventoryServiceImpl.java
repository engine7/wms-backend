package egovframework.let.wms.inventory.service.impl;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.let.wms.inventory.service.InventoryService;
import egovframework.let.wms.inventory.service.InventoryVO;
import egovframework.let.wms.inventory.service.UserDefaultVO;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * 일반회원관리에 관한비지니스클래스를 정의한다.
 * @author 공통서비스 개발팀 조재영
 * @since 2009.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.04.10  JJY            최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Service("inventoryService")
public class InventoryServiceImpl extends EgovAbstractServiceImpl implements InventoryService {

	@Resource(name="inventoryDAO")
	private InventoryDAO inventoryDAO;

	/* LIST (COUNT) */
	
	@Override
	public List<InventoryVO> selectInventoryList(UserDefaultVO userSearchVO) {
		return inventoryDAO.selectInventoryList(userSearchVO);
	}
	
	@Override
	public int selectInventoryListTotCnt(UserDefaultVO userSearchVO) {
    	return inventoryDAO.selectInventoryListTotCnt(userSearchVO);
    }
	
	/* CRUD */
	
	@Override
	public int insertInventory(InventoryVO inventoryVO) throws Exception  {

		int result = inventoryDAO.insertInventory(inventoryVO);
		return result;
	}
	
	@Override
	public InventoryVO selectInventory(InventoryVO inventoryVO) {
		return inventoryDAO.selectInventory(inventoryVO);
	}
	
	@Override
	public void updateInventory(InventoryVO inventoryVO) throws Exception {
		inventoryDAO.updateInventory(inventoryVO);
	}

	@Override
	public void deleteInventory(InventoryVO inventoryVO)  {
		inventoryDAO.deleteInventory(inventoryVO);
	}
}