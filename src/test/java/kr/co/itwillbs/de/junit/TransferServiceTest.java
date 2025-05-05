package kr.co.itwillbs.de.junit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.common.service.TransferService;
import kr.co.itwillbs.de.common.vo.TransferDTO;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class TransferServiceTest {

	@Autowired
	private TransferService transferService;

	@Test
	@Transactional
	@Rollback(false) // rollback(false)로 해야 DB에서 직접 눈으로 확인 가능
	void testRollbackOnException() {
		TransferDTO dto = new TransferDTO();
		dto.setTransType(TransType.IN);
		dto.setOrderDocumentNumber("100083");
		dto.setForceError(true); // 강제로 에러 발생시킬 플래그

		try {
			transferService.transfer(dto);
		} catch (Exception e) {
			log.error("강제 오류 발생: {}", e.getMessage());
		}

		// 여기서 DB에서 직접 확인:
		// - forceError=false → insert/commit 됨
		// - forceError=true → rollback, 데이터 없음
	}

	@Test
	@Transactional
	@Rollback(false)
	void testNormalExecution() {
		TransferDTO dto = new TransferDTO();
		dto.setTransType(TransType.IN);
		dto.setOrderDocumentNumber("100084");
		dto.setForceError(false); // 정상 실행

		try {
			transferService.transfer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("강제 오류 발생: {}", e.getMessage());
		}

		// DB에서 직접 확인:
		// - insert/commit 됨
	}
}
