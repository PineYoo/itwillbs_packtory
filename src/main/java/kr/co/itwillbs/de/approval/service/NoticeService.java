package kr.co.itwillbs.de.approval.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.approval.dto.NoticeDTO;
import kr.co.itwillbs.de.approval.dto.NoticeSearchDTO;
import kr.co.itwillbs.de.approval.mapper.NoticeMapper;
import kr.co.itwillbs.de.sample.constant.IsDeleted;
import kr.co.itwillbs.de.sample.dto.SampleDTO;
import kr.co.itwillbs.de.sample.dto.SampleSearchDTO;
import kr.co.itwillbs.de.sample.mapper.SampleMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeService {
	@Autowired
	private NoticeMapper noticeMapper;
	
	/**
	 * 매퍼에서 샘플 리스트 가져오기
	 * @return
	 * @throws Exception
	 */
	public List<NoticeDTO> getNoticeList() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return noticeMapper.getNoticeList();
	}
	
	/**
	 * 매퍼에서 단건 가져오기
	 * @return
	 * @throws Exception
	 */
	public NoticeDTO getNotice(String idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return noticeMapper.getNotice(idx);
	}

	/**
	 * 샘플 Insert
	 * @param sampleVO
	 */
	public void registerNotice(NoticeDTO noticeDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		noticeDTO.setRegId("testUser");
		
		int generatedIdx = noticeMapper.registerNotice(noticeDTO);
		log.info("generated Idx value : {}", generatedIdx);
	}

	/**
	 * 샘플 검색 조건 조회
	 * @param sampleSearchDTO
	 * @return
	 */
	public List<NoticeDTO> getNoticeSearchList(NoticeSearchDTO noticeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return noticeMapper.getNoticeSearchList(noticeSearchDTO);
	}

	/**
	 * 샘플 업데이트
	 * @param sampleDTO
	 */
	public void modifyNotice(NoticeDTO noticeDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		noticeDTO.setModId("testUser");
		
		int affectedRow = noticeMapper.modifyNotice(noticeDTO);
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
	public void removeItem(String idx) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		NoticeDTO noticeDTO = new NoticeDTO();
		noticeDTO.setIdx(idx);
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		noticeDTO.setModId("testUser");
		//	ENUM 대신 임시로 N값 하드코딩
		noticeDTO.setIsDeleted("N");
		
		int affectedRow = noticeMapper.removeNotice(noticeDTO);
		log.info("affectedRow is {}", affectedRow);
		//TODO 0이 나올 경우 예외처리 필요? 다음엔 좀더 예쁘게?
		if(affectedRow < 1) {
			throw new Exception("업데이트할 샘플 데이터가 존재하지 않습니다.");
		}
	}
}
