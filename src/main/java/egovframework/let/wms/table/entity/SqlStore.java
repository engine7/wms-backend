package egovframework.let.wms.table.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SQL_STORE")
public class SqlStore {

    @Id
    private String sqlId;

    private String sqlText;

    private String description;

    // Getters, Setters
    public String getSqlText() {
		return sqlText;
	}
    
}