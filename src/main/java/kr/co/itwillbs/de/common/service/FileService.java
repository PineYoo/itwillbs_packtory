package kr.co.itwillbs.de.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.common.mapper.FileMapper;
import kr.co.itwillbs.de.common.vo.FileVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {

	private final FileMapper fileMapper;
	
	public FileService(FileMapper fileMapper) {
		this.fileMapper = fileMapper;
	}
	
	/**
	 * INSERT INTO t_files values (fileVO) 
	 * @param fileVO
	 * @return
	 */
	public int registerFile(FileVO fileVO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		
		
		return fileMapper.registerFile(fileVO);
	}
	
	/**
	 * INSERT INTO t_files values (List<fileVO>) 
	 * @param fileVOList
	 * @return
	 */
	public int registerFiles(List<FileVO> fileVOList) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		int affectedRow = fileMapper.registerFiles(fileVOList);
		
		return affectedRow;
	}
	
	/**
	 * SELECT from t_files where idx = #{idx} 
	 * @param idx
	 * @return
	 */
	public FileVO getFileByIdx(long idx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		FileVO fileVo = fileMapper.getFileByIdx(idx);
		
		return fileVo;
	}
	
	/**
	 * SELECT from t_files where major_idx = #{majorIdx}
	 * @param idx
	 * @return
	 */
	public List<FileVO> getFilesByMajorIdx(long majorIdx) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		List<FileVO> fileList = fileMapper.getFilesByMajorIdx(majorIdx);
		
		return fileList;
	}
}
