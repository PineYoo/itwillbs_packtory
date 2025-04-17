package kr.co.itwillbs.de.mes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseDTO;
import kr.co.itwillbs.de.mes.dto.WarehouseSearchDTO;
import kr.co.itwillbs.de.mes.service.ProductService;
import kr.co.itwillbs.de.mes.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/warehouse")
public class WarehouseController {

	private final ProductService productService;
	private final WarehouseService warehouseService;

	// 창고 등록 폼 페이지
	@GetMapping("/new")
	public String warehouseRegisterForm(Model model) {
		// 로그인 유저 정보 세팅
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			LoginVO loginVO = userDetails.getLoginVO();
			model.addAttribute("userDetails", userDetails);
			model.addAttribute("loginVO", loginVO);
		}

		// 상품 정보 가져와서 WarehouseDTO에 담기
		List<ProductDTO> productList = productService.getProductList();
		model.addAttribute("productList", productList);
		model.addAttribute("warehouseDTO", new WarehouseDTO());

		return "mes/warehouse/warehouse_form";
	}

	// 창고 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> warehouseRegister(@RequestBody @Valid WarehouseDTO warehouseDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(warehouseDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			warehouseService.registerWarehouse(warehouseDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 창고 목록 조회 (검색)
	@GetMapping("")
	public String getWarehouseList(@ModelAttribute WarehouseSearchDTO searchDTO, Model model) {
		log.info("getWarehouseList --- start");

		// 페이징
		searchDTO.getPageDTO().setTotalCount(warehouseService.searchWarehouseCount(searchDTO));

		// 창고 목록 조회
		List<WarehouseDTO> warehouseList = warehouseService.getWarehouseList(searchDTO);
		model.addAttribute("warehouseList", warehouseList);

		// 상품 정보 가져오기
		List<ProductDTO> productList = productService.getProductList();
		model.addAttribute("productList", productList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return "mes/warehouse/warehouse_list";
	}

	// 창고 상세 조회
	@GetMapping("/{idx}")
	public String getWarehouse(@PathVariable("idx") Long idx, Model model) {
		log.info("getWarehouse --- start");

		// 창고 상세정보 조회
		WarehouseDTO warehouseDTO = warehouseService.getWarehouseByIdx(idx);
		model.addAttribute("warehouseDTO", warehouseDTO);
		
		// 상품 정보 가져오기
		List<ProductDTO> productList = productService.getProductList();
		model.addAttribute("productList", productList);

		return "mes/warehouse/warehouse_detail";
	}

	// 창고 수정
	@PutMapping("/{idx}")
	public ResponseEntity<Map<String, Object>> updateWarehouse(@PathVariable("idx") Long idx,
			@RequestBody WarehouseDTO warehouseDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			warehouseService.updateWarehouse(warehouseDTO);
			response.put("status", "success");
			response.put("message", "창고 수정이 완료되었습니다.");
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			log.error("창고 수정 실패: {}", e.getMessage());
			response.put("status", "error");
			response.put("message", "창고를 찾을 수 없습니다.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (Exception e) {
			log.error("창고 수정 실패: {}", e.getMessage());
			response.put("status", "error");
			response.put("message", "창고 수정 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// 창고 삭제 (Soft Delete)
	@DeleteMapping("/{idx}")
	@ResponseBody
	public String deleteWarehouse(@PathVariable("idx") Long idx) {
		try {
			warehouseService.deleteWarehouse(idx);
			return "success";
		} catch (Exception e) {
			log.error("창고 삭제 실패: {}", e.getMessage());
			return "error";
		}
	}
}
