package kr.co.itwillbs.de.groupware.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itwillbs.de.common.service.CustomUserDetails;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import kr.co.itwillbs.de.common.vo.LoginVO;
import kr.co.itwillbs.de.groupware.dto.MessageDTO;
import kr.co.itwillbs.de.groupware.dto.MessageSearchDTO;
import kr.co.itwillbs.de.groupware.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/groupware/message")
public class MessageController {

    private final MessageService messageService;
    private final CommonCodeUtil commonCodeUtil;

    // 메시지 등록 폼
    @GetMapping("/new")
    public String MessageRegisterForm(Model model) {
        log.info("MessageRegisterForm --- start");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			LoginVO loginVO = userDetails.getLoginVO();
			String memberId = userDetails.getUsername(); // 이렇게 자유롭게 사용 가능!
			log.info("userDetails is {}",userDetails);
			log.info("loginVO is {}",loginVO);
			model.addAttribute("userDetails", userDetails);
			model.addAttribute("loginVO", loginVO);
		}
        model.addAttribute("messageDTO", new MessageDTO());
        model.addAttribute("messageTypes", commonCodeUtil.getCodeItems("MESSAGE_TYPE"));
        return "groupware/message/form";
    }

    // 메시지 등록 처리
    @PostMapping("/new")
    public String registerMessage(@ModelAttribute MessageDTO dto) {
        log.info("registerMessage --- dto: {}", dto);
        messageService.registerMessage(dto);
        return "redirect:/groupware/message";
    }

    // 메시지 목록 조회 (검색 기능 추가)
    @GetMapping("")
    public String getMessageList(@ModelAttribute MessageSearchDTO searchDTO, Model model) {
        log.info("getMessageList --- search: {}", searchDTO);
        model.addAttribute("messageDTOList", messageService.getMessageList(searchDTO));
        model.addAttribute("messageTypes", commonCodeUtil.getCodeItems("MESSAGE_TYPE"));
        model.addAttribute("searchDTO", searchDTO);
        return "groupware/message/list";
    }

    // 상세 조회
    @GetMapping("/{idx}")
    public String getMessageDetail(@PathVariable("idx") Long idx, Model model) {
        log.info("getMessageDetail --- idx: {}", idx);
        MessageDTO dto = messageService.getMessageByIdx(idx);
        model.addAttribute("messageDTO", dto);
        model.addAttribute("messageTypes", commonCodeUtil.getCodeItems("MESSAGE_TYPE"));
        return "groupware/message/detail";
    }

    // 수정 처리
    @PostMapping("/{idx}")
    public String updateMessage(@PathVariable("idx") Long idx, @ModelAttribute MessageDTO dto) {
        log.info("updateMessage --- idx: {}, dto: {}", idx, dto);
        messageService.updateMessage(idx, dto);
        return "redirect:/groupware/message/" + idx;
    }

    // 삭제 처리 (Soft Delete)
    @DeleteMapping("/delete/{idx}")
    @ResponseBody
    public String deleteMessage(@PathVariable("idx") Long idx) {
        try {
            log.info("deleteMessage --- idx: {}", idx);
            messageService.softDeleteMessage(idx);
            return "success";
        } catch (Exception e) {
            log.error("삭제 실패", e);
            return "fail";
        }
    }
}
