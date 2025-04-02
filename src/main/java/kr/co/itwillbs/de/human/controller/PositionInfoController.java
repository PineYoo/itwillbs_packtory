package kr.co.itwillbs.de.human.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.EntityNotFoundException;
import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.human.dto.PositionInfoDTO;
import kr.co.itwillbs.de.human.service.PositionInfoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/position")
public class PositionInfoController {

    @Autowired
    private PositionInfoService positionInfoService;

    @Autowired
    private CommonCodeUtil commonCodeUtil;

    // 직급 등록 폼 페이지
    @GetMapping("/new")
    public String positionRegisterForm(Model model) {
        log.info("positionRegisterForm --- start");
        model.addAttribute("positionInfoDTO", new PositionInfoDTO());
        model.addAttribute("codeItems", commonCodeUtil.getCodeItems("POSITION_CODE"));
        return "human/info/position/form";
    }

    // 직급 등록 처리
    @PostMapping("/new")
    public String positionRegister(@ModelAttribute PositionInfoDTO positionInfoDTO) {
        log.info("positionRegister --- start");
        positionInfoService.registerPosition(positionInfoDTO);
        return "redirect:/position";
    }

    // 직급 목록 조회 (검색 기능 추가)
    @GetMapping("")
    public String getPositionList(@RequestParam(value = "positionCode", required = false) String positionCode, Model model) {
        log.info("getPositionList --- start");

        // 검색 조건에 따라 조회
        List<PositionInfoDTO> positionList;
        if (positionCode != null && !positionCode.trim().isEmpty()) {
            positionList = positionInfoService.getPositionListByCode(positionCode.trim());
        } else {
            positionList = positionInfoService.getPositionList();
        }

        model.addAttribute("positionInfoDTOList", positionList);

        // 공통 코드 리스트 (직급 코드 선택용)
        List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("POSITION_CODE");
        model.addAttribute("codeItems", codeItems);
        model.addAttribute("selectedCode", positionCode); // 검색 시 선택된 코드 유지

        return "human/info/position/list";
    }

    // 단일 직급 조회
    @GetMapping("/detail/{idx}")
    public String getPosition(@PathVariable("idx") Long idx, Model model) {
        log.info("getPosition --- start");
        model.addAttribute("positionInfoDTO", positionInfoService.getPositionByIdx(idx));
        model.addAttribute("codeItems", commonCodeUtil.getCodeItems("POSITION_CODE"));
        return "human/info/position/detail";
    }

    // 직급 수정
    @PostMapping("/detail/{idx}")
    public String updatePosition(@PathVariable("idx") Long idx, @ModelAttribute PositionInfoDTO positionInfoDTO) {
        try {
            positionInfoService.updatePosition(idx, positionInfoDTO);
            return "redirect:/position/detail/" + idx;
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
