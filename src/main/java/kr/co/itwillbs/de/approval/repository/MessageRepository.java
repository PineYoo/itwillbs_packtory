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
               "WHERE m.isDeleted = 'N' " +
               "AND m.sendDate BETWEEN :startDate AND :endDate " +
               "AND (:keyword IS NULL OR m.senderId LIKE %:keyword% OR m.receiverId LIKE %:keyword%) " +
               "AND (:type IS NULL OR m.type = :type) " +
               "ORDER BY m.sendDate DESC")
    List<Message> searchMessages(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("keyword") String keyword,
            @Param("type") String type
    );
}
