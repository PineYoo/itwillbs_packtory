package kr.co.itwillbs.de.mes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.sample.constant.IsDeleted;
import kr.co.itwillbs.de.sample.dto.SampleDTO;
import kr.co.itwillbs.de.sample.dto.SampleSearchDTO;
import kr.co.itwillbs.de.sample.mapper.SampleMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SampleService {

	private final SampleMapper sampleMapper;
	//@Autowired
	public SampleService(SampleMapper sampleMapper) {
		this.sampleMapper = sampleMapper;
	}
	
	/**
	 * 매퍼에서 샘플 리스트 가져오기
	 * @return
	 * @throws Exception
	 */
	public List<SampleDTO> getSampleList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sampleMapper.getSampleList();
	}
	
	/**
	 * 매퍼에서 단건 가져오기
	 * @return
	 * @throws Exception
	 */
	public SampleDTO getSample(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sampleMapper.getSample(idx);
	}

	/**
	 * 샘플 Insert
	 * @param sampleVO
	 */
	public void registerSample(SampleDTO sampleDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		sampleDTO.setRegId("testUser");
		
		int generatedIdx = sampleMapper.registerSample(sampleDTO);
		log.info("generated Idx value : {}", generatedIdx);
	}

	/**
	 * 샘플 검색 조건 조회
	 * @param sampleSearchDTO
	 * @return
	 */
	public List<SampleDTO> getSampleSearchList(SampleSearchDTO sampleSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return sampleMapper.getSampleSearchList(sampleSearchDTO);
	}

	/**
	 * 샘플 업데이트
	 * @param sampleDTO
	 */
	public void modifySample(SampleDTO sampleDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		sampleDTO.setModId("testUser");
		
		int affectedRow = sampleMapper.modifySample(sampleDTO);
		log.info("affectedRow is {}", affectedRow);
		//TODO 0이 나올 경우 예외처리 필요? 다음엔 좀더 예쁘게?
		if(affectedRow < 1) {
			throw new Exception("업데이트할 샘플 데이터가 존재하지 않습니다.");
		}
		
	}

	/**
	 * 샘플 isDeleted = Y 로 변경
	 * @param idx 테이블 PK 컬럼
	 * @throws Exception
	 */
	public void removeItem(long idx) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		SampleDTO sampleDTO = new SampleDTO();
		sampleDTO.setIdx(idx);
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		sampleDTO.setModId("testUser");
		sampleDTO.setIsDeleted(IsDeleted.Y);
		
		int affectedRow = sampleMapper.removeSample(sampleDTO);
		log.info("affectedRow is {}", affectedRow);
		//TODO 0이 나올 경우 예외처리 필요? 다음엔 좀더 예쁘게?
		if(affectedRow < 1) {
			throw new Exception("업데이트할 샘플 데이터가 존재하지 않습니다.");
		}
	}
}
