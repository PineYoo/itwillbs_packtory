package kr.co.itwillbs.de.human.service;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.service.CommonService;
import kr.co.itwillbs.de.human.dto.DepartmentCodeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDTO;
import kr.co.itwillbs.de.human.dto.EmployeeDetailDTO;
import kr.co.itwillbs.de.human.dto.EmployeeSearchDTO;
import kr.co.itwillbs.de.human.mapper.EmployeeDetailMapper;
import kr.co.itwillbs.de.human.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeMapper employeeMapper;
	private final EmployeeDetailMapper employeeDetailMapper;
    private final CommonService commonService;
    private final DepartmentInfoService departmentInfoService;

    // 사번 자동 생성
    public String generateEmployeeId() {
        return commonService.createSeqEmpIdfromMysql();
    }

    // 사원 등록
    @LogExecution
    @Transactional
    public String registerEmployee(EmployeeDTO employeeDTO) {
        log.info("사원 등록 요청: {}", employeeDTO);

        // 사번은 컨트롤러에서 이미 생성되어 들어옴
        String employeeId = employeeDTO.getId(); 
        log.info("전달된 사번: {}", employeeId);

        // 사원 정보 등록
        employeeMapper.insertEmployee(employeeDTO);
        log.info("사원 등록 완료 - id: {}, 이름: {}", employeeId, employeeDTO.getName());
        
        // 2. 디테일 테이블에 사번만 우선 등록
        employeeMapper.insertEmployeeDetail(employeeDTO.getId());

        return employeeId;
    }
	
	// 하위 부서 코드 조회
    public List<DepartmentCodeDTO> getSubDepartmentCodes(String departmentCode) {
        return departmentInfoService.getSubDepartmentCodes(departmentCode);
    }

    // 사원 목록 조회
    public List<EmployeeDTO> getEmployeeList(EmployeeSearchDTO searchDTO) {
        log.info("사원 목록 조회");
        return employeeMapper.searchEmployees(searchDTO);
    }

    // 사원 상세 조회
    public EmployeeDTO getEmployeeByIdWithDetail(String id) {
        log.info("사원 + 상세정보 조회 - id: {}", id);

        EmployeeDTO employee = employeeMapper.getEmployeeById(id);
        EmployeeDetailDTO detail = employeeDetailMapper.getEmployeeDetailById(id);
        employee.setDetail(detail);

        return employee;
    }

    // 사원 정보 업데이트 서비스 메서드
    @LogExecution
    @Transactional
    public void updateEmployeeWithDetail(EmployeeDTO employeeDTO) {
        log.info("사원 + 상세정보 수정 - id: {}", employeeDTO.getId());

        // 사원 기본 정보 업데이트
        if (employeeDTO != null) {
            employeeMapper.updateEmployee(employeeDTO);
        } else {
            log.warn("사원 기본 정보가 null입니다.");
        }

        // 사원 상세 정보가 있을 경우 업데이트
        if (employeeDTO.getDetail() != null) {
            // 이메일 아이디 + 도메인 조합
            String emailId = employeeDTO.getEmailId();
            String emailDomain = employeeDTO.getEmailDomain();

            if (emailId != null && emailDomain != null &&
                    !emailId.isBlank() && !emailDomain.isBlank()) {
                String fullEmail = emailId + "@" + emailDomain;
                employeeDTO.getDetail().setEmail(fullEmail);
                log.info("조합된 이메일: {}", fullEmail);
            } else {
                log.warn("이메일 정보가 불완전하여 이메일을 설정하지 않습니다.");
                employeeDTO.getDetail().setEmail(null); // 또는 유지할 수 있도록 기존 값 유지
            }

            log.info("업데이트할 사원 상세 정보: {}", employeeDTO.getDetail());
            employeeDetailMapper.updateEmployeeDetail(employeeDTO.getDetail());
        } else {
            log.warn("사원 상세 정보가 null입니다.");
        }
    }


	// 사원 삭제 (사원 및 상세정보 soft delete)
	@Transactional
	public void deleteEmployee(String id) {
		log.info("사원 삭제 요청 - id: {}", id);
		employeeMapper.deleteEmployee(id);
		employeeDetailMapper.deleteEmployeeDetail(id);
	}
}
