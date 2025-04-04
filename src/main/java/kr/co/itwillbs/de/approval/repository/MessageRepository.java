package kr.co.itwillbs.de.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.itwillbs.de.approval.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

	// 기간 + (발신자 or 수신자) 이름 + 알림 유형으로 검색
	@Query("SELECT m FROM Message m " +
	           "WHERE m.isDeleted = 'N' " +  // 삭제되지 않은 메시지
	           "AND m.sendDate BETWEEN :startDate AND :endDate " +  // 기간 필터
	           "AND (:senderId IS NULL OR :senderId = '' OR m.senderId LIKE %:senderId%) " +  // 발신자 ID 필터
	           "AND (:receiverId IS NULL OR :receiverId = '' OR m.receiverId LIKE %:receiverId%) " +  // 수신자 ID 필터
	           "AND (:type IS NULL OR :type = '' OR m.type = :type) " +  // 알림 유형 필터
	           "ORDER BY m.sendDate DESC")
	List<Message> searchMessages(
	        @Param("startDate") LocalDateTime startDate,
	        @Param("endDate") LocalDateTime endDate,
	        @Param("senderId") String senderId,
	        @Param("receiverId") String receiverId,
	        @Param("type") String type
	);
}
