package kr.co.itwillbs.de.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 통계 쿼리랑 이어질 매퍼까지만...
 */
@Mapper
public interface StatisticsMapper {

	@Select("SELECT NOW()")
	String getSelectNow();
}
