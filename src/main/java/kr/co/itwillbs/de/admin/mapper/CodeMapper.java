package kr.co.itwillbs.de.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.admin.dto.CodeDTO;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.admin.dto.CodeSearchDTO;

@Mapper
public interface CodeMapper {

	/**
	 * INSERT t_commonCode
	 * @param codeDTO
	 * @return int -> useGeneratedKeys="true" keyProperty="idx"
	 */
	int registerCode(CodeDTO codeDTO);

	
	/**
	 * SELECT t_commonCode 
	 * @return List<CodeDTO>
	 */
	List<CodeDTO> getCodes();

	/**
	 * SELECT count(*) t_commonCode where codeSearchDTO
	 * @param codeSearchDTO
	 * @return count(*) int
	 */
	int getCodesCountBySearchDTO(CodeSearchDTO codeSearchDTO);
	
	/**
	 * SELECT t_commonCode where codeSearchDTO
	 * @param codeSearchDTO
	 * @return
	 */
	List<CodeDTO> getCodesBySearchDTO(CodeSearchDTO codeSearchDTO);

	/**
	 * SELECT t_commonCode where idx = codeSearchDTO.idx
	 * @param codeSearchDTO
	 * @return CodeDTO
	 */
	CodeDTO getCodeByIdx(CodeSearchDTO codeSearchDTO);


	/**
	 * SELECT t_commoncode_item where major_code = #{codeSearchDTO.majorCode}
	 * @param codeSearchDTO
	 * @return List<CodeItemDTO>
	 */
	List<CodeItemDTO> getCodeItemsByMajorCode(CodeSearchDTO codeSearchDTO);


	/**
	 * UPDATE t_commoncode set codeDTO where idx = #{codeDTO.idx}
	 * @param codeDTO
	 * @return int => affectedRow
	 */
	int modifyCode(CodeDTO codeDTO);


	/**
	 * DELETE t_commoncode_item where major_code = #{itemList.get(0).majorCode}
	 * <br> minor 코드 등록 전 전체 삭제를 진행
	 * @param codeItemDTO
	 * @return int => affectedRow
	 */
	int removeCodeItems(CodeItemDTO codeItemDTO);

	/**
	 * INSERT t_commoncode_item values (itemList.get(n)),(itemList.get(n+1)), ...
	 * <br> minor 코드 등록은 리스트로 진행
	 * @param itemList
	 * @return int => affectedRow
	 */
	int registerCodeItems(List<CodeItemDTO> itemList);

}
