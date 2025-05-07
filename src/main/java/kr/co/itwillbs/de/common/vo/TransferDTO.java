package kr.co.itwillbs.de.common.vo;

import java.util.List;

import kr.co.itwillbs.de.common.constant.CommandOption;
import kr.co.itwillbs.de.common.constant.TransStatus;
import kr.co.itwillbs.de.common.constant.TransType;
import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Command 패턴을 위한 클래스
 * <br>오랜만이라 기억이 잘... ㄱ-) 일단 간다!
 */
@Getter
@Setter
@NoArgsConstructor
public class TransferDTO {
	
	private TransType transType;
	private TransStatus status;
	private CommandOption commandOption;
	
	// 입고시 사용되는 변수
	// 아마도 tradeCode
	// 아마도 tradeStatus 둘다 또 봐야할 것 같은데숭?
	private String orderDocumentNumber;
	private String productIdx;
	private String materialIdx;
	private String sourceLocationIdx;
	private String destinationLocationIdx;
	private String unit;
	private int quantity;
	
	// Qc에 사용되는 변수
	private String warehouseIdx;
	private List<QcLogDTO> inputQCList;
	
	// 자재 이동시 사용되는 변수
	private String lotsIdx;
	
	// test용 코드
	private boolean forceError;
	
	public boolean isForceError() {
		return forceError;
	}
}
