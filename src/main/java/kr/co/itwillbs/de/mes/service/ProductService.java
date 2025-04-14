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

	// 상품 등록 처리
	@LogExecution
	@Transactional
	public String registerProduct(ProductDTO productDTO) {
		log.info("상품 등록 요청: {}", productDTO);
		
		// 상품 정보 등록
        productMapper.insertProduct(productDTO);
        log.info("상품 등록 완료 - name: {}", productDTO.getName());

		return "redirect:/mes/product";
	}

	// 상품 목록 페이징
	public int searchProductCount(ProductSearchDTO searchDTO) {
		log.info("상품 목록 조회");
		return productMapper.searchProductCount(searchDTO);
	}

	// 상품 목록 조회
	public List<ProductDTO> getProductList(ProductSearchDTO searchDTO) {
		log.info("상품 목록 조회", searchDTO);
		
		return productMapper.searchProduct(searchDTO);
	}

	// 상품 상세 조회
	public ProductDTO getProductByIdx(Long idx) {
		log.info("상품 + 상세정보 조회 - id: {}", idx);
		
		ProductDTO product = productMapper.getProductByIdx(idx);

		return product;
	}

	// 상품 정보 업데이트 서비스 메서드
	@LogExecution
	@Transactional
	public void updateProduct(ProductDTO productDTO) {
		log.info("상품 + 상세정보 수정 - idx: {}", productDTO.getIdx());
		
		// 사원 기본 정보 업데이트
        if (productDTO != null) {
            productMapper.updateProduct(productDTO);
        } else {
            log.warn("상품 기본 정보가 null입니다.");
        }

	}

	// 상품 삭제 (상품 및 상세정보 soft delete)
	@Transactional
	public void deleteProduct(Long idx) {
		log.info("상품 삭제 요청 - id: {}", idx);
		
		productMapper.deleteProduct(idx);
	}
}
