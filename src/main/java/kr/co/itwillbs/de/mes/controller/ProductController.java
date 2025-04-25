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
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;
import kr.co.itwillbs.de.mes.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mes/product")
public class ProductController {

	private final ProductService productService;
	private final CommonCodeUtil commonCodeUtil;
	private final String PATH = "/mes/product";

	// 상품 등록 폼 페이지
	@GetMapping("/new")
	public String productRegisterForm(Model model) {

		// 공통 코드 가져오기
		model.addAttribute("status", commonCodeUtil.getCodeItems("PRODUCT_STATUS"));

		// DTO에 저장
		model.addAttribute("productDTO", new ProductDTO());

		return PATH + "/product_form";
	}

	// 상품 등록 폼 페이지 AJAX용
	@PostMapping(value = { "/new", "/" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	private ResponseEntity<Map<String, Object>> productRegister(@RequestBody @Valid ProductDTO productDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		log.info("requestDTO : {}", StringUtil.objToString(productDTO));

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			productService.registerProduct(productDTO);
			response.put("status", "success");
			response.put("message", "정상적으로 수행 되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
			response.put("message", "정상적으로 수행되지 않았습니다.\n 잠시 후 다시 시도해주시기 바랍니다.");
		}

		return ResponseEntity.ok(response);
	}

	// 상품 목록 조회 (검색)
	@GetMapping("")
	public String getProductList(@ModelAttribute ProductSearchDTO searchDTO, Model model) {
		log.info("getProductList --- start");

		// 페이징 정보 조회
		searchDTO.getPageDTO().setTotalCount(productService.searchProductCount(searchDTO));

		// 상품 목록 조회
		List<ProductDTO> productList = productService.getProductList(searchDTO);
		model.addAttribute("productList", productList);

		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용

		return PATH + "/product_list";
	}

	// 상품 상세 조회
	@GetMapping("/{idx}")
	public String getProduct(@PathVariable("idx") Long idx, Model model) {
		log.info("getProduct --- start");

		// 상품 상세정보 조회
		ProductDTO productDTO = productService.getProductByIdx(idx);
		model.addAttribute("productDTO", productDTO);

		// 공통 코드 가져오기
		model.addAttribute("status", commonCodeUtil.getCodeItems("PRODUCT_STATUS"));

		return PATH + "/product_detail";
	}

	// 상품 수정
	@PutMapping("/updateProduct")
	public ResponseEntity<Map<String, Object>> updateProduct(@RequestBody ProductDTO productDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());

		// 리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		try {
			productService.updateProduct(productDTO);
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
