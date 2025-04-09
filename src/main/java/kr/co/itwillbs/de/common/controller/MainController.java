package kr.co.itwillbs.de.common.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.commute.dto.CommuteDTO;
import kr.co.itwillbs.de.commute.service.CommuteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@Autowired
	private CommuteService commuteService;
	@Autowired
	private CommonCodeUtil commonCodeUtil;
	
	private final String COMMON_MAJOR_CODE_TRADE = "WORK_STATUS_CODE";
	/**
	 * 브라우저 주소창에 Host:port 만 입력 했을 때 들어오는 페이지 <br>
	 * 예시)localhost:8080 또는 localhost:8080/ 둘다 index.html 로 연결 시켜주기 위함 <br>
	 * ""만 있어도 가능하나, 고객사들이 "/" 남겨놓고 왜 안되냐는 VOC가 많아 PTSD적으로 작성하는 버릇이 있음
	 * 
	 * @return
	 */
	@GetMapping(value = { "", "/" })
	public String mainView() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		return "/index";
	}

	/**
	 * 검색로봇에게 사이트 및 웹페이지를 수집할 수 있도록 허용하거나 제한하는 국제 권고안 <br>
	 * robots.txt 에 대해서 (href:
	 * https://searchadvisor.naver.com/guide/seo-basic-robots) <br>
	 * (href:
	 * https://jihyunhillcs.tistory.com/39#Spring_Boot_%EC%97%90%EC%84%9C_%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)
	 * <br>
	 * TODO 25.03.23 이 방식은 SpringMVC에서 쓰던 방법인데 Boot용으로 설정이 편리하다면 바꿀 예정
	 * 
	 * @return
	 */
	@GetMapping(value = { "/robots.txt", "/robot.txt" })
	@ResponseBody
	public String robots() {
		return "User-agent: *\n" + "Allow: /\n" + "Disallow: /files/*\n" + // 업로드한 파일 수집X
				"Disallow: /admin/*\n"; // 어드민 수집X
	}

	@GetMapping("/test/draft")
	public String test() {
		return "groupware/approval/draft_form.html";
	}

	/**
	 * 로그인
	 * 
	 * @return
	 */
	@GetMapping(value = { "/login", "/login/" })
	public String loginForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		model.addAttribute("loginVO", new LoginVO());

		return "main/login_form";
	}

	/**
	 * 로그인 > 성공 > 메인
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = { "/main", "/main/" })
	public String packtoryMainView(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			LoginVO loginVO = userDetails.getLoginVO();
			String memberId = userDetails.getUsername(); // 이렇게 자유롭게 사용 가능!
			log.info("userDetails is {}", userDetails);
			log.info("loginVO is {}", loginVO);
			model.addAttribute("userDetails", userDetails);
			model.addAttribute("loginVO", loginVO);
		}

		// 출퇴근 기록 코드
		List<CodeItemDTO> codeItemList = commonCodeUtil.getCodeItems(COMMON_MAJOR_CODE_TRADE);
		log.info("codeItemList : " + codeItemList);
		model.addAttribute("codeItemList", codeItemList);
		
		// ------------------------------
		// 세션 아이디(사번) 불러오는 코드
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String id = userDetails.getUsername();
        // ------------------------------
		
		// 로그인한 사번의 오늘 출퇴근 기록 조회
		LocalDate today = LocalDate.now();
		
		// 오늘 출근 기록
		CommuteDTO checkInRecord = commuteService.getCheckInTime(id, today);
		CommuteDTO checkOutRecord = commuteService.getCheckOutTime(id, today);
		
		// 마지막 기록
		CommuteDTO lastCommuteRecord = commuteService.getTodayLastCommute(id, today);
		
		log.info("checkInTime : " + checkInRecord);
		model.addAttribute("checkInTime", checkInRecord != null ? checkInRecord.getRegDate().toLocalTime() : null);	// 오늘 출근기록 있을 경우 출근시간
		model.addAttribute("checkOutTime", checkOutRecord != null ? checkOutRecord.getRegDate().toLocalTime() : null);	// 오늘 퇴근기록 있을 경우 퇴근시간
		log.info("lastCommuteRecord : " + lastCommuteRecord);	// 오늘 출퇴근 마지막 기록
		model.addAttribute("lastCommuteRecord", lastCommuteRecord);	// 오늘 출퇴근 마지막 기록
		
		return "/main/main";
	}

}
