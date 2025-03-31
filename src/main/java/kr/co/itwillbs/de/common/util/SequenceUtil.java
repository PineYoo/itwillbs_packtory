package kr.co.itwillbs.de.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.itwillbs.de.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SequenceUtil {

	private final CommonService commonService;
	
	public SequenceUtil(CommonService commonService) {
		this.commonService = commonService;
	}
	
	@Value("${spring.datasource.hikari.driver-class-name}")
	private String dbDriverClassName;
	private final String H2_DATABASE = "h2";
	private final String MYSQL_DATABASE = "mysql";
	
	/**
	 * application.yml 에서 어떤 프로필이든 이걸 쓰면 됨
	 * @return
	 */
	public String getEmpSeqFromDB() {
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.debug("driverClassName is {}", dbDriverClassName);
		
		if(dbDriverClassName.toLowerCase().contains(H2_DATABASE)) {
			return getEmpSeqFromDBverH2();
		} else if(dbDriverClassName.toLowerCase().contains(MYSQL_DATABASE)) {
			return getEmpSeqFromDBverMySQL();
		}
		
		return "누구냐 넌?";
	}
	
	/**
	 * MySQL DB에서 시퀀스 테이블 만들어서 사용하기 ver.Mybatis
	 * @return
	 */
	public String getEmpSeqFromDBverMySQL() {
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		String seqEmpId = commonService.createSeqEmpIdfromMysql();
		log.debug("MySQL database seq_emp_id is : {}", seqEmpId);
		return seqEmpId;
	}
	
	/**
	 * H2 DB에서 시퀀스 테이블 만들어서 사용하기 ver.Mybatis
	 * @deprecated
	 * @return
	 */
	public String getEmpSeqFromDBverH2() {
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		String seqEmpId = commonService.createSeqEmpIdfromH2();
		log.debug("H2 database seq_emp_id is : {}", seqEmpId);
		return seqEmpId;
	}
	
	private static final String NUMBERING_PROP_FILE = "./data/seq.properties";
	
	/**
	 * 파일로 읽고/기록 하는 Seq 만드는 것
	 * <br> 버전관리할때 프로퍼티 파일도 함께 공유해야되서 불편함이 있을 경우 Mybatis 버전을 사용하기 바람
	 * @return String => 이번에 사용해야하는 seq
	 */
	public static String getEmpSeqFromFile() {
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		// 프로퍼티 파일 읽기
		Properties properties = loadProperties(NUMBERING_PROP_FILE);

		// 프로퍼티 값 출력
		properties.forEach((key, value) -> System.out.println(key + " = " + value));

		String empSeq = properties.getProperty("emp-id", "100000");
		empSeq = String.valueOf(Integer.parseInt(empSeq) + 1);
		log.debug("읽어온 프로퍼티 파일 {}, 사용가능한 empSeq : {}", NUMBERING_PROP_FILE, empSeq);
		// 프로퍼티 값 변경 및 저장
		properties.setProperty("emp-id", empSeq);
		saveProperties(properties, NUMBERING_PROP_FILE, "Updated properties file");
		
		return empSeq;
	}
	
	/**
	 * 프로퍼티 파일 가져오기 => 없는 경우 생성
	 * @param filePath
	 * @return
	 */
	private static Properties loadProperties(String filePath) {
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		Properties properties = new Properties();
		File file = new File(filePath);

		// 파일이 없으면 생성
		if (!file.exists()) {
			try {
				file.createNewFile();
				log.debug("새로운 properties 파일 생성: " + filePath);
			} catch (IOException e) {
				log.error("파일을 생성할 수 없습니다: " + e.getMessage());
			}
		}

		try (InputStream input = new FileInputStream(file)) {
			properties.load(input);
		} catch (IOException e) {
			log.error("Failed to load properties file: " + e.getMessage());
		}
		return properties;
	}
	
	/**
	 * 프로퍼티 파일 저장
	 * @param properties 저장할 프로퍼티
	 * @param filePath 저장할 경로
	 * @param comments 코멘트(없어도 가능, null 가능)
	 */
	private static void saveProperties(Properties properties, String filePath, String comments) {
		log.debug("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		try (OutputStream output = new FileOutputStream(filePath)) {
			properties.store(output, comments);
		} catch (IOException e) {
			log.error("Failed to save properties file: " + e.getMessage());
		}
	}
	
}
