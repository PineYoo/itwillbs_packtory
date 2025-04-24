package kr.co.itwillbs.de.mes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
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

import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleDTO;
import kr.co.itwillbs.de.mes.dto.WorkerScheduleSearchDTO;
import kr.co.itwillbs.de.mes.service.LocationInfoService;
import kr.co.itwillbs.de.mes.service.WorkerScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/workerschedule")
public class WorkerSchedulesController {

	private final WorkerScheduleService workerScheduleService;
	private final LocationInfoService locationInfoService;
	private final CommonCodeUtil commonCodeUtil;
	private final String PATH = "/mes/workerschedule";

	// 보유 자격증 정보 등록 폼 페이지
	@GetMapping("/new")
	public String workerScheduleRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 공통코드 + 장소
		model.addAttribute("shiftType", commonCodeUtil.getCodeItems("SHIFT_TYPE"));
		model.addAttribute("locationList", locationInfoService.getLocationInfoList());

		model.addAttribute("workerScheduleDTO", new WorkerScheduleDTO());

		return PATH + "/workerschedule_form";
	}

	// 보유 자격증 정보 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> workerScheduleRegister(
			@RequestBody @Valid WorkerScheduleDTO workerScheduleDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(workerScheduleDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			workerScheduleService.insertWorkerSchedule(workerScheduleDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 보유 자격증 정보 목록 조회 (검색)
	@GetMapping("")
	public String getWorkerScheduleList(@ModelAttribute WorkerScheduleSearchDTO searchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 페이징
		searchDTO.getPageDTO().setTotalCount(workerScheduleService.searchWorkerScheduleCount(searchDTO));

		// 보유 자격증 정보 목록 조회
		List<WorkerScheduleDTO> workerScheduleList = workerScheduleService.searchWorkerSchedule(searchDTO);
		model.addAttribute("workerScheduleList", workerScheduleList);

		model.addAttribute("locationList", locationInfoService.getLocationInfoList());

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return PATH + "/workerschedule_list";
	}

	// 보유 자격증 정보 상세 조회
	@GetMapping("/{idx}")
	public String getWorkerSchedule(@PathVariable("idx") Long idx, Model model) {
		log.info("{}---start, request param {}", Thread.currentThread().getStackTrace()[1].getMethodName(), idx);

		// 보유 자격증 정보 상세정보 조회
		WorkerScheduleDTO workerScheduleDTO = workerScheduleService.getWorkerScheduleByIdx(idx);
		model.addAttribute("workerScheduleDTO", workerScheduleDTO);

		// 공통코드 + 장소
		model.addAttribute("shiftType", commonCodeUtil.getCodeItems("SHIFT_TYPE"));
		model.addAttribute("locationList", locationInfoService.getLocationInfoList());

		return PATH + "/workerschedule_detail";
	}

	// 보유 자격증 정보 수정
	@PutMapping("/updateWorkerSchedule")
	public ResponseEntity<Map<String, Object>> updateWorkerSchedule(@RequestBody WorkerScheduleDTO workerScheduleDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			workerScheduleService.updateWorkerSchedule(workerScheduleDTO);
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
