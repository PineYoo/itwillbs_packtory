package kr.co.itwillbs.de.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.sample.dto.SampleDTO;
import kr.co.itwillbs.de.sample.dto.SampleSearchDTO;

@Mapper
public interface SampleMapper {

	List<SampleDTO> getSampleList();

	SampleDTO getSample(String idx);

	int registerSample(SampleDTO sampleVO);

	List<SampleDTO> getSampleSearchList(SampleSearchDTO sampleSearchDTO);

	int modifySample(SampleDTO sampleDTO);

	int removeSample(SampleDTO sampleDTO);
}
