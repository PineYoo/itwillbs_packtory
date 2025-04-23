package kr.co.itwillbs.de.common.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.co.itwillbs.de.groupware.dto.ApprovalDTO;

/**
 * 통계 쿼리랑 이어질 매퍼까지만...
 */
@Mapper
public interface StatisticsMapper {

	@Select("SELECT NOW()")
	String getSelectNow();

	/**
	 * 오늘 결재완료 된 기안서 조회
	 * SELECT * 
	 *   FROM t_approval
	 *  WHERE progress_status = '4'	-- 결재 완료
  	 *    AND DATE(mod_date) = CURDATE(); -- 오늘 날짜
	 * @return List<ApprovalDTO>
	 */
	List<ApprovalDTO> getSelectApprovedToday();

	/**
	 * 결재완료 된 레시피 update
	 * 결재상태(approval_status) - 0: 작성완료, 1: 결재완료, 2: 반려
	 * @param recipeMasterIdx
	 */
	void updateApprovedRecipe(String recipeMasterIdx);

	/**
	 * 결재완료 된 휴가서 update
	 * @param employeeId
	 * @param date
	 */
	void insertApprovedVacation(@Param("employeeId") String employeeId, 
								@Param("date") LocalDate date);
	
	

	
}
