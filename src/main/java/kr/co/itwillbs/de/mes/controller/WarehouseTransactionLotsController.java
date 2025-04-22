package kr.co.itwillbs.de.mes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionLotsSearchDTO;
import kr.co.itwillbs.de.mes.service.WarehouseTransactionLotsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/warehousetransactionlots")
public class WarehouseTransactionLotsController {

	private final WarehouseTransactionLotsService warehouseTransactionLotsService;
	private final String QC_PATH = "/mes/warehousetransactionlots";

	// 트랜잭션 LOT 등록 폼 페이지
	@GetMapping("/new")
	public String warehouseTransactionLotsRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		model.addAttribute("warehouseTransactionLotsDTO", new WarehouseTransactionLotsDTO());

		return QC_PATH + "/warehousetransactionlots_form";
	}

	// 트랜잭션 LOT 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> warehouseTransactionLotsRegister(@RequestBody @Valid WarehouseTransactionLotsDTO warehouseTransactionLotsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(warehouseTransactionLotsDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			warehouseTransactionLotsService.insertWarehouseTransactionLots(warehouseTransactionLotsDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 트랜잭션 LOT 목록 조회 (검색)
	@GetMapping("")
	public String getWarehouseTransactionLotsList(@ModelAttribute WarehouseTransactionLotsSearchDTO searchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 페이징
		searchDTO.getPageDTO().setTotalCount(warehouseTransactionLotsService.searchWarehouseTransactionLotsCount(searchDTO));

		// 트랜잭션 LOT 목록 조회
		List<WarehouseTransactionLotsDTO> transactionLotsList = warehouseTransactionLotsService.searchWarehouseTransactionLots(searchDTO);
		model.addAttribute("transactionLotsList", transactionLotsList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return QC_PATH + "/warehousetransactionlots_list";
	}

	// 트랜잭션 LOT 상세 조회
	@GetMapping("/{idx}")
	public String getWarehouseTransactionLots(@PathVariable("idx") Long idx, Model model) {
		log.info("{}---start, request param {}", Thread.currentThread().getStackTrace()[1].getMethodName(), idx);

		// 트랜잭션 LOT 상세정보 조회
		WarehouseTransactionLotsDTO transactionLotDTO = warehouseTransactionLotsService.getWarehouseTransactionLotsByIdx(idx);
		model.addAttribute("transactionLotDTO", transactionLotDTO);

		return QC_PATH + "/warehousetransactionlots_detail";
	}

	// 트랜잭션 LOT 수정
	@PutMapping("/updateWarehouseTransactionLots")
	public ResponseEntity<Map<String, Object>> updateWarehouseTransactionLots(@RequestBody WarehouseTransactionLotsDTO warehouseTransactionLotsDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			warehouseTransactionLotsService.updateWarehouseTransactionLots(warehouseTransactionLotsDTO);
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

	// 트랜잭션 LOT 삭제 (Soft Delete)
	@DeleteMapping("/{idx}")
	@ResponseBody
	public String deleteWarehouseTransactionLots(@PathVariable("idx") Long idx) {
		try {
			warehouseTransactionLotsService.deleteWarehouseTransactionLots(idx);
			return "success";
		} catch (Exception e) {
			log.error("트랜잭션LOT 삭제 실패: {}", e.getMessage());
			return "error";
		}
	}
}
