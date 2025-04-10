package kr.co.itwillbs.de.groupware.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.common.vo.FileVO;
import kr.co.itwillbs.de.groupware.dto.NoticeDTO;
import kr.co.itwillbs.de.groupware.dto.NoticeSearchDTO;

@Mapper
public interface NoticeMapper {
	List<NoticeDTO> getNoticeList(NoticeSearchDTO noticeSearchDTO);
	
	NoticeDTO getNotice(String idx);
	
	int registerNotice(NoticeDTO noticeVO);
	
	int modifyNotice(NoticeDTO noticeDTO);
	
	int removeNotice(NoticeDTO noticeDTO);

	int removeFile(FileVO fileVO);

	int getNoticeCountBySearchDTO(NoticeSearchDTO noticeSearchDTO);
	
	
}
