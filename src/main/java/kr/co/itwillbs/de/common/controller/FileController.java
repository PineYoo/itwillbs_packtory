
package kr.co.itwillbs.de.common.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

	private final FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * 
	 * @param fileIdx
	 * @return ResponseEntity<Resource> 정상 200, 파일 찾을 수 없음 404, 그외 500
	 */
	@GetMapping("/view/{fileIdx}")
	public ResponseEntity<Resource> getFileForView(@PathVariable("fileIdx") String fileIdx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		if(!StringUtil.isLongValue(null)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
		}
		
		try {
			ResponseEntity<Resource> response = fileService.getFile(Long.parseLong(fileIdx));
			log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
			return response;
		} catch (ResponseStatusException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * 
	 * @param fileIdx
	 * @return ResponseEntity<Resource> 정상 200, 파일 찾을 수 없음 404, 그외 500
	 */
	@GetMapping("/download/{fileIdx}")
	public ResponseEntity<Resource> getFileForDownloads(@PathVariable("fileIdx") String fileIdx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		if (!StringUtil.isLongValue(null)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
		}

		return fileService.getFile(Long.parseLong(fileIdx));
	}
}
