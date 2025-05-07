package kr.co.itwillbs.de.common.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.common.util.LogUtil;
import kr.co.itwillbs.de.common.vo.TransferDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * CommandPattern
 * transferService.transfer(transferDTO);
 * TransferDTO.TransType (이게 명령어 주요 키값)
 * TransferDTO.CommandOption (하위 명령어)
 * 
 * transferDTO 에 
 * </pre>
 */
@Slf4j
@Service
public class TransferService {
	/**
	 * <pre>
	 * MES 서비스의 꽃!!! 원자재,부자재,포장재,상품의 데이터 이동을 서비스로 꽉 제어한다!
	 * 
	 * 기본 개발 요구사항 정리
	 * - IN / OUT 시에
	 * 1) Account의 출금/입금을 처리 할 때 처럼 각 row에 Lock을 건다. JPA를 사용하는 현대(?)에는 낙관적락(Optimistic-Lock)을 권하는 내용들이 많다.
	 * 		(사실 이 문제를 해결하는 가장 좋은 방법은 DB의 이중화, Master는 Insert용 Slave 는 Select를 처리하는 방식이었던 것 같은데 내 개발 세포 어디갔니..?
	 * 		t_orders_item이나 t_warehouse_transaction 까지는 동시작업이 흔하게 일어나지 않으니 비관적락(Pessimistic-Lock)을 해도 되지 않을까? 이게 롤백도 편했던걸로 기억함.
	 * 		batch 서비스들도 거기는 건들지 않으니 일단 도전!)
	 * 2) 락을 걸고 데이터를 복제(?) 하고 unit && quantity 를 검증하고 문제가 없을 경우 Lock 해제, 그리고 Log 남기기(이전값 -> 이후 값)
	 * 3) 여기까지 작업 도중 RuntimeException이 발생한 경우 스프링Transactional rollback으로 롤백하기 정도의 간단한 규칙을 잡고 진행해 보자!
	 * ※ 비관적락을 사용하기로 했으니 최대한 다중row는 처리하지 않고 짧게 짧게 치고 빠지게 만들어보자. 
	 * 
	 * - 각 서비스의 느슨한 연결을 위해 command 패턴을 활용해서 메서드들을 연결해보자!
	 * 		여기의 로그는 AOP로 처리하면 될것 같다.
	 * - 오류 로그도 AOP?로 처리할까?
	 * </pre>
	 */	
	private final Map<TransType, TransferCommand> commandMap;
//	private final Map<TransType, TransferCommand> commandMap = new EnumMap<>(TransType.class);
	private final InTransferCommand inTransferCommand;
	private final QcTransferCommand qcTrasnFerCommand;
	private final GeneralTransferCommand generalTransferCommand;
//	private final OutTransferCommand outTransferCommand;

	public TransferService(Map<TransType, TransferCommand> commandMap,
							InTransferCommand inTransferCommand,
							QcTransferCommand qcTrasnFerCommand,
							GeneralTransferCommand generalTransferCommand) {
		this.commandMap = commandMap;
		this.inTransferCommand = inTransferCommand;
		this.qcTrasnFerCommand = qcTrasnFerCommand;
		this.generalTransferCommand = generalTransferCommand;
	}

	@PostConstruct
	public void init() {
		commandMap.put(TransType.IN, inTransferCommand); // 입고
		commandMap.put(TransType.QC, qcTrasnFerCommand); // 입고QC
		commandMap.put(TransType.MOVE, generalTransferCommand); // TO_PRODUCTION: 작업지시서 후 자재 이동, TO_FINISHED_GOODS: 생상된 제품 이동
//		commandMap.put(TransType.OUT, outTransferCommand); // 출고
		// 필요 시 더 작성
	}

	public void transfer(TransferDTO dto) throws Exception {
		LogUtil.logStart(log);
		
		TransferCommand command = commandMap.get(dto.getTransType());
		if (command == null) {
			throw new UnsupportedOperationException("지원하지 않는 전송 타입");
		}
		
		command.execute(dto);
	}
}
