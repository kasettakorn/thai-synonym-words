package com.example.error_report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import com.example.error_report.models.OverviewNSR;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
public class OverviewNSRController {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@GetMapping("/overview/nsl/{landoffice}")
	public List<OverviewNSR> getAllNSR(@PathVariable String landoffice) {
		String sql = "SELECT COUNT(CASE WHEN TUMBON_CODE IS NULL THEN 1 END) AS NO_TUMBON_CODE,\r\n"
				+ "COUNT(CASE WHEN TUMBON_CODE IS NOT NULL AND AREA_TUMBON_CODE IS NULL THEN 1 END) AS INVALID_TUMBON_CODE,\r\n"
				+ "COUNT(CASE WHEN NVL(TO_NUMBER(NOSORO_YEAR),NULL) IS NULL THEN 1 END) AS NAN_NOSORO_YEAR,\r\n"
				+ "COUNT(CASE WHEN NOSORO_YEAR < 2460 OR NOSORO_YEAR > TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')+543) THEN 1 END) AS INVALID_NOSORO_YEAR,\r\n"
				+ "COUNT(CASE WHEN NVL(TO_NUMBER(NOSORO_NO),NULL) IS NULL THEN 1 END) AS NAN_NOSORO_NO,\r\n"
				+ "COUNT(CASE WHEN NGAN > 3 THEN 1 END) AS OVERFLOW_NGAN,\r\n"
				+ "COUNT(CASE WHEN WA > 99 THEN 1 END) AS OVERFLOW_WA,\r\n"
				+ "COUNT(CASE WHEN LAND_NO = 0 OR LAND_NO IS NULL THEN 1 END) AS NO_LANDNO,\r\n"
				+ "COUNT(CASE WHEN RAVANG_CODE IS NULL OR RAVANG_NO_P IS NULL OR RAVANG_NO IS NULL OR TO_NUMBER(RAVANG_NO_P) = 0 THEN 1 END) AS INVALID_UTM_FORMAT,\r\n"
				+ "COUNT(CASE WHEN (RAVANG_RATIO <> 500 AND RAVANG_RATIO <> 1000 AND RAVANG_RATIO <> 2000 AND RAVANG_RATIO <> 40000 AND RAVANG_RATIO <> 5000)\r\n"
				+ "        OR (RAVANG_RATIO = 500 AND (RAVANG_PAGE > 64 OR RAVANG_PAGE < 1 OR RAVANG_PAGE IS NULL))\r\n"
				+ "        OR (RAVANG_RATIO = 1000 AND (RAVANG_PAGE > 16 OR RAVANG_PAGE < 1 OR RAVANG_PAGE IS NULL))\r\n"
				+ "        OR (RAVANG_RATIO = 2000 AND (RAVANG_PAGE > 4 OR RAVANG_PAGE < 1 OR RAVANG_PAGE IS NULL))\r\n"
				+ "        OR (RAVANG_RATIO = 4000 AND (RAVANG_PAGE <> 0 OR RAVANG_PAGE IS NULL))\r\n"
				+ "        OR (RAVANG_RATIO = 5000 AND (RAVANG_PAGE = 0  OR RAVANG_PAGE IS NULL)) THEN 1 END) AS INVALID_RAVANG_RATIO\r\n"
				+ "FROM (\r\n"
				+ "SELECT DISTINCT NSR.LANDOFFICE_SEQ, NVL(NSR.CHANGWAT_ABBR,'-') AS CHANGWAT_ABBR, NSR.NOSORO_NO AS NOSORO_NO,  NSR.NOSORO_YEAR, nsr.ravang_flag, substr(NSR.ISSUE_DATE, -least(length(NSR.ISSUE_DATE), 4)) AS YEAR_ISSUE,\r\n"
				+ "      NSR.CHANGWAT_ABBR||NSR.NOSORO_NO||\r\n"
				+ "      CASE NVL(NSR.NOSORO_YEAR,'0') WHEN '0' THEN NULL\r\n"
				+ "      ELSE '/'||NSR.NOSORO_YEAR END AS NSR_ID, TO_NUMBER('1') AS KEY,\r\n"
				+ "      NSR.RAI||'-'||NSR.NGAN||'-'||NSR.WA AS AREA,\r\n"
				+ "      AR1.AMPHUR_DESC, AR1.TUMBON_DESC,\r\n"
				+ "      AR1.AMPHUR_TCODE AS AREA_AMPHUR_TCODE,\r\n"
				+ "      AR1.TUMBON_CODE AS AREA_TUMBON_CODE,\r\n"
				+ "      NSR.CHANGWAT, NSR.AMPHUR_CODE, NSR.TUMBON_CODE,\r\n"
				+ "      NSR.NGAN, NSR.WA,\r\n"
				+ "      NSR.RAVANG_RATIO, NSR.RAVANG_CODE, NSR.RAVANG_NO_P, NSR.RAVANG_NO, NSR.RAVANG_PAGE, NSR.LAND_NO\r\n"
				+ "  FROM DATAM.L_NOSORO NSR\r\n"
				+ "  LEFT JOIN DATAM.AREA AR1\r\n"
				+ "          ON NSR.LANDOFFICE_SEQ = AR1.LANDOFFICE_SEQ\r\n"
				+ "          AND NSR.AMPHUR_CODE = AR1.AMPHUR_TCODE\r\n"
				+ "          AND NSR.TUMBON_CODE = AR1.TUMBON_CODE\r\n"
				+ "          AND AR1.CHANODETYPE = 0\r\n"
				+ "  WHERE NSR.LANDOFFICE_SEQ = " + landoffice + " \r\n"
				+ "  AND NSR.RAVANG_FLAG IN (1,2,3)\r\n"
				+ "      AND(NSR.AMPHUR_CODE IS NULL\r\n"
				+ "      OR NSR.TUMBON_CODE IS NULL\r\n"
				+ "      OR NOT EXISTS (\r\n"
				+ "          SELECT AR.AMPHUR_CODE FROM DATAM.AREA AR\r\n"
				+ "          WHERE AR.LANDOFFICE_SEQ = NSR.LANDOFFICE_SEQ\r\n"
				+ "          AND AR.AMPHUR_CODE = NSR.AMPHUR_CODE\r\n"
				+ "          AND AR.TUMBON_CODE = NSR.TUMBON_CODE\r\n"
				+ "          )\r\n"
				+ "      OR CASE NSR.NOSORO_YEAR WHEN '0' THEN NULL ELSE NSR.NOSORO_YEAR END IS NOT NULL\r\n"
				+ "          AND (REGEXP_LIKE(NSR.NOSORO_YEAR, '[^0-9]')\r\n"
				+ "          OR TO_NUMBER(NSR.NOSORO_YEAR) NOT BETWEEN 2460 AND TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')+543))\r\n"
				+ "      OR REGEXP_LIKE(NSR.NOSORO_NO, '[^0-9]')\r\n"
				+ "      OR NSR.NGAN > 3\r\n"
				+ "      OR NSR.WA > 99\r\n"
				+ "      OR NSR.LAND_NO IS NULL OR NSR.LAND_NO = 0\r\n"
				+ "      OR NSR.RAVANG_RATIO NOT IN ('500', '1000', '2000', '4000', '5000')\r\n"
				+ "      OR (NSR.RAVANG_RATIO = '5000' AND LPAD(RAVANG_PAGE,2,'0') <> '0')\r\n"
				+ "      OR (NSR.RAVANG_RATIO = '4000' AND LPAD(RAVANG_PAGE,2,'0') <> '00')\r\n"
				+ "      OR (NSR.RAVANG_RATIO = '2000' AND LPAD(RAVANG_PAGE,2,'0') NOT BETWEEN 01 AND 04)\r\n"
				+ "      OR (NSR.RAVANG_RATIO = '1000' AND LPAD(RAVANG_PAGE,2,'0') NOT BETWEEN 01 AND 16)\r\n"
				+ "      OR (NSR.RAVANG_RATIO = '500' AND LPAD(RAVANG_PAGE,2,'0') NOT BETWEEN 01 AND 64)\r\n"
				+ "      OR NSR.RAVANG_FLAG IS NULL OR NSR.RAVANG_CODE IS NULL\r\n"
				+ "      OR NSR.RAVANG_PAGE IS NULL OR NSR.RAVANG_NO IS NULL\r\n"
				+ "      OR NSR.RAVANG_NO_P IS NULL))";
	
		List<OverviewNSR> list = jdbcTemplate.query(sql, 
				BeanPropertyRowMapper.newInstance(OverviewNSR.class));
		return list;
	
	}
	
	

}
