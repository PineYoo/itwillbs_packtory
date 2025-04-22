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
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.RawMaterialDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseTransactionSearchDTO;
import kr.co.itwillbs.de.mes.service.ProductService;
import kr.co.itwillbs.de.mes.service.RawMaterialService;
import kr.co.itwillbs.de.mes.service.WarehouseTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/warehousetransaction")
public class WarehouseTransactionController {

	private final WarehouseTransactionService warehouseTransactionService;
	private final ProductService productService;
	private final RawMaterialService rawMaterialService;
	private final String QC_PATH = "/mes/warehousetransaction";

	// 창고 정보 등록 폼 페이지
	@GetMapping("/new")
	public String warehouseTransactionRegisterForm(Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 상품 목록 조회
		List<ProductDTO> productList = productService.getProductList();
		model.addAttribute("productList", productList);

		// 원자재 목록 조회
		List<RawMaterialDTO> rawMaterialList = rawMaterialService.getRawMaterialList();
		model.addAttribute("rawMaterialList", rawMaterialList);

		model.addAttribute("warehouseTransactionDTO", new WarehouseTransactionDTO());

		return QC_PATH + "/warehousetransaction_form";
	}

	// 창고 정보 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> warehouseTransactionRegister(@RequestBody @Valid WarehouseTransactionDTO warehouseTransactionDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(warehouseTransactionDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			warehouseTransactionService.insertWarehouseTransaction(warehouseTransactionDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 창고 정보 목록 조회 (검색)
	@GetMapping("")
	public String getWarehouseTransactionList(@ModelAttribute WarehouseTransactionSearchDTO searchDTO, Model model) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 페이징
		searchDTO.getPageDTO().setTotalCount(warehouseTransactionService.searchWarehouseTransactionCount(searchDTO));

		// 창고 정보 목록 조회
		List<WarehouseTransactionDTO> warehouseTransactionList = warehouseTransactionService.searchWarehouseTransaction(searchDTO);
		model.addAttribute("warehouseTransactionList", warehouseTransactionList);

		// 상품 목록 조회
		List<ProductDTO> productList = productService.getProductList();
		model.addAttribute("productList", productList);

		// 원자재 목록 조회
		List<RawMaterialDTO> rawMaterialList = rawMaterialService.getRawMaterialList();
		model.addAttribute("rawMaterialList", rawMaterialList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return QC_PATH + "/warehousetransaction_list";
	}

	// 창고 정보 상세 조회
	@GetMapping("/{idx}")
	public String getWarehouseTransaction(@PathVariable("idx") Long idx, Model model) {
		log.info("{}---start, request param {}", Thread.currentThread().getStackTrace()[1].getMethodName(), idx);

		// 창고 정보 상세정보 조회
		WarehouseTransactionDTO warehouseTransactionDTO = warehouseTransactionService.getWarehouseTransactionByIdx(idx);
		model.addAttribute("warehouseTransactionDTO", warehouseTransactionDTO);

		// 상품 목록 조회
		List<ProductDTO> productList = productService.getProductList();
		model.addAttribute("productList", productList);

		// 원자재 목록 조회
		List<RawMaterialDTO> rawMaterialList = rawMaterialService.getRawMaterialList();
		model.addAttribute("rawMaterialList", rawMaterialList);

		return QC_PATH + "/warehousetransaction_detail";
	}

	// 창고 정보 수정
	@PutMapping("/updateWarehouseTransaction")
	public ResponseEntity<Map<String, Object>> updateWarehouseTransaction(
			@RequestBody WarehouseTransactionDTO warehouseTransactionDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			warehouseTransactionService.updateWarehouseTransaction(warehouseTransactionDTO);
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

	// 창고 정보 삭제 (Soft Delete)
	@DeleteMapping("/{idx}")
	@ResponseBody
	public String deleteWarehouseTransaction(@PathVariable("idx") Long idx) {
		try {
			warehouseTransactionService.deleteWarehouseTransaction(idx);
			return "success";
		} catch (Exception e) {
			log.error("창고 정보 삭제 실패: {}", e.getMessage());
			return "error";
		}
	}
}
