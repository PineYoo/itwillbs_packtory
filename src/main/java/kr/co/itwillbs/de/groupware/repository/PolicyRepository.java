package kr.co.itwillbs.de.groupware.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.itwillbs.de.groupware.entity.Message;
import kr.co.itwillbs.de.groupware.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long>, JpaSpecificationExecutor<Policy> {

    // 검색 조건 적용된 규정 목록 조회
	@Query("SELECT p FROM Policy p WHERE " +
		       "p.status = 'Y' AND " +  // 게시된 규정만 조회
		       "(:type IS NULL OR :type = '' OR p.type = :type) AND " +
		       "(:title IS NULL OR :title = '' OR p.title LIKE %:title%) AND " +
		       "(:regId IS NULL OR :regId = '' OR p.regId = :regId)")
	List<Policy> searchPublishedPolicies(
		        @Param("type") String type,
		        @Param("title") String title,
		        @Param("regId") String regId
	);
	
	// 게시된 정책만 조회 (status가 "Y" 또는 "PUBLISHED"인 경우)
    List<Policy> findByStatus(String status);
    
    @Query("SELECT p FROM Policy p WHERE p.status = 'Y' ORDER BY p.idx ASC")
    List<Message> findValidPolicys();
}
