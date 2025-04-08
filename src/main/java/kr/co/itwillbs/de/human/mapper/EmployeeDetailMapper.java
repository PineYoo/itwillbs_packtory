package kr.co.itwillbs.de.human.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.human.dto.EmployeeDetailDTO;

@Mapper
public interface EmployeeDetailMapper {
	
	// 사원 상세 정보 조회
	public EmployeeDetailDTO getEmployeeDetailById(String id);

	// 사원 상세 정보 수정
	public void updateEmployeeDetail(EmployeeDetailDTO detailDTO);
	
	// 사원 삭제
    public void deleteEmployeeDetail(String id);

}
