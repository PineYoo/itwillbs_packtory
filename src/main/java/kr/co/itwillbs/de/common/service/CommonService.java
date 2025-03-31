package kr.co.itwillbs.de.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.admin.dto.CodeDTO;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.admin.dto.CodeSearchDTO;
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

	/**
	 * SELECT t_commoncode where is_deleted = 'N' 
	 * @param majorCode
	 * @return
	 */
	public List<CodeDTO> getCodes() {
		
		return commonMapper.getCodes();
	}

	/**
	 * SELECT t_commoncode_item where is_deleted = 'N' and mojor_code = #{majorCode}
	 * @param majorCode
	 * @return
	 */
	public List<CodeItemDTO> getCodeItems(String majorCode) {
		
		return commonMapper.getCodeItems(majorCode);
	}
}
