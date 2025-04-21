package kr.co.itwillbs.de.groupware.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import kr.co.itwillbs.de.common.aop.annotation.LogExecution;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.groupware.dto.MessageDTO;
import kr.co.itwillbs.de.groupware.dto.MessageSearchDTO;
import kr.co.itwillbs.de.groupware.entity.Message;
import kr.co.itwillbs.de.groupware.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;

	@LogExecution
	// 메시지 등록
	public void registerMessage(MessageDTO messageDTO) {
		log.info("registerMessage --- start");
		messageRepository.save(messageDTO.toEntity());
	}

	// 메시지 전체 목록
	public List<MessageDTO> getMessageList(MessageSearchDTO searchDTO) {
		log.info("getMessageList --- searchDTO: {}", searchDTO);

		return messageRepository
				.searchMessages(searchDTO.getSendDate(), searchDTO.getReceiveDate(), searchDTO.getType(),
						searchDTO.getReceiverId(), searchDTO.getSenderId())
				.stream().map(Message::toDto).collect(Collectors.toList());
	}

	/**
	 * SearchDTO를 가져와서 리스트 조건 조회하기
	 * 
	 * @param searchDTO
	 * @return
	 */
	public Page<MessageDTO> getMessagesBySearchDTO(MessageSearchDTO searchDTO) {
		LogUtil.logStart(log);

		Pageable pageable = searchDTO.getPageDTO().toPageable(Sort.by("idx").descending());

		// Specification을 Message 엔티티에 대해 작성
		Specification<Message> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (searchDTO.getSendDate() != null) {
				predicates.add(cb.equal(root.get("sendDate"), searchDTO.getSendDate()));
			}
			if (searchDTO.getReceiveDate() != null) {
				predicates.add(cb.equal(root.get("receiveDate"), searchDTO.getReceiveDate()));
			}
			if (searchDTO.getSenderId() != null && !searchDTO.getSenderId().isBlank()) {
				predicates.add(cb.equal(root.get("senderId"), searchDTO.getSenderId()));
			}
			if (searchDTO.getReceiverId() != null && !searchDTO.getReceiverId().isBlank()) {
				predicates.add(cb.equal(root.get("receiverId"), searchDTO.getReceiverId()));
			}
			if (searchDTO.getType() != null && !searchDTO.getType().isBlank()) {
				predicates.add(cb.equal(root.get("type"), searchDTO.getType()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		// findAll 메서드를 사용하여 Message 엔티티를 조회
		Page<Message> result = messageRepository.findAll(spec, pageable);

		// 조회된 Message 엔티티를 MessageDTO로 변환하여 반환
		return result.map(Message::toDto);
	}

	// 상세 조회
	public MessageDTO getMessageByIdx(Long idx) {
		log.info("getMessageByIdx --- idx: {}", idx);

		return messageRepository.findById(idx).filter(m -> !"Y".equals(m.getIsDeleted())).map(Message::toDto)
				.orElseThrow(() -> new EntityNotFoundException("메시지를 찾을 수 없습니다."));
	}

	// 메시지 수정
	@LogExecution
	@Transactional
	public void updateMessage(Long idx, MessageDTO dto) {
		log.info("updateMessage --- idx: {}", idx);

		Message entity = messageRepository.findById(idx)
				.orElseThrow(() -> new EntityNotFoundException("메시지를 찾을 수 없습니다."));

		// null이 아닌 값만 업데이트
		if (dto.getTitle() != null)
			entity.setTitle(dto.getTitle());
		if (dto.getType() != null)
			entity.setType(dto.getType());
		if (dto.getContents() != null)
			entity.setContents(dto.getContents());
		if (dto.getReceiverId() != null)
			entity.setReceiverId(dto.getReceiverId());

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
