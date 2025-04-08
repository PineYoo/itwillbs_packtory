package kr.co.itwillbs.de.human.mapper;

import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    // 사원 등록
    public void insertEmployee(EmployeeDTO employeeDTO);

    // 디테일에 사원 아이디 저장
    public void insertEmployeeDetail(String id);
    
    // 사원 목록 + 검색 (조건: 이름, 부서, 직급 등)
    public List<EmployeeDTO> searchEmployees(EmployeeSearchDTO searchDTO);

    // 사원 상세 조회
    public EmployeeDTO getEmployeeById(String id);

    // 사원 기본 정보 수정
    public void updateEmployee(EmployeeDTO employeeDTO);

    // 사원 삭제
    public void deleteEmployee(String id);
    
}
