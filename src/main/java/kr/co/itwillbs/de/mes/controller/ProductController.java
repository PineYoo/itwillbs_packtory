package kr.co.itwillbs.de.mes.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.EntityNotFoundException;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
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

	// 상품 등록 폼 페이지
	@GetMapping("/new")
	public String productRegisterForm(Model model) {
		log.info("productRegisterForm --- start");

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
		
		// 상품 타입 조회
		model.addAttribute("productDTO", new ProductDTO());
	    model.addAttribute("codeItems", commonCodeUtil.getCodeItems("PRODUCT_TYPE"));
	        
		return "mes/product/product_form";
	}

	// 상품 등록 처리
	@PostMapping("/new")
	public String productRegister(@ModelAttribute ProductDTO productDTO) {
		log.info("productRegister --- start");

		productService.registerProduct(productDTO);

		return "redirect:/mes/product";
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
		
		// 상품 타입 조회
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("PRODUCT_TYPE");
		model.addAttribute("codeItems", codeItems);
		
		model.addAttribute("searchDTO", searchDTO); // 검색조건 유지용
		
		return "mes/product/product_list";
	}

	// 상품 상세 조회
	@GetMapping("/{idx}")
	public String getProduct(@PathVariable("idx") Long idx, Model model) {
		log.info("getProduct --- start");

		// 상품 상세정보 조회
		ProductDTO productDTO = productService.getProductByIdx(idx);
		model.addAttribute("productDTO", productDTO);
		
		// 상품 타입 조회
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("PRODUCT_TYPE");
		model.addAttribute("codeItems", codeItems);

		return "mes/product/product_detail";
	}

	// 상품 수정
	@PostMapping("/{idx}")
	public String updateProduct(@PathVariable("idx") Long idx, @ModelAttribute ProductDTO productDTO) {
		try {
			productService.updateProduct(productDTO);
			return "redirect:/mes/product/" + idx;
		} catch (EntityNotFoundException e) {
			log.error("상품 수정 실패: {}", e.getMessage());
			return "error";
		}
	}

	// 상품 삭제 (Soft Delete)
	@DeleteMapping("/{idx}")
	@ResponseBody
	public String deleteProduct(@PathVariable("idx") Long idx) {
		try {
			productService.deleteProduct(idx);
			return "success";
		} catch (Exception e) {
			log.error("상품 삭제 실패: {}", e.getMessage());
			return "error";
		}
	}
}
