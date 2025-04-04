package kr.co.itwillbs.de.approval.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import kr.co.itwillbs.de.approval.dto.PolicyDTO;
import kr.co.itwillbs.de.approval.dto.PolicySearchDTO;
import kr.co.itwillbs.de.approval.service.PolicyService;
import kr.co.itwillbs.de.common.service.FileService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.util.FileUtil;
import kr.co.itwillbs.de.common.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;
    private final CommonCodeUtil commonCodeUtil;
    private final FileService fileService;
    private final FileUtil fileUtil;

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
    public String policyRegister(@ModelAttribute PolicyDTO policyDTO,
                                 @RequestParam("policyFiles") List<MultipartFile> policyFiles) {
        log.info("policyRegister --- start");
        log.info("requestData : {}", policyDTO);

        // 규정 저장 후 ID 반환
        String idx = policyService.registerPolicy(policyDTO);

        // 파일 업로드 및 저장 처리
        List<FileVO> fileList = policyFiles.stream()
                .filter(file -> StringUtils.hasLength(file.getOriginalFilename()))
                .map(file -> {
                    try {
                        FileVO fileVO = fileUtil.setFile(file);
                        fileVO.setMajorIdx(idx);
                        fileVO.setType("t_policy");
                        fileVO.setIsDeleted("N");
                        return fileVO;
                    } catch (Exception e) {
                        log.error("파일 업로드 중 오류 발생", e);
                        return null;
                    }
                })
                .filter(fileVO -> fileVO != null)
                .toList();

        if (!fileList.isEmpty()) {
            fileService.registerFiles(fileList);
        }

        return "redirect:/policy";
    }

    // 규정 목록 조회 (검색 기능 추가)
    @GetMapping("")
    public String getPolicyList(@ModelAttribute PolicySearchDTO searchDTO, Model model) {
        log.info("getPolicyList --- start");
        model.addAttribute("policyDTOList", policyService.getPolicyList(searchDTO));
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
    public String updatePolicy(@PathVariable("idx") Long idx,
                               @ModelAttribute PolicyDTO policyDTO,
                               @RequestParam(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles) {
        try {
            policyService.updatePolicy(idx, policyDTO);

            if (uploadFiles != null && !uploadFiles.isEmpty()) {
                policyService.addPolicyFiles(String.valueOf(idx), uploadFiles);
            }

            return "redirect:/policy/detail/" + idx;
        } catch (EntityNotFoundException e) {
            log.error("규정 수정 실패: {}", e.getMessage());
            return "errorPage";
        }
    }
    
    // 규정 삭제 (게시 여부를 'N'로 변경)
    @DeleteMapping("/detail/{idx}")
    @ResponseBody
    public ResponseEntity<String> deletePolicy(@PathVariable("idx") Long idx) {
    	try {
    		policyService.changePolicyStatus(idx, "N");
    		return ResponseEntity.ok("success");
    	} catch (Exception e) {
    		log.error("규정 삭제 실패: {}", e.getMessage());
    		return ResponseEntity.status(500).body("error");
    	}
    }
    
//    // 규정 파일 추가
//    @PostMapping("/detail/{idx}/file")
//    public String updatePolicyFiles(@PathVariable("idx") String idx,
//                                    @RequestParam("policyFiles") List<MultipartFile> policyFiles) {
//        log.info("updatePolicyFiles --- start");
//        log.info("target policy idx : {}", idx);
//
//        List<FileVO> fileList = policyFiles.stream()
//                .filter(file -> StringUtils.hasLength(file.getOriginalFilename()))
//                .map(file -> {
//                    try {
//                        FileVO fileVO = fileUtil.setFile(file);
//                        fileVO.setMajorIdx(idx);        // 규정 번호 설정
//                        fileVO.setType("t_policy");     // 테이블명 또는 유형 설정
//                        fileVO.setIsDeleted("N");       // 삭제 여부
//                        return fileVO;
//                    } catch (Exception e) {
//                        log.error("파일 처리 중 오류 발생", e);
//                        return null;
//                    }
//                })
//                .filter(Objects::nonNull)
//                .toList();
//
//        if (!fileList.isEmpty()) {
//            fileService.registerFiles(fileList);
//            log.info("파일 {}건 저장 완료", fileList.size());
//        }
//
//        return "redirect:/policy/detail/" + idx;
//    }
    
    // 파일 삭제
    @PostMapping("/detail/{idx}/file/delete/{fileId}")
    @ResponseBody
    public String deleteFile(@PathVariable("idx") Long idx, @PathVariable("fileId") Long fileId) {
        try {
            FileVO fileVO = new FileVO();
            fileVO.setIdx(String.valueOf(fileId));
            fileVO.setIsDeleted("Y"); // 삭제 상태로 변경
            int result = fileService.removeFile(fileVO);
            return result > 0 ? "success" : "fail";
        } catch (Exception e) {
            log.error("파일 삭제 실패 - 파일 ID: {}, 오류: {}", fileId, e.getMessage());
            return "error";
        }
    }

}
