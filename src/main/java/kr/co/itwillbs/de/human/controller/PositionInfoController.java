package kr.co.itwillbs.de.human.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.EntityNotFoundException;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.human.dto.PositionInfoDTO;
import kr.co.itwillbs.de.human.dto.PositionSearchDTO;
import kr.co.itwillbs.de.human.service.PositionInfoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/human/position")
public class PositionInfoController {

    @Autowired
    private PositionInfoService positionInfoService;

    @Autowired
    private CommonCodeUtil commonCodeUtil;

    // 직급 등록 폼 페이지
    @GetMapping("/new")
    public String positionRegisterForm(Model model) {
    	LogUtil.logStart(log);
        
        model.addAttribute("positionInfoDTO", new PositionInfoDTO());
        model.addAttribute("codeItems", commonCodeUtil.getCodeItems("POSITION_CODE"));
        return "human/info/position/form";
    }

    // 직급 등록 처리
    @PostMapping("/new")
    public String positionRegister(@ModelAttribute PositionInfoDTO positionInfoDTO) {
    	LogUtil.logStart(log);
        positionInfoService.registerPosition(positionInfoDTO);
        return "redirect:/human/position";
    }

	// 직급 목록 조회 (검색 기능 추가)
	@GetMapping("")
	public String getPositionList(@ModelAttribute PositionSearchDTO searchDTO, Model model) {
		LogUtil.logStart(log);

		// 검색 조건에 따라 조회
		Page<PositionInfoDTO> result = positionInfoService.getPositionsBySearchDTO(searchDTO);
		// totalCount를 넣어주면 PageDTO 내부 계산값들이 자동 처리됨
		searchDTO.getPageDTO().setTotalCount((int) result.getTotalElements());
		model.addAttribute("positionInfoDTOList", result.getContent());
		
		int startNo = searchDTO.getPageDTO().getTotalCount() - searchDTO.getPageDTO().getOffset(); // 역순 번호
		model.addAttribute("startNo", startNo);
		// 공통 코드 리스트 (직급 코드 선택용)
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("POSITION_CODE");
		model.addAttribute("codeItems", codeItems);
		
		model.addAttribute("searchDTO", searchDTO); // 검색 조건 유지

		return "human/info/position/list";
	}

//    // 직급 목록 조회 (검색 기능 추가)
//    @GetMapping("")
//    public String getPositionList(@ModelAttribute PositionSearchDTO searchDTO, Model model) {
//    	LogUtil.logStart(log);
//    	
//    	// 검색 조건에 따라 조회
//    	List<PositionInfoDTO> positionList;
//    	if (searchDTO.getPositionCode() != null && !searchDTO.getPositionCode().isEmpty()) {
//    		positionList = positionInfoService.getPositionListByCode(searchDTO.getPositionCode().trim());
//    	} else {
//    		positionList = positionInfoService.getPositionList();
//    	}
//    	
//    	model.addAttribute("positionInfoDTOList", positionList);
//    	
//    	// 공통 코드 리스트 (직급 코드 선택용)
//    	List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("POSITION_CODE");
//    	model.addAttribute("codeItems", codeItems);
//    	model.addAttribute("searchDTO", searchDTO); // 검색 시 선택된 코드 유지
//    	
//    	return "human/info/position/list";
//    }

    // 단일 직급 조회
    @GetMapping("/detail/{idx}")
    public String getPosition(@PathVariable("idx") Long idx, Model model) {
    	LogUtil.logStart(log);
        model.addAttribute("positionInfoDTO", positionInfoService.getPositionByIdx(idx));
        model.addAttribute("codeItems", commonCodeUtil.getCodeItems("POSITION_CODE"));
        return "human/info/position/detail";
    }

    // 직급 수정
    @PostMapping("/detail/{idx}")
    public String updatePosition(@PathVariable("idx") Long idx, @ModelAttribute PositionInfoDTO positionInfoDTO) {
    	LogUtil.logStart(log);
        try {
            positionInfoService.updatePosition(idx, positionInfoDTO);
            return "redirect:/human/position/detail/" + idx;
        } catch (EntityNotFoundException e) {
            log.error("직급 수정 실패: {}", e.getMessage());
            return "errorPage";
        }
    }

    // 직급 삭제 (Soft Delete)
    @DeleteMapping("/detail/{idx}")
    @ResponseBody
    public String deletePosition(@PathVariable("idx") Long idx) {
        try {
            positionInfoService.softDeletePosition(idx);
            return "success";
        } catch (Exception e) {
            log.error("직급 삭제 실패: {}", e.getMessage());
            return "error";
        }
    }
}
