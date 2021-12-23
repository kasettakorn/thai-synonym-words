package com.example.error_report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.error_report.models.OverviewBUR;
import com.example.error_report.models.OverviewCaveat;
import com.example.error_report.models.OverviewDT;
import com.example.error_report.models.OverviewM9;
import com.example.error_report.models.OverviewUnitCondo;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
public class OverviewCondoroom {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@GetMapping("/overview/unit_condo/{landoffice}")
	public List<OverviewUnitCondo> getAllUnitCondo(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN SUBSTR(REG_NO,-4,4) < 2460 OR SUBSTR(REG_NO,-4,4) > TO_CHAR(SYSDATE, 'YYYY') OR LENGTH(SUBSTR(REG_NO,-4,4)) <> 4 THEN 1 END) AS INVALID_REG_NO,\r\n"
				+ "COUNT(CASE WHEN BLD_NO = '-' OR BLD_NO IS NULL THEN 1 END) AS NO_BLD_NO\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT AR1.AMPHUR_DESC, AR1.TUMBON_DESC, AR1.AMPHUR_CODE, AR1.TUMBON_CODE, CNDP.PARCEL_ID, HD.PARCEL_ID,\r\n"
				+ "                    SUBSTR(UCND.REG_NO,0,instr(UCND.REG_NO,'/')-1) REG, substr(UCND.REG_NO,instr(UCND.REG_NO,'/')+1) YEAR ,\r\n"
				+ "                    SUBSTR(UCND.UNIT_NO,0,instr(UCND.UNIT_NO,'/')-1) UNIT, substr(UCND.UNIT_NO,instr(UCND.UNIT_NO,'/')+1) AS NO,\r\n"
				+ "                    UCND.LANDOFFICE_SEQ, UCND.REG_NO, CNDR.NAME_CONDO, UCND.UNIT_NO, UCND.BLD_NO,to_number('1') as Key\r\n"
				+ "                  FROM DATAM.UNIT_CONDO UCND\r\n"
				+ "                  LEFT JOIN DATAM.CONDO_REG CNDR\r\n"
				+ "                      ON CNDR.LANDOFFICE_SEQ = UCND.LANDOFFICE_SEQ\r\n"
				+ "                      AND CNDR.REG_NO = UCND.REG_NO\r\n"
				+ "                  LEFT JOIN  DATAM.CONDO_PARCEL CNDP\r\n"
				+ "                      ON CNDR.LANDOFFICE_SEQ = CNDP.LANDOFFICE_SEQ\r\n"
				+ "                      AND CNDR.REG_NO = CNDP.REG_NO\r\n"
				+ "                  LEFT JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                    ON CNDP.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "                    AND CNDP.PARCEL_ID = HD.PARCEL_ID\r\n"
				+ "                    --AND CNDP.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "                  LEFT JOIN DATAM.AREA AR1\r\n"
				+ "                    ON CNDP.LANDOFFICE_SEQ = AR1.LANDOFFICE_SEQ\r\n"
				+ "                    AND HD.AMPHUR_CODE = AR1.AMPHUR_CODE\r\n"
				+ "                    AND HD.TUMBON_CODE = AR1.TUMBON_CODE\r\n"
				+ "                  WHERE UCND.LANDOFFICE_SEQ = " + landoffice + " AND (UCND.REG_NO NOT LIKE '%/%'\r\n"
				+ "                    OR TO_NUMBER(REGEXP_SUBSTR(UCND.REG_NO, '[^/]+', 1, 2)) > TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY'))+543\r\n"
				+ "                    OR TO_NUMBER(REGEXP_SUBSTR(UCND.REG_NO, '[^/]+', 1, 2)) < 2400\r\n"
				+ "                    OR UCND.BLD_NO LIKE '-'))";
	
		List<OverviewUnitCondo> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewUnitCondo.class));
		return list;
	
	}

	@GetMapping("/overview/owner_condo/{landoffice}")
	public List<OverviewDT> getOwnerCondo(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN RECV_CODE IS NULL OR RECV_DESC IS NULL THEN 1 END) AS no_recv_code,\r\n"
				+ "COUNT(CASE WHEN REG_CODE IS NULL OR REG_DESC IS NULL THEN 1 END) AS no_reg_code,\r\n"
				+ "COUNT(CASE WHEN OWN_PROV IS NULL THEN 1 END) AS no_own_prov,\r\n"
				+ "COUNT(CASE WHEN OWN_AMPHUR IS NULL THEN 1 END) AS no_own_amphur,\r\n"
				+ "COUNT(CASE WHEN OWN_TUMB IS NULL THEN 1 END) AS no_own_tumb,\r\n"
				+ "COUNT(CASE WHEN TITLE IS NULL THEN 1 END) AS no_title,\r\n"
				+ "COUNT(CASE WHEN FAT_TITLE IS NULL THEN 1 END) AS no_fat_title,\r\n"
				+ "COUNT(CASE WHEN MOT_TITLE IS NULL THEN 1 END) AS no_mot_title,\r\n"
				+ "COUNT(CASE WHEN AGE < 1 OR AGE > 999 THEN 1 END) AS wrong_age\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT AR1.AMPHUR_DESC, AR1.TUMBON_DESC, AR1.AMPHUR_CODE, AR1.TUMBON_CODE, OCND.AGE,\r\n"
				+ "                    SUBSTR(OCND.REG_NO,0,instr(OCND.REG_NO,'/')-1) REG, substr(OCND.REG_NO,instr(OCND.REG_NO,'/')+1) YEAR ,\r\n"
				+ "                    SUBSTR(OCND.UNIT_NO,0,instr(OCND.UNIT_NO,'/')-1) UNIT, substr(OCND.UNIT_NO,instr(OCND.UNIT_NO,'/')+1) AS NO,\r\n"
				+ "                    OCND.LANDOFFICE_SEQ, OCND.REG_NO, OCND.UNIT_NO, OCND.CHANGWAT, OCND.LINE_NO, OCND.TITLE, OCND.FNAME, OCND.LNAME,\r\n"
				+ "                    OCND.OWN_PROV, OCND.OWN_AMPHUR, OCND.OWN_TUMB, OCND.RECV_CODE, OCND.RECV_DESC, OCND.REG_CODE, OCND.REG_DESC,\r\n"
				+ "                    OCND.FAT_TITLE, OCND.MOT_TITLE,CNDR.NAME_CONDO, to_number('2') as Key\r\n"
				+ "                  FROM DATAM.OWNER_CONDO OCND\r\n"
				+ "                  LEFT JOIN DATAM.CONDO_REG CNDR\r\n"
				+ "                      ON CNDR.LANDOFFICE_SEQ = OCND.LANDOFFICE_SEQ\r\n"
				+ "                      AND CNDR.REG_NO = OCND.REG_NO\r\n"
				+ "                  LEFT JOIN  DATAM.CONDO_PARCEL CNDP\r\n"
				+ "                      ON CNDR.LANDOFFICE_SEQ = CNDP.LANDOFFICE_SEQ\r\n"
				+ "                      AND CNDR.REG_NO = CNDP.REG_NO\r\n"
				+ "                  LEFT JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                      ON CNDP.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "                      AND CNDP.PARCEL_ID = HD.PARCEL_ID\r\n"
				+ "                      --AND CNDP.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "                  LEFT JOIN DATAM.AREA AR1\r\n"
				+ "                      ON HD.LANDOFFICE_SEQ = AR1.LANDOFFICE_SEQ\r\n"
				+ "                      AND HD.AMPHUR_CODE = AR1.AMPHUR_CODE\r\n"
				+ "                      AND HD.TUMBON_CODE = AR1.TUMBON_CODE\r\n"
				+ "                  WHERE OCND.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "                       AND (OCND.RECV_CODE IS NULL\r\n"
				+ "                        OR    OCND.RECV_DESC IS NULL\r\n"
				+ "                        OR    OCND.REG_CODE IS NULL\r\n"
				+ "                        OR    OCND.REG_DESC IS NULL\r\n"
				+ "                        OR    OCND.OWN_PROV IS NULL\r\n"
				+ "                        OR    OCND.OWN_AMPHUR IS NULL\r\n"
				+ "                        OR    OCND.OWN_TUMB IS NULL\r\n"
				+ "                        OR    OCND.TITLE IS NULL\r\n"
				+ "                        OR    OCND.FAT_TITLE IS NULL\r\n"
				+ "                        OR    OCND.MOT_TITLE IS NULL\r\n"
				+ "                        OR    OCND.AGE NOT BETWEEN 1 AND 999))";
	
		List<OverviewDT> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewDT.class));
		return list;
	
	}
	
	@GetMapping("/overview/condoroom_caveat/{landoffice}")
	public List<OverviewCaveat> getCaveatCondoroom(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN CAVEAT_NAME IS NULL THEN 1 END) AS NO_CAVEAT_NAME,\r\n"
				+ "COUNT(CASE WHEN LENGTH(CAVEAT_NAME) > 200 THEN 1 END) AS OVERFLOW_CAVAET_NAME,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_DATE IS NULL THEN 1 END) AS NO_CAVEAT_DATE,\r\n"
				+ "--COUNT(CASE WHEN CAVEAT_DATE IS NOT NULL AND TO_CHAR(SYSDATE, 'YYYY') = 2021 THEN 1 END) AS INVALID_YEAR\r\n"
				+ "COUNT(CASE WHEN EN_DATE IS NULL THEN 1 END) AS NO_EN_DATE,\r\n"
				+ "COUNT(CASE WHEN CAN_DATE IS NULL THEN 1 END) AS NO_CAN_DATE,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_FLG IS NULL THEN 1 END) AS NO_CAVEAT_FLG,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_FLG <> '1' AND CAVEAT_FLG <> 'D' THEN 1 END) AS INVALID_CAVEAT_FLG\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT AR1.AMPHUR_DESC, AR1.TUMBON_DESC,\r\n"
				+ "    SUBSTR(CV.REG_NO,0,instr(CV.REG_NO,'/')-1) REG, substr(CV.REG_NO,instr(CV.REG_NO,'/')+1) YEAR ,\r\n"
				+ "    SUBSTR(CV.UNIT_NO,0,instr(CV.UNIT_NO,'/')-1) UNIT, substr(CV.UNIT_NO,instr(CV.UNIT_NO,'/')+1) AS NO,\r\n"
				+ "    CV.* ,CNDR.NAME_CONDO ,to_number('3') as Key\r\n"
				+ "FROM DATAM.CONDO_CAVEAT CV\r\n"
				+ "LEFT JOIN DATAM.CONDO_REG CNDR\r\n"
				+ "    ON CNDR.LANDOFFICE_SEQ = CV.LANDOFFICE_SEQ\r\n"
				+ "    AND CNDR.REG_NO = CV.REG_NO\r\n"
				+ "LEFT JOIN  DATAM.CONDO_PARCEL CNDP\r\n"
				+ "    ON CNDR.LANDOFFICE_SEQ = CNDP.LANDOFFICE_SEQ\r\n"
				+ "    AND CNDR.REG_NO = CNDP.REG_NO\r\n"
				+ "LEFT JOIN DATAM.PARCEL_HD HD\r\n"
				+ "    ON CNDP.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "    AND CNDP.PARCEL_ID = HD.PARCEL_ID\r\n"
				+ "    AND CNDP.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "LEFT JOIN DATAM.AREA AR1\r\n"
				+ "    ON HD.LANDOFFICE_SEQ = AR1.LANDOFFICE_SEQ\r\n"
				+ "    AND HD.AMPHUR_CODE = AR1.AMPHUR_CODE\r\n"
				+ "    AND HD.TUMBON_CODE = AR1.TUMBON_CODE\r\n"
				+ "WHERE CV.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "     AND (REPLACE(CV.CAVEAT_NAME,' ') IS NULL --3.1\r\n"
				+ "     OR LENGTH(CAVEAT_NAME) > 200 --3.2\r\n"
				+ "     OR CAVEAT_DATE IS NULL --3.3\r\n"
				+ "     OR SUBSTR(TO_CHAR(REGEXP_REPLACE(CV.CAVEAT_DATE, ' 0:00:00')),-4) > 2400 --3.4\r\n"
				+ "     OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,1)) > 12\r\n"
				+ "     OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,3)) > 2400\r\n"
				+ "     OR EN_DATE IS NULL --3.5\r\n"
				+ "     OR TO_DATE(EN_DATE, 'YYYY-MM-DD HH24:MI:SS') > TO_DATE('2400', 'YYYY') --3.6\r\n"
				+ "     OR (CAN_DATE IS NULL AND CAVEAT_FLG = 'D') --3.7\r\n"
				+ "     OR (TO_DATE(CAN_DATE, 'YYYY-MM-DD HH24:MI:SS') > TO_DATE('2400', 'YYYY') AND CAVEAT_FLG = 'D') --3.8\r\n"
				+ "     OR CAVEAT_FLG IS NULL --3.9\r\n"
				+ "     OR CAVEAT_FLG IS NOT NULL AND CAVEAT_FLG NOT IN ('1', 'D')))";
	
		List<OverviewCaveat> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewCaveat.class));
		return list;
	
	}
	
	@GetMapping("/overview/condo_encumb/{landoffice}")
	public List<OverviewBUR> getEncumbCondo(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN ENCUMB_CODE IS NULL THEN 1 END) AS NO_BUR_CODE,\r\n"
				+ "COUNT(CASE WHEN LINE_NO > 999 THEN 1 END) AS INVALID_LINENO\r\n"
				+ "FROM (\r\n"
				+ "SELECT DISTINCT AR1.AMPHUR_DESC, AR1.TUMBON_DESC, AR1.AMPHUR_CODE, AR1.TUMBON_CODE,\r\n"
				+ "                    SUBSTR(ENCUMB.REG_NO,0,instr(ENCUMB.REG_NO,'/')-1) REG, substr(ENCUMB.REG_NO,instr(ENCUMB.REG_NO,'/')+1) YEAR ,\r\n"
				+ "                    SUBSTR(ENCUMB.UNIT_NO,0,instr(ENCUMB.UNIT_NO,'/')-1) UNIT, substr(ENCUMB.UNIT_NO,instr(ENCUMB.UNIT_NO,'/')+1) AS NO,\r\n"
				+ "                    ENCUMB.LANDOFFICE_SEQ , ENCUMB.REG_NO, ENCUMB.UNIT_NO, ENCUMB.ENCUMB_CODE, ENCUMB.ENCUMB_DESC, CNDR.NAME_CONDO,\r\n"
				+ "                    to_number('5') as Key , ENCUMB.LINE_NO\r\n"
				+ "                    FROM DATAM.CONDO_ENCUMB ENCUMB\r\n"
				+ "                    LEFT JOIN DATAM.CONDO_REG CNDR\r\n"
				+ "                        ON CNDR.LANDOFFICE_SEQ = ENCUMB.LANDOFFICE_SEQ\r\n"
				+ "                        AND CNDR.REG_NO = ENCUMB.REG_NO\r\n"
				+ "                    LEFT JOIN  DATAM.CONDO_PARCEL CNDP\r\n"
				+ "                        ON CNDR.LANDOFFICE_SEQ = CNDP.LANDOFFICE_SEQ\r\n"
				+ "                        AND CNDR.REG_NO = CNDP.REG_NO\r\n"
				+ "                    LEFT JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                        ON CNDP.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "                        AND CNDP.PARCEL_ID = HD.PARCEL_ID\r\n"
				+ "                        AND CNDP.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "                    LEFT JOIN DATAM.AREA AR1\r\n"
				+ "                        ON HD.LANDOFFICE_SEQ = AR1.LANDOFFICE_SEQ\r\n"
				+ "                        AND HD.AMPHUR_CODE = AR1.AMPHUR_CODE\r\n"
				+ "                        AND HD.TUMBON_CODE = AR1.TUMBON_CODE\r\n"
				+ "                    WHERE ENCUMB.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "                         AND (ENCUMB.ENCUMB_CODE IS NULL\r\n"
				+ "                         OR ENCUMB.ENCUMB_DESC IS NULL\r\n"
				+ "                         OR ENCUMB.LINE_NO > 999))";
	
		List<OverviewBUR> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewBUR.class));
		return list;
	
	}
	
	@GetMapping("/overview/condo_m9/{landoffice}")
	public List<OverviewM9> getCondoM9(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN TITLE IS NULL THEN 1 END) AS NO_TITLE,\r\n"
				+ "COUNT(CASE WHEN PROV IS NULL THEN 1 END) AS NO_PROV,\r\n"
				+ "COUNT(CASE WHEN AMPHUR IS NULL THEN 1 END) AS NO_AMPHUR,\r\n"
				+ "COUNT(CASE WHEN TUMB IS NULL THEN 1 END) AS NO_TUMB,\r\n"
				+ "COUNT(CASE WHEN AGE < 1 OR AGE > 99 THEN 1 END) AS INVALID_AGE\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT AR1.AMPHUR_DESC, AR1.TUMBON_DESC, AR1.AMPHUR_CODE, AR1.TUMBON_CODE, M9.AGE,\r\n"
				+ "                        SUBSTR(M9.REG_NO,0,instr(M9.REG_NO,'/')-1) REG, substr(M9.REG_NO,instr(M9.REG_NO,'/')+1) YEAR ,\r\n"
				+ "                        SUBSTR(M9.UNIT_NO,0,instr(M9.UNIT_NO,'/')-1) UNIT, substr(M9.UNIT_NO,instr(M9.UNIT_NO,'/')+1) AS NO,\r\n"
				+ "                        M9.LANDOFFICE_SEQ, M9.REG_NO, M9.UNIT_NO, M9.TITLE, M9.PROV, M9.AMPHUR, M9.TUMB, CNDR.NAME_CONDO,\r\n"
				+ "                        to_number('6') as Key,\r\n"
				+ "                        CASE REGEXP_REPLACE(M9.FLG,' ','')\r\n"
				+ "                            WHEN 'B' THEN 'ขายฝาก'\r\n"
				+ "                            WHEN 'M' THEN 'มรดก'  END AS TYPE\r\n"
				+ "                    FROM DATAM.CONDO_M9 M9\r\n"
				+ "                    LEFT JOIN DATAM.CONDO_REG CNDR\r\n"
				+ "                        ON CNDR.LANDOFFICE_SEQ = M9.LANDOFFICE_SEQ\r\n"
				+ "                        AND CNDR.REG_NO = M9.REG_NO\r\n"
				+ "                    LEFT JOIN DATAM.CONDO_PARCEL CNDP\r\n"
				+ "                        ON CNDR.LANDOFFICE_SEQ = CNDP.LANDOFFICE_SEQ\r\n"
				+ "                        AND CNDR.REG_NO = CNDP.REG_NO\r\n"
				+ "                    LEFT JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                        ON CNDP.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "                        AND CNDP.PARCEL_ID = HD.PARCEL_ID\r\n"
				+ "                        AND CNDP.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "                    LEFT JOIN DATAM.AREA AR1\r\n"
				+ "                        ON HD.LANDOFFICE_SEQ = AR1.LANDOFFICE_SEQ\r\n"
				+ "                        AND HD.AMPHUR_CODE = AR1.AMPHUR_CODE\r\n"
				+ "                        AND HD.TUMBON_CODE = AR1.TUMBON_CODE\r\n"
				+ "                    WHERE M9.LANDOFFICE_SEQ = " + landoffice + " AND M9.FLG IN ('B','M')\r\n"
				+ "                           AND (M9.TITLE IS NULL\r\n"
				+ "                            OR  M9.PROV IS NULL\r\n"
				+ "                            OR  M9.AMPHUR IS NULL\r\n"
				+ "                            OR  M9.TUMB IS NULL\r\n"
				+ "                            OR  M9.AGE NOT BETWEEN 1 AND 999))";
	
		List<OverviewM9> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewM9.class));
		return list;
	
	}	

}
