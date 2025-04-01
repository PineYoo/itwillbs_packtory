package kr.co.itwillbs.de.approval.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.approval.dto.NoticeDTO;
import kr.co.itwillbs.de.approval.dto.NoticeSearchDTO;

@Mapper
public interface NoticeMapper {
	List<NoticeDTO> getNoticeList(String major_code);
	
	NoticeDTO getNotice(String idx);
	
	int registerNotice(NoticeDTO noticeVO);
	
	List<NoticeDTO> getNoticeSearchList(NoticeSearchDTO noticeSearchDTO);
	
	int modifyNotice(NoticeDTO noticeDTO);
	
	int removeNotice(NoticeDTO noticeDTO);
	
	
}
