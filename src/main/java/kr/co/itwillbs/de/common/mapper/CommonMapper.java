package kr.co.itwillbs.de.common.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

	/**
	 * H2에서 seq 가져오기
	 * <br> 시퀀스 생성 쿼리 : CREATE SEQUENCE IF NOT EXISTS seq_emp_id START WITH 100000 INCREMENT BY 1;
	 * <br> 시퀀스 삭제 쿼리 : DROP SEQUENCE seq_emp_id;
	 * @return seq_emp_id
	 */
	String getSeqEmpIdfromH2();
	
	/**
	 * MySQL에서 seq 가져오기
	 * <br> 시퀀스로 이용할 테이블 생성
	 * <br> 시퀀스를 생성 할 프로시저 생성
	 * <br> 생성 한 시퀀스의 다음 값을 가져오는 함수 생성
	 * <br> 초기값 셋팅
	 * <br> 함수 호출 사용! SELECT nextval('seq_emp_id')
	 * @return seq_emp_id
	 */
	String getSeqEmpIdfromMySQL();
}
