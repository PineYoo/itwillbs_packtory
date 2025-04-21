package kr.co.itwillbs.de.groupware.controller;

import java.util.List;

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

import kr.co.itwillbs.de.admin.dto.CodeItemDTO;
import kr.co.itwillbs.de.common.util.CommonCodeUtil;
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
		model.addAttribute("messageDTO", new MessageDTO());

		// 공통 코드 리스트 (메시지 타입 선택용)
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("MESSAGE_TYPE");
		model.addAttribute("codeItems", codeItems);

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

		// 메시지 목록 조회 (검색 조건에 따라 페이징 처리)
		Page<MessageDTO> result = messageService.getMessagesBySearchDTO(searchDTO);

		// 총 아이템 수 설정
		searchDTO.getPageDTO().setTotalCount((int) result.getTotalElements());

		// 모델에 메시지 목록과 페이징 정보 추가
		model.addAttribute("messageDTOList", result.getContent());

		// 역순 번호 계산 (페이징의 첫 번째 항목 번호 계산)
		int startNo = searchDTO.getPageDTO().getTotalCount() - searchDTO.getPageDTO().getOffset();
		model.addAttribute("startNo", startNo);

		// 공통 코드 리스트 (메시지 타입 선택용)
		List<CodeItemDTO> codeItems = commonCodeUtil.getCodeItems("MESSAGE_TYPE");
		model.addAttribute("codeItems", codeItems);

		// 검색 DTO 추가 (검색 조건 포함)
		model.addAttribute("searchDTO", searchDTO);

		// 메시지 목록 뷰 반환
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
