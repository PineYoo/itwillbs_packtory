package kr.co.itwillbs.de.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.itwillbs.de.mes.dto.QcLogDTO;
import kr.co.itwillbs.de.mes.dto.QcStandardDTO;

public class QcValidator {
	
	public static ValidationResult validateQcLogs(List<QcStandardDTO> standards, List<QcLogDTO> logs) {
		ValidationResult result = new ValidationResult();

		// 표준 값들을 인덱스로 맵핑하여 빠른 접근이 가능하도록 함
		Map<String, QcStandardDTO> standardMap = new HashMap<>();
		for (QcStandardDTO standard : standards) {
			standardMap.put(String.valueOf(standard.getIdx()), standard);
		}

		// 각 로그에 대해 검증 수행
		for (QcLogDTO log : logs) {
			String qcIdx = log.getQcIdx();
			QcStandardDTO standard = standardMap.get(qcIdx);

			if (standard == null) {
				// 해당 인덱스의 표준이 없으면 실패로 처리
				result.failList.add(log);
				continue;
			}

			String value = log.getValue();
			if (standard.isPassed(value)) {
				result.successList.add(log);
			} else {
				result.failList.add(log);
			}
		}

		return result;
	}

	/**
	 * 항목별 통과율 계산
	 */
//	public static float calculatePassRate(List<QcLogDTO> logs, int qcIdx) {
//		int totalCount = 0;
//		int passCount = 0;
//
//		for (QcLogDTO log : logs) {
//			if (log.getQcIdx() == qcIdx) {
//				totalCount++;
//
//				QcStandardDTO standard = getStandardByIdx(qcIdx);
//				if (standard != null && standard.isPassed(log.getValue())) {
//					passCount++;
//				}
//			}
//		}
//
//		if (totalCount > 0) {
//			return (float) passCount / totalCount;
//		} else {
//			return 0;
//		}
//	}

	/**
	 * 항목별 허용 통과율 기준 충족 여부 확인
	 */
	public static boolean isQcItemAcceptable(List<QcLogDTO> logs, QcStandardDTO standard) {
		String qcIdx = String.valueOf(standard.getIdx());
		float acceptableRate = standard.getAcceptablePassRate(); // 허용 통과율 (예: 0.9)

		int totalCount = 0;
		int passCount = 0;

		for (QcLogDTO log : logs) {
			if (log.getQcIdx() == qcIdx) {
				totalCount++;
				if (standard.isPassed(log.getValue())) {
					passCount++;
				}
			}
		}

		float actualPassRate = 0;
		if (totalCount > 0) {
			actualPassRate = (float) passCount / totalCount;
		}

		return actualPassRate >= acceptableRate;
	}

	/**
	 * 전체 QC 항목에 대한 종합 판정
	 */
	public static boolean isOverallQualityAcceptable(List<QcStandardDTO> standards, List<QcLogDTO> logs) {
		// 로그를 QC 인덱스별로 그룹화
		Map<String, List<QcLogDTO>> logsByQcIdx = new HashMap<>();

		for (QcLogDTO log : logs) {
			String qcIdx = String.valueOf(log.getQcIdx());

			if (!logsByQcIdx.containsKey(qcIdx)) {
				logsByQcIdx.put(qcIdx, new ArrayList<QcLogDTO>());
			}

			logsByQcIdx.get(qcIdx).add(log);
		}

		// 각 QC 항목별로 허용 통과율 충족 여부 확인
		for (QcStandardDTO standard : standards) {
			String qcIdx = String.valueOf(standard.getIdx());
			List<QcLogDTO> logsForQc = logsByQcIdx.get(qcIdx);

			// 해당 QC에 대한 로그가 없으면 다음으로 넘어감
			if (logsForQc == null) {
				continue;
			}

			if (!isQcItemAcceptable(logsForQc, standard)) {
				return false; // 하나라도 허용 통과율 미달이면 전체 불합격
			}
		}

		return true; // 모든 항목이 허용 통과율을 충족
	}

	/**
	 * QC 결과를 담는 확장 DTO
	 */
	public static class QcResultDTO {
		private QcStandardDTO standard;
		private int totalCount;
		private int passCount;
		private float passRate;
		private boolean isAcceptable;

		public QcStandardDTO getStandard() {
			return standard;
		}

		public void setStandard(QcStandardDTO standard) {
			this.standard = standard;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public int getPassCount() {
			return passCount;
		}

		public void setPassCount(int passCount) {
			this.passCount = passCount;
		}

		public float getPassRate() {
			return passRate;
		}

		public void setPassRate(float passRate) {
			this.passRate = passRate;
		}

		public boolean isAcceptable() {
			return isAcceptable;
		}

		public void setAcceptable(boolean isAcceptable) {
			this.isAcceptable = isAcceptable;
		}
	}

	/**
	 * 각 QC 항목별 통계 생성
	 */
	public static List<QcResultDTO> generateQcStatistics(List<QcStandardDTO> standards, List<QcLogDTO> logs) {
		List<QcResultDTO> results = new ArrayList<QcResultDTO>();

		for (QcStandardDTO standard : standards) {
			QcResultDTO result = new QcResultDTO();
			result.setStandard(standard);

			String qcIdx = String.valueOf(standard.getIdx());
			int totalCount = 0;
			int passCount = 0;

			for (QcLogDTO log : logs) {
				if (log.getQcIdx() == qcIdx) {
					totalCount++;
					if (standard.isPassed(log.getValue())) {
						passCount++;
					}
				}
			}

			result.setTotalCount(totalCount);
			result.setPassCount(passCount);

			float passRate = 0;
			if (totalCount > 0) {
				passRate = (float) passCount / totalCount;
			}
			result.setPassRate(passRate);

			// 허용 통과율과 비교
			result.setAcceptable(passRate >= standard.getAcceptablePassRate());

			results.add(result);
		}

		return results;
	}
	
	public static void main(String[] args) {
		List<QcStandardDTO> standards = createSampleStandards();
		List<QcLogDTO> logs = createSampleLogs();

		// 성공/실패 리스트 분리
		ValidationResult validationResult = validateQcLogs(standards, logs);
		System.out.println("성공 항목 수: " + validationResult.getSuccessList().size());
		System.out.println("실패 항목 수: " + validationResult.getFailList().size());

		
		// 검증 및 통계 수행
		List<QcResultDTO> statistics = generateQcStatistics(standards, logs);

		// 결과 출력
		System.out.println("=== QC 항목별 통계 ===");
		for (QcResultDTO stat : statistics) {
			System.out.println("항목: " + stat.getStandard().getName());
			System.out.println("총 검사 수: " + stat.getTotalCount());
			System.out.println("통과 수: " + stat.getPassCount());
			System.out.println("통과율: " + (stat.getPassRate() * 100) + "%");
			System.out.println("허용 통과율: " + (stat.getStandard().getAcceptablePassRate() * 100) + "%");
			System.out.println("판정: " + (stat.isAcceptable() ? "허용" : "불합격"));
			System.out.println("-----------------------");
		}

		boolean overall = isOverallQualityAcceptable(standards, logs);
		System.out.println("종합 판정: " + (overall ? "합격" : "불합격"));
	}
	// 이건 이너 클래스로 만들? 
	public static class ValidationResult {
		
		private final List<QcLogDTO> successList = new ArrayList<>();
		private final List<QcLogDTO> failList = new ArrayList<>();

		public List<QcLogDTO> getSuccessList() {
			return successList;
		}

		public List<QcLogDTO> getFailList() {
			return failList;
		}
	}
	
	private static List<QcStandardDTO> createSampleStandards() {
		List<QcStandardDTO> standards = new ArrayList<>();

		QcStandardDTO standard1 = new QcStandardDTO();
		standard1.setName("입고 외관 상태");
		standard1.setType("10");
		standard1.setIdx(1L);
		standard1.setTargetValue("양호");
		standard1.setQuantity(100);
		standard1.setAcceptablePassRate(0.95f);
		standards.add(standard1);

		QcStandardDTO standard2 = new QcStandardDTO();
		standard2.setName("입고 중량 오차");
		standard2.setType("11");
		standard2.setMinValue("-5");
		standard2.setMaxValue("5");
		standard2.setUnit("2");
		standard2.setIdx(4L);
		standard2.setTargetValue("0");
		standard2.setQuantity(100);
		standard2.setAcceptablePassRate(0.9f);
		standards.add(standard2);

		return standards;
	}

	private static List<QcLogDTO> createSampleLogs() {
		List<QcLogDTO> logs = new ArrayList<>();

//		QcLogDTO log1 = new QcLogDTO();
//		log1.setValue("pass");
//		log1.setQcIdx("1");
//		log1.setQuantity(200);
//		logs.add(log1);
//
//		QcLogDTO log2 = new QcLogDTO();
//		log2.setValue("6");
//		log2.setQcIdx("4");
//		log1.setQuantity(200);
//		logs.add(log2);
		// 외관 상태 로그 (100개 중 96개 통과)
		for (int i = 0; i < 96; i++) {
			QcLogDTO log = new QcLogDTO();
			log.setValue("pass");
			log.setQcIdx("1");
			logs.add(log);
		}
		for (int i = 0; i < 4; i++) {
			QcLogDTO log = new QcLogDTO();
			log.setValue("fail");
			log.setQcIdx("1");
			logs.add(log);
		}
		// 중량 오차 로그 (100개 중 89개 통과)
		for (int i = 0; i < 89; i++) {
			QcLogDTO log = new QcLogDTO();
			log.setValue(String.valueOf(i % 5)); // -4~4 사이 값으로 통과
			log.setQcIdx("4");
			logs.add(log);
		}
		for (int i = 0; i < 11; i++) {
			QcLogDTO log = new QcLogDTO();
			log.setValue("10"); // 범위 벗어나 실패
			log.setQcIdx("4");
			logs.add(log);
		}
		return logs;
	}
	
}
