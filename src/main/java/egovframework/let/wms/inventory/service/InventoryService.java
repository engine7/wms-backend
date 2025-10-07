package egovframework.let.wms.inventory.service;

import java.util.List;

/**
 * 일반회원관리에 관한 인터페이스클래스를 정의한다.
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
 *   2009.04.10  조재영          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
public interface InventoryService {

	/* LIST (COUNT) */
	
	public List<InventoryVO> selectInventoryList(UserDefaultVO userSearchVO) throws Exception;
	
	public int selectInventoryListTotCnt(UserDefaultVO userSearchVO) throws Exception;
	
	/* CRUD */
	
	public int insertInventory(InventoryVO inventoryVO) throws Exception;
	
	public InventoryVO selectInventory(InventoryVO inventoryVO) throws Exception;
	
	public void updateInventory(InventoryVO inventoryVO) throws Exception;
	
	public void deleteInventory(InventoryVO inventoryVO) throws Exception;
	
	/**
	 * 입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param checkId 중복여부 확인대상 아이디
	 * @return 사용가능여부(아이디 사용회수 int)
	 * @throws Exception
	 */
	public int checkIdDplct(InventoryVO inventoryVO) throws Exception;
	
}