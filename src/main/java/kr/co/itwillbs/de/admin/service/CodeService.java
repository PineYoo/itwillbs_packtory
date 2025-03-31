package kr.co.itwillbs.de.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.admin.dto.CodeDTO;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.admin.dto.CodeSearchDTO;
import kr.co.itwillbs.de.admin.mapper.CodeMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeService {

	private final CodeMapper codeMapper;
	//@Autowired
	public CodeService(CodeMapper codeMapper) {
		this.codeMapper = codeMapper;
	}
	
	/**
	 * INSERT t_commoncode 
	 * @param codeDTO
	 * @return
	 */
	public int registerCode(CodeDTO codeDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//TODO 세션에서 regId 가져와서 셋
		codeDTO.setRegId("superUser");
		int affectedRow = codeMapper.registerCode(codeDTO);
		log.info("affectedRow is {}", affectedRow);
		return affectedRow;
	}

	/**
	 * SELECT t_commoncode 
	 * @return List<CodeDTO>
	 */
	public List<CodeDTO> getCodes() {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodes();
	}
	
	/**
	 * SELECT t_commoncode 
	 * @param codeSearchDTO
	 * @return List<CodeDTO>
	 */
	public List<CodeDTO> getCodesBySearchDTO(CodeSearchDTO codeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodesBySearchDTO(codeSearchDTO);
	}

	/**
	 * SELECT t_commoncode where idx = #{codeSearchDTO.idx}
	 * @param codeSearchDTO
	 * @return CodeDTO
	 */
	public CodeDTO getCodeByIdx(CodeSearchDTO codeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodeByIdx(codeSearchDTO);
	}

	/**
	 * SELECT t_commoncode_item where major_code = #{codeSearchDTO.majorCode}
	 * @param codeSearchDTO
	 * @return List<CodeItemDTO>
	 */
	public List<CodeItemDTO> getCodeItemsByMajorCode(CodeSearchDTO codeSearchDTO) {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return codeMapper.getCodeItemsByMajorCode(codeSearchDTO);
	}

	/**
	 * UPDATE t_commoncode set codeDTO where idx = #{codeDTO.idx}
	 * @param codeDTO
	 * @throws Exception 
	 */
	public void modifyCode(CodeDTO codeDTO) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		//TODO 세션에서 mod_id 가져와서 셋
		codeDTO.setModId("superUser");
		
		int affectedRow = codeMapper.modifyCode(codeDTO);
		if(affectedRow < 1) {
			throw new Exception("그룹코드 수정을 완료하지 못했습니다.\\n잠시 후 다시 시도해주시기 바랍니다.");
		}
	}

	/**
	 * DELETE t_commoncode_item where major_code = #{itemList.get(0).majorCode}
	 * @param itemList
	 * @throws Exception 
	 */
	public void codeItemsRegister(List<CodeItemDTO> itemList) throws Exception {
		log.info("{}---start", Thread.currentThread().getStackTrace()[1].getMethodName());
		
		int affectedRow = 0;
		if(itemList.size() > 0) {
			//등록 전에 삭제부터 하고
			affectedRow = codeMapper.removeCodeItems((CodeItemDTO)itemList.get(0));
			log.info("after removeCodeItems, affectedRow is {}", affectedRow);
		}
		//TODO 세션에서 reg_id 가져와서 셋		
		for (CodeItemDTO itemDTO : itemList) {
			itemDTO.setRegId("superUser");
		}
		
		affectedRow = 0;
		affectedRow = codeMapper.registerCodeItems(itemList);
		log.info("itemList.size is {}, // affectedRow is {}", itemList.size(), affectedRow);
		
		if(affectedRow < 1 || itemList.size() != affectedRow) {
			throw new Exception("코드 입력을 완료하지 못했습니다.\\n잠시 후 다시 시도해주시기 바랍니다.");
		}
	}
	
}
