package kr.co.itwillbs.de.info.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.itwillbs.de.info.entity.DepartmentInfo;

@Repository
public interface DepartmentInfoRepository extends JpaRepository<DepartmentInfo, Long> {
	
	// 모든 부서 리스트 조회
	public List<DepartmentInfo> findAll();

	// 검색 조건에 맞는 부서 리스트 조회
	@Query("SELECT d FROM DepartmentInfo d WHERE "
	        + "(:departmentCode IS NULL OR d.departmentCode LIKE %:departmentCode%) "
	        + "AND (:childCode IS NULL OR d.childCode LIKE %:childCode%) "
	        + "AND (:locationIdx IS NULL OR d.locationIdx = :locationIdx)")
	public List<DepartmentInfo> findBySearchParams(@Param("departmentCode") String departmentCode,
	        									   @Param("childCode") String childCode,
	        									   @Param("locationIdx") String locationIdx);
}
    											   
