package kr.co.itwillbs.de.sample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.itwillbs.de.sample.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

	/**
	 * 아래의 findByItemNm 과 역할이 동일하다.
	 * <br>LIKE 쿼리만 사용시 간결하게 사용 가능!
	 * @param itemNm
	 * @return
	 */
	List<Item> findByItemNmContaining(String itemNm);
	
	/**
	 * 검색 조건이 itemNm 일 때,
	 * <br>아래의 파라미터를 사용해 조회
	 * @param itemNm
	 * @return
	 */
	@Query("""
			SELECT i 
			FROM Item i 
			WHERE i.itemNm LIKE %:itemNm%
			ORDER BY i.itemNm DESC
			""")
	List<Item> findByItemNm(@Param("itemNm")String itemNm);

	/**
	 * 검색 조건이 price 일 때,
	 * <br>아래 파라미터를 사용해 최소가격 최대가격 조회
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	@Query("""
			SELECT i 
			FROM Item i 
			WHERE (:minPrice IS NULL OR i.price >= :minPrice)
			AND (:maxPrice IS NULL OR i.price <= :maxPrice)
			""")
	List<Item> findByPriceRange(@Param("minPrice")Integer minPrice, @Param("maxPrice")Integer maxPrice);

	/**
	 * 검색 조건이 어떤(itemNm||price) 것이든
	 * <br>아래 파라미터를 사용해 조회 -> 쿼리 성능에 따라 사용을 고려해보자!
	 * @param type
	 * @param itemNm
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	@Query("""
			SELECT i 
			FROM Item i 
			WHERE (:itemNm IS NULL OR i.itemNm LIKE %:itemNm%)
			AND (:minPrice IS NULL OR i.price >= :minPrice)
			AND (:maxPrice IS NULL OR i.price <= :maxPrice)
			""")
	List<Item> findBySearchParams(@Param("type")String type, @Param("itemNm")String itemNm, @Param("minPrice")Integer minPrice, @Param("maxPrice")Integer maxPrice);
}
