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
import com.example.error_report.models.OverviewM9;
import com.example.error_report.models.OverviewNS3HD;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
public class OverviewNS3Controller {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@GetMapping("/overview/ns3hd/{landoffice}")
	public List<OverviewNS3HD> getAllNS3HD(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN NVL(TO_NUMBER(NS3_ID),NULL) IS NULL THEN 1 END) AS NAN_NS3ID,\r\n"
				+ "COUNT(CASE WHEN NNHAN > 3 THEN 1 END) AS OVERFLOW_NNHAN,\r\n"
				+ "COUNT(CASE WHEN NWAH > 99 THEN 1 END) AS OVERFLOW_NWAH\r\n"
				+ "FROM\r\n"
				+ "(    \r\n"
				+ "    SELECT DISTINCT HD.LANDOFFICE_SEQ, HD.NS3_ID, AR1.AMPHUR_DESC, AR1.TUMBON_DESC, HD.AMPHUR_CODE, HD.TUMBON_CODE, HD.MOO_NO,\r\n"
				+ "      NRAI||'-'||NNHAN||'-'||NWAH AS AREA, NNHAN, NWAH,\r\n"
				+ "      to_number('1') as Key\r\n"
				+ "    FROM DATAM.NS3_HD HD\r\n"
				+ "    LEFT JOIN DATAM.AREA AR1\r\n"
				+ "      ON HD.LANDOFFICE_SEQ = AR1.LANDOFFICE_SEQ\r\n"
				+ "      AND HD.AMPHUR_CODE = AR1.AMPHUR_TCODE\r\n"
				+ "      AND HD.TUMBON_CODE = AR1.TUMBON_CODE\r\n"
				+ "    WHERE HD.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "    AND ( REGEXP_LIKE(HD.NS3_ID, '[^0-9]')\r\n"
				+ "      OR NNHAN > 3\r\n"
				+ "      OR NWAH > 99)\r\n"
				+ "  \r\n"
				+ "  )";
	
		List<OverviewNS3HD> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewNS3HD.class));
		return list;
	
	}
	@GetMapping("/overview/ns3dt/{landoffice}")
	public List<OverviewDT> getAllNS3DT(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN REG_CODE IS NULL OR REG_DESC IS NULL THEN 1 END) AS no_reg_code,\r\n"
				+ "COUNT(CASE WHEN OWN_PROV IS NULL THEN 1 END) AS no_own_prov,\r\n"
				+ "COUNT(CASE WHEN OWN_AMPHUR IS NULL THEN 1 END) AS no_own_amphur,\r\n"
				+ "COUNT(CASE WHEN OWN_TUMB IS NULL THEN 1 END) AS no_own_tumb,\r\n"
				+ "COUNT(CASE WHEN TITLE IS NULL THEN 1 END) AS no_title,\r\n"
				+ "COUNT(CASE WHEN FAT_TITLE IS NULL THEN 1 END) AS no_fat_title,\r\n"
				+ "COUNT(CASE WHEN MOT_TITLE IS NULL THEN 1 END) AS no_mot_title,\r\n"
				+ "COUNT(CASE WHEN AGE < 1 OR AGE > 999 THEN 1 END) AS wrong_age\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT AMPHUR_DESC, TUMBON_DESC, DT.LANDOFFICE_SEQ, DT.CHANGWAT, DT.NS3_ID, DT.AMPHUR_CODE, DT.TUMBON_CODE, DT.MOO_NO, DT.LINE_NO,\r\n"
				+ "    DT.TITLE, DT.FNAME, DT.LNAME, DT.OWN_TYPE, DT.OWN_MOO, DT.OWN_TUMB, DT.OWN_AMPHUR, DT.OWN_PROV, DT.AGE,\r\n"
				+ "    DT.RECV_CODE, DT.RECV_DESC, DT.REG_CODE, DT.REG_DESC, DT.FAT_TITLE, DT.MOT_TITLE,\r\n"
				+ "    TO_NUMBER('2') AS KEY\r\n"
				+ "    FROM DATAM.NS3_DT DT\r\n"
				+ "    INNER JOIN DATAM.NS3_HD HD\r\n"
				+ "        ON DT.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "        AND DT.NS3_ID  = HD.NS3_ID\r\n"
				+ "        AND DT.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "        AND DT.TUMBON_CODE = HD.TUMBON_CODE\r\n"
				+ "        AND DT.MOO_NO = HD.MOO_NO\r\n"
				+ "    LEFT JOIN DATAM.AREA AR\r\n"
				+ "        ON DT.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "        AND DT.AMPHUR_CODE = AR.AMPHUR_TCODE\r\n"
				+ "        AND DT.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "        AND AR.CHANODETYPE = 0\r\n"
				+ "  WHERE DT.LANDOFFICE_SEQ = " + landoffice + " AND  (DT.RECV_CODE IS NULL\r\n"
				+ "         OR    DT.RECV_DESC IS NULL\r\n"
				+ "         OR    DT.REG_CODE IS NULL\r\n"
				+ "         OR    DT.REG_DESC IS NULL\r\n"
				+ "         OR    DT.OWN_PROV IS NULL\r\n"
				+ "         OR    DT.OWN_AMPHUR IS NULL\r\n"
				+ "         OR    DT.OWN_TUMB IS NULL\r\n"
				+ "         OR    DT.TITLE IS NULL\r\n"
				+ "         OR    (DT.OWN_TYPE = 1 AND   DT.FAT_TITLE IS NULL)\r\n"
				+ "         OR    (DT.OWN_TYPE = 1 AND   DT.MOT_TITLE IS NULL)\r\n"
				+ "         OR    DT.AGE NOT BETWEEN 1 AND 999)\r\n"
				+ ")";
	
		List<OverviewDT> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewDT.class));
		return list;
	
	}
	
	@GetMapping("/overview/ns3_caveat/{landoffice}")
	public List<OverviewCaveat> getAllNS3Caveat(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN CAVEAT_NAME IS NULL THEN 1 END) AS NO_CAVEAT_NAME,\r\n"
				+ "COUNT(CASE WHEN LENGTH(CAVEAT_NAME) > 200 THEN 1 END) AS OVERFLOW_CAVAET_NAME,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_DATE IS NULL THEN 1 END) AS NO_CAVEAT_DATE,\r\n"
				+ "COUNT(CASE WHEN EN_DATE IS NULL THEN 1 END) AS NO_EN_DATE,\r\n"
				+ "COUNT(CASE WHEN CAN_DATE IS NULL THEN 1 END) AS NO_CAN_DATE,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_FLG IS NULL THEN 1 END) AS NO_CAVEAT_FLG,\r\n"
				+ "COUNT(CASE WHEN CAVEAT_FLG <> '1' AND CAVEAT_FLG <> 'D' THEN 1 END) AS INVALID_CAVEAT_FLG\r\n"
				+ "FROM\r\n"
				+ "(SELECT DISTINCT AMPHUR_DESC, TUMBON_DESC, CV.LANDOFFICE_SEQ, CV.CHANGWAT, CV.AMPHUR_CODE, CV.TUMBON_CODE, CV.MOO_NO, CV.NS3_ID, CV.CAVEAT_NAME, CV.CAVEAT_DATE, CV.CAVEAT_FLG, CV.EN_DATE, CV.CAN_DATE, TO_NUMBER('3') AS KEY\r\n"
				+ "                    FROM DATAM.NS3_CAVEAT CV\r\n"
				+ "                    INNER JOIN DATAM.NS3_HD HD\r\n"
				+ "                        ON CV.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "                        AND CV.NS3_ID  = HD.NS3_ID\r\n"
				+ "                        AND CV.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "                        AND CV.TUMBON_CODE = HD.TUMBON_CODE\r\n"
				+ "                        AND CV.MOO_NO = HD.MOO_NO\r\n"
				+ "                    LEFT JOIN DATAM.AREA AR\r\n"
				+ "                        ON CV.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "                        AND CV.AMPHUR_CODE = AR.AMPHUR_TCODE\r\n"
				+ "                        AND CV.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "                        AND AR.CHANODETYPE = 0\r\n"
				+ "                    WHERE CV.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "                         AND (REPLACE(CV.CAVEAT_NAME,' ') IS NULL --3.1\r\n"
				+ "                         OR LENGTH(CV.CAVEAT_NAME) > 200 --3.2\r\n"
				+ "                         OR CV.CAVEAT_DATE IS NULL --3.3\r\n"
				+ "                         OR SUBSTR(TO_CHAR(REGEXP_REPLACE(CV.CAVEAT_DATE, ' 0:00:00')),-4) > 2400--3.4\r\n"
				+ "                         OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,1)) > 12\r\n"
				+ "                         OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,3)) > 2400\r\n"
				+ "                         OR TO_NUMBER(REGEXP_SUBSTR(CAVEAT_DATE, '[^/]+',1,3)) < TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')-543)\r\n"
				+ "                         OR EN_DATE IS NULL --3.5\r\n"
				+ "                         OR TO_DATE(EN_DATE, 'YYYY-MM-DD HH24:MI:SS') > TO_DATE('2400', 'YYYY') --3.6\r\n"
				+ "                         OR (CAN_DATE IS NULL AND CAVEAT_FLG = 'D') --3.7\r\n"
				+ "                         OR (TO_DATE(CAN_DATE, 'YYYY-MM-DD HH24:MI:SS') > TO_DATE('2400', 'YYYY') AND CAVEAT_FLG = 'D') --3.8\r\n"
				+ "                         OR CAVEAT_FLG IS NULL --3.9\r\n"
				+ "                         OR CAVEAT_FLG IS NOT NULL AND CAVEAT_FLG NOT IN ('1', 'D') )\r\n"
				+ ")";
	
		List<OverviewCaveat> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewCaveat.class));
		return list;
	
	}
	@GetMapping("/overview/ns3_bur/{landoffice}")
	public List<OverviewBUR> getAllNS3Bur(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN BUR_CODE IS NULL THEN 1 END) AS NO_BUR_CODE,\r\n"
				+ "COUNT(CASE WHEN LINE_NO > 999 THEN 1 END) AS INVALID_LINENO\r\n"
				+ "FROM (\r\n"
				+ "SELECT DISTINCT AMPHUR_DESC, TUMBON_DESC, BUR.*, to_number('5') as Key\r\n"
				+ "                      FROM DATAM.NS3_BUR BUR\r\n"
				+ "                      INNER JOIN DATAM.NS3_HD HD\r\n"
				+ "                            ON HD.LANDOFFICE_SEQ = BUR.LANDOFFICE_SEQ\r\n"
				+ "                            AND HD.AMPHUR_CODE = BUR.AMPHUR_CODE\r\n"
				+ "                            AND HD.TUMBON_CODE = BUR.TUMBON_CODE\r\n"
				+ "                            AND HD.MOO_NO = BUR.MOO_NO\r\n"
				+ "                      LEFT JOIN DATAM.AREA AR\r\n"
				+ "                            ON BUR.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "                            AND BUR.AMPHUR_CODE = AR.AMPHUR_TCODE\r\n"
				+ "                            AND BUR.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "                            AND AR.CHANODETYPE = 0\r\n"
				+ "                      WHERE BUR.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "                             AND (BUR.BUR_CODE IS NULL\r\n"
				+ "                             OR BUR.BUR_DESC IS NULL\r\n"
				+ "                             OR BUR.LINE_NO > 999))";
	
		List<OverviewBUR> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewBUR.class));
		return list;
	
	}
	
	@GetMapping("/overview/ns3_m9/{landoffice}")
	public List<OverviewM9> getAllNS3M9(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN TITLE IS NULL THEN 1 END) AS NO_TITLE,\r\n"
				+ "COUNT(CASE WHEN PROV IS NULL THEN 1 END) AS NO_PROV,\r\n"
				+ "COUNT(CASE WHEN AMPHUR IS NULL THEN 1 END) AS NO_AMPHUR,\r\n"
				+ "COUNT(CASE WHEN TUMB IS NULL THEN 1 END) AS NO_TUMB,\r\n"
				+ "COUNT(CASE WHEN AGE < 1 OR AGE > 99 THEN 1 END) AS INVALID_AGE\r\n"
				+ "FROM (\r\n"
				+ "SELECT DISTINCT AMPHUR_DESC, TUMBON_DESC,\r\n"
				+ "    M9.LANDOFFICE_SEQ, M9.AMPHUR_CODE, M9.TUMBON_CODE, M9.MOO_NO, M9.NS3_ID, M9.TYPE,\r\n"
				+ "    M9.PROV, M9.AMPHUR, M9.TUMB, M9.AGE,\r\n"
				+ "    M9.TITLE||M9.FNAME||' '||M9.LNAME AS M9ER, M9.FLG,M9.TITLE,\r\n"
				+ "    CASE REGEXP_REPLACE(M9.FLG,' ','')\r\n"
				+ "      WHEN 'B' THEN 'ขายฝาก'\r\n"
				+ "      WHEN 'M' THEN 'มรดก' END AS TYPE,\r\n"
				+ "    to_number('6') as Key\r\n"
				+ "      FROM DATAM.NS3_M9 M9\r\n"
				+ "      INNER JOIN DATAM.NS3_HD HD\r\n"
				+ "          ON M9.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "          AND M9.NS3_ID  = HD.NS3_ID\r\n"
				+ "          AND M9.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "          AND M9.TUMBON_CODE = HD.TUMBON_CODE\r\n"
				+ "          AND M9.MOO_NO = HD.MOO_NO\r\n"
				+ "      LEFT JOIN DATAM.AREA AR\r\n"
				+ "          ON M9.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "          AND M9.AMPHUR_CODE = AR.AMPHUR_TCODE\r\n"
				+ "          AND M9.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "          AND AR.CHANODETYPE = 0\r\n"
				+ "      WHERE  M9.LANDOFFICE_SEQ = " + landoffice + " AND M9.FLG IN ('B','M') \r\n"
				+ "              OR M9.TITLE IS  NULL\r\n"
				+ "              OR M9.PROV IS NULL  \r\n"
				+ "              OR M9.AMPHUR IS NULL \r\n"
				+ "              OR M9.TUMB IS NULL\r\n"
				+ "              OR M9.AGE NOT BETWEEN 1 AND 999\r\n"
				+ ")";
	
		List<OverviewM9> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewM9.class));
		return list;
	
	}
	
	@GetMapping("/overview/ns3_con/{landoffice}")
	public List<OverviewCON> getAllNS3Con(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN CONSTR_CODE IS NULL THEN 1 END) AS NO_CONSTR_CODE,\r\n"
				+ "COUNT(CASE WHEN CON_CONSTR_CODE IS NULL THEN 1 END) AS NO_CON_CONSTR_CODE\r\n"
				+ "FROM (\r\n"
				+ "SELECT DISTINCT AMPHUR_DESC, TUMBON_DESC, NS3CON.*, CON.CONSTR_CODE AS CON_CONSTR_CODE, to_number('7') as Key\r\n"
				+ "                        FROM DATAM.NS3_CON NS3CON\r\n"
				+ "                        INNER JOIN DATAM.NS3_HD HD\r\n"
				+ "                            ON NS3CON.LANDOFFICE_SEQ = HD.LANDOFFICE_SEQ\r\n"
				+ "                            AND NS3CON.NS3_ID  = HD.NS3_ID\r\n"
				+ "                            AND NS3CON.AMPHUR_CODE = HD.AMPHUR_CODE\r\n"
				+ "                            AND NS3CON.TUMBON_CODE = HD.TUMBON_CODE\r\n"
				+ "                            AND NS3CON.MOO_NO = HD.MOO_NO\r\n"
				+ "                        LEFT JOIN DATAM.CONSTR CON\r\n"
				+ "                              ON NS3CON.CONSTR_CODE = CON.CONSTR_CODE\r\n"
				+ "                              AND NS3CON.LANDOFFICE_SEQ = CON.LANDOFFICE_SEQ\r\n"
				+ "                        LEFT JOIN DATAM.AREA AR\r\n"
				+ "                              ON NS3CON.LANDOFFICE_SEQ = AR.LANDOFFICE_SEQ\r\n"
				+ "                              AND NS3CON.AMPHUR_CODE = AR.AMPHUR_TCODE\r\n"
				+ "                              AND NS3CON.TUMBON_CODE = AR.TUMBON_CODE\r\n"
				+ "                              AND AR.CHANODETYPE = 0\r\n"
				+ "                        WHERE  NS3CON.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "                               AND (NS3CON.CONSTR_CODE IS NULL\r\n"
				+ "                               OR CON.CONSTR_CODE IS NULL))";
	
		List<OverviewCON> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewCON.class));
		return list;
	
	}
	

}
