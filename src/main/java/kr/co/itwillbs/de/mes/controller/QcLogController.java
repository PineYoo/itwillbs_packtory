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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcLogFormDTO;
import kr.co.itwillbs.de.mes.dto.QcLogSearchDTO;
import kr.co.itwillbs.de.mes.dto.QcRequiredLogDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionSearchDTO;
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
	private final CommonCodeUtil commonCodeUtil;
	private final String QC_PATH = "/mes/qclog";

	// 품질로그 등록 폼 페이지
	@GetMapping("/new")
	public String qcLogRegisterForm(Model model) {
		LogUtil.logStart(log);

		// 공통코드 + 품질 기준
		model.addAttribute("qcResult", commonCodeUtil.getCodeItems("QC_RESULT"));
		model.addAttribute("itemUnit", commonCodeUtil.getCodeItems("ITEM_UNIT"));
		model.addAttribute("qcStandaradList", qcStandardService.getQcStandardList());

		model.addAttribute("qcLogDTO", new QcLogDTO());

		return QC_PATH + "/qclog_form";
	}

	// 품질로그 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> qcLogRegister(@RequestBody @Valid QcLogDTO qcLogDTO) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requestDTO : {}", StringUtil.objToString(qcLogDTO));

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
		LogUtil.logStart(log);

		// 페이징
		searchDTO.getPageDTO().setTotalCount(qcLogService.searchQcLogCount(searchDTO));

		// 품질로그 목록 조회
		List<QcLogDTO> qcLogList = qcLogService.searchQcLog(searchDTO);
		model.addAttribute("qcLogList", qcLogList);

		// 공통코드 + 품질 기준
		model.addAttribute("qcStandaradList", qcStandardService.getQcStandardList());
		model.addAttribute("qcResult", commonCodeUtil.getCodeItems("QC_RESULT"));

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return QC_PATH + "/qclog_list";
	}

	// 품질로그 상세 조회
	@GetMapping("/{idx}")
	public String getQcLog(@PathVariable("idx") Long idx, Model model) {
		LogUtil.logStart(log);

		// 품질로그 상세정보 조회
		QcLogDTO qcLogDTO = qcLogService.getQcLogByIdx(idx);
		model.addAttribute("qcLogDTO", qcLogDTO);

		// 공통코드 + 품질 기준
		model.addAttribute("qcResult", commonCodeUtil.getCodeItems("QC_RESULT"));
		model.addAttribute("itemUnit", commonCodeUtil.getCodeItems("ITEM_UNIT"));
		model.addAttribute("qcStandaradList", qcStandardService.getQcStandardList());

		return QC_PATH + "/qclog_detail";
	}

	// 품질로그 수정
	@PutMapping("/updateQcLog")
	public ResponseEntity<Map<String, Object>> updateQcLog(@RequestBody QcLogDTO qcLogDTO) {
		LogUtil.logStart(log);

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
	
	// =======================================품질 검사 조회 , 등록, 수정?
	
	/**
	 * 창고 품질 검사 대기 목록 조회
	 * @param status
	 * @param model
	 * @return
	 */
	@GetMapping("/required/{status}")
	public String getRequiredQCListByStatus(@PathVariable(name="status") String status,
											@ModelAttribute WarehouseTransactionSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);
		
		// status 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(status)) {
			return "redirect:/main"; // 일단 메인으로 던지자
		}
		// 값 init
		searchDTO.setStatus(status);
		
		// 페이징 카운트, 리스트 가져오기
		int totalCount = qcLogService.getRequiredQCCount(searchDTO);
		searchDTO.getPageDTO().setTotalCount(totalCount);
		List<WarehouseTransactionDTO> list = totalCount > 0
									? qcLogService.getRequiredQCListBySearchDTO(searchDTO) : List.of();
		
		// viewResolver 에 전달 할 model
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("requiredList", list);
		
		return QC_PATH + "/required_list";
	}
	
	
	/**
	 * 품질검사 대기 자재/상품의 품질 작성 페이지(리스트형)
	 * @param idx t_warehouse_transaction.idx > 자재에 따라 검사 해야하는 품질검사기준의 목록을 가져와 작성화면을 만듬
	 * @param status 1: 입고, 5: 출고
	 * @param model
	 * @return
	 */
	@GetMapping("/group/new/{idx}")
	public String qcLogRegisterFormByIdx(@PathVariable(name="idx") String idx,
										@RequestParam(name="status", required = false) String status, Model model) {
		LogUtil.logStart(log);

		// idx 값이 숫자가 아닐 때 리스트로 리다이렉트
		if(!StringUtil.isLongValue(idx)) {
			return "redirect:/main"; // 일단 메인으로 던지자
		}
		
		// 공통코드 + 품질 기준
		QcLogFormDTO formDTO = new QcLogFormDTO();
		formDTO.setStandardList(qcLogService.selectQcStandardGroupByIdx(idx));
		
		// viewResolver 에 전달 할 model
		formDTO.setIdx(idx);
		formDTO.setViewStatus(status);
		model.addAttribute("formDTO", formDTO);

		return QC_PATH + "/qclog_group_form";
	}
	
	@PostMapping(value="/group/new", consumes= {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setQCLogRegisterForm(@RequestBody QcLogFormDTO formDTO) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "requried data: {}", formDTO.getQcList());
		
		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			// qc검사 서비스로 보내기
			qcLogService.validatingQCs(formDTO);
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
	
	@GetMapping("/required/list")
	public String getQCRequiredLogList(@ModelAttribute WarehouseTransactionSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);
		
		// 페이징 카운트, 리스트 가져오기
		int totalCount = qcLogService.getRequiredQCLogCountBySearchDTO(searchDTO);
		searchDTO.getPageDTO().setTotalCount(totalCount);
		List<QcRequiredLogDTO> list = totalCount > 0
									? qcLogService.getRequiredQCLogListBySearchDTO(searchDTO) : List.of();
		
		// viewResolver 에 전달 할 model
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("requiredList", list);
		
		return QC_PATH + "/required_log_list";
	}
}
