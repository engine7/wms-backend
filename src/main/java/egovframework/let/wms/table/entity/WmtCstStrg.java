package egovframework.let.wms.table.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WMT_CST_STRG")
@IdClass(WmtCstStrgId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WmtCstStrg {

    @Id
    @Column(name = "WH_CD", length = 20, nullable = false)
    private String whCd;

    @Id
    @Column(name = "LOT_NO", length = 20, nullable = false)
    private String lotNo;

    @Id
    @Column(name = "CELL_NO", length = 20, nullable = false)
    private String cellNo;

    @Column(name = "INVN_QTY", nullable = false)
    private BigDecimal invnQty = BigDecimal.ZERO;

    @Column(name = "AVLB_QTY", nullable = false)
    private BigDecimal avlbQty = BigDecimal.ZERO;

    @Column(name = "ALLOC_QTY", nullable = false)
    private BigDecimal allocQty = BigDecimal.ZERO;

    @Column(name = "HLD_QTY", nullable = false)
    private BigDecimal hldQty = BigDecimal.ZERO;

    @Column(name = "INS_USER_ID", length = 20, nullable = false)
    private String insUserId;

    @Column(name = "INS_DATETIME", length = 14, nullable = false)
    private String insDatetime;

    @Column(name = "UPD_USER_ID", length = 20, nullable = false)
    private String updUserId;

    @Column(name = "UPD_DATETIME", length = 14, nullable = false)
    private String updDatetime;

    // getter, setter 생략
}
