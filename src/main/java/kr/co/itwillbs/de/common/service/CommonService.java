package kr.co.itwillbs.de.common.service;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.mapper.CommonMapper;

@Service
public class CommonService {

	private final CommonMapper commonMapper;
	//@Autowired
	public CommonService(CommonMapper commonMapper) {
		this.commonMapper = commonMapper;
	}
	
	/**
	 * H2 DB에서 시퀀스 가져오기
	 * @return String seq_*!
	 */
	public String createSeqEmpIdfromH2() {
		return commonMapper.getSeqEmpIdfromH2(); 
	}
	
	/**
	 * MySQL DB에서 시퀀스 가져오기 >> emp_id
	 * @return String seq_*!
	 */
	public String createSeqEmpIdfromMysql() {
		return commonMapper.getSeqEmpIdfromMySQL(); 
	}
	
	/**
	 * MySQL DB에서 시퀀스 가져오기 >> order_number
	 * @return String seq_*!
	 */
	public String getSeqOrderNumberfromMySQL() {
		return commonMapper.getSeqOrderNumberfromMySQL(); 
	}
}
