package kr.co.itwillbs.de.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.common.vo.FileVO;

@Mapper
public interface FileMapper {

	int registerFile(FileVO fileVO);

	int registerFiles(List<FileVO> fileVOList);
	
	FileVO getFileByIdx(long idx);

	List<FileVO> getFilesByMajorIdx(@Param("type")String type, 
									@Param("majorIdx")long majorIdx);

	List<FileVO> getFilesByTypeAndMajorIdx(@Param("type")String type, 
									@Param("majorIdx")String majorIdx);
	
	int removeFile(FileVO fileVO);

	int getMaxRankNumberByTypeAndMajorIdx(@Param("type")String type, 
									@Param("majorIdx")String majorIdx);

}
