package egovframework.let.wms.component.inventory.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.wms.component.inventory.service.InventoryService;
import egovframework.let.wms.component.inventory.service.InventoryVO;
import egovframework.let.uss.umt.service.UserDefaultVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 재고관련 요청을  비지니스 클래스로 전달하고 처리된결과를  해당   웹 화면으로 전달하는  Controller를 정의한다
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
 *   2024.07.22  김일국          Boot 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@RestController
@Tag(name="InventoryApiController",description = "재고 관리")
public class InventoryApiController {

	@Autowired
    private EgovJwtTokenUtil jwtTokenUtil;
    public static final String HEADER_STRING = "Authorization";
    
	/** inventoryService */
	@Resource(name = "inventoryService")
	private InventoryService inventoryService;

	/** cmmUseService */
	@Resource(name = "EgovCmmUseService")
	private EgovCmmUseService cmmUseService;

	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** DefaultBeanValidator beanValidator */
	@Autowired
	private DefaultBeanValidator beanValidator;

	/* LIST (COUNT) */
	
	/**
	 * 관리자단에서 재고목록을 조회한다. (pageing)
	 * @param request
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "관리자단에서 재고 목록조회화면",
			description = "관리자단에서 재고에 대한 목록을 조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping(value = "/inventory")
	public ResultVO selectInventoryList(
			@Parameter(
					in = ParameterIn.QUERY,
					schema = @Schema(type = "object",
							additionalProperties = Schema.AdditionalPropertiesValue.TRUE, 
							ref = "#/components/schemas/searchMap"),
					style = ParameterStyle.FORM,
					explode = Explode.TRUE
			) @RequestParam Map<String, Object> commandMap, 
			@Parameter(hidden = true) @AuthenticationPrincipal LoginVO user)
		throws Exception {
		ResultVO resultVO = new ResultVO();
		InventoryVO userSearchVO = new InventoryVO();
		userSearchVO.setSearchCondition((String)commandMap.get("searchCnd"));
		userSearchVO.setSearchKeyword((String)commandMap.get("searchWrd"));
		
		/** EgovPropertyService */
		userSearchVO.setPageUnit(propertiesService.getInt("Globals.pageUnit"));
		userSearchVO.setPageSize(propertiesService.getInt("Globals.pageSize"));
		
		/** pageing */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userSearchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(userSearchVO.getPageUnit());
		paginationInfo.setPageSize(userSearchVO.getPageSize());

		userSearchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		userSearchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		userSearchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		List<InventoryVO> resultList = inventoryService.selectInventoryList(userSearchVO);
		
		int totCnt = inventoryService.selectInventoryListTotCnt(userSearchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		
		//그룹정보를 조회 - GROUP_ID정보(스프링부트에서는 실제로 이 값만 사용한다.)
		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		vo.setTableNm("LETTNORGNZTINFO");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("paginationInfo", paginationInfo);
		resultMap.put("user", user);
		resultMap.put("groupId_result", cmmUseService.selectGroupIdDetail(vo));
		resultMap.put("resultList", resultList);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		resultVO.setResult(resultMap);

		return resultVO;
	}

	/* CUD (R) */
	
	/**
	 * 관리자단에서 재고등록화면으로 이동한다.
	 * @param userSearchVO 검색조건정보
	 * @param mberManageVO 재고초기화정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Hidden
	@Operation(
			summary = "관리자단에서 재고 등록화면",
			description = "관리자단에서 재고등록화면에 필요한 값 생성",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/inventory/insert")
	public ResultVO insertInventoryView(UserDefaultVO userSearchVO, InventoryVO inventoryVO)
			throws Exception {

		ComDefaultCodeVO vo = new ComDefaultCodeVO();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//그룹정보를 조회 - GROUP_ID정보(스프링부트에서는 실제로 이 값만 사용한다.)
		vo.setTableNm("LETTNORGNZTINFO");
		resultMap.put("groupId_result", cmmUseService.selectGroupIdDetail(vo));
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		resultVO.setResult(resultMap);
		
		return resultVO;
	}

	/**
	 * 관리자단에서 재고등록처리
	 * @param mberManageVO 재고등록정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "관리자단에서 재고 등록처리",
			description = "관리자단에서 재고 등록처리",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping("/inventory/insert")
	public ResultVO insertInventory(InventoryVO inventoryVO, BindingResult bindingResult) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVO = new ResultVO();
		beanValidator.validate(inventoryVO, bindingResult);
		if (bindingResult.hasErrors()) {
			ComDefaultCodeVO vo = new ComDefaultCodeVO();

			resultMap.put("resultMsg", "fail.common.insert");
			resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.SAVE_ERROR.getMessage());
		} else {
			inventoryService.insertInventory(inventoryVO);
			//Exception 없이 진행시 등록 성공메시지
			resultMap.put("resultMsg", "success.common.insert");
			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		}
		resultVO.setResult(resultMap);
		
		return resultVO;
	}
	
	/**
	 * 관리자단에서 재고등록처리
	 * @param mberManageVO 재고등록정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "관리자단에서 재고 등록처리 (맵)",
			description = "관리자단에서 재고 등록처리 (맵)",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryMapApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PostMapping("/inventoryMap/insert")
	public ResultVO insertInventoryMap(@RequestBody Map<String, Object> params, BindingResult bindingResult) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVO = new ResultVO();
//		beanValidator.validate(inventoryVO, bindingResult);
//		if (bindingResult.hasErrors()) {
//			ComDefaultCodeVO vo = new ComDefaultCodeVO();
//
//			resultMap.put("resultMsg", "fail.common.insert");
//			resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
//			resultVO.setResultMessage(ResponseCode.SAVE_ERROR.getMessage());
//		} else {
			inventoryService.insertInventoryMap(params);	/* JPA */
			//Exception 없이 진행시 등록 성공메시지
			resultMap.put("resultMsg", "success.common.insert");
			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
//		}
//		resultVO.setResult(resultMap);
		
		return resultVO;
	}

	/**
	 * 관리자단에서 재고정보 수정을 위해 재고정보를 상세조회한다.
	 * @param uniqId 상세조회대상 재고아이디
	 * @param userSearchVO 검색조건
	 * @return resultVO
	 * @throws Exception
	 */
//	@Hidden
	@Operation(
			summary = "관리자단에서 재고정보 수정용 상세조회화면",
			description = "관리자단에서 재고정보 수정을 위해 재고정보를 상세조회",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/inventory/update/{whCd}/{lotNo}/{cellNo}")
	public ResultVO updateInventoryView(@PathVariable("whCd") String whCd
									, @PathVariable("lotNo") String lotNo
									, @PathVariable("cellNo") String cellNo
									, UserDefaultVO userSearchVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVO = new ResultVO();
		
		InventoryVO inventoryVO = new InventoryVO();
		
		inventoryVO.setWhCd(whCd);
		inventoryVO.setLotNo(lotNo);
		inventoryVO.setCellNo(cellNo);
		
		resultMap.put("inventoryVO", inventoryService.selectInventory(inventoryVO));
		resultMap.put("userSearchVO", userSearchVO);

		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		resultVO.setResult(resultMap);
		
		return resultVO;
	}

	/**
	 * 관리자단에서 재고수정 처리
	 * @param mberManageVO 재고수정정보
	 * @param bindingResult 입력값검증용 bindingResult
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "관리자단에서 재고 수정처리",
			description = "관리자단에서 재고 수정처리",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "등록 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@PutMapping("/inventory/update")
	public ResultVO updateInventory(@RequestBody InventoryVO inventoryVO, BindingResult bindingResult) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVO = new ResultVO();

		beanValidator.validate(inventoryVO, bindingResult);
		if (bindingResult.hasErrors()) {
			
			resultMap.put("resultMsg", "fail.common.insert");
			resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.SAVE_ERROR.getMessage());
		} else {
			inventoryService.updateInventory(inventoryVO);
			//Exception 없이 진행시 수정성공메시지
			resultMap.put("resultMsg", "success.common.update");
			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		}
		resultVO.setResult(resultMap);
		
		return resultVO;
	}

	/**
	 * 관리자단에서 재고정보삭제.
	 * @param checkedIdForDel 삭제대상 아이디 정보
	 * @param userSearchVO 검색조건정보
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "관리자단에서 재고 삭제처리",
			description = "관리자단에서 재고 삭제처리",
			security = {@SecurityRequirement(name = "Authorization")},
			tags = {"InventoryApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "삭제 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님"),
			@ApiResponse(responseCode = "900", description = "입력값 무결성 오류")
	})
	@DeleteMapping("/inventory/delete/{whCd}/{lotNo}/{cellNo}")
	public ResultVO deleteInventory(@PathVariable("whCd") String whCd
									, @PathVariable("lotNo") String lotNo
									, @PathVariable("cellNo") String cellNo
									, UserDefaultVO userSearchVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVO = new ResultVO();
		
		InventoryVO inventoryVO = new InventoryVO();
		
		inventoryVO.setWhCd(whCd);
		inventoryVO.setLotNo(lotNo);
		inventoryVO.setCellNo(cellNo);
		
		inventoryService.deleteInventory(inventoryVO);
		
		//Exception 없이 진행시 삭제성공메시지
		resultMap.put("resultMsg", "success.common.delete");
		resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
		resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
		resultVO.setResult(resultMap);
	
		return resultVO;
	}
	
	/**
	 * 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param commandMap 파라메터전달용 commandMap
	 * @return resultVO
	 * @throws Exception
	 */
	@Operation(
			summary = "사용자아이디의 중복여부 체크처리",
			description = "사용자아이디의 중복여부 체크처리",
			tags = {"InventoryApiController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "403", description = "인가된 사용자가 아님")
	})
	@GetMapping("/etc/inventory_checkid/{whCd}/{lotNo}/{cellNo}")
	public ResultVO checkIdDplct(@PathVariable("whCd") String whCd
								, @PathVariable("lotNo") String lotNo
								, @PathVariable("cellNo") String cellNo
								) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultVO resultVO = new ResultVO();
		
		whCd = new String(whCd.getBytes("ISO-8859-1"), "UTF-8");
		lotNo = new String(lotNo.getBytes("ISO-8859-1"), "UTF-8");
		cellNo = new String(cellNo.getBytes("ISO-8859-1"), "UTF-8");
		
		String checkId = whCd + ", " + lotNo + ", " + cellNo;

		if ( (whCd == null || whCd.equals(""))
				&& (lotNo == null || lotNo.equals(""))
				&& (cellNo == null || cellNo.equals(""))
			) {
			resultVO.setResultCode(ResponseCode.INPUT_CHECK_ERROR.getCode());
			resultVO.setResultMessage(ResponseCode.INPUT_CHECK_ERROR.getMessage());
		}else {
			
			InventoryVO inventoryVO = new InventoryVO();
			
			inventoryVO.setWhCd(whCd);
			inventoryVO.setLotNo(lotNo);
			inventoryVO.setCellNo(cellNo);
			
			int inventoryCnt = inventoryService.checkIdDplct(inventoryVO);
			resultMap.put("inventoryCnt", inventoryCnt);
			resultMap.put("checkId", checkId);
		
			resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
			resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
			resultVO.setResult(resultMap);
		}
		return resultVO;
	}
}