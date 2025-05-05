package kr.co.itwillbs.de.common.vo;

import java.util.ArrayList;
import java.util.List;

import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import lombok.Getter;

/**
 * <pre>
 * QC결과를 담아두는 클래스
 * success일 경우에도 LotNumber가 생성되고
 * fail 일 경우에도 재검사 로직을 위한 LotNumber가 생성되어야 하기에
 * 입력받은 하나의 검사list에서 검증을 한 후 두개의 list로 관리하게 됨
 * 
 * 실제 현실에서는 QC할 품목을 하나하나 모두 검사하여 수치를 기록하여 
 * </pre>
 */
@Getter
public class QcValidationResult {
	
	private final List<QcLogDTO> successList = new ArrayList<>();
	private final List<QcLogDTO> failList = new ArrayList<>();

	public void addSuccess(QcLogDTO log) {
		successList.add(log);
	}

	public void addFail(QcLogDTO log) {
		failList.add(log);
	}

	public int getTotalCount() {
		return successList.size() + failList.size();
	}
	
	public int getAvgSuccessQty() {
		return calculateQuantityAverage(successList);
	}
	public int getMaxSuccessQty() {
		return findMaxQuantity(successList);
	}
	public int getMinSuccessQty() {
		return findMinQuantity(successList);
	}
	
	public int getAvgFailQty() {
		return calculateQuantityAverage(failList);
	}
	public int getMaxFailQty() {
		return findMaxQuantity(failList);
	}
	public int getMinFailQty() {
		return findMinQuantity(failList);
	}
	
	/**
	 * 이건 좀 아닌데.. 이걸 서비스로 떼어내기도 애매하다.
	 * <br> List<QcLogDTO> logList 에서 quantity의 평균값을 계산
	 * 
	 * @param logList QcLogDTO
	 * @return quantity의 평균값 (list 가 비어있거나 유효한 값이 없을 경우 0 반환)
	 */
	private int calculateQuantityAverage(List<QcLogDTO> logList) {
		if (logList == null || logList.isEmpty()) {
			return 0;
		}

		int sum = 0;
		int count = 0;

		for (int i = 0; i < logList.size(); i++) {
			QcLogDTO log = logList.get(i);
			Integer quantity = log.getQuantity();

			if (quantity != null) {
				sum += quantity;
				count++;
			}
		}

		if (count == 0) {
			return 0;
		}

		return (int) sum / count;
	}
	
	/**
	 * 이건 좀 아닌데.. 이걸 서비스로 떼어내기도 애매하다2
	 * <br>List<QcLogDTO> logList 에서 quantity 값 중 최대값 찾기
	 * <br>Integer.MIN_VALUE / Integer.MAX_VALUE 이런거까지 비교는 안해도 되겠지?
	 * @param logList QcLogDTO
	 * @return quantity의 최대값 (list 가 비어있거나 유효한 값이 없을 경우 0 반환)
	 */
	public int findMaxQuantity(List<QcLogDTO> logList) {
		if (logList == null || logList.isEmpty()) {
			return 0;
		}

		Integer maxQuantity = null;

		for (int i = 0; i < logList.size(); i++) {
			QcLogDTO log = logList.get(i);
			Integer quantity = log.getQuantity(); // QcLogDTO의 quantity가 Integer 타입

			if (quantity != null) {
				if (maxQuantity == null || quantity > maxQuantity) {
					maxQuantity = quantity;
				}
			}
		}
		
		return maxQuantity != null ? maxQuantity : 0;
	}
	
	/**
	 * 이건 좀 아닌데.. 이걸 서비스로 떼어내기도 애매하다2
	 * <br>List<QcLogDTO> logList 에서 quantity 값 중 최소값 찾기
	 * <br>Integer.MIN_VALUE / Integer.MAX_VALUE 이런거까지 비교는 안해도 되겠지?
	 * @param logList QcLogDTO
	 * @return quantity의 최소값 (list 가 비어있거나 유효한 값이 없을 경우 0 반환)
	 */
	public int findMinQuantity(List<QcLogDTO> logList) {
		if (logList == null || logList.isEmpty()) {
			return 0;
		}
		
		Integer minQuantity = null;
		
		for (int i = 0; i < logList.size(); i++) {
			QcLogDTO log = logList.get(i);
			Integer quantity = log.getQuantity(); // QcLogDTO의 quantity가 Integer 타입
			
			if (quantity != null) {
				if (minQuantity == null || quantity < minQuantity) {
					minQuantity = quantity;
				}
			}
		}
		
		return minQuantity != null ? minQuantity : 0;
	}
}
