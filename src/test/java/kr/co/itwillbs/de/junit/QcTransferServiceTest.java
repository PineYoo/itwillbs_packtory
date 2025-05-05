package kr.co.itwillbs.de.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.common.service.TransferService;
import kr.co.itwillbs.de.common.vo.TransferDTO;
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@DisplayName("QC 전송 커맨드 테스트")
public class QcTransferServiceTest {
	@Autowired
	private TransferService transferService;

	@Test
	@Transactional
	@Rollback(false) // 롤백 안 하고 DB에 직접 확인하고 싶을 때
	void testQcTransferCommand_withPassAndFail() throws Exception {
		// given
		TransferDTO dto = new TransferDTO();
		dto.setTransType(TransType.QC); // QC 커맨드 실행 조건
		dto.setWarehouseIdx("4"); // 검사 대상 입출고 row

		List<QcLogDTO> inputQcList = new ArrayList<>();

		// 성공 항목
		QcLogDTO pass1 = new QcLogDTO();
		pass1.setQcIdx("1");
		pass1.setValue("9.9");
		pass1.setQcResult("PASS");
		pass1.setQuantity(90);
		inputQcList.add(pass1);

		// 실패 항목
		QcLogDTO fail1 = new QcLogDTO();
		fail1.setQcIdx("2");
		fail1.setValue("13.5");
		fail1.setQcResult("FAIL");
		fail1.setQuantity(10);
		inputQcList.add(fail1);

		dto.setInputQCList(inputQcList);

		// when
		try {
			transferService.transfer(dto);
		} catch (Exception e) {
			log.error("전송 중 예외 발생", e);
			throw e; // 꼭 다시 던지기!
		}
//		transferService.transfer(dto); // 실제 실행

		// then
		// DB에서 LOT, QC_LOG, WAREHOUSE_TRANSACTION 등 insert 확인
		// 또는 log.info 찍고 로그 확인
	}
}
