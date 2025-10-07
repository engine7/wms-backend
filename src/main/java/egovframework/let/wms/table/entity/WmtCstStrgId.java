package egovframework.let.wms.table.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WmtCstStrgId implements Serializable {
    private String whCd;
    private String lotNo;
    private String cellNo;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof WmtCstStrgId)) return false;
        WmtCstStrgId that = (WmtCstStrgId) o;
        return Objects.equals(whCd, that.whCd)
            && Objects.equals(lotNo, that.lotNo)
            && Objects.equals(cellNo, that.cellNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whCd, lotNo, cellNo);
    }
}

