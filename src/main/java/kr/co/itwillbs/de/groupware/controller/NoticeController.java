package kr.co.itwillbs.de.groupware.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.FileUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.common.vo.FileVO;
import kr.co.itwillbs.de.groupware.dto.NoticeDTO;
import kr.co.itwillbs.de.groupware.dto.NoticeSearchDTO;
import kr.co.itwillbs.de.groupware.service.NoticeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value={"/groupware/notice"})
@Controller
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private CommonCodeUtil commonCodeUtil;
	
	@Autowired FileUtil fileUtil;
	
	//	공지사항 공통코드
	private final String COMMON_MAJOR_CODE_NOTICE_TYPE = "NOTICE_TYPE";
	private final String FILE_COMMON_TYPE = "T_NOTICE";
	
	/**
	 * 공지사항 등록 페이지(view)를 요청하는 "/groupware/notice/new" 연결
	 * @param model
	 * @return approval/notice/notice_register_form
	 */
	@GetMapping(value={"/new"})
	public String noticeRegisterForm(@ModelAttribute NoticeDTO noticeDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		//	----------------------------------------------------------
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String memberId = userDetails.getUsername();
		noticeDTO.setRegId(memberId);
		//	----------------------------------------------------------
		model.addAttribute("noticeDTO", noticeDTO);
		//	공통코드 가져오기
		model.addAttribute("codeType", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_NOTICE_TYPE));
		
		return "groupware/notice/notice_register_form";
	}

	/**
	 * 공지사항 등록(INSERT)을 하는 "/groupware/notice" 연결 POST!
	 * <br>http.header.Content-Type: 'application/x-www-form-urlencoded (이게 기본 값임)
	 * <br>이 경우 @ModelAttribute 어노테이션으로 받으면 매핑되는 DTO의 필드와 이름이 같고 setter가 존재하면 DTO에 필드 값이 채워 짐
	 * @return
	 */
	@PostMapping(value={"","/"})
	public String noticeRegister(@ModelAttribute("noticeDTO") NoticeDTO noticeDTO,
								 @RequestParam("noticeFiles") List<MultipartFile> noticeFiles,
								 Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestData : " + noticeDTO.toString());
		
		//	t_files 테이블에 넣기위한 idx 값 가져오기
		String idx = noticeService.registerNotice(noticeDTO);
		
		//	noticeFiles 안에 들어있는 값을 넣기위한 List 객체 선언
		List<FileVO> fileList = new ArrayList<>();
		//	파일 랭크넘버용
		int rank_number = 1;
		
		//	파일 업로드 작업
		for(MultipartFile file : noticeFiles) {
			log.info("들어오는거 확인 " + file.getOriginalFilename());
			
			//	파일을 첨부하지 않았을 때 파일 업로드 작업 중지
			if(!StringUtils.hasLength(file.getOriginalFilename())) {
				break;
			}
			
			try {
				//	setFile 메서드 호출하여 FileVO 리턴 받아 List에 저장
				fileList.add(fileUtil.setFile(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		for(FileVO fileVO : fileList) {
			//	idx 세팅
			fileVO.setMajorIdx(idx);
			//	type 세팅
			fileVO.setType(FILE_COMMON_TYPE);
			//	삭제유무 기본값 N
			fileVO.setIsDeleted("N");
			//	랭크넘버 세팅(int 값 String으로 변환)
			fileVO.setRankNumber(String.valueOf(rank_number));
			//	랭크넘버 값 증가
			rank_number++;
			
			fileService.registerFile(fileVO);
		}
		
		return "redirect:/groupware/notice";
		
	}
	
	/**
	 * 공지사항 목록 조회(SELECT)를 요청하는 "/groupware/notice" 연결 GET!
	 * @param model
	 * @return approval/notice/notice_list
	 */
	@GetMapping(value={"","/"})
	public String getNoticeList(@ModelAttribute NoticeSearchDTO noticeSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//	리스트 검색 DTO 뷰에 전달
		noticeSearchDTO.getPageDTO().setTotalCount(noticeService.getNoticeCountBySearchDTO(noticeSearchDTO));
		model.addAttribute("noticeSearchDTO", noticeSearchDTO);
		//	notice type select 용 리스트 뷰에 전달
		model.addAttribute("type", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_NOTICE_TYPE));
		
		//	리스트 결과 DTOlist 뷰에 전달
		List<NoticeDTO> noticeDTOlist = noticeService.getNoticeList(noticeSearchDTO);
		model.addAttribute("noticeDTOlist", noticeDTOlist);
		
		return "groupware/notice/notice_list";
	}
	
	/**
	 * 공지사항 게시글 검색 조건 조회
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/search")
	public String noticeGetList(@ModelAttribute NoticeSearchDTO noticeSearchDTO, Model model) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {} ", noticeSearchDTO.toString());
		//	리스트 검색 DTO 뷰에 전달
		noticeSearchDTO.getPageDTO().setTotalCount(noticeService.getNoticeCountBySearchDTO(noticeSearchDTO));
		model.addAttribute("noticeSearchDTO", noticeSearchDTO);
		//	notice type select 용 리스트 뷰에 전달
		model.addAttribute("type", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_NOTICE_TYPE));
		
		//	리스트 결과 DTOlist 뷰에 전달
		List<NoticeDTO> noticeDTOList = noticeService.getNoticeList(noticeSearchDTO);
		model.addAttribute("noticeDTOlist", noticeDTOList);
		
		return "groupware/notice/notice_list";
	}
	
	/**
	 * 단일 공지사항 게시글 조회(SELECT) "/groupware/notice/{idx}" 주소 매핑(GET)
	 * @param idx 샘플 테이블 PK값
	 * @param model sampleDTO
	 * @return
	 */
	@GetMapping("/{idx}")
	public String getNotice(@PathVariable(name = "idx") String idx, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {} ", idx);
		
		if(StringUtil.isLongValue(idx)) {
			// 정수일 경우 조회 가능
			model.addAttribute("noticeDTO", noticeService.getNotice(idx));
			model.addAttribute("fileList", fileService.getFilesByMajorIdx(FILE_COMMON_TYPE, Long.parseLong(idx)));
			// 조회 수도 있기 때문에 업데이트 하자!
			NoticeDTO noticeDTO = new NoticeDTO();
			// Idx만 update 쿼리에 보낼 경우 readCnt를 증가시키게 작성해두었다.
			noticeDTO.setIdx(idx);
			
			//	DTO에서 idx를 String 으로 선언해둠
//			noticeDTO.setIdx(Long.parseLong(idx));
			try {
				noticeService.modifyNotice(noticeDTO);
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/groupware/notice";
			}
			
		} else {
			//	정수가 아닐 경우 다시 리스트로 리다이렉트
			return "redirect:/groupware/notice";
		}
		
		return "groupware/notice/notice_detail";
	}
	
	/**
	 * 공지사항 수정폼을 요청하는 "/groupware/notice/modify/{idx}" 주소 매핑(GET)
	 * @param idx
	 * @param model
	 * @return
	 */
	@GetMapping("/modify/{idx}")
	public String modifyNotice(@PathVariable(name = "idx") String idx, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info(idx);
		model.addAttribute("noticeDTO", noticeService.getNotice(idx));
		model.addAttribute("fileList", fileService.getFilesByMajorIdx(FILE_COMMON_TYPE, Long.parseLong(idx)));
		model.addAttribute("codeType", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_NOTICE_TYPE));
		return "groupware/notice/notice_modify_form";
	}
	
	/**
	 * 샘플 수정(UPDATE)를 요청하는 "/groupware/notice/{idx}" 주소 매핑(PUT)
	 * <br> 히든 메서드 필터에 의해 PUT으로 변경헤서 서브밋함
	 * @param idx 샘플 테이블 PK값
	 * @param sampleDTO 샘플DTO
	 * @return
	 */
	@PutMapping("/{idx}")
	public String putNotice(@PathVariable(name = "idx") String idx,
							@ModelAttribute("noticeDTO") NoticeDTO noticeDTO,
							@RequestParam("noticeFiles") List<MultipartFile> noticeFiles) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : param = {}, dto = {}, file[0] = {} ", idx, noticeDTO, noticeFiles.get(0));
		
		try {
			if(StringUtil.isLongValue(idx)) {
				//정수일 경우 업데이트 가능
				noticeDTO.setIdx(idx);
				noticeDTO.setModId("testUser");
				noticeService.modifyNotice(noticeDTO);
			} else {
				// 정수가 아닐 경우 다시 정보 페이지로
				return "groupware/notice/notice_detail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "groupware/notice/notice_detail";
		}
		
		//	noticeFiles 안에 들어있는 값을 넣기위한 List 객체 선언
		List<FileVO> fileList = new ArrayList<>();
		//	t_files 테이블에 들어있는 rank_number중 가장 큰 수 가져와서 + 1
		int maxRankNumber = fileService.getMaxRankNumberByTypeAndMajorIdx(FILE_COMMON_TYPE, idx) + 1;
		
		//	파일 업로드 작업
		for(MultipartFile file : noticeFiles) {
			log.info("noticeFiles -> file 값이 있는지 확인 : " + file.getOriginalFilename());
			//	파일을 첨부하지 않았을 때 파일 업로드 작업 중지
			if(!StringUtils.hasLength(file.getOriginalFilename())) {
				break;
			}
			
			try {
				//	setFile 메서드 호출하여 FileVO 리턴 받아 List에 저장
				fileList.add(fileUtil.setFile(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		for(FileVO fileVO : fileList) {
			log.info("fileList -> fileVO 값이 있는지 확인 : " + fileVO.getFileOriginalName());
			
			if(!StringUtils.hasLength(fileVO.getFileOriginalName())) {
				break;
			}
			//	idx 세팅
			fileVO.setMajorIdx(idx);
			//	type 세팅
			fileVO.setType(FILE_COMMON_TYPE);
			//	삭제유무 기본값 N
			fileVO.setIsDeleted("N");
			//	랭크넘버 값 세팅(int 값 String으로 변환)
			fileVO.setRankNumber(String.valueOf(maxRankNumber));
			//	랭크넘버 값 증가
			maxRankNumber++;
			
			fileService.registerFile(fileVO);
		}
		
		// 샘플 정보 페이지 리다이렉트 (GET)방식 
		return "redirect:/groupware/notice/" + idx;
	}
	
	/**
	 * 샘플 삭제 -> 샘플 테이블의 isDeleted = Y 로 변경
	 * <br> Ajax 로 호출하는데 두번씩 호출 되어서 이상한 꼼수로 막은것 같다... 정답을 찾아보자
	 * <br> href(https://api.jquery.com/jQuery.ajax/)
	 * <br> "application/json" || 'application/x-www-form-urlencoded; charset=UTF-8'
	 * @param idx 샘플 테이블 PK
	 * @return
	 */
	@DeleteMapping("/{idx}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> removeNotice(@PathVariable(name = "idx") String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {} ", idx);
		
		Map<String, Object> response = new HashMap<>();
		try {
			if(StringUtil.isLongValue(idx)) {
				noticeService.removeItem(idx);
				
				response.put("status", "success");
				response.put("message", "정상적으로 수정 되었습니다.");
			} else {
				throw new Exception("해당 샘플이 존재하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 해당 상품 조회 실패 시 삭제가 불가능하므로 AJAX 응답을 실패로 전송
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 공지사항 수정폼에서 파일 개별 삭제 요청(AJAX)
	 * @param idx
	 * @return
	 */
	@PostMapping("/fileDelete/{idx}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> removeNoticeFile(@PathVariable(name = "idx") String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		Map<String, Object> response = new HashMap<>();
		try {
			if(StringUtil.isLongValue(idx)) {
				noticeService.removeFile(idx);
				
				response.put("status", "success");
				response.put("message", "삭제 되었습니다.");
			} else {
				throw new Exception("해당 파일이 존재하지 않습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 해당 상품 조회 실패 시 삭제가 불가능하므로 AJAX 응답을 실패로 전송
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		
		return ResponseEntity.ok(response);
		
	}
	
}
