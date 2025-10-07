package egovframework.let.wms.component.inventory.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.let.wms.component.inventory.service.InventoryService;
import egovframework.let.wms.component.inventory.service.InventoryVO;
import egovframework.let.wms.component.inventory.service.UserDefaultVO;
import egovframework.let.wms.table.entity.WmtCstStrg;
import egovframework.let.wms.table.service.WmtCstStrgService;

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
	
	@Resource(name = "wmtCstStrgService")
    private WmtCstStrgService wmtCstStrgService;

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
	public int insertInventoryMap(Map<String, Object> params) throws Exception  {	/* (Map) */

//		int result = inventoryDAO.insertInventoryMap(params);
		
		int result = 1;
		WmtCstStrg wmtCstStrg = wmtCstStrgService.insert(params);
		
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
	
	/**
	 * 입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param checkId 중복여부 확인대상 아이디
	 * @return 사용가능여부(아이디 사용회수 int)
	 * @throws Exception
	 */
	@Override
	public int checkIdDplct(InventoryVO inventoryVO) {
		return inventoryDAO.checkIdDplct(inventoryVO);
	}
}