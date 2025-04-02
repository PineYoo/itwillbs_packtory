package kr.co.itwillbs.de.human.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.itwillbs.de.human.entity.EmployeeDetail;

@Repository
public interface EmployeeDetailRepository extends JpaRepository<EmployeeDetail, Long> {

    // EmployeeId로 EmployeeDetail을 Optional로 반환하도록 수정
    Optional<EmployeeDetail> findByEmployeeId(String employeeId);

	void deleteById(String id);
}

