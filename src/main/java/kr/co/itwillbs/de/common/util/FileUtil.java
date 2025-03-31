package kr.co.itwillbs.de.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

	// 이 클래스 밖으로 알리고 싶지 않은 변수이기에 private 선언 -> 다른 common.util 패키지내에서 써야 할 경우 private 지우거나 package 선언
	@Value("${spring.servlet.multipart.location}")
	private static String uploadDir;
	
	/**
	 * 멀티파트 파일 받아서 처리하기
	 * <pre>
	 * <br>(위에 선언된)uploadDir(separator)yyyy(separator)mm(separator)dd(separator)UUID.fileExtension
	 * <br>에 저장을 하고 저장된 경로(filePath), 파일명(fileName) 를 리턴함
	 * <br>임시로 map에 담아서 전달
	 * <br>TODO 25.03.18 객체가 결정 될 시 변동 있음
	 * <pre>
	 * @param mfile
	 * @throws Exception 
	 */
	public static Map<String, String> setFile(MultipartFile mfile) throws Exception {
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
			try {
				Files.createDirectories(uploadsPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//UUID로 파일명 저장
		String fileName = UUID.randomUUID().toString() + getExtension(mfile);
		//String fileName = UUID.randomUUID().toString() + getExtension(mfile.getOriginalFilename());
		Path filePath = uploadsPath.resolve(fileName);
		
		// 파일 저장
		try {
			if(isSpecial(mfile.getOriginalFilename())) {
				throw new Exception("허용할 수 없는 특수문자가 존재하는 파일 입니다.");
			}
			mfile.transferTo(filePath.toFile());
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		log.info("{} wrote success", mfile.getOriginalFilename());
		//return new FilesVO(uploadsPath.toString(), fileName);
		
		Map<String, String> returnMap = new HashMap<>();
		returnMap.put("FILE_PATH", uploadsPath.toString());
		returnMap.put("FILE_NAME", fileName);
		returnMap.put("FILE_SIZE", String.valueOf(mfile.getSize()));
		
		return returnMap;
	}
	
	/**
	 * 파일 이름의 확장자 추출
	 * @param fileName
	 * @return String => extension
	 */
	private static String getExtension(String fileName) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		int extIndex = fileName.lastIndexOf('.');
		return extIndex > 0 ? fileName.substring(extIndex) : "";
	}
	
	/**
	 * 파일 이름의 확장자 추출(Spring StringUtils.getFilenameExtension)
	 * @param multipartFile
	 * @return String => extension
	 */
	private static String getExtension(MultipartFile multipartFile) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
		return extension;
	}
	
	/**
	 * 파일을 필수로 받아야하는 컨트롤러에서 사용해야할 valid 
	 * @param MultipartFile mfile
	 * @return false : 파일 없음, true : 파일 존재함
	 */
	public static boolean isValidateForRequiredFile(MultipartFile mfile) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("getOriginalFilename is {}, getSize is {}, getName is {}", mfile.getOriginalFilename(), mfile.getSize(), mfile.getName());
		if(mfile.getSize() == 0) return false;
		
		return true;
	}
	
	/**
	 * 파일명에 특수문자가 있는지 확인함
	 * @param fileName
	 * @return boolean true: 존재함(업로드x) false: 존재하지 않음
	 */
	public static boolean isSpecial(String fileName) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean b = false;
		// 특수문자 + 시스템 우회문자 확인
		String[] filter_word = {"?","/",">","<",",",";",":","[","]","{","}","~","!","@","#","$","%","^","&","*","(",")","=","+","|","\\","`","%00","%zz","%70"};
		
		for(int i=0 ; i < filter_word.length ; i++){
			if(fileName.indexOf(filter_word[i]) > -1){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * 파일의 존재여부 ver.File
	 * @param filePath
	 * @param fileName
	 * @return boolean true: 파일 존재함, false: 파일 없음
	 */
	public static boolean existsFile1(String filePath, String fileName) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		File file = new File(filePath, fileName);
		return file.isFile();
	}
	
	/**
	 * 파일의 존재여부 ver.Files
	 * @param filePath
	 * @param fileName
	 * @return boolean true: 파일 존재함, false: 파일 없음
	 */
	public static boolean existsFile7(String filePath, String fileName) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		Path path = Paths.get(filePath, fileName);
		return Files.exists(path);
	}
	
	/**
	 * 어떡하냐, 너무 옛날 사람인가봐
	 * @param args
	 */
	public static void main(String... args) {
		String filename = "filename%zz";
		System.out.println(isSpecial(filename));
	}
}
