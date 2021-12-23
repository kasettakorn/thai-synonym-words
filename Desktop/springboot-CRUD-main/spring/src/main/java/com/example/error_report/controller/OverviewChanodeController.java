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
import com.example.error_report.models.OverviewCON;
import com.example.error_report.models.OverviewCaveat;
import com.example.error_report.models.OverviewDT;
import com.example.error_report.models.OverviewHD;
import com.example.error_report.models.OverviewM9;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
public class OverviewChanodeController {
	@Autowired
	JdbcTemplate jdbcTemplate;

	
	@GetMapping("/overview/hd/{landoffice}/{type}")
	public List<OverviewHD> getAllParcelHD(@PathVariable String landoffice, @PathVariable String type) {
		String sql = "SELECT COUNT(CASE WHEN CHANODETYPE IS NULL THEN 1 END) AS NO_DATA,\r\n"
				+ "    COUNT(CASE WHEN CHANODETYPE < 0 OR CHANODETYPE > 2 THEN 1 END) AS WRONGTYPE,\r\n"
				+ "    COUNT(CASE WHEN TUMBON_CODE IS NULL THEN 1 END) AS NO_TAMBON_CODE,\r\n"
				+ "    COUNT(CASE WHEN AREA_TUMBON_CODE IS NULL THEN 1 END) AS NO_AREA_TUMBON_CODE,\r\n"
				+ "    COUNT(CASE WHEN NVL(TO_NUMBER(PARCEL_ID), NULL) IS NULL THEN 1 END) AS NAN_PARCEL_ID,\r\n"
				+ "    COUNT(CASE WHEN SURVEY IS NULL THEN 1 END) AS NO_SURVEY,\r\n"
				+ "    COUNT(CASE WHEN NVL(TO_NUMBER(SURVEY), NULL) IS NULL THEN 1 END) AS NAN_SURVEY,\r\n"
				+ "    COUNT(CASE WHEN NNHAN > 3 THEN 1 END) AS MORE_3NNHAN,\r\n"
				+ "    COUNT(CASE WHEN NWAH > 99 THEN 1 END) AS MORE_99NWAH,\r\n"
				+ "    COUNT(CASE WHEN UTM_LANDNO IS NULL OR UTM_LANDNO = 0 THEN 1 END) AS NO_UTM_LANDNO,\r\n"
				+ "    COUNT(CASE WHEN UTM_CODE IS NULL OR UTM_NO_P IS NULL OR UTM_NO IS NULL OR UTM_NO_P = 0 THEN 1 END) AS WRONG_UTM_FORMAT,\r\n"
				+ "    COUNT(CASE WHEN (UTM_RATIO <> 500 AND UTM_RATIO <> 1000 AND UTM_RATIO <> 2000 AND UTM_RATIO <> 4000) \r\n"
				+ "        OR (UTM_RATIO = 500 AND (UTM_PAGE > 64 OR UTM_PAGE < 1 OR UTM_PAGE IS NULL))\r\n"
				+ "        OR (UTM_RATIO = 1000 AND (UTM_PAGE > 16 OR UTM_PAGE < 1 OR UTM_PAGE IS NULL))\r\n"
				+ "        OR (UTM_RATIO = 2000 AND (UTM_PAGE > 4 OR UTM_PAGE < 1 OR UTM_PAGE IS NULL))\r\n"
				+ "        OR (UTM_RATIO = 4000 AND (UTM_PAGE <> 0 OR UTM_PAGE IS NULL)) THEN 1 END) AS WRONG_UTM_RATIO,\r\n"
				+ "    COUNT (CASE WHEN UTM_RATIO <> 500 AND UTM_RATIO <> 1000 AND UTM_RATIO <> 2000 AND UTM_RATIO <> 4000 AND RATIO <> 5000 THEN 1 END) AS WRONG_RATIO\r\n"
				+ "FROM\r\n"
				+ "("
				+ "    SELECT DISTINCT HD.LANDOFFICE_SEQ, HD.PARCEL_ID, NVL(HD.CHANODETYPE, 0) AS CHANODETYPE, AR.AMPHUR_DESC, AR.TUMBON_DESC,\r\n"
				+ "        HD.AMPHUR_TCODE, AR.AMPHUR_TCODE AS AREA_AMPHUR_TCODE, HD.TUMBON_CODE, AR.TUMBON_CODE AS AREA_TUMBON_CODE, TO_NUMBER('1') AS KEY,\r\n"
				+ "        HD.SURVEY, HD.NNHAN, HD.NWAH,\r\n"
				+ "        HD.NRAI||'-'||HD.NNHAN||'-'||HD.NWAH AS AREA,\r\n"
				+ "        HD.UTM_RATIO AS UTM_RATIO,\r\n"
				+ "        HD.UTM_CODE AS UTM_CODE,\r\n"
				+ "        HD.UTM_NO_P AS UTM_NO_P,\r\n"
				+ "        HD.UTM_NO AS UTM_NO,\r\n"
				+ "        HD.UTM_PAGE AS UTM_PAGE,\r\n"
				+ "        HD.UTM_LANDNO AS UTM_LANDNO,\r\n"
				+ "        HD.RATIO AS RATIO\r\n"
				+ "    FROM DATAM.PARCEL_HD HD\r\n"
				+ "    LEFT JOIN DATAM.AREA AR\r\n"
				+ "        ON HD.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "        AND HD.AMPHUR_TCODE = AR.AMPHUR_TCODE\r\n"
				+ "        AND HD.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "        AND NVL(HD.CHANODETYPE, 0) = AR.CHANODETYPE\r\n"
				+ "        AND AR.CHANODETYPE = " + type + " \r\n"			
				+ "        AND (HD.AMPHUR_TCODE IS NOT NULL AND HD.TUMBON_CODE IS NOT NULL) --1.4\r\n"
				+ "    WHERE HD.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "         AND HD.CHANODETYPE = " + type + " \r\n"							
				+ "         AND (NVL(HD.CHANODETYPE, 0) NOT IN (1,2,0)  --1.1\r\n"
				+ "          OR HD.TUMBON_CODE IS NULL --1.3\r\n"
				+ "          OR HD.AMPHUR_TCODE IS NULL\r\n"
				+ "          OR HD.CHANODETYPE IN (0,1,2) AND REGEXP_LIKE(HD.PARCEL_ID, '[ก-ฮ]') --1.6\r\n"
				+ "          OR HD.SURVEY IS NULL --1.7\r\n"
				+ "          OR REGEXP_LIKE(HD.SURVEY, '[^0-9]') --1.8\r\n"
				+ "          OR HD.NNHAN > 3 --1.9\r\n"
				+ "          OR HD.NWAH > 99 --1.10\r\n"
				+ "          OR HD.UTM_RATIO IS NULL OR HD.UTM_CODE IS NULL OR HD.UTM_NO_P IS NULL --1.12\r\n"
				+ "          OR HD.UTM_NO IS NULL OR HD.UTM_PAGE IS NULL OR HD.UTM_LANDNO IS NULL\r\n"
				+ "          OR (HD.UTM_RATIO = '4000' AND LPAD(UTM_PAGE,2,'0') <> '00') --1.13\r\n"
				+ "          OR (HD.UTM_RATIO = '2000'\r\n"
				+ "            AND CASE\r\n"
				+ "            WHEN REGEXP_LIKE(UTM_PAGE, '[^0-9]') THEN NULL\r\n"
				+ "            ELSE TO_NUMBER(UTM_PAGE,99)\r\n"
				+ "            END NOT BETWEEN 01 AND 04)\r\n"
				+ "          OR (HD.UTM_RATIO = '1000'\r\n"
				+ "            AND CASE\r\n"
				+ "            WHEN REGEXP_LIKE(UTM_PAGE, '[^0-9]') THEN NULL\r\n"
				+ "            ELSE TO_NUMBER(UTM_PAGE,99)\r\n"
				+ "            END  NOT BETWEEN 01 AND 16)\r\n"
				+ "          OR (HD.UTM_RATIO = '500'\r\n"
				+ "            AND CASE\r\n"
				+ "            WHEN REGEXP_LIKE(UTM_PAGE, '[^0-9]') THEN NULL\r\n"
				+ "            ELSE TO_NUMBER(UTM_PAGE,99)\r\n"
				+ "            END NOT BETWEEN 01 AND 64)\r\n"
				+ "          OR HD.UTM_RATIO NOT IN ('500', '1000', '2000', '4000') --1.14\r\n"
				+ "          OR HD.UTM_LANDNO = 0\r\n"
				+ "          OR (HD.RATIO IS NOT NULL AND HD.RATIO NOT IN ('500', '1000', '2000', '4000', '5000')))\r\n"
				+ ")";
	
//		jdbcTemplate.setMaxRows(25);
		List<OverviewHD> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewHD.class));
		return list;
	
	}
	
	@GetMapping("/overview/dt/{landoffice}/{type}")
	public List<OverviewDT> getAllParcelDT(@PathVariable String landoffice, @PathVariable String type) {
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
				+ "(SELECT DISTINCT DT.LANDOFFICE_SEQ, DT.PARCEL_ID, AR.AMPHUR_DESC, AR.TUMBON_DESC, NVL(DT.CHANODETYPE, 0) AS CHANODETYPE,TO_NUMBER('2') AS KEY,\r\n"
				+ "                    DT.AMPHUR_TCODE, HD.TUMBON_CODE, HD.SURVEY,\r\n"
				+ "                    DT.RECV_CODE, DT.RECV_DESC, DT.REG_CODE, DT.REG_DESC,\r\n"
				+ "                    DT.OWN_PROV, DT.OWN_AMPHUR, DT.OWN_TUMB,\r\n"
				+ "                    DT.LINE_NO, DT.TITLE, DT.FNAME, DT.LNAME,DT.AGE,\r\n"
				+ "                    DT.FAT_TITLE, DT.MOT_TITLE\r\n"
				+ "                    FROM DATAM.PARCEL_DT DT\r\n"
				+ "                    INNER JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                        ON HD.LANDOFFICE_SEQ = DT.LANDOFFICE_SEQ\r\n"
				+ "                        AND NVL(HD.CHANODETYPE, 0) = NVL(DT.CHANODETYPE, 0)\r\n"
				+ "                        AND HD.PARCEL_ID = DT.PARCEL_ID\r\n"
				+ "                        AND HD.AMPHUR_TCODE = DT.AMPHUR_TCODE\r\n"
				+ "                    LEFT JOIN DATAM.AREA AR\r\n"
				+ "                        ON  DT.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "                        AND DT.AMPHUR_TCODE = AR.AMPHUR_TCODE\r\n"
				+ "                        AND HD.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "        AND AR.CHANODETYPE = " + type + " \r\n"		
				+ "                      WHERE DT.LANDOFFICE_SEQ = " + landoffice + " \r\n"
							+ "        AND DT.CHANODETYPE = " + type + " \r\n"		
				+ "                            AND  (DT.RECV_CODE IS NULL\r\n"
				+ "                            OR    DT.RECV_DESC IS NULL\r\n"
				+ "                            OR    DT.REG_CODE IS NULL\r\n"
				+ "                            OR    DT.REG_DESC IS NULL\r\n"
				+ "                            OR    DT.OWN_PROV IS NULL\r\n"
				+ "                            OR    DT.OWN_AMPHUR IS NULL\r\n"
				+ "                            OR    DT.OWN_TUMB IS NULL\r\n"
				+ "                            OR    DT.TITLE IS NULL\r\n"
				+ "                            OR    (DT.OWN_TYPE = 1 AND   DT.FAT_TITLE IS NULL)\r\n"
				+ "                            OR    (DT.OWN_TYPE = 1 AND   DT.MOT_TITLE IS NULL))\r\n"
				+ ")";
	
//		jdbcTemplate.setMaxRows(25);
		List<OverviewDT> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewDT.class));
		return list;
	
	}
	@GetMapping("/overview/caveat/{landoffice}/{type}")
	public List<OverviewCaveat> getAllParcelCaveat(@PathVariable String landoffice, @PathVariable String type) {
		String sql = "SELECT COUNT(CASE WHEN CAVEAT_NAME IS NULL THEN 1 END) AS NO_CAVEAT_NAME,\r\n"
				+ "COUNT(CASE WHEN LENGTH(CAVEAT_NAME) > 200 THEN 1 END) AS OVERFLOW_CAVAET_NAME,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_DATE IS NULL THEN 1 END) AS NO_CAVEAT_DATE,\r\n"
				+ "--COUNT(CASE WHEN CAVEAT_DATE IS NOT NULL AND TO_CHAR(SYSDATE, 'YYYY') = 2021 THEN 1 END) AS INVALID_YEAR\r\n"
				+ "COUNT(CASE WHEN EN_DATE IS NULL THEN 1 END) AS NO_EN_DATE,\r\n"
				+ "COUNT(CASE WHEN CAN_DATE IS NULL THEN 1 END) AS NO_CAN_DATE,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_FLG IS NULL THEN 1 END) AS NO_CAVEAT_FLG,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_FLG <> '1' AND CAVEAT_FLG <> 'D' THEN 1 END) AS INVALID_CAVEAT_FLG\r\n"
				+ "FROM\r\n"
				+ "( SELECT DISTINCT AR.AMPHUR_DESC, AR.TUMBON_DESC ,HD.SURVEY, CV.*, NVL(CV.CHANODETYPE, 0) AS CHANODETYPE , TO_NUMBER('3') AS KEY ,HD.TUMBON_CODE\r\n"
				+ "                      FROM DATAM.CAVEAT CV\r\n"
				+ "                      INNER JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                          ON HD.LANDOFFICE_SEQ = CV.LANDOFFICE_SEQ\r\n"
				+ "                          AND NVL(HD.CHANODETYPE, 0) = NVL(CV.CHANODETYPE, 0)\r\n"
				+ "                          AND HD.PARCEL_ID = CV.PARCEL_ID\r\n"
				+ "                          AND HD.AMPHUR_TCODE = CV.AMPHUR_TCODE\r\n"
				+ "                      LEFT JOIN DATAM.AREA AR\r\n"
				+ "                          ON  CV.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "                          AND CV.AMPHUR_TCODE = AR.AMPHUR_TCODE\r\n"
				+ "                          AND HD.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "        AND AR.CHANODETYPE = " + type + " \r\n"		
				+ "                      WHERE CV.LANDOFFICE_SEQ = " + landoffice + " AND CV.CHANODETYPE = 0\r\n"
				+ "        AND CV.CHANODETYPE = " + type + " \r\n"		
				+ "                          AND (REPLACE(CV.CAVEAT_NAME, ' ') IS NULL --3.1\r\n"
				+ "                          OR LENGTH(CAVEAT_NAME) > 200 --3.2\r\n"
				+ "                          OR CAVEAT_DATE IS NULL --3.3\r\n"
				+ "                          OR SUBSTR(TO_CHAR(REGEXP_REPLACE(CV.CAVEAT_DATE, ' 0:00:00')),-4) > 2400\r\n"
				+ "                          OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,1)) > 12\r\n"
				+ "                          OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,3)) > 2400\r\n"
				+ "                          OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,3)) < TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')-543)\r\n"
				+ "                          OR EN_DATE IS NULL --3.5\r\n"
				+ "                          OR TO_DATE(EN_DATE, 'YYYY-MM-DD HH24:MI:SS') > TO_DATE('2400', 'YYYY') --3.6\r\n"
				+ "                          OR (CAN_DATE IS NULL AND CAVEAT_FLG = 'D') --3.7\r\n"
				+ "                          OR (TO_DATE(CAN_DATE, 'YYYY-MM-DD HH24:MI:SS') > TO_DATE('2400', 'YYYY') AND CAVEAT_FLG = 'D') --3.8\r\n"
				+ "                          OR CAVEAT_FLG IS NULL --3.9\r\n"
				+ "                          OR CAVEAT_FLG IS NOT NULL AND CAVEAT_FLG NOT IN ('1', 'D') )\r\n"
				+ ")";
	
//		jdbcTemplate.setMaxRows(25);
		List<OverviewCaveat> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewCaveat.class));
		return list;
	
	}
	@GetMapping("/overview/bur/{landoffice}/{type}")
	public List<OverviewBUR> getAllParcelBur(@PathVariable String landoffice, @PathVariable String type) {
		String sql = "SELECT COUNT(CASE WHEN BUR_CODE IS NULL THEN 1 END) AS NO_BUR_CODE,\r\n"
				+ "COUNT(CASE WHEN LINE_NO > 999 THEN 1 END) AS INVALID_LINENO\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT AR.AMPHUR_DESC, AR.TUMBON_DESC , BUR.*, NVL(BUR.CHANODETYPE, 0) AS CHANODETYPE, TO_NUMBER('5') AS KEY,\r\n"
				+ "                          HD.TUMBON_CODE, HD.SURVEY\r\n"
				+ "                      FROM DATAM.PARCEL_BUR BUR\r\n"
				+ "                      INNER JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                          ON HD.LANDOFFICE_SEQ = BUR.LANDOFFICE_SEQ\r\n"
				+ "                          AND NVL(HD.CHANODETYPE, 0) = NVL(BUR.CHANODETYPE, 0)\r\n"
				+ "                          AND HD.PARCEL_ID = BUR.PARCEL_ID\r\n"
				+ "                          AND HD.AMPHUR_TCODE = BUR.AMPHUR_TCODE\r\n"
				+ "                      LEFT JOIN DATAM.AREA AR\r\n"
				+ "                            ON  BUR.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "                            AND BUR.AMPHUR_TCODE = AR.AMPHUR_TCODE\r\n"
				+ "                            AND HD.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "                            AND AR.CHANODETYPE = " + type + " \r\n"
				+ "                      WHERE BUR.LANDOFFICE_SEQ = " + landoffice + " AND BUR.CHANODETYPE = " + type + " \r\n"
				+ "                             AND (BUR.BUR_CODE IS NULL\r\n"
				+ "                              OR BUR.BUR_DESC IS NULL)\r\n"
				+ ")";
	
//		jdbcTemplate.setMaxRows(25);
		List<OverviewBUR> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewBUR.class));
		return list;
	
	}
	@GetMapping("/overview/m9/{landoffice}/{type}")
	public List<OverviewM9> getAllParcelM9(@PathVariable String landoffice, @PathVariable String type) {
		String sql = "SELECT COUNT(CASE WHEN TITLE IS NULL THEN 1 END) AS NO_TITLE,\r\n"
				+ "COUNT(CASE WHEN PROV IS NULL THEN 1 END) AS NO_PROV,\r\n"
				+ "COUNT(CASE WHEN AMPHUR IS NULL THEN 1 END) AS NO_AMPHUR,\r\n"
				+ "COUNT(CASE WHEN TUMB IS NULL THEN 1 END) AS NO_TUMB,\r\n"
				+ "COUNT(CASE WHEN AGE < 1 OR AGE > 99 THEN 1 END) AS INVALID_AGE\r\n"
				+ "FROM\r\n"
				+ "( SELECT DISTINCT AR.AMPHUR_DESC, AR.TUMBON_DESC, HD.SURVEY, M9.*,HD.TUMBON_CODE,\r\n"
				+ "                        TO_NUMBER('6') AS KEY,\r\n"
				+ "                            CASE REGEXP_REPLACE(M9.FLG,' ','')\r\n"
				+ "                                WHEN 'B' THEN 'ขายฝาก'\r\n"
				+ "                                WHEN 'M' THEN 'มรดก'  END AS TYPE\r\n"
				+ "                        FROM DATAM.PARCEL_M9 M9\r\n"
				+ "                        INNER JOIN DATAM.PARCEL_HD HD\r\n"
				+ "                            ON HD.LANDOFFICE_SEQ = M9.LANDOFFICE_SEQ\r\n"
				+ "                            AND HD.CHANODETYPE = M9.CHANODETYPE\r\n"
				+ "                            AND HD.PARCEL_ID = M9.PARCEL_ID\r\n"
				+ "                            AND HD.AMPHUR_TCODE = M9.AMPHUR_TCODE\r\n"
				+ "                        LEFT JOIN DATAM.AREA AR\r\n"
				+ "                            ON  M9.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "                            AND M9.AMPHUR_TCODE = AR.AMPHUR_TCODE\r\n"
				+ "                            AND HD.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "                            AND AR.CHANODETYPE = " + type + " \r\n"
				+ "                        WHERE M9.LANDOFFICE_SEQ = " + landoffice + " AND (M9.FLG IN ('B','M') AND M9.CHANODETYPE IS NOT NULL AND M9.AMPHUR_TCODE IS NOT NULL) \r\n"
				+ "                              AND M9.CHANODETYPE = " + type + " \r\n"
				+ "                               AND (M9.TITLE IS NULL\r\n"
				+ "                                OR M9.PROV IS NULL\r\n"
				+ "                                OR M9.AMPHUR IS NULL\r\n"
				+ "                                OR M9.TUMB IS NULL)\r\n"
				+ ")";
	
//		jdbcTemplate.setMaxRows(25);
		List<OverviewM9> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewM9.class));
		return list;
	
	}	
	@GetMapping("/overview/con/{landoffice}/{type}")
	public List<OverviewCON> getAllParcelCon(@PathVariable String landoffice, @PathVariable String type) {
		String sql = "SELECT COUNT(CASE WHEN CONSTR_CODE IS NULL THEN 1 END) AS NO_CONSTR_CODE,\r\n"
				+ "COUNT(CASE WHEN CON_CONSTR_CODE IS NULL THEN 1 END) AS NO_CON_CONSTR_CODE\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT PCON.*, NVL(PCON.CHANODETYPE, 0) AS CHANODETYPE, TO_NUMBER('7') AS KEY,\r\n"
				+ "   HD.SURVEY, CON.CONSTR_CODE AS CON_CONSTR_CODE, AR.AMPHUR_DESC, AR.TUMBON_DESC, HD.TUMBON_CODE\r\n"
				+ "FROM DATAM.PARCEL_CON PCON\r\n"
				+ "LEFT JOIN DATAM.CONSTR CON\r\n"
				+ "    ON PCON.CONSTR_CODE = CON.CONSTR_CODE\r\n"
				+ "    AND PCON.LANDOFFICE_SEQ = CON.LANDOFFICE_SEQ\r\n"
				+ "INNER JOIN DATAM.PARCEL_HD HD\r\n"
				+ "  ON HD.LANDOFFICE_SEQ = PCON.LANDOFFICE_SEQ\r\n"
				+ "  AND NVL(HD.CHANODETYPE, 0) = NVL(PCON.CHANODETYPE, 0)\r\n"
				+ "  AND HD.PARCEL_ID = PCON.PARCEL_ID\r\n"
				+ "  AND HD.AMPHUR_TCODE = PCON.AMPHUR_TCODE\r\n"
				+ "LEFT JOIN DATAM.AREA AR\r\n"
				+ "  ON  PCON.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "  AND PCON.AMPHUR_TCODE = AR.AMPHUR_TCODE\r\n"
				+ "  AND HD.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "  AND AR.CHANODETYPE = " + type + " \r\n"
				+ "WHERE PCON.LANDOFFICE_SEQ = " + landoffice + " AND PCON.CHANODETYPE = " + type + " \r\n"
				+ "   AND (PCON.CONSTR_CODE IS NULL\r\n"
				+ "   OR CON.CONSTR_CODE IS NULL)\r\n"
				+ "   )";
	
		List<OverviewCON> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewCON.class));
		return list;
	
	}	

}
