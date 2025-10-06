package egovframework.let.wms.inventory.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 일반회원VO클래스로서 일반회원관리 비지니스로직 처리용 항목을 구성한다.
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
 *	 2024.07.24	 김일국			스프링부트 롬복사용으로 변경
 * </pre>
 */
@Schema(description = "재고 VO")
@Getter
@Setter
public class InventoryVO extends UserDefaultVO implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Schema(description = "창고코드")
    private String whCd = "";
    
	@Schema(description = "LOT번호")
	private String lotNo="";

	@Schema(description = "셀번호")
	private String cellNo="";

	@Schema(description = "재고수량")
	private String invnQty="";

	@Schema(description = "가용수량")
	private String avlbQty="";

	@Schema(description = "할당수량")
	private String allocQty="";

	@Schema(description = "보류수량")
	private String hldQty="";
	
	@Schema(description = "입력사용자ID")
	private String insUserId="";

	@Schema(description = "입력일시")
	private String insDatetime="";

	@Schema(description = "수정사용자ID")
	private String updUserId="";

	@Schema(description = "수정일시")
	private String updDatetime;
	
	/**
	 * toString 메소드를 대치한다.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}