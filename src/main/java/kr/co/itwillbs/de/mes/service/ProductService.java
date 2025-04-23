package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;
import kr.co.itwillbs.de.mes.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductMapper productMapper;

	// 상품 등록
	@LogExecution
	@Transactional
	public String registerProduct(ProductDTO productDTO) {
		log.info("상품 등록 요청: {}", productDTO);
		productMapper.insertProduct(productDTO);
		log.info("상품 등록 완료 - name: {}", productDTO.getName());
		return "redirect:/mes/product";
	}

	// 상품 총 개수 (검색 조건 포함)
	public int searchProductCount(ProductSearchDTO searchDTO) {
		log.info("상품 개수 조회 - 검색 조건: {}", searchDTO);
		return productMapper.searchProductCount(searchDTO);
	}

	// 상품 목록 조회 (검색 + 페이징)
	public List<ProductDTO> getProductList(ProductSearchDTO searchDTO) {
		log.info("상품 목록 조회 - 검색 조건: {}", searchDTO);
		return productMapper.searchProduct(searchDTO);
	}

	// 상품 상세 조회
	public ProductDTO getProductByIdx(Long idx) {
		log.info("상품 상세 조회 - idx: {}", idx);
		return productMapper.getProductByIdx(idx);
	}

	// 상품 수정
	@LogExecution
	@Transactional
	public void updateProduct(ProductDTO productDTO) {
		log.info("상품 수정 요청 - idx: {}", productDTO.getIdx());

		// productDTO가 null이 아닌지 체크
		if (productDTO != null) {
			// 상품 정보를 업데이트하는 쿼리 호출
			productMapper.updateProduct(productDTO);
			log.info("상품 수정 완료 - name: {}", productDTO.getName());
		}
	}

	// 상품 정보 담아 가기 (외부용)
	public List<ProductDTO> getProductList() {
		return productMapper.selectProductList();
	}
	
}