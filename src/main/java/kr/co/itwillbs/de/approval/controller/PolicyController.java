package kr.co.itwillbs.de.approval.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import kr.co.itwillbs.de.approval.dto.PolicyDTO;
import kr.co.itwillbs.de.approval.dto.PolicySearchDTO;
import kr.co.itwillbs.de.approval.service.PolicyService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private CommonCodeUtil commonCodeUtil;

    // 규정 등록 폼 페이지
    @GetMapping("/new")
    public String policyRegisterForm(Model model) {
        log.info("policyRegisterForm --- start");
        model.addAttribute("policyDTO", new PolicyDTO());
        model.addAttribute("policyTypes", commonCodeUtil.getCodeItems("POLICY_TYPE"));
        return "approval/policy/form";
    }

    // 규정 등록 처리
    @PostMapping("/new")
    public String policyRegister(@ModelAttribute PolicyDTO policyDTO) {
        log.info("policyRegister --- start");
        policyService.registerPolicy(policyDTO);
        return "redirect:/policy";
    }

    // 규정 목록 조회 (검색 기능 추가)
    @GetMapping("")
    public String getPolicyList(@ModelAttribute PolicySearchDTO searchDTO, Model model) {
        log.info("getPolicyList --- start");

        List<PolicyDTO> policyList = policyService.getPolicyList(searchDTO);
        model.addAttribute("policyDTOList", policyList);
        model.addAttribute("policyTypes", commonCodeUtil.getCodeItems("POLICY_TYPE"));
        model.addAttribute("searchDTO", searchDTO);
        return "approval/policy/list";
    }

    // 단일 규정 조회
    @GetMapping("/detail/{idx}")
    public String getPolicy(@PathVariable("idx") Long idx, Model model) {
        log.info("getPolicy --- start");
        model.addAttribute("policyDTO", policyService.getPolicyByIdx(idx));
        model.addAttribute("policyTypes", commonCodeUtil.getCodeItems("POLICY_TYPE"));
        return "approval/policy/detail";
    }

    // 규정 수정
    @PostMapping("/detail/{idx}")
    public String updatePolicy(@PathVariable("idx") Long idx, @ModelAttribute PolicyDTO policyDTO) {
        try {
            policyService.updatePolicy(idx, policyDTO);
            return "redirect:/policy/detail/" + idx;
        } catch (EntityNotFoundException e) {
            log.error("규정 수정 실패: {}", e.getMessage());
            return "errorPage";
        }
    }

    // 규정 삭제 (게시 여부를 'DRAFT'로 변경)
    @DeleteMapping("/detail/{idx}")
    @ResponseBody
    public String deletePolicy(@PathVariable("idx") Long idx) {
        try {
            policyService.changePolicyStatus(idx, "N");
            return "success";
        } catch (Exception e) {
            log.error("규정 삭제 실패: {}", e.getMessage());
            return "error";
        }
    }
}
