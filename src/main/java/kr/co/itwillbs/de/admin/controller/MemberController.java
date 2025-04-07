package kr.co.itwillbs.de.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.admin.dto.MemberDTO;
import kr.co.itwillbs.de.admin.dto.MemberSearchDTO;
import kr.co.itwillbs.de.admin.dto.MemberViewDTO;
import kr.co.itwillbs.de.admin.service.MemberService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/member")
public class MemberController {
/**
 * t_employee 에서 등록한 직원정보로
 * front와 admin을 사용 할수 있는
 * t_member 에 사용자 등록/수정/웹권한을 관리하는 페이지
 */
	private final MemberService memberService; 
	private final CommonCodeUtil commonCodeUtil;
	
	public MemberController(MemberService memberService, CommonCodeUtil commonCodeUtil) {
		this.memberService = memberService;
		this.commonCodeUtil = commonCodeUtil;
	}
	
	// 계속 사용하게 될 클래스 RequestMapping 문자열 값
	private final String MEMBER_PATH="/admin/member";
	private final String COMMON_MAJOR_CODE_MEMBER_STATUS = "member_status";
	private final String COMMON_MAJOR_CODE_MEMBER_ROLE = "member_role";
	/**
	 * 어드민 > 멤버관리 > 사용자 등록 -> 직원 조회하기
	 * <br>t_employee 정보들을 조회하여 t_member로 등록하기 위한 폼(GET)
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/newForm"})
	public String memberRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<MemberDTO> memberList = memberService.getBeforeMembers();
		
		model.addAttribute("memberDTOList", memberList);
		
		return MEMBER_PATH+"/register_form";
	}

	/**
	 * 홈페이지 접속 가능한 멤버 검색 조건 조회
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/newForm/search","/newForm/search/"})
	public String memberRegisterFormToSearch(@ModelAttribute MemberSearchDTO memberSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		
		return MEMBER_PATH+"/member_list";
	}
	
	/**
	 * 어드민 > 멤버관리 > 사용자 등록
	 * <br>t_employee 정보들을 조회하여 t_member로 등록(POST)
	 * @param codeDTO
	 * @param model
	 * @return
	 */
	@PostMapping("/newForm")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> memberRegister(@RequestBody List<MemberDTO> memberDTOList, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		log.info("requestDTO : {}", StringUtil.objToString(memberDTOList));
		log.info("requestDTO : {}", memberDTOList);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			memberService.registerMembers(memberDTOList);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
			//return ResponseEntity.badRequest().body(response);
		}
		
		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아니었나? 하는 기억에 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
	
	/**
	 * 어드민 > 멤버관리 > 사용자 조회
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"","/"})
	public String getMembers(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 일단 공통코드에서 가져와서 뷰에 던지자 이걸로 selectbox만들어서 .. 어케든 해봐야지
		MemberSearchDTO memberSearchDTO = new MemberSearchDTO();
		memberSearchDTO.setCodeItemList(commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_STATUS));
		model.addAttribute("memberSearchDTO", memberSearchDTO);
		
		MemberViewDTO memberViewDTO = new MemberViewDTO();
		memberViewDTO.setMemberDTOList(memberService.getMembers());
		memberViewDTO.setRoleItemList(commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_ROLE));
		memberViewDTO.setStatusItemList(commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_STATUS));
		model.addAttribute("memberViewDTO", memberViewDTO);
		
		
		return MEMBER_PATH+"/list";
	}
	
	/**
	 * 어드민 > 멤버관리 > 사용자 검색 조건 조회
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/search","/search/"})
	public String getMembers(@ModelAttribute MemberSearchDTO memberSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		
		return MEMBER_PATH+"/member_list";
	}
	
	/**
	 * 어드민 > 멤버관리 > 사용자 상세 조회
	 * @param memberId
	 * @param memberSearchDTO
	 * @param model
	 * @return
	 */
	@GetMapping("/{memberId}")
	public String getMember(@PathVariable("memberId") String memberId,
							@ModelAttribute MemberSearchDTO memberSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("request memberId : {}, memberSearchDTO : {}", memberId, memberSearchDTO);
		
		MemberDTO memberDTO = new MemberDTO();
		memberDTO = memberService.getMember(memberId);
		memberDTO.setRoleItemList(commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_ROLE));
		memberDTO.setStatusItemList(commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_STATUS));
		model.addAttribute("memberDTO", memberDTO);
		
		return MEMBER_PATH+"/detail";
	}
	
	/**
	 * 어드민 > 멤버관리 > 사용자 상세 업데이트
	 * @param memberDTO
	 * @return
	 */
	@PutMapping("/{memberId}")
	public String modifyMemberToSubmit(@PathVariable("memberId") String memberId,
									@ModelAttribute MemberDTO memberDTO) {
		log.info("requestBody : {}", memberDTO);
		
		try {
			
			if(StringUtil.isLongValue(memberId)) {
				memberService.modifyMember(memberDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return MEMBER_PATH+"/detail";
		}
		
		return "redirect:"+MEMBER_PATH;
	}
	
	/**
	 * 어드민 > 멤버관리 > 사용자 상세 업데이트
	 * @param memberDTO
	 * @return
	 */
	@PutMapping("modifyMemberToJson")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modifyMemberToJson(@ModelAttribute MemberDTO memberDTO) {
		
		log.info("requestBody : {}", memberDTO);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			memberService.modifyMember(memberDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}
		
		// 비동기 통신 success에 들어가는 것은 HTTP 200||201 이 아니었나? 하는 기억에 리턴 객체 만듦
		return ResponseEntity.ok(response);
	}
}
