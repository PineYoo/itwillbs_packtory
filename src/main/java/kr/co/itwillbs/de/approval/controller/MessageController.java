package kr.co.itwillbs.de.approval.controller;

import kr.co.itwillbs.de.approval.dto.MessageDTO;
import kr.co.itwillbs.de.approval.dto.MessageSearchDTO;
import kr.co.itwillbs.de.approval.service.MessageService;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final CommonCodeUtil commonCodeUtil;

    // 메시지 등록 폼
    @GetMapping("/new")
    public String MessageRegisterForm(Model model) {
        log.info("MessageRegisterForm --- start");
        model.addAttribute("messageDTO", new MessageDTO());
        model.addAttribute("messageTypes", commonCodeUtil.getCodeItems("MESSAGE_TYPE"));
        return "approval/message/form";
    }

    // 메시지 등록 처리
    @PostMapping("/new")
    public String registerMessage(@ModelAttribute MessageDTO dto) {
        log.info("registerMessage --- dto: {}", dto);
        messageService.registerMessage(dto);
        return "redirect:/message";
    }

    // 메시지 목록 조회 (검색 기능 추가)
    @GetMapping("")
    public String getMessageList(@ModelAttribute MessageSearchDTO searchDTO, Model model) {
        log.info("getMessageList --- search: {}", searchDTO);
        model.addAttribute("messageDTOList", messageService.getMessageList(searchDTO));
        model.addAttribute("messageTypes", commonCodeUtil.getCodeItems("MESSAGE_TYPE"));
        model.addAttribute("searchDTO", searchDTO);
        return "approval/message/list";
    }

    // 상세 조회
    @GetMapping("/{idx}")
    public String getMessageDetail(@PathVariable("idx") Long idx, Model model) {
        log.info("getMessageDetail --- idx: {}", idx);
        MessageDTO dto = messageService.getMessageByIdx(idx);
        model.addAttribute("message", dto);
        model.addAttribute("MessageTypes", commonCodeUtil.getCodeItems("MESSAGE_TYPE"));
        return "approval/message/detail";
    }

    // 수정 처리
    @PostMapping("/{idx}")
    public String updateMessage(@PathVariable("idx") Long idx, @ModelAttribute MessageDTO dto) {
        log.info("updateMessage --- idx: {}, dto: {}", idx, dto);
        messageService.updateMessage(idx, dto);
        return "redirect:/message/" + idx;
    }

    // 삭제 처리 (Soft Delete)
    @PostMapping("/{idx}/delete")
    public String deleteMessage(@PathVariable("idx") Long idx) {
        log.info("deleteMessage --- idx: {}", idx);
        messageService.softDeleteMessage(idx);
        return "redirect:/message";
    }
}
