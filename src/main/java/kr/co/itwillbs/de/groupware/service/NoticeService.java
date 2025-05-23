package kr.co.itwillbs.de.groupware.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.vo.FileVO;
import kr.co.itwillbs.de.groupware.dto.NoticeDTO;
import kr.co.itwillbs.de.groupware.dto.NoticeSearchDTO;
import kr.co.itwillbs.de.groupware.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeService {
	@Autowired
	private NoticeMapper noticeMapper;
	
	@Autowired
	private FileService fileService;
	
	
	/**
	 * 매퍼에서 공지사항 리스트 가져오기
	 * @return
	 * @throws Exception
	 */
	public List<NoticeDTO> getNoticeList(NoticeSearchDTO noticeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return noticeMapper.getNoticeList(noticeSearchDTO);
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
	 * 공지사항 Insert
	 * @param sampleVO
	 */
	@LogExecution // 로그 남길 서비스
	public String registerNotice(NoticeDTO noticeDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
//		noticeDTO.setRegId("testUser");
		
		int generatedIdx = noticeMapper.registerNotice(noticeDTO);
		log.info("generated Idx value : {}", generatedIdx, noticeDTO.getIdx());
		return noticeDTO.getIdx();
	}


	/**
	 * 공지사항 업데이트
	 * @param noticeDTO
	 */
	@LogExecution // 로그 남길 서비스
	public void modifyNotice(NoticeDTO noticeDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		// 상세보기 페이지를 요청할 때 조회수를 올릴 때 이 메서드가 호출됨
		// 수정을 하지 않았음에도 mod_id가 등록 됨
//		noticeDTO.setModId("testUser");
		
		int affectedRow = noticeMapper.modifyNotice(noticeDTO);
		log.info("affectedRow is {}", affectedRow);
		//TODO 0이 나올 경우 예외처리 필요? 다음엔 좀더 예쁘게?
		if(affectedRow < 1) {
			throw new Exception("업데이트할 게시글이 존재하지 않습니다.");
		}
		
	}

	/**
	 * 공지사항 isDeleted = Y 로 변경
	 * @param idx 테이블 PK 컬럼
	 * @throws Exception
	 */
	@LogExecution // 로그 남길 서비스
	public void removeItem(String idx) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		NoticeDTO noticeDTO = new NoticeDTO();
		noticeDTO.setIdx(idx);
		// 나중엔 regId와 modId를 세션에서 가져와 작업할 예정이기에 지금은 하드코딩처리함
		noticeDTO.setModId("testUser");
		//	ENUM 대신 임시로 Y값 하드코딩
		noticeDTO.setIsDeleted("Y");
		
		int affectedRow = noticeMapper.removeNotice(noticeDTO);
		log.info("affectedRow is {}", affectedRow);
		//TODO 0이 나올 경우 예외처리 필요? 다음엔 좀더 예쁘게?
		if(affectedRow < 1) {
			throw new Exception("업데이트할 샘플 데이터가 존재하지 않습니다.");
		}
	}

	/**
	 * 공지사항 파일 개별 삭제(AJAX) 
	 * @param idx
	 */
	@LogExecution // 로그 남길 서비스
	public void removeFile(String idx) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		FileVO fileVO = new FileVO();
		
		//	받아온 idx 세팅
		fileVO.setIdx(idx);
		//	ENUM 대신 임시로 Y값 하드코딩
		fileVO.setIsDeleted("Y");
		
		int affectedRow = fileService.removeFile(fileVO);
		log.info("affectedRow is {}", affectedRow);
		if(affectedRow < 1) {
			throw new Exception("업데이트할 샘플 데이터가 존재하지 않습니다.");
		}
		
	}

	/**
	 * SELECT count(*) FROM t_notice
	 * @param noticeSearchDTO
	 * @return int
	 */
	public int getNoticeCountBySearchDTO(NoticeSearchDTO noticeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return noticeMapper.getNoticeCountBySearchDTO(noticeSearchDTO);
	}

}
