package kr.co.itwillbs.de.approval.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.itwillbs.de.approval.dto.MessageDTO;
import kr.co.itwillbs.de.approval.dto.MessageSearchDTO;
import kr.co.itwillbs.de.approval.entity.Message;
import kr.co.itwillbs.de.approval.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    // 메시지 등록
    public void registerMessage(MessageDTO messageDTO) {
        log.info("registerMessage --- start");
        messageRepository.save(messageDTO.toEntity());
    }

    // 메시지 전체 목록
    public List<MessageDTO> getMessageList(MessageSearchDTO searchDTO) {
        log.info("getMessageList --- searchDTO: {}", searchDTO);

        return messageRepository.searchMessages(
        		searchDTO.getStartDate(),
        		searchDTO.getEndDate(),
        		searchDTO.getType(),
                searchDTO.getReceiverId(),
                searchDTO.getSenderId()
            )
        	.stream()
            .map(Message::toDto)
            .collect(Collectors.toList());
    }

    // 상세 조회
    public MessageDTO getMessageByIdx(Long idx) {
        log.info("getMessageByIdx --- idx: {}", idx);

        return messageRepository.findById(idx)
                .filter(m -> !"Y".equals(m.getIsDeleted()))
                .map(Message::toDto)
                .orElseThrow(() -> new EntityNotFoundException("메시지를 찾을 수 없습니다."));
    }

    // 메시지 수정
    @Transactional
    public void updateMessage(Long idx, MessageDTO dto) {
        log.info("updateMessage --- idx: {}", idx);

        Message entity = messageRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("메시지를 찾을 수 없습니다."));

        entity.updateFromDTO(dto);
    }

    // 메시지 삭제 (soft delete)
    @Transactional
    public void softDeleteMessage(Long idx) {
        log.info("softDeleteMessage --- idx: {}", idx);

        Message message = messageRepository.findById(idx)
                .orElseThrow(() -> new EntityNotFoundException("메시지를 찾을 수 없습니다."));

        message.setIsDeleted("Y");
    }
}
