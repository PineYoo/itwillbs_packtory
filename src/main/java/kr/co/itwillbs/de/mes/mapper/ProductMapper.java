package kr.co.itwillbs.de.mes.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.mes.dto.ProductDTO;
import kr.co.itwillbs.de.mes.dto.ProductSearchDTO;

@Mapper
public interface ProductMapper {

	// 상품 등록
	public void insertProduct(ProductDTO productDTO);

	// 상품 목록 페이징
	public int ProductCount(ProductSearchDTO searchDTO);

	// 상품 목록 + 검색
	public List<ProductDTO> Product(ProductSearchDTO searchDTO);

	// 상품 상세 조회
	public ProductDTO getProductByIdx(Long idx);

	// 상품 정보 수정
	public void updateProduct(ProductDTO productDTO);

	// 상품 정보 들고 가기 (외부용)
	public List<ProductDTO> selectProductList();

}
