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
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcLogSearchDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;
import kr.co.itwillbs.de.mes.service.QcLogService;
import kr.co.itwillbs.de.mes.service.QcStandardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/qclog")
public class QcLogController {

	private final QcLogService qcLogService;
	private final QcStandardService qcStandardService;
	private final String QC_PATH = "/mes/qclog";

	// 품질로그 등록 폼 페이지
	@GetMapping("/new")
	public String qcLogRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 품질기준 목록 조회
		List<QcStandardDTO> qcStandaradList = qcStandardService.getQcStandardList();
		model.addAttribute("qcStandaradList", qcStandaradList);

		model.addAttribute("qcLogDTO", new QcLogDTO());

		return QC_PATH + "/qclog_form";
	}

	// 품질로그 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> qcLogRegister(@RequestBody @Valid QcLogDTO qcLogDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(qcLogDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			qcLogService.insertQcLog(qcLogDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 품질로그 목록 조회 (검색)
	@GetMapping("")
	public String getQcLogList(@ModelAttribute QcLogSearchDTO searchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 페이징
		searchDTO.getPageDTO().setTotalCount(qcLogService.searchQcLogCount(searchDTO));

		// 품질 목록 조회
		List<QcLogDTO> qcLogList = qcLogService.searchQcLog(searchDTO);
		model.addAttribute("qcLogList", qcLogList);
		
		// 품질기준 목록 조회
		List<QcStandardDTO> qcStandaradList = qcStandardService.getQcStandardList();
		model.addAttribute("qcStandaradList", qcStandaradList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return QC_PATH + "/qclog_list";
	}

	// 품질로그 상세 조회
	@GetMapping("/{idx}")
	public String getQcLog(@PathVariable("idx") Long idx, Model model) {
		log.info("{}---start, request param {}", Thread.currentThread().getStackTrace()[1].getMethodName(), idx);

		// 품질로그 상세정보 조회
		QcLogDTO qcLogDTO = qcLogService.getQcLogByIdx(idx);
		model.addAttribute("qcLogDTO", qcLogDTO);

		// 품질기준 목록 조회
		List<QcStandardDTO> qcStandaradList = qcStandardService.getQcStandardList();
		model.addAttribute("qcStandaradList", qcStandaradList);

		return QC_PATH + "/qclog_detail";
	}

	// 품질로그 수정
	@PutMapping("/updateQcLog")
	public ResponseEntity<Map<String, Object>> updateQcLog(@RequestBody QcLogDTO qcLogDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			qcLogService.updateQcLog(qcLogDTO);
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
