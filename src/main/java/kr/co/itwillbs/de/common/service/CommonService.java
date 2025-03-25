package kr.co.itwillbs.de.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.mapper.CommonMapper;

@Service
public class CommonService {

//	@Autowired
//	private EmpSeqRepository empSeqRepository;
	
	@Autowired
	private CommonMapper commonMapper;
	
	/**
	 * JPA 버전 seq 는 잠시 중단..
	 * @param name
	 * @return
	 */
//	@Transactional
//	public EmpSeq createSeq(String name) {
//		EmpSeq empSeq = new EmpSeq("making seq!");
//		
//		return empSeqRepository.save(empSeq);
//	}
	
	/**
	 * H2 DB에서 시퀀스 가져오기
	 * @return String seq_*!
	 */
	public String createSeqEmpIdfromH2() {
		return commonMapper.getSeqEmpIdfromH2(); 
	}
	
	/**
	 * MySQL DB에서 시퀀스 가져오기
	 * @return String seq_*!
	 */
	public String createSeqEmpIdfromMysql() {
		return commonMapper.getSeqEmpIdfromMySQL(); 
	}
}
