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
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsDTO;
import kr.co.itwillbs.de.mes.dto.WorkerMetricsSearchDTO;
import kr.co.itwillbs.de.mes.service.WorkerMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/workermetrics")
public class WorkerMetricsController {

	private final WorkerMetricsService workerMetricsService;
	private final String PATH = "/mes/workermetrics";

	// 보유 자격증 정보 등록 폼 페이지
	@GetMapping("/new")
	public String workerMetricsRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		model.addAttribute("workerMetricsDTO", new WorkerMetricsDTO());

		return PATH + "/workermetrics_form";
	}

	// 보유 자격증 정보 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> workerMetricsRegister(@RequestBody @Valid WorkerMetricsDTO workerMetricsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(workerMetricsDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			workerMetricsService.insertWorkerMetrics(workerMetricsDTO);
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
	public String getLocationInfoList(@ModelAttribute WorkerMetricsSearchDTO searchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 페이징
		searchDTO.getPageDTO().setTotalCount(workerMetricsService.searchWorkerMetricsCount(searchDTO));

		// 보유 자격증 정보 목록 조회
		List<WorkerMetricsDTO> workerMetricsList = workerMetricsService.searchWorkerMetrics(searchDTO);
		model.addAttribute("workerMetricsList", workerMetricsList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return PATH + "/workermetrics_list";
	}

	// 보유 자격증 정보 상세 조회
	@GetMapping("/{idx}")
	public String getWorkerMetrics(@PathVariable("idx") Long idx, Model model) {
		log.info("{}---start, request param {}", Thread.currentThread().getStackTrace()[1].getMethodName(), idx);

		// 보유 자격증 정보 상세정보 조회
		WorkerMetricsDTO workerMetricsDTO = workerMetricsService.getWorkerMetricsByIdx(idx);
		model.addAttribute("workerMetricsDTO", workerMetricsDTO);

		return PATH + "/workermetrics_detail";
	}

	// 보유 자격증 정보 수정
	@PutMapping("/updateWorkerMetrics")
	public ResponseEntity<Map<String, Object>> updateWorkerMetrics(@RequestBody WorkerMetricsDTO workerMetricsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			workerMetricsService.updateWorkerMetrics(workerMetricsDTO);
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
