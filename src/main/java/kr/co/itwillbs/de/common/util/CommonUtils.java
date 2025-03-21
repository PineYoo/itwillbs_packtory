package kr.co.itwillbs.de.common.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonUtils {

	@Value("${spring.servlet.multipart.location}")
	private String uploadDir;
	
	/**
	 * 멀티파트 파일 받아서 처리하기
	 * (위에 선언된)uploadDir(separator)yyyy(separator)mm(separator)dd(separator)UUID.fileExtension
	 * 에 저장을 하고 저장된 경로(filePath), 파일명(fileName) 를 리턴함
	 * 임시로 map에 담아서 전달
	 * TODO 25.03.18 객체가 결정 될 시 변동 있음
	 * @param mfile
	 * @throws Exception
	 */
	public Map setFile(MultipartFile mfile) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("uploadDir {}, separator {}, pathSeparator {}", uploadDir, File.separator, File.pathSeparator);
		
		String yyyy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")).toString();
		String mm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")).toString();
		String dd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")).toString();
		
		log.info("yyyy {}, mm {}, dd {}", yyyy, mm, dd);
		
		String uploadPath = uploadDir+File.separator
				+yyyy+File.separator
				+mm+File.separator
				+dd+File.separator;
		
		log.info("uploadPath {}, getOriginalFilename {}, dd {}", uploadPath, mfile.getOriginalFilename());
		
		//File filePath = new File(uploadPath); 뭐여 이거 1.0 이쟎아!?
		Path uploadsPath = Paths.get(uploadPath);
		
		//디렉토리가 없으면 생성
		if (!Files.exists(uploadsPath)) {
			log.info("It worked successfully to make a directory.");
			Files.createDirectories(uploadsPath);
		}
		//UUID로 파일명 저장
		String fileName = UUID.randomUUID().toString() + getExtension(mfile.getOriginalFilename());
		Path filePath = uploadsPath.resolve(fileName);
		
		// 파일 저장
		mfile.transferTo(filePath.toFile());
		
		log.info("{} wrote success", mfile.getOriginalFilename());
		//return new FilesVO(uploadsPath.toString(), fileName);
		
		Map<String, String> returnMap = new HashMap<>();
		returnMap.put("FILE_PATH", uploadsPath.toString());
		returnMap.put("FILE_NAME", fileName);
		
		return returnMap;
	}
	
	/**
	 * 파일 이름의 확장자 추출
	 * @param fileName
	 * @return
	 */
	private String getExtension(String fileName) {
		int extIndex = fileName.lastIndexOf('.');
		return extIndex > 0 ? fileName.substring(extIndex) : "";
	}
	
	/**
	 * Object 안에 null이 아닌 필드 값 출력
	 * @param obj
	 * @return StringBuffer.toString()
	 */
	public String ObjToString(Object obj) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		Class<?> cls = this.getClass();
		Method[] arrMethod = cls.getMethods();
		StringBuffer sb = new StringBuffer(/*this.getClass().toString() 유틸에서는 여기 클래스가 찍히니 패스!*/);
		sb.append("\n");
		try {
			for (Method m : arrMethod) {
				if(m.getName().startsWith("get") && !m.getName().equals("getClass") && m.invoke(this) != null) {
					sb.append(m.getName());
					sb.append(" : ");
					sb.append(m.invoke(this));
					sb.append("\n ");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	/**
	 * String 값을 받아서
	 * <br>Long 값 체크
	 * TODO 25.03.17 이런거 더 만들어야 할까?
	 * @param str 문자열
	 * @return boolean -> true: 정수 값, false: 정수가 아닌 값
	 */
	public boolean isLongValue(String str){
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			Long.parseLong(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
