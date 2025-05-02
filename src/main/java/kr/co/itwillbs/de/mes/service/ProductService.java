package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
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
	public void registerProduct(ProductDTO productDTO) {
		LogUtil.logStart(log);

		productMapper.insertProduct(productDTO);
	}

	// 상품 총 개수 (검색 조건 포함)
	public int searchProductCount(ProductSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return productMapper.ProductCount(searchDTO);
	}

	// 상품 목록 조회 (검색 + 페이징)
	public List<ProductDTO> getProductList(ProductSearchDTO searchDTO) {
		LogUtil.logStart(log);

		return productMapper.Product(searchDTO);
	}

	// 상품 상세 조회
	public ProductDTO getProductByIdx(Long idx) {
		LogUtil.logStart(log);

		return productMapper.getProductByIdx(idx);
	}

	// 상품 수정
	@LogExecution
	@Transactional
	public void updateProduct(ProductDTO productDTO) {
		LogUtil.logStart(log);

		productMapper.updateProduct(productDTO);
	}

	// 상품 정보 담아 가기 (외부용)
	public List<ProductDTO> getProductList() {
		return productMapper.selectProductList();
	}

}