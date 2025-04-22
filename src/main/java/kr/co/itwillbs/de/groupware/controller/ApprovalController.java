package kr.co.itwillbs.de.groupware.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.FileUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.common.vo.FileVO;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.groupware.dto.ApprovalDTO;
import kr.co.itwillbs.de.groupware.dto.ApprovalSearchDTO;
import kr.co.itwillbs.de.groupware.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;

/* 전자결재 */
@Slf4j
@RequestMapping(value={"/groupware/approval"})
@Controller
public class ApprovalController {
	@Autowired
	private ApprovalService approvalService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;
	
	@Autowired
	private FileUtil fileUtil;
	@Autowired
	private FileService fileService;
	
	//	전자결재 공통코드
	private final String COMMON_MAJOR_CODE_APPROVAL_TYPE = "APPROVAL_TYPE";
	private final String COMMON_MAJOR_CODE_PROGRESS_TYPE = "PROGRESS_STATUS";
//	private final String COMMON_MAJOR_CODE_DOC_TYPE = "FORM_TEMPLATE";
	private final String FILE_COMMON_TYPE = "T_APPROVAL";
	
	// 전자결재 페이지 매핑
	@GetMapping(value={"","/"})
	public String approvalForm(@ModelAttribute ApprovalSearchDTO approvalSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		//	------------------------------------------
		// 세션 아이디(사번) 불러오는 코드
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			String memberId = userDetails.getUsername();
			approvalSearchDTO.setDrafterId(memberId);
			//	--------------------------------------------

			// 전자결재 리스트 조회(로그인 한 회원 기준)
			List<ApprovalDTO> approvalDTOList = approvalService.getApprovalList(approvalSearchDTO);
			//페이징용 totalCount
			approvalSearchDTO.getPageDTO().setTotalCount(approvalService.getApprovalCountBySearchDTO(approvalSearchDTO));
			
			model.addAttribute("progressStatusList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_PROGRESS_TYPE));	// 전자결재 진행상태
			model.addAttribute("approvalTypeList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_APPROVAL_TYPE));	// 전자결재 타입
			model.addAttribute("approvalDTOList", approvalDTOList);
		}
		
		return "groupware/approval/approval_list";
	}
	// ==================================================================================================================================
	// 기안서 작성 페이지 매핑 (GET)
	@GetMapping(value={"/regist"})
	public String apporvalRegisterForm(@ModelAttribute ApprovalDTO approvalDTO, Model model) {
//	public String apporvalRegisterForm(@RequestParam("userId") String userId, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		//	------------------------------------------------------------
		// 세션 아이디(사번) 불러오는 코드
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		LoginVO loginVO = userDetails.getLoginVO();
		//	------------------------------------------------------------
		
		// 기안자 정보 set(id, name, departmentName, positionName)
		approvalDTO.setDrafterId(loginVO.getId());
		approvalDTO.setName(loginVO.getName());
		approvalDTO.setDepartmentName(loginVO.getDepartmentName());
		approvalDTO.setPositionName(loginVO.getPositionName());
		
		//	공통코드 가져와서 approval_type 넣기
		List<CodeItemDTO> approvalTypeList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_APPROVAL_TYPE);
		log.info("approvalType : " + approvalTypeList.toString());
		
		model.addAttribute("approvalDTO", approvalDTO);
		model.addAttribute("approvalTypeList", approvalTypeList);
		return "groupware/approval/approval_reg_form";
	}
	
	// ==================================================================================================================================
	/**
	 * 전자결재 문서 등록(INSERT)을 하는 "/groupware/regist" 연결(POST)
	 * @param approvalDTO
	 * @param approvalFiles
	 * @param bindingResult
	 * @param model
	 * @return
	 * @throws JsonProcessingException
	 */
//	@PostMapping(value={"/regist"})
	@Deprecated
	public String approvalRegister(
			@ModelAttribute("approvalDTO") @Valid ApprovalDTO approvalDTO,
			@RequestParam("approvalFiles") List<MultipartFile> approvalFiles,
			BindingResult bindingResult,
			Model model) throws JsonProcessingException {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		System.out.println("입력한 정보 : " +  approvalDTO);
		
		// 유효성 체크
//		if(bindingResult.hasErrors()) {
//			return "approval/approval_reg_form";
//		}
		
		//	기안서 등록 요청
		String approvalNo = approvalService.registerApproval(approvalDTO);
		
		//	approvalFiles 안에 들어있는 값을 넣기위한 List 객체 선언
		List<FileVO> fileList = new ArrayList<>();
		//	파일 랭크넘버용
		int rankNumber = 1;
		
		for(MultipartFile file : approvalFiles) {
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
			//	문서번호 세팅
			fileVO.setMajorIdx(approvalNo);
			//	type 세팅
			fileVO.setType("t_approval");
			//	삭제유무 기본값 N
			fileVO.setIsDeleted("N");
			//	랭크넘버 세팅(int 값 String으로 변환)
			fileVO.setRankNumber(String.valueOf(rankNumber));
			//	랭크넘버 값 증가
			rankNumber++;
			
			fileService.registerFile(fileVO);
		}
		
		//	전자결재 리스트페이지 리다이렉트
		return "redirect:/groupware/approval";
		
	}
	
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * 전자결재 문서 등록(파일 포함, AJAX용) 25.04.22
	 * @param approvalDTO
	 * @param approvalFiles
	 * @param bindingResult
	 * @return JSON 응답
	 */
//	@PostMapping(value = "/regist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PostMapping(value={"/regist"})
	@ResponseBody
	public ResponseEntity<Map<String, Object>> approvalRegisterForJson(
			@RequestBody @Valid ApprovalDTO approvalDTO,
//	        @RequestParam("approvalFiles") List<MultipartFile> approvalFiles,
	        BindingResult bindingResult) {
	    log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
	    log.info("입력한 정보 : {}", approvalDTO);

		//리턴 객체 생성
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // 1. 기안서 등록
	        String approvalNo = approvalService.registerApproval(approvalDTO);

	        // 2. 파일 처리 - 보류 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	        List<FileVO> fileList = new ArrayList<>();
//	        int rankNumber = 1;
//
//	        for (MultipartFile file : approvalFiles) {
//	            if (!StringUtils.hasLength(file.getOriginalFilename())) continue;
//
//	            try {
//	                FileVO fileVO = fileUtil.setFile(file);
//	                fileVO.setMajorIdx(approvalNo);
//	                fileVO.setType("t_approval");
//	                fileVO.setIsDeleted("N");
//	                fileVO.setRankNumber(String.valueOf(rankNumber++));
//	                fileService.registerFile(fileVO);
//	            } catch (Exception e) {
//	                log.error("파일 처리 실패", e);
//	            }
//	        }

	        // 성공 응답
	        response.put("status", "success");
	        response.put("message", "등록이 완료되었습니다.");
//	        response.put("approvalNo", approvalNo);

	    } catch (Exception e) {
	        log.error("전자결재 등록 실패", e);
	        response.put("status", "fail");
	        response.put("message", "오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
	    }

	    return ResponseEntity.ok(response);
	}
	// ==================================================================================================================================
	/**
	 * 단일 전자결재 조회(SELECT) "/approval/{idx}" 주소 매핑(GET)
	 * @param idx 샘플 테이블 PK값
	 * @param model
	 * @return
	 */
	@GetMapping("/{docNo}")
	public String getApproval(@PathVariable(name = "docNo") String docNo, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("idx 넘어오는거 확인" + docNo);
		
		ApprovalDTO approvalDTO = approvalService.getApprovalByDocNo(docNo);
		//	결재번호를 기준으로 문서 정보 가져오기
		model.addAttribute("approvalDTO", approvalDTO);
		//	결재번호를 기준으로 첨부된 파일 가져오기
//		model.addAttribute("fileList", fileService.getFilesByTypeAndMajorIdx(FILE_COMMON_TYPE, docNo));
		//	기안서, 품위서 등 결재유형 가져오기
		model.addAttribute("approvalTypeList" ,commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_APPROVAL_TYPE));
		
		return "groupware/approval/approval_detail";
	}
	
	/**
	 * 전자결재(UPDATE)를 요청하는 "/groupware/approval/{approvalNo}" 주소 매핑(PUT)
	 * @param approvalNo
	 * @param approvalDTO
	 * @param approvalFiles
	 * @return
	 */
	@PutMapping("/{docNo}")
	public String putApproval(@PathVariable(name = "docNo") String docNo,
//							@RequestParam("approvalFiles") List<MultipartFile> approvalFiles,
							@ModelAttribute("approvalDTO") ApprovalDTO approvalDTO
							) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
//		log.info("requestData : param = {}, dto = {}, file[0] = {} ", docNo, approvalDTO, approvalFiles.get(0));
		log.info("requestData : param = {}, dto = {}, file[0] = {} ", docNo, approvalDTO);
		
		try {
			approvalService.modifyApproval(approvalDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return "groupware/approval/approval_detail";
		}
		
		//	approvalFiles 안에 들어있는 값을 넣기위한 List 객체 선언
//		List<FileVO> fileList = new ArrayList<>();
//		int maxRankNumber = fileService.getMaxRankNumberByTypeAndMajorIdx(FILE_COMMON_TYPE, docNo) + 1;
//		
//		for(MultipartFile file : approvalFiles) {
//			if(!StringUtils.hasLength(file.getOriginalFilename())) {
//				break;
//			}
//			
//			try {
//				//	setFile 메서드 호출하여 FileVO 리턴 받아 List에 저장
//				fileList.add(fileUtil.setFile(file));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		for(FileVO fileVO : fileList) {
//			//	문서번호 세팅
//			fileVO.setMajorIdx(docNo);
//			//	type 세팅
//			fileVO.setType(FILE_COMMON_TYPE);
//			//	삭제유무 기본값 N
//			fileVO.setIsDeleted("N");
//			//	랭크넘버 세팅(int 값 String으로 변환)
//			fileVO.setRankNumber(String.valueOf(maxRankNumber));
//			//	랭크넘버 값 증가
//			maxRankNumber++;
//			
//			fileService.registerFile(fileVO);
//		}
		
		return "redirect:/groupware/approval";
	}
	
	/**
	 * 전자결재 검색 조건 조회
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/search")
	public String getApprovalSearchList(@ModelAttribute ApprovalSearchDTO approvalSearchDTO, Model model) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestData : {} ", approvalSearchDTO.toString());
		
		//	------------------------------------------------------
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String memberId = userDetails.getUsername();
		approvalSearchDTO.setDrafterId(memberId);
		//	------------------------------------------------------
		//	페이지용 카운트 세기
		approvalSearchDTO.getPageDTO().setTotalCount(approvalService.getApprovalCountBySearchDTO(approvalSearchDTO));
		model.addAttribute("noticeSearchDTO", approvalSearchDTO);
		//	결재 진행상태 값 List 넘기기
		model.addAttribute("progressStatusList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_PROGRESS_TYPE));
		//	문서 타입 값 List 넘기기
		model.addAttribute("approvalTypeList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_APPROVAL_TYPE));
		List<ApprovalDTO> approvalDTOList = approvalService.getApprovalList(approvalSearchDTO);
		
		// 조회 결과 값 뷰에 전달
		model.addAttribute("approvalDTOList", approvalDTOList);
		
		log.info("noticeDTOList : {} ", approvalSearchDTO.toString());
		
		
		return "groupware/approval/approval_list";
	}
	
	/**
	 * 전자결재 상세 페이지에서 파일 개별 삭제 요청(AJAX)
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
				approvalService.removeFile(idx);
				
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
	
	// 검색바
	@PostMapping("/filter")
	@ResponseBody
//	public List<ApprovalDTO> approvalFilter(@RequestParam("filter") String filter, @RequestParam("memberId") String memberId) {
	public List<ApprovalDTO> approvalFilter(@ModelAttribute ApprovalSearchDTO approvalSearchDTO, @RequestParam("filter") String filter) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String memberId = userDetails.getUsername();
		approvalSearchDTO.setDrafterId(memberId);
		log.info(">>>>>>>>>>>>>>>>>>>>>>filter 값 : {}, memberID 값 : {}", filter, memberId);
		
		
		List<ApprovalDTO> approvalDTOList = new ArrayList<>();
		switch (filter) {
		case "all": 
			approvalDTOList = approvalService.getApprovalList(approvalSearchDTO);
			break;
			
		case "drafted":
		case "waiting":
		case "completed":
			approvalDTOList = approvalService.getApprovalListByFilter(filter, memberId);
		}
		log.info("approvalDTOList : {} " + approvalDTOList.toString());
		return approvalDTOList;
	}
	
}
