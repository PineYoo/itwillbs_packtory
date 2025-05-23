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

import kr.co.itwillbs.de.admin.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.admin.dto.MemberDTO;
import kr.co.itwillbs.de.admin.dto.MemberSearchDTO;
import kr.co.itwillbs.de.admin.service.MemberService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/admin/member", name = "멤버 관리")
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
	private final String VIEW_PATH="/admin/member";
	private final String COMMON_MAJOR_CODE_MEMBER_STATUS = "MEMBER_STATUS";
	private final String COMMON_MAJOR_CODE_MEMBER_ROLE = "MEMBER_ROLE";
	private final String COMMON_MAJOR_CODE_DEPARTMENT_CODE = "DEPARTMENT_CODE";
	private final String COMMON_MAJOR_CODE_POSITION_CODE = "POSITION_CODE";
	/**
	 * 어드민 > 멤버관리 > 사용자 등록 -> 직원 조회하기
	 * <br>t_employee 정보들을 조회하여 t_member로 등록하기 위한 폼(GET)
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/newForm"})
	public String memberRegisterForm(@ModelAttribute EmployeeSearchDTO employeeSearchDTO, Model model) {
		LogUtil.logStart(log);
		
		int totalCount = memberService.getBeforeMembersCountByEmployeeSearch(employeeSearchDTO);
		employeeSearchDTO.getPageDTO().setTotalCount(totalCount);
		List<MemberDTO> memberList = totalCount > 0 ? memberService.getBeforeMembersByEmployeeSearch(employeeSearchDTO) : List.of();
		
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		model.addAttribute("memberDTOList", memberList);
		
		// 검색 이름, 사번, 부실명, 직급명, 입사일
		model.addAttribute("departmentItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_DEPARTMENT_CODE));
		model.addAttribute("positionItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_POSITION_CODE));
		
		return VIEW_PATH+"/member_register_form";
	}

	/**
	 * 홈페이지 접속 가능한 멤버 검색 조건 조회
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/newForm/search","/newForm/search/"})
	public String memberRegisterFormToSearch(@ModelAttribute EmployeeSearchDTO employeeSearchDTO, Model model) {
		LogUtil.logStart(log);
		
		int totalCount = memberService.getBeforeMembersCountByEmployeeSearch(employeeSearchDTO);
		employeeSearchDTO.getPageDTO().setTotalCount(totalCount);
		List<MemberDTO> memberList = totalCount > 0 ? memberService.getBeforeMembersByEmployeeSearch(employeeSearchDTO) : List.of();
		
		model.addAttribute("employeeSearchDTO", employeeSearchDTO);
		model.addAttribute("memberDTOList", memberList);
		
		// 검색 이름, 사번, 부실명, 직급명, 입사일
		model.addAttribute("departmentItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_DEPARTMENT_CODE));
		model.addAttribute("positionItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_POSITION_CODE));
		
		return VIEW_PATH+"/member_register_form";
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
		
		//log.info("requestDTO : {}", StringUtil.objToString(memberDTOList));
		log.info("requestDTO : {}", memberDTOList);
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			// 원래 하나였는데 2개로 분리함.
			memberDTOList = memberService.registerMembersPartOne(memberDTOList);
			// AOP 받을 메서드
			memberService.registerMembersPartTwo(memberDTOList);
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
	public String getMembers(@ModelAttribute MemberSearchDTO memberSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 리스트 검색 DTO 뷰에 전달
		memberSearchDTO.getPageDTO().setTotalCount(memberService.getMembersCountBySearchDTO(memberSearchDTO));
		model.addAttribute("memberSearchDTO", memberSearchDTO);
		
		// 공통코드 selectbox용 리스트 뷰에 전달
		model.addAttribute("statusItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_STATUS));
		model.addAttribute("roleItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_ROLE));
		
		// 리스트 결과 DTOlist 뷰에 전달
		model.addAttribute("memberDTOList", memberService.getMembersBySearchDTO(memberSearchDTO));
		
		return VIEW_PATH+"/member_list";
	}
	
	/**
	 * 어드민 > 멤버관리 > 사용자 검색 조건 조회
	 * @param model
	 * @return
	 */
	@GetMapping(value= {"/search","/search/"})
	public String getMembersBySearchDTO(@ModelAttribute MemberSearchDTO memberSearchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 리스트 검색 DTO 뷰에 전달
		memberSearchDTO.getPageDTO().setTotalCount(memberService.getMembersCountBySearchDTO(memberSearchDTO));
		model.addAttribute("memberSearchDTO", memberSearchDTO);
		
		// 공통코드 selectbox용 리스트 뷰에 전달
		model.addAttribute("statusItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_STATUS));
		model.addAttribute("roleItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_ROLE));
		
		// 리스트 결과 DTOlist 뷰에 전달
		model.addAttribute("memberDTOList", memberService.getMembersBySearchDTO(memberSearchDTO));
		return VIEW_PATH+"/member_list";
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
		
		// 공통코드 selectbox용 리스트 뷰에 전달
		model.addAttribute("statusItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_STATUS));
		model.addAttribute("roleItemList", commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_MEMBER_ROLE));
		
		// 조회 결과 뷰에 전달
		MemberDTO memberDTO = new MemberDTO();
		memberDTO = memberService.getMember(memberId);
		model.addAttribute("memberDTO", memberDTO);
		
		return VIEW_PATH+"/member_detail";
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
			return VIEW_PATH+"/member_detail";
		}
		
		return "redirect:"+VIEW_PATH;
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
