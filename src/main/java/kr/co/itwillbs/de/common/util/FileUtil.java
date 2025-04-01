package kr.co.itwillbs.de.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.itwillbs.de.common.vo.FileVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtil {

	// 이 클래스 밖으로 알리고 싶지 않은 변수이기에 private 선언 -> 다른 common.util 패키지내에서 써야 할 경우 private 지우거나 package 선언
	@Value("${spring.servlet.multipart.location}")
	public String UPLOAD_DIR;
	
//	public void setUPLOAD_DIR(String str) {
//		UPLOAD_DIR = str;
//	}
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
	public FileVO setFile(MultipartFile mfile) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("uploadDir {}, separator {}, pathSeparator {}", UPLOAD_DIR, File.separator, File.pathSeparator);
		
		String yyyy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy")).toString();
		String mm = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM")).toString();
		String dd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd")).toString();
		
		//log.info("yyyy {}, mm {}, dd {}", yyyy, mm, dd);
		
		String uploadPath = File.separator
				+yyyy+File.separator
				+mm+File.separator
				+dd+File.separator;
		
		//log.info("uploadPath {}, getOriginalFilename {}, dd {}", uploadPath, mfile.getOriginalFilename());
		
		Path uploadsPath = Paths.get(chekcFileSeparator(uploadPath));
		
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
		String fileName = UUID.randomUUID().toString() +getExtension(mfile);
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
		//return new FilesVO(fileName, uploadsPath.toString(), String.valueOf(mfile.getSize()));
		
		FileVO fileVO = new FileVO(uploadPath, fileName, mfile.getOriginalFilename(), String.valueOf(mfile.getSize()));
		log.info("fileVO toString : {}", fileVO.toString());
		return fileVO;
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
		return "."+extension;
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
	 * 업로드/다운로드 작업 전 파일 경로 확인 메서드
	 * <br> 현재 시스템에 맞춰서 (\|/)를 바꿔준다.
	 * @param path
	 * @return "" : path 파라미터가 null 또는 "" 일때 반환, 그 외엔 신뢰할 수 있는 경로 임
	 */
	public static String chekcFileSeparator(String path) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		if(!StringUtils.hasLength(path)) {
			return "";
		}
		String regex = "[\\\\/]";
		String replacedText = Matcher.quoteReplacement(File.separator);
		String returnStr = path.replaceAll(regex, replacedText);
		log.info("input path : {} -> changedPath : {}", path, returnStr);
		return returnStr;
	}
	
	/**
	 * 어떡하냐, 너무 옛날 사람인가봐
	 * @param args
	 */
	public static void main(String... args) {
		String filename = "filename%zz.dhjaksd.jpg";
		String pathName = "/packtory/uploads\\yyyy\\mm\\dd";
		System.out.println(isSpecial(filename));
		System.out.println(chekcFileSeparator(pathName));
		System.out.println(getExtension(filename));
	}
}
