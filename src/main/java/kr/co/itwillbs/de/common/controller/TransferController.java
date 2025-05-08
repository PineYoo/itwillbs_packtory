package kr.co.itwillbs.de.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.common.service.TransferService;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.util.StringUtil;
import kr.co.itwillbs.de.common.vo.TransferDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/transfer")
@RestController
public class TransferController {

	private final TransferService transferService;
	
	public TransferController(TransferService transferService) {
		this.transferService = transferService;
	}
	
	/**
	 * [입고신청]에서 이용
	 * @param documentNumber
	 * @return
	 */
	@GetMapping("/inbound/{documentNumber}")
	public ResponseEntity<Map<String, Object>> requestInbound(@PathVariable("documentNumber") String documentNumber) {
		LogUtil.logStart(log);
		LogUtil.logDetail(log, "request: {}", StringUtil.objToString(documentNumber));
		
		//리턴 객체 생성
		Map<String, Object> response = new HashMap<>();
		
		if (StringUtil.isLongValue(documentNumber)) {
			response.put("error", "문서 번호는 숫자만 입력해야 합니다.");
			return ResponseEntity.badRequest().body(response);
		}
		
		try {
			TransferDTO transferDTO = new TransferDTO();
			transferDTO.setTransType(TransType.IN); // IN 입력
			transferDTO.setOrderDocumentNumber(documentNumber);
			
			transferService.transfer(transferDTO);
		} catch (Exception e) {
			LogUtil.error(log, "orderItem NotFound, documentNumber is {}", documentNumber);
			ResponseEntity.ok(response);
		}
		
		return ResponseEntity.ok(response);
	}
}
