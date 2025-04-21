package kr.co.itwillbs.de.human.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.human.dto.DepartmentCodeDTO;
import kr.co.itwillbs.de.human.entity.DepartmentInfo;
import kr.co.itwillbs.de.human.entity.PositionInfo;

import org.springframework.data.repository.query.Param;

@Repository
public interface DepartmentInfoRepository extends JpaRepository<DepartmentInfo, Long>, JpaSpecificationExecutor<DepartmentInfo> {

    // 검색 시, 삭제되지 않은 부서만 조회하도록 수정
	@Query("SELECT d FROM DepartmentInfo d WHERE d.isDeleted = 'N' "
	        + "AND (:departmentCode IS NULL OR d.departmentCode LIKE %:departmentCode%) "
	        + "AND (:childCode IS NULL OR d.childCode LIKE %:childCode%) "
	        + "AND (:locationIdx IS NULL OR d.locationIdx = :locationIdx)")
    List<DepartmentInfo> findBySearchParams(@Param("departmentCode") String departmentCode,
                                            @Param("childCode") String childCode,
                                            @Param("locationIdx") String locationIdx);

    // 삭제되지 않은 부서 리스트 조회
    List<DepartmentInfo> findByIsDeleted(String isDeleted);

	@Transactional
	@Modifying
	@Query("UPDATE DepartmentInfo d SET d.isDeleted = 'Y' WHERE d.idx = :idx")
	void softDeleteById(@Param("idx") Long idx);
    
	// 하위 부서 코드 조회
	@Query("SELECT new kr.co.itwillbs.de.human.dto.DepartmentCodeDTO(d.parentCode, d.childCode, d.childName) " +
		   "FROM DepartmentInfo d " +
		   "WHERE d.parentCode = :departmentCode AND d.isDeleted = 'N'")
	List<DepartmentCodeDTO> findSubDepartmentsByParentCode(@Param("departmentCode") String departmentCode);
	
    @Query("SELECT d FROM DepartmentInfo d WHERE d.isDeleted = 'N' ORDER BY d.rankNumber ASC")
    List<DepartmentInfo> findValidDepartments();






}
