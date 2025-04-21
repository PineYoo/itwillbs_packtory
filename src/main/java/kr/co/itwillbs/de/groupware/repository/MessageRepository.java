package kr.co.itwillbs.de.groupware.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.itwillbs.de.groupware.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

	// 기간 + (발신자 or 수신자) 이름 + 알림 유형으로 검색
	@Query("SELECT m FROM Message m " +
		       "WHERE m.isDeleted = 'N' " +
		       "AND (:sendDate IS NULL OR :receiveDate IS NULL OR m.sendDate BETWEEN :sendDate AND :receiveDate) " +
		       "AND (:senderId IS NULL OR :senderId = '' OR m.senderId LIKE %:senderId%) " +
		       "AND (:receiverId IS NULL OR :receiverId = '' OR m.receiverId LIKE %:receiverId%) " +
		       "AND (:type IS NULL OR :type = '' OR m.type = :type) " +
		       "ORDER BY m.sendDate DESC")
	List<Message> searchMessages(
		    @Param("sendDate") LocalDate sendDate,
		    @Param("receiveDate") LocalDate receiveDate,
		    @Param("senderId") String senderId,
		    @Param("receiverId") String receiverId,
		    @Param("type") String type
	);
	
	@Query("SELECT m FROM Message m WHERE m.isDeleted = 'N' ORDER BY m.idx ASC")
    List<Message> findValidMessages();
}
