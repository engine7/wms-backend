package egovframework.let.wms.inventory.service.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;

import egovframework.let.wms.inventory.service.InventoryVO;
import egovframework.let.wms.inventory.service.UserDefaultVO;

import org.springframework.stereotype.Repository;

/**
 * 일반회원관리에 관한 데이터 접근 클래스를 정의한다.
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
 *	 2024.07.23	 김일국			스프링부트용으로 커스터마이징
 * </pre>
 */
@Repository("inventoryDAO")
public class InventoryDAO extends EgovAbstractMapper{

	/* LIST (COUNT) */
	
    @SuppressWarnings("unchecked")
	public List<InventoryVO> selectInventoryList(UserDefaultVO userSearchVO){
        return selectList("inventoryDAO.selectInventoryList", userSearchVO);
    }

    public int selectInventoryListTotCnt(UserDefaultVO userSearchVO) {
        return selectOne("inventoryDAO.selectInventoryListTotCnt", userSearchVO);
    }

    /* CRUD */
    
    public int insertInventory(InventoryVO inventoryVO){
        return insert("inventoryDAO.insertInventory", inventoryVO);
    }
    
    public InventoryVO selectInventory(InventoryVO inventoryVO){
        return (InventoryVO) selectOne("inventoryDAO.selectInventory", inventoryVO);
    }
    
    public void updateInventory(InventoryVO inventoryVO){
        update("inventoryDAO.updateInventory",inventoryVO);
    }
    
    public void deleteInventory(InventoryVO inventoryVO){
        delete("inventoryDAO.deleteInventory", inventoryVO);
    }
}