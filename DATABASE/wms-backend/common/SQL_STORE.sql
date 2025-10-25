-- 쿼리 관리 테이블 예시
CREATE TABLE SQL_STORE (
    SQL_ID VARCHAR(50) PRIMARY KEY,
    SQL_TEXT TEXT,
    DESCRIPTION VARCHAR(200)
);

-- 예시 데이터 삽입
INSERT INTO SQL_STORE (SQL_ID, SQL_TEXT, DESCRIPTION)
VALUES 
('selectInventoryMapToastList', 
 'SELECT ... FROM WMT_CST_STRG WHERE ...', 
 '재고 맵 토스트 리스트');
 
INSERT INTO SQL_STORE (SQL_ID, SQL_TEXT, DESCRIPTION)
VALUES 
('insertInventoryMapToast', 
 'INSERT INTO', 
 '재고 맵 토스트 입력');

select * from SQL_STORE;

update SQL_STORE
set SQL_TEXT = '<script>
SELECT WH_CD, LOT_NO, CELL_NO, INVN_QTY, AVLB_QTY, ALLOC_QTY, HLD_QTY, INS_USER_ID, INS_DATETIME, UPD_USER_ID, UPD_DATETIME 
FROM WMT_CST_STRG 
<where> <if test="searchCondition == 0"> WH_CD LIKE CONCAT(''%'' , #{searchKeyword}, ''%'') </if> <if test="searchCondition == 1"> LOT_NO LIKE CONCAT(''%'' , #{searchKeyword}, ''%'') </if> <if test="searchCondition == 2"> CELL_NO LIKE CONCAT(''%'' , #{searchKeyword}, ''%'') </if> </where> ORDER BY WH_CD, LOT_NO, CELL_NO DESC LIMIT #{recordCountPerPage} OFFSET #{firstIndex}
</script>'
where SQL_ID = 'selectInventoryMapToastList'
;

UPDATE SQL_STORE
SET SQL_TEXT = '/* selectInventoryListTotCnt */
   	
        SELECT COUNT(1) totcnt
        FROM	(
	        		SELECT 
				        	WH_CD				AS whCd		
							, LOT_NO            AS lotNo      
							, CELL_NO           AS cellNo     
							, INVN_QTY          AS invnQty    
							, AVLB_QTY          AS avlbQty    
							, ALLOC_QTY         AS allocQty   
							, HLD_QTY           AS hldQty     
							, INS_USER_ID       AS insUserId  
							, INS_DATETIME      AS insDatetime
							, UPD_USER_ID       AS updUserId  
							, UPD_DATETIME      AS updDatetime
				    FROM    WMT_CST_STRG
	    		) A
        WHERE 1=1
        <if test="searchCondition == 0">AND
	    	whCd LIKE CONCAT(''%'', #{searchKeyword}, ''%'')
	    </if>
	    <if test="searchCondition == 1">AND
	    	lotNo LIKE CONCAT(''%'', #{searchKeyword}, ''%'')
	    </if>
	    <if test="searchCondition == 2">AND
	    	cellNo LIKE CONCAT(''%'', #{searchKeyword}, ''%'')
	    </if>'
WHERE SQL_ID = 'selectInventoryMapToastListTotCnt';

update SQL_STORE
set SQL_TEXT = '<script>
INSERT INTO WMT_CST_STRG 
            (   
                WH_CD		
				, LOT_NO      
				, CELL_NO     
				, INVN_QTY    
				, AVLB_QTY    
				, ALLOC_QTY   
				, HLD_QTY     
				, INS_USER_ID 
				, INS_DATETIME
				, UPD_USER_ID 
				, UPD_DATETIME        
			)
          VALUES
          	(
                #{whCd}		
				, #{lotNo}      
				, #{cellNo}     
				, IFNULL (NULLIF(#{invnQty},''''), 0)    
				, IFNULL (NULLIF(#{avlbQty},''''), 0)
				, IFNULL (NULLIF(#{allocQty},''''), 0)   
				, IFNULL (NULLIF(#{hldQty}, ''''), 0)     
				, ''sklee''  
				, DATE_FORMAT(NOW(), ''%Y%m%d%H%i%s'')
				, ''sklee''  
				, DATE_FORMAT(NOW(), ''%Y%m%d%H%i%s'')
            )   
</script>'
where SQL_ID = 'insertInventoryMapToast'
;