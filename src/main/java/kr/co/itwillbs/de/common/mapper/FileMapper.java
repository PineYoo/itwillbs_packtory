package kr.co.itwillbs.de.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.common.vo.FileVO;

@Mapper
public interface FileMapper {

	int registerFile(FileVO fileVO);

	int registerFiles(List<FileVO> fileVOList);
	
	FileVO getFileByIdx(long idx);

	List<FileVO> getFilesByMajorIdx(long majorIdx);


}
