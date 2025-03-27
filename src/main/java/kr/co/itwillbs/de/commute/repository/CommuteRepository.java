package kr.co.itwillbs.de.commute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.co.itwillbs.de.commute.entity.Commute;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, Long>{

//	/**
//	 * 아래의 findByEmployeeId 과 역할이 동일하다.
//	 * <br>LIKE 쿼리만 사용시 간결하게 사용 가능!
//	 * @param employeeId 
//	 * @return
//	 */
//	List<Commute> findByEmployeeIdContaining(String employeeId);
//	
//	/**
//	 * 검색 조건이 employeeId 일 때,
//	 * <br>아래의 파라미터를 사용해 조회
//	 * @param employeeId
//	 * @return
//	 */
//	@Query("""
//			SELECT c 
//			FROM Commute c 
//			WHERE c.employeeId LIKE %:employeeId%
//			ORDER BY c.regDate
//			""")
//	List<Commute> findByEmployeeId(@Param("employeeId")String employeeId);

}
