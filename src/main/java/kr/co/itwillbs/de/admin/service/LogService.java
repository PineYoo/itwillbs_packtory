package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.admin.dto.LogDTO;
import kr.co.itwillbs.de.admin.dto.LogSearchDTO;
import kr.co.itwillbs.de.admin.mapper.LogMapper;
import kr.co.itwillbs.de.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LogService {

	private final LogMapper logMapper;
	//@Autowired
	public LogService(LogMapper logMapper) {
		this.logMapper = logMapper;
	}
	/**
	 * (개발테스트용)
	 * @param logDTO
	 * @return int : affectedRow 반환 1 정상 입력, 0 입력되지 않음
	 */
	public int registerLog(LogDTO logDTO) {
		LogUtil.logStart(log);
		
		// insert 쿼리라 affectedRow에는 idx값이 리턴된다.
		int affectedRow = logMapper.registerLog(logDTO);
		
		return affectedRow;
	}

	/**
	 * 로그 리스트 매퍼와 연결
	 * @return List<LogDTO>
	 */
	public List<LogDTO> getLogList() {
		LogUtil.logStart(log);
		
		return logMapper.getLogList();
	}

	/**
	 * 로그 리스트 검색조건 가져오기(count)
	 * @param logSearchDTO
	 * @return
	 */
	public int getLogSearchCountForPaging(LogSearchDTO logSearchDTO) {
		LogUtil.logStart(log);
		
		return logMapper.getLogSearchCountForPaging(logSearchDTO);
	}
	
	/**
	 * 로그 리스트 검색조건 가져오기
	 * @param logSearchDTO
	 * @return List<LogDTO>
	 */
	public List<LogDTO> getLogSearchList(LogSearchDTO logSearchDTO) {
		LogUtil.logStart(log);
		
		return logMapper.getLogSearchList(logSearchDTO);
	}

	/**
	 * 로그 상세 보기
	 * @param idx
	 * @return
	 */
	public LogDTO getLog(Long idx) {
		LogUtil.logStart(log);
		
		return logMapper.getLog(idx);
	}

}
