package kr.co.itwillbs.de.common.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import kr.co.itwillbs.de.common.mapper.FileMapper;
import kr.co.itwillbs.de.common.util.FileUtil;
import kr.co.itwillbs.de.common.vo.FileVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {

	private final FileMapper fileMapper;
	private final FileUtil fileUtil;
	
	// view에서 파일리소스 주소 prefix 값
	public static final String DOWNLOADS_PATH="/file/view";
	
	@Value("${spring.servlet.multipart.location}")
	public String UPLOAD_DIR;
	
	public FileService(FileMapper fileMapper, FileUtil fileUtil) {
		this.fileMapper = fileMapper;
		this.fileUtil = fileUtil;
	}
	
	/**
	 * 
	 * @param fileIdx
	 * @return
	 */
	public ResponseEntity<Resource> getFile(Long fileIdx) throws ResponseStatusException {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// fileIdx로 파일 정보 가져오기
		FileVO fileVO = fileMapper.getFileByIdx(fileIdx);
		log.info("fileVo is {}", fileVO);
		// 다운로드 할 파일 정보를 서버 상의 업로드 디렉토리에 접근하여 가져오기
		String strPath = UPLOAD_DIR+fileVO.getFilePath();
		log.info("strPath is {}", strPath);
		// 업로드/다운로드 작업 전 파일 경로 검증 메서드
		Path uploadPath = Paths.get(FileUtil.chekcFileSeparator(strPath));
		log.info("uploadPath is {}", uploadPath);
		// 파일 경로 + 파일 명 더하기!
		Path path = Paths.get(uploadPath.toString())
					.resolve(fileVO.getFileName()).normalize();
		log.info("path is {}", path);
		// UrlResource 객체를 생성하여 이미지 파일에 대한 리소스 객체 생성 => Resource 타입으로 업캐스팅
		try {
			Resource resource = new UrlResource(path.toUri());
			log.info("path {}, path.toUri {}", path, path.toUri());
			
			// 경로 존재 여부 판별 및 파일 접근 가능 여부 확인
			if(!resource.exists() || !resource.isReadable()) {
				log.info("!resource.exists() || !resource.isReadable() were true!");
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
			}
			
			// 파일의 MIME 타입(컨텐츠 타입) 설정
			// 실제 파일로부터 컨텐츠 타입 알아내기
			String contentType = Files.probeContentType(path);
			if(contentType == null) { // 컨텐츠 타입 조회 실패 시
				log.info("contentType == null => application/octet-stream");
				contentType = "application/octet-stream";
			}
			
			// 응답 데이터(ResponseEntity(Resource> 타입) 리턴
			return ResponseEntity.ok() // 정상적인 응답(HTTP_OK = 200)으로 설정
					.contentType(MediaType.parseMediaType(contentType)) // 자동으로 설정된 컨텐츠 타입으로 설정 이미지 파일은 브라우저에서 직접 열리고, 그 외의 대부분의 파일은 다운로드 창 열림
					//.contentType(MediaType.APPLICATION_OCTET_STREAM) // 자동으로 설정된 컨텐츠 타입으로 설정 이미지 파일은 브라우저에서 직접 열리고, 그 외의 대부분의 파일은 다운로드 창 열림
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ fileVO.getFileName() + "\"")
					.body(resource);
		} catch (MalformedURLException e) { 
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드 실패");
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 타입 확인 실패");
		}
	}
	
	/**
	 * INSERT INTO t_files values (fileVO) 
	 * @param fileVO
	 * @return
	 */
	public int registerFile(FileVO fileVO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return fileMapper.registerFile(fileVO);
	}
	
	/**
	 * INSERT INTO t_files values (List<fileVO>) 
	 * @param fileVOList
	 * @return
	 */
	public int registerFiles(List<FileVO> fileVOList) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		int affectedRow = fileMapper.registerFiles(fileVOList);
		
		return affectedRow;
	}
	
	/**
	 * SELECT from t_files where idx = #{idx} 
	 * @param idx
	 * @return
	 */
	public FileVO getFileByIdx(long idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		FileVO fileVo = fileMapper.getFileByIdx(idx);
		
		return fileVo;
	}
	
	/**
	 * SELECT from t_files where major_idx = #{majorIdx}
	 * @param idx
	 * @return
	 */
	public List<FileVO> getFilesByMajorIdx(String type, long majorIdx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<FileVO> fileList = fileMapper.getFilesByMajorIdx(type, majorIdx);
		
		return fileList;
	}
}
