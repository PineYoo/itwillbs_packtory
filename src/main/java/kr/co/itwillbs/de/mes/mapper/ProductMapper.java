package kr.co.itwillbs.de.mes.mapper;

import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

	// 상품 등록
	public void insertProduct(ProductDTO productDTO);

	// 상품 목록 페이징
	public int searchProductCount(ProductSearchDTO searchDTO);

	// 상품 목록 + 검색
	public List<ProductDTO> searchProduct(ProductSearchDTO searchDTO);

	// 상품 상세 조회
	public ProductDTO getProductByIdx(Long idx);

	// 상품 정보 수정
	public void updateProduct(ProductDTO productDTO);

	// 상품 삭제
	public void deleteProduct(Long idx);

}
