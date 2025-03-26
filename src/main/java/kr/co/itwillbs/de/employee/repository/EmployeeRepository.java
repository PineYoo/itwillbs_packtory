package kr.co.itwillbs.de.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kr.co.itwillbs.de.employee.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	void deleteById(String id);

	List<Employee> findByNameContaining(String searchValue);

	List<Employee> findByDepartmentCodeContaining(String searchValue);

	Optional<Employee> findById(String id);
}
